package com.afb.portal.buisness.monitoring.atm.worker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import afb.dsi.dpd.portal.jpa.entities.User;

import com.afb.portal.buisness.monitoring.worker.ConnexionSVFE;
import com.afb.portal.buisness.monitoring.worker.shared.IRapportJourMonitoring;
import com.afb.portal.buisness.monitoring.worker.shared.IRepportManager;
import com.afb.portal.dao.shared.IAtmDAOLocal;
import com.afb.portal.jpa.alerte.SendFileEmail;
import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.monitoring.RapportElement;
import com.afb.portal.jpa.gab.parameter.GabParameter;
import com.afb.portal.jpa.gab.parameter.TypeIncident;


/**
 * RapportJourMonitoring
 * @author Owner
 * @version 1.0
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class RapportJourMonitoring implements IRapportJourMonitoring{

	/**
	 * Service DAO
	 */
	@EJB
	private IAtmDAOLocal dao;

	/**
	 * Service Repport
	 */
	@EJB
	private IRepportManager repportManager;


	public static final Integer rupture = 100000;

	private Boolean lance = Boolean.FALSE;

	private String hour = "";

	private String lashour = "";

	private TimerTask task;
	private Timer timer;

	/**
	 * creerTimer
	 */
	public void creerTimer(){

		try{

			lashour = "";
			lance = Boolean.FALSE;

			// boucle
			int sec = 60;
			int min = 1;
			if(timer != null)timer.cancel();
			if(task != null)task.cancel();

			task = new TimerTask(){
				@Override
				public void run(){
					try {
						runMonitoring();
					}catch(Exception e){
						e.printStackTrace();
					}
				}	
			};

			timer = new Timer(true);
			timer.schedule(task, DateUtils.addMinutes(new Date(),1) , min*sec*1000);

		}catch(Exception e){
			// TODO: handle exception
			e.printStackTrace();
			creerTimer();
		}

	}


	public void runMonitoring(){

		try{

			lance = Boolean.FALSE;
			String times =  repportManager.findTimeMonitoring();
			String mois = new SimpleDateFormat("HH:mm:ss").format(new Date());
			String lastTime = "12:50";
			//times = "12:50,18:10,112:40";

			if(times != null){
				for(String time : times.split(",")){
					if(mois.startsWith(time)){
						lance = Boolean.TRUE; 
						hour = time;
					}else{
						lastTime = time;
					}
				}

				if(lashour.trim().isEmpty() && Boolean.TRUE.equals(lance)){
					lashour = hour;
					process(lastTime,hour);
				}else if( Boolean.TRUE.equals(lance) && !lashour.startsWith(hour)){
					lashour = hour;
					process(lastTime,hour);
				}
			}

			GregorianCalendar cal = new GregorianCalendar();
			if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
				//System.out.println("----Check----InitLogCCJourEx-----");
				if(mois.startsWith("05:00")){
					// processus d initialisation du Fichier de Log 
					//System.out.println("----Begin----");
					InitLogCCJourEx();
					//System.out.println("----End-----");
				}
			}

		}catch(Exception e){
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 */
	@Asynchronous
	public  void process(User user){

		try{

			//System.out.println("----RapportJourMonitoring---OK-----");
			AtmsLost = new ArrayList<Atm>();
			Atms = new ArrayList<Atm>();
			collections = new ArrayList<RapportElement>();
			//List<RapportElement> collections = new ArrayList<RapportElement>();
			//collections = processRapport();
			String lastTime ="09H00";
			String hour = DateFormatUtils.format(new Date(),"HH:mm");
			processRapport(lastTime,hour);
			Collections.sort(collections);
			//System.out.println("----RapportJourMonitoring---OK-----"+collections.size());
			if(!collections.isEmpty()){
				int i = 1;
				Double nbok = 0d;
				Double nbnonok = 0d;
				Double nbbleu = 0d;
				Double nbnon = 0d;
				for(RapportElement el : collections){
					if(el.getErreur().equalsIgnoreCase("OK"))nbok++;
					else if(el.getErreur().equalsIgnoreCase("KO")) nbnonok++;
					else if(el.getErreur().equalsIgnoreCase("LOOK")) nbnon++;
					else nbbleu++;
				}

				for(RapportElement el : collections){
					el.setColor(String.valueOf(i));
					i++;
				}

				String mois = new SimpleDateFormat("ddMMyyyyHHMMss").format(new Date());
				HashMap<Object, Object> param = new HashMap<Object, Object>();
				Double total = nbnon+nbnonok+nbok+nbbleu;
				Double pourOK = Double.valueOf((nbok+nbnon+nbbleu)/total);
				Double pourNONOK = Double.valueOf((nbnonok)/total);
				NumberFormat defaultFormat = NumberFormat.getPercentInstance();
				defaultFormat.setMinimumFractionDigits(1);

				param.put("ok", String.valueOf(nbok.intValue()));
				param.put("nonok", String.valueOf(nbnonok.intValue()));
				param.put("nbnon", String.valueOf(nbnon.intValue()));
				param.put("nbbleu", String.valueOf(nbbleu.intValue()));
				param.put("nb", String.valueOf(total.intValue()));
				param.put("pok", defaultFormat.format(pourOK));
				param.put("pnonok",defaultFormat.format(pourNONOK));
				String outFileName ="RapportMonitoring"+mois+".pdf";
				repportManager.ExportReportPDF(outFileName, param, collections, "RapportMonitoring.jasper");

				// envoi de Mail
				String heure = new SimpleDateFormat("dd/MM/yyyy HH:MM:ss").format(new Date());
				String subject = "Rapport de Monitoring des Gabs Afriland First Bank du "+heure;
				String messageCorps ="Bonjour ci joint le Rapport de Monitoring des Gabs du "+heure+" Merci ..";
				String from = "alerteGab@afrilandfirstbank.com"; 

				List<String> mailsTo = new ArrayList<String>();
				//GabParameter parameter = repportManager.findParameter("MAILS_REPORT_JOUR");
				if(user != null ){
					if(user.getEmail() != null && !user.getEmail() .trim().isEmpty() ){
						for(String mail : user.getEmail().split(",")){
							mailsTo.add(mail.trim());
						}
					}
				}
				List<String> mailsToCopy = new ArrayList<String>();
				SendFileEmail.SendMail(outFileName,repportManager.getReportsDir()+File.separator+outFileName, from, mailsTo,mailsToCopy, subject, messageCorps);

				// Save
				dao.saveList(collections,true);
			}

		}catch(Exception e){
			e.printStackTrace();
		}

	}
	

	/**
	 * 
	 */
	public  void process(String lastTime,String hour){

		try{

			//System.out.println("----RapportJourMonitoring---OK-----");
			AtmsLost = new ArrayList<Atm>();
			Atms = new ArrayList<Atm>();
			collections = new ArrayList<RapportElement>();
			//List<RapportElement> collections = new ArrayList<RapportElement>();
			//collections = processRapport();
			processRapport(lastTime,hour);
			Collections.sort(collections);
			//System.out.println("----RapportJourMonitoring---OK-----"+collections.size());
			if(!collections.isEmpty()){
				int i = 1;
				Double nbok = 0d;
				Double nbnonok = 0d;
				Double nbbleu = 0d;
				Double nbnon = 0d;
				for(RapportElement el : collections){
					if(el.getErreur().equalsIgnoreCase("OK"))nbok++;
					else if(el.getErreur().equalsIgnoreCase("KO")) nbnonok++;
					else if(el.getErreur().equalsIgnoreCase("LOOK")) nbnon++;
					else nbbleu++;
				}

				for(RapportElement el : collections){
					el.setColor(String.valueOf(i));
					i++;
				}

				String mois = new SimpleDateFormat("ddMMyyyyHHMMss").format(new Date());
				HashMap<Object, Object> param = new HashMap<Object, Object>();
				Double total = nbnon+nbnonok+nbok+nbbleu;
				Double pourOK = Double.valueOf((nbok+nbnon+nbbleu)/total);
				Double pourNONOK = Double.valueOf((nbnonok)/total);
				NumberFormat defaultFormat = NumberFormat.getPercentInstance();
				defaultFormat.setMinimumFractionDigits(1);

				param.put("ok", String.valueOf(nbok.intValue()));
				param.put("nonok", String.valueOf(nbnonok.intValue()));
				param.put("nbnon", String.valueOf(nbnon.intValue()));
				param.put("nbbleu", String.valueOf(nbbleu.intValue()));
				param.put("nb", String.valueOf(total.intValue()));
				param.put("pok", defaultFormat.format(pourOK));
				param.put("pnonok",defaultFormat.format(pourNONOK));
				String outFileName ="RapportMonitoring"+mois+".pdf";
				repportManager.ExportReportPDF(outFileName, param, collections, "RapportMonitoring.jasper");

				// envoi de Mail
				String heure = new SimpleDateFormat("dd/MM/yyyy HH:MM:ss").format(new Date());
				String subject = "Rapport de Monitoring des Gabs Afriland First Bank du "+heure;
				String messageCorps ="Bonjour ci joint le Rapport de Monitoring des Gabs du "+heure+" Merci ..";
				String from = "alerteGab@afrilandfirstbank.com"; 

				List<String> mailsTo = new ArrayList<String>();
				/**mailsTo.add("francis_konchou@afrilandfirstbank.com");
				mailsTo.add("maurice_temdemnou@afrilandfirstbank.com");
				mailsTo.add("hermann_tchokokap@afrilandfirstbank.com");
				mailsTo.add("maurice_ngankam@afrilandfirstbank.com");
				mailsTo.add("habib_noubissi@afrilandfirstbank.com");
				mailsTo.add("aumer_soufo@afrilandfirstbank.com");
				mailsTo.add("harauld_dnjiki@afrilandfirstbank.com");
				mailsTo.add("atsala@afrilandfirstbank.com");
				mailsTo.add("jp_yamcheu@afrilandfirstbank.com");*/
				GabParameter parameter = repportManager.findParameter("MAILS_REPORT_JOUR");
				if(parameter != null ){
					if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty()){
						for(String mail : parameter.getValue().split(",")){
							mailsTo.add(mail);
						}
					}
				}
				List<String> mailsToCopy = new ArrayList<String>();
				SendFileEmail.SendMail(outFileName,repportManager.getReportsDir()+File.separator+outFileName, from, mailsTo,mailsToCopy, subject, messageCorps);

				// Save
				dao.saveList(collections,true);
			}

		}catch(Exception e){
			e.printStackTrace();
		}

	}


	public Boolean ControlCassette(String lib,String poss){
		if(lib != null && !lib.trim().isEmpty()){
			String [] tabCass = lib.split(",");	
			for(String v : tabCass){
				v = v.trim();
				if(v.equalsIgnoreCase(poss.trim())) return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	public void processRapport(String lastTime,String hour){

		//List<RapportElement> elems = dao.filter(RapportElement.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("code",DateFormatUtils.format(new Date(),"ddmmyyyy")+"-"+lastTime)).add(Restrictions.eq("type", "ATM")),OrderContainer.getInstance().add(Order.desc("dateCrt")), null, 0, -1);
		//for(RapportElement el :elems)mapElement.put(el.getAge(),el);
		collections = new ArrayList<RapportElement>();
		Atms = new ArrayList<Atm>();
		AtmsLost = new ArrayList<Atm>();
		List<Atm> AllAtms = repportManager.findActiveAtm(Boolean.FALSE);
		processCheck(AllAtms,Boolean.TRUE,hour);

		//!AtmsLost.isEmpty() &&
		/**if(!AtmsLost.isEmpty()){
			System.out.println("-------AtmsLost--111----"+AtmsLost.size());
			Atms.addAll(AtmsLost);
			AtmsLost = new ArrayList<Atm>();
			processCheck(Atms,Boolean.TRUE,hour);
		}*/

		/** if(!AtmsLost.isEmpty()){
			System.out.println("-------AtmsLost--222----"+AtmsLost.size());
			Atms.addAll(AtmsLost);
			AtmsLost = new ArrayList<Atm>();
			processCheck(Atms,Boolean.TRUE,hour);
		}
		if(!AtmsLost.isEmpty()){
			System.out.println("-------AtmsLost--333----"+AtmsLost.size());
			Atms.addAll(AtmsLost);
			AtmsLost = new ArrayList<Atm>();
			processCheck(Atms,Boolean.TRUE,hour);
		}*/

		if(!AtmsLost.isEmpty()){
			//System.out.println("-------AtmsLost--444----"+AtmsLost.size());
			Atms.addAll(AtmsLost);
			AtmsLost = new ArrayList<Atm>();
			processCheck(Atms,Boolean.FALSE,hour);
		}

		// Controle de la génération 
		//System.out.println("-----LasReport.getDateCrt()---555-----");
		/**if(!mapElement.isEmpty()){
			for(RapportElement elem : collections){
				RapportElement el = mapElement.get(elem.getAge());
				if(el != null){
					if(Boolean.FALSE.equals(el.getCheckJournal()) && Boolean.FALSE.equals(el.getCheckImage())){
						if(Boolean.FALSE.equals(elem.getCheckJournal()) && Boolean.FALSE.equals(elem.getCheckImage())){
							elem.setEtat("NON OK");
						}
					}else{
						if(Boolean.FALSE.equals(elem.getCheckJournal()) && Boolean.FALSE.equals(elem.getCheckImage())){
							elem.setEtat("NON");
						}
					}
				}
			}
		}*/

	}


	List<RapportElement> collections = new ArrayList<RapportElement>();

	List<Atm> AtmsLost = new ArrayList<Atm>();

	List<Atm> Atms = new ArrayList<Atm>();

	Map<String, RapportElement> mapElement = new HashMap<String, RapportElement>();

	ConnexionSVFE svfe = new ConnexionSVFE();

	/**
	 * 
	 * @return List<RapportElement>
	 */
	public void processCheck(List<Atm> Atms , Boolean check,String lastTime){

		try{
			AtmsLost = new ArrayList<Atm>();
			svfe = new ConnexionSVFE();
			svfe.openSVFEConnection(repportManager.findDataSystem("SMVISTA"));
			RapportElement element = new RapportElement();
			String libelleAdmin = "";
			String sql = "";
			ResultSet rs ;
			int ii = 1;
			Boolean checkJournal = Boolean.FALSE;
			Boolean checkImage = Boolean.FALSE;

			Map<String,String> MapAtmHosService = new HashMap<String, String>();
			sql = " select pid,tid,remote_address,tid,street,city from def_tab,tcp_tab  " +
			" where tcp_tab.pid=def_tab.id and tid not in (select tid  from V_ATM_WARNING) order  by street ";
			rs = svfe.Execute(sql);
			while(rs.next()){
				MapAtmHosService.put(rs.getString("tid"),rs.getString("street"));
			}

			// System.out.println("-------processRapport------"+Atms.size());
			// parcour
			for(Atm value : Atms){

				try{

					checkJournal = Boolean.FALSE;
					checkImage = Boolean.FALSE;
					//System.out.println("------"+value.getNom());
					libelleAdmin = "";
					element = new RapportElement();
					element.setCode(DateFormatUtils.format(new Date(),"ddmmyyyy")+"-"+lastTime);
					element.setAge(value.getTid());
					element.setVille(value.getAdresse());
					element.setAtm(value.getNom());
					element.setColor(String.valueOf(ii));	
					element.setAtmPid(value.getPid());
					element.setAtmTid(value.getTid());
					element.setType(value.getTypeAtm().toString());
					element.setLibatm(value.getLibelle());
					ii++;

					// consultion du solde
					sql ="select ATM_PID,ATM_TID,INITIAL1,INITIAL2,INITIAL3,INITIAL4,CURRENT_BILLS1,CURRENT_BILLS2,CURRENT_BILLS3,CURRENT_BILLS4,DVAL1,DVAL2,DVAL3,DVAL4 "
						+ "from ATM_CASH "
						+ "where ATM_TID='"+value.getTid()+"' ";
					rs = svfe.Execute(sql);
					Integer c1_start_bills = 0;
					Integer c2_start_bills = 0;
					Integer c3_start_bills = 0;
					Integer c4_start_bills = 0;
					Integer c_c1bills = 0;
					Integer c_c2bills = 0;
					Integer c_c3bills = 0;
					Integer c_c4bills = 0;
					Integer SoldeInitial = 0;
					Integer SoldeRestant = 0;
					Boolean ruptureFond = Boolean.FALSE;
					Boolean horService = Boolean.FALSE;

					if(rs.next()){
						// debut
						c1_start_bills = rs.getInt("INITIAL1");
						c2_start_bills = rs.getInt("INITIAL2");
						c3_start_bills = rs.getInt("INITIAL3");
						c4_start_bills = rs.getInt("INITIAL4");

						// reste
						c_c1bills = rs.getInt("CURRENT_BILLS1");
						c_c2bills = rs.getInt("CURRENT_BILLS2");
						c_c3bills = rs.getInt("CURRENT_BILLS3");
						c_c4bills = rs.getInt("CURRENT_BILLS4");

						// Rejet 
						Integer n1 = rs.getInt("DVAL1");
						Integer n2 = rs.getInt("DVAL2");
						Integer n3 = rs.getInt("DVAL3");
						Integer n4 = rs.getInt("DVAL4");

						// calcul
						SoldeInitial =  (c1_start_bills*n1)+(c2_start_bills*n2)+(c3_start_bills*n3)+(c4_start_bills*n4);
						SoldeRestant = (c_c1bills*n1)+(c_c2bills*n2)+(c_c3bills*n3)+(c_c4bills*n4);
						if(SoldeRestant < 0) SoldeRestant = 0;

						// set Value
						if(SoldeRestant <= rupture  && SoldeInitial > 0 ){		
							libelleAdmin = libelleAdmin + " - Rupture de fonds "+"\n";
							element.setEtat("NON");
							ruptureFond = Boolean.TRUE;
						}
					}


					// controle de letat du ATM si l ATM communique avec le serveru monetique
					if(MapAtmHosService.containsKey(value.getTid()) && Boolean.FALSE.equals(ruptureFond)){
						libelleAdmin = libelleAdmin + "- Gab Hors Service "+CheckTransactionsAtm(value)+"\n";
						element.setEtat("KO");
						horService = Boolean.TRUE;
					}

					
					if(Boolean.FALSE.equals(horService)){
						// Controle Bourrage dans le Gab 
						sql="select * from V_ATM_DISABLE where tid='"+value.getTid()+"' and status_descx like '%bill%dispenser%'";
						rs = svfe.Execute(sql);
						int nbr = 0;
						String lib ="";
						String lib1 ="";
						String lib2 ="";
						String lib3 ="";
						String lib4 ="";
						String libcass = value.getLibcassete();
						while(rs.next()){					
							if(rs.getString("status_id").equalsIgnoreCase("First bill dispenser down") && ControlCassette(libcass, "1")){
								nbr ++;	lib1 = "1";
							}
							if(rs.getString("status_id").equalsIgnoreCase("Fourth bill dispenser down")){
								nbr ++;	lib4 = "4";
							}
							if(rs.getString("status_id").equalsIgnoreCase("Second bill dispenser down")){
								nbr ++;	lib2 = "2";
							}
							if(rs.getString("status_id").equalsIgnoreCase("Third bill dispenser down")){
								nbr ++;	lib3 = "3";
							}
						}

						lib =  lib1;
						if(!lib2.trim().isEmpty() && !lib.trim().isEmpty() && value.getNomnbreCass().intValue() > 1 ){
							lib = lib +"; "+ lib2;
						}else lib =  lib2;
						if(!lib3.trim().isEmpty() && !lib.trim().isEmpty() && value.getNomnbreCass().intValue() > 2 ){
							lib = lib +"; "+ lib3;
						}else lib =  lib3;
						if(!lib4.trim().isEmpty() && !lib.trim().isEmpty() && value.getNomnbreCass().intValue() > 3 ){
							lib = lib +"; "+ lib4;
						}else lib =  lib4;

						if(nbr > 0 ){
							if(nbr == value.getNomnbreCass().intValue() && Boolean.FALSE.equals(ruptureFond) ){
								libelleAdmin = libelleAdmin + "- Bourrage "+"\n";
								element.setEtat("KO");
							}else if(nbr > 1 && nbr < value.getNomnbreCass().intValue() && Boolean.FALSE.equals(ruptureFond)){
								libelleAdmin = libelleAdmin + "- Probleme sur les Caissettes ("+lib+") "+"\n";
								element.setEtat("NON");
							}else if(nbr == 1 && nbr < value.getNomnbreCass().intValue() && Boolean.FALSE.equals(ruptureFond)){
								libelleAdmin = libelleAdmin + "- Probleme sur la Caissette ("+lib+") "+"\n";
								element.setEtat("NON");
							}
						}
						// fin Controle Bourrage dans le Gab
					}

					// Clavier EPP
					sql="select ID,V.TID,STREET,CITY,status_descx from V_ATM_DISABLE V,ATM_DEF_VIEW A where V.PID=A.ID and V.tid='"+value.getTid()+"' and status_descx like '%Encryptor down%'";
					rs = svfe.Execute(sql);
					if(rs.next() &&  Boolean.FALSE.equals(horService)){
						//controle = false;
						libelleAdmin = libelleAdmin + "- Clavier EPP Bloqué dans le Gab"+"\n"; 
						element.setEtat("KO");
					}
					// fin Clavier EPP 

					//if(Boolean.FALSE.equals(horService)){

					// journal Gab 
					SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
					String journal = formater.format(new Date())+".jrn";
					String repImage = formater.format(new Date());
					File Chemin = null;
					//File CheminImg = null;
					//boolean journalOK = false;
					//boolean transactionAtm = false;
					int nbr = 0 ;
					String dateFormat = new SimpleDateFormat("yyyyMMdd").format(new Date());
					// Transaction
					sql = "select count(*) as nbr from CURR_TRANS where ATMID='"+value.getTid().trim()+"' and UDATE='"+dateFormat+"'";
					rs = svfe.Execute(sql);
					if(rs.next()){
						nbr = rs.getInt("nbr");
						//if(nbr > 0)transactionAtm = true;
					}

					// Controle du Ping
					/**if(Boolean.FALSE.equals(horService) ){
						boolean ping =  NetworkPing.isReachableByPing(value.getIp());
						if(ping == false){
							libelleAdmin = libelleAdmin + "- Rupture de la connexion reseau ; "+"\n";
							element.setEtat("NON");
						}
				    }*/

					if(nbr > 0){

						// AccessDirectorie
						int i = 0;
						while( Chemin == null && i < 3){
							Chemin  = AccessDirectorie(value);
							i++;
						}

						if(Chemin != null){

							// Autorisation sur le Gab 	
							try{
								Chemin = new File("\\\\"+value.getIp()+"\\c$\\JOURNAL");
								if(Chemin.exists()){
									Chemin = new File("\\\\"+value.getIp()+"\\c$\\JOURNAL\\"+journal);
									if(!Chemin.exists()){
										libelleAdmin = libelleAdmin + "- le Fichier Journal n'a pas été crée"; 
										String log = CheckJournalAtm(value);
										libelleAdmin = libelleAdmin + log +"\n"; 
										//libelleAdmin = libelleAdmin +"\n";
										if(Boolean.FALSE.equals(ruptureFond) && Boolean.FALSE.equals(horService)){
											element.setEtat("LOOK");
											//if(log.trim().isEmpty())element.setEtat("NON");
											//else element.setEtat("LOOK");
										}
										checkJournal = Boolean.FALSE;
									}else{
										if(Chemin.length() <= 1 ){
											libelleAdmin = libelleAdmin + "- le Fichier Journal crée est Vide ";
											String log = CheckJournalAtm(value);
											libelleAdmin = libelleAdmin + log +"\n"; 
											//libelleAdmin = libelleAdmin +"\n";
											if(Boolean.FALSE.equals(ruptureFond) && Boolean.FALSE.equals(horService)){
												element.setEtat("LOOK");
												//if(log.trim().isEmpty())element.setEtat("NON");
												//else element.setEtat("LOOK");
											}
										}else if(Chemin.length() > 1){
											libelleAdmin = libelleAdmin + "- Journal OK "+"\n"; 
										}
										checkJournal = Boolean.TRUE;	
									}
								}else if(!Chemin.exists()){
									checkJournal = Boolean.FALSE;
									//libelleAdmin = libelleAdmin + "- Le repertoire des Fichiers Journaux n'est pas accessible "+"\n";
									//element.setEtat("NON");
								}
							}catch(Exception ex){
								checkJournal = Boolean.FALSE;
								ex.printStackTrace();
							}


							// Autorisation sur le Gab
							try{
								Chemin = new File("\\\\"+value.getIp()+"\\c$\\VIDEOARCHIV");
								if(Chemin.exists()){
									Chemin = new File("\\\\"+value.getIp()+"\\c$\\VIDEOARCHIV\\"+repImage);
									if(!Chemin.exists()){
										libelleAdmin = libelleAdmin + "- le Repertoire des Images n'a pas été crée "; 
										String log = CheckImageAtm(value);
										libelleAdmin = libelleAdmin + log +"\n"; 
										if(Boolean.FALSE.equals(ruptureFond) && Boolean.FALSE.equals(horService) ){
											element.setEtat("LOOK");
											//if(log.trim().isEmpty())element.setEtat("NON");
											//else element.setEtat("LOOK");
										}
									}else{
										File[] files = Chemin.listFiles();
										if(files != null){
											int tail = files.length;
											if(tail == 0){
												libelleAdmin = libelleAdmin + "- Le Repertoire des Images crée est Vide "; 
												String log = CheckImageAtm(value);
												libelleAdmin = libelleAdmin + log +"\n"; 
												//libelleAdmin = libelleAdmin +"\n";
												if(Boolean.FALSE.equals(ruptureFond) && Boolean.FALSE.equals(horService) ){
													element.setEtat("LOOK");
													//if(log.trim().isEmpty())element.setEtat("NON");
													//else element.setEtat("LOOK");
												}
											}else if(tail > 0){
												libelleAdmin = libelleAdmin + "- Images OK "+"\n"; 
												checkImage = Boolean.TRUE;
											}		
											checkImage = Boolean.TRUE;
										}else{
											libelleAdmin = libelleAdmin + "- Images OK "+"\n"; 
											checkImage = Boolean.TRUE;
											//libelleAdmin = libelleAdmin + "- Le Repertoire des Images crée est Vide "+"\n"; 
											//libelleAdmin = libelleAdmin + CheckImageAtm(value);
										}
									}
								}else{
									checkImage = Boolean.FALSE;
									//libelleAdmin = libelleAdmin + "- Le repertoire des Images n'est pas accessible "+"\n";
									//element.setEtat("NON");
								}
							}catch(Exception ex){
								checkImage = Boolean.FALSE;
								ex.printStackTrace();
							}


							// detection des Erreur dans le fichier journal
							if(Boolean.TRUE.equals(checkJournal)){
								try{
									List<TypeIncident> incidentJounal =  repportManager.findTypeIncident(Boolean.TRUE);
									Chemin = new File("\\\\"+value.getIp()+"\\c$\\JOURNAL\\"+journal);
									String txtdebut="";
									txtdebut = new SimpleDateFormat("HH:mm:ss").format(DateUtils.addHours(new Date(),-1)).substring(0,2).trim();
									int debut = Integer.valueOf(txtdebut);
									txtdebut = String.valueOf(debut);
									if(txtdebut.length() == 1) txtdebut = "0"+txtdebut;
									if(Chemin != null){
										InputStream ips = new FileInputStream(Chemin); 
										if(ips != null){
											InputStreamReader ipsr = new InputStreamReader(ips);
											if(ipsr != null){
												BufferedReader br = new BufferedReader(ipsr);
												if(br != null){
													String ligne;
													while ((ligne = br.readLine())!= null){
														if(ligne.startsWith(txtdebut)){
															for(TypeIncident inci : incidentJounal){
																if(ligne != null && !ligne.trim().isEmpty()){
																	String codeErreur1 = "";
																	if(inci.getCodeErreur1() != null && !inci.getCodeErreur1().trim().isEmpty())codeErreur1 = inci.getCodeErreur1();
																	String codeErreur2 = "";
																	if(inci.getCodeErreur2() != null && !inci.getCodeErreur2().trim().isEmpty()) codeErreur2 = inci.getCodeErreur2();
																	if( (ligne.contains(codeErreur1) && !codeErreur1.trim().isEmpty()) || (ligne.contains(codeErreur2)  && !codeErreur2.trim().isEmpty()) ){
																		if(inci.getProbleme() != null && !inci.getProbleme().trim().isEmpty() && inci.getJournalError()!= null && inci.getJournalError() != null ){
																			if(!libelleAdmin.contains(inci.getProbleme()))libelleAdmin = libelleAdmin + "- "+inci.getProbleme()+"\n"; 
																			if(Boolean.TRUE.equals(inci.getJournalError())){
																				if(Boolean.FALSE.equals(ruptureFond) && Boolean.FALSE.equals(horService) ) element.setEtat("NON OK");
																			}else if(Boolean.TRUE.equals(inci.getJournalError())){
																				if(Boolean.FALSE.equals(ruptureFond) && Boolean.FALSE.equals(horService) ) element.setEtat("NON");
																			}
																		}
																	}
																}
															}
														}
													}
													br.close(); 
												}
											}
										}

									}
								}catch(Exception e){
									//e.printStackTrace();
								}
							}

						}else{
							libelleAdmin = libelleAdmin + "- Le repertoire des Fichiers Journaux n'est pas accessible "+"\n";
							libelleAdmin = libelleAdmin + "- Le repertoire des Images n'est pas accessible "+"\n";
							if(Boolean.FALSE.equals(ruptureFond) && Boolean.FALSE.equals(horService) ) element.setEtat("NON");
						}
					}else{
						if(Boolean.FALSE.equals(ruptureFond) && Boolean.FALSE.equals(horService) ){
							libelleAdmin = libelleAdmin + "- Aucune Transaction Sur le Gab "+"\n"; 
							/**String log = CheckJournalAtm(value);
							libelleAdmin = libelleAdmin + log +"\n"; 
							if(!log.trim().isEmpty())element.setEtat("LOOK");
							
							log = CheckImageAtm(value);
							libelleAdmin = libelleAdmin + log +"\n"; 
							if(!log.trim().isEmpty())element.setEtat("LOOK");*/
						}
						checkImage = Boolean.TRUE;
						checkJournal = Boolean.TRUE;
					}

					System.out.println("---"+libelleAdmin);
					//String script = "NET USE * /DELETE /YES";
					//Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", script});
					//}

					element.setCheckImage(checkImage);
					element.setCheckJournal(checkJournal);

					if(check.equals(Boolean.TRUE)){
						if(checkImage.equals(Boolean.TRUE) && checkJournal.equals(Boolean.TRUE)){
							if(libelleAdmin.trim().isEmpty()) libelleAdmin = "";
							//element.setErreur(libelleAdmin);
							element.setErreur(element.getEtat());
							element.setEtat(libelleAdmin);
							addAtm(element);
						}else{
							AtmsLost.add(value);
						}
					}else{
						if(libelleAdmin.trim().isEmpty()) libelleAdmin = "";
						//element.setErreur(libelleAdmin);
						element.setErreur(element.getEtat());
						element.setEtat(libelleAdmin);
						addAtm(element);
					}

				}catch(Exception ex){
					ex.printStackTrace();
					AtmsLost.add(value);
				}

			}

		}catch(SQLException ex){
			ex.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}

		//return collections;

	}


	public void addAtm(RapportElement el){
		boolean trouv = false;
		for(RapportElement v : collections){
			if(v.getAge().equalsIgnoreCase(el.getAge())) trouv = true;
		}
		if(!trouv)collections.add(el);
	}


	public String CheckTransactionsAtm(Atm value){
		String lib = "";
		try{
			boolean trouv = false;
			int i = 0;
			int nb = 0;
			Date date = DateUtils.addDays(new Date(),-1);
			while(!trouv && i < 30){
				int nbr = 0 ;
				String dateFormat = new SimpleDateFormat("yyyyMMdd").format(date);
				String sql = "select count(*) as nbr from CURR_TRANS where ATMID='"+value.getTid().trim()+"' and UDATE='"+dateFormat+"'";
				ResultSet rs = svfe.Execute(sql);
				if(rs.next()){
					nbr = rs.getInt("nbr");
				}
				if(nbr > 0){
					trouv = true;
				}else{
					nb++;
					date = DateUtils.addDays(date,-1);
				}
				i++;
			}
			if(nb > 3 ) lib = " Aucune transaction Depuis "+nb+" jours";
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lib;
	}
	

	public String CheckJournalAtm(Atm value){
		String lib = "";
		try{
			boolean trouv = false;
			SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
			int i = 0;
			int nb = 0;
			Date date = DateUtils.addDays(new Date(),-1);
			while(!trouv && i < 30){
				int nbr = 0 ;
				String dateFormat = new SimpleDateFormat("yyyyMMdd").format(date);
				String sql = "select count(*) as nbr from CURR_TRANS where ATMID='"+value.getTid().trim()+"' and UDATE='"+dateFormat+"'";
				ResultSet rs = svfe.Execute(sql);
				if(rs.next()){
					nbr = rs.getInt("nbr");
				}
				if(nbr > 0){
					String journal = formater.format(date)+".jrn";
					File Chemin = new File("\\\\"+value.getIp()+"\\c$\\JOURNAL");
					if(Chemin.exists()){
						Chemin = new File("\\\\"+value.getIp()+"\\c$\\JOURNAL\\"+journal);
						if(!Chemin.exists()){
							nb++;						
						}else{
							if(Chemin.length() == 0 ){
								nb++;
							}else if(Chemin.length() > 0){
								trouv = true;
							}
						}
						date = DateUtils.addDays(date,-1);
					}
				}
				i++;
			}
			if(nb > 1 )lib = " Depuis "+nb+" jours";
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lib;
	}


	public String CheckImageAtm(Atm value){
		String lib = "";
		try{
			boolean trouv = false;
			SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
			int i = 0;
			int nb = 0;
			Date date = DateUtils.addDays(new Date(),-1);
			while(!trouv && i < 30){	
				int nbr = 0 ;
				String dateFormat = new SimpleDateFormat("yyyyMMdd").format(date);
				String sql = "select count(*) as nbr from CURR_TRANS where ATMID='"+value.getTid().trim()+"' and UDATE='"+dateFormat+"'";
				ResultSet rs = svfe.Execute(sql);
				if(rs.next()){
					nbr = rs.getInt("nbr");
				}
				if(nbr > 0){
					String repImage = formater.format(date);
					File Chemin = new File("\\\\"+value.getIp()+"\\c$\\VIDEOARCHIV");
					if(Chemin.exists()){
						Chemin = new File("\\\\"+value.getIp()+"\\c$\\VIDEOARCHIV\\"+repImage);
						if(!Chemin.exists()){
							nb++;						
						}else{
							File[] files = Chemin.listFiles();
							if(files != null){
								int tail = files.length;
								if(tail == 0){
									nb++;
								}else if(tail > 0){
									trouv = true;
								}											
							}else{
								nb++;
							}						
						}
						date = DateUtils.addDays(date,-1);
					}
				}
				i++;
			}
			if(nb > 1 )lib = " Depuis "+nb+" jours";
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return lib;
	}


	private synchronized File AccessDirectorie(Atm value){
		try{
			String ip = value.getIp();
			//String login = value.getLogin().trim(); 
			//String password = value.getPsw().trim();
			String script = "net use * \\\\"+ip.trim()+"\\c$ /USER:administrateur password /PERSISTENT:NO";
			//if(password != null && login != null && !login.isEmpty() && !password.isEmpty()){
			//script = "net use * \\\\"+ip.trim()+"\\c$ /USER:"+login.trim()+" "+password.trim()+" /PERSISTENT:NO";
			//}
			Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "NET USE * /DELETE /YES"});
			Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", script});
			File Chemin = new File("\\\\"+value.getIp()+"\\c$");
			if(Chemin.exists()){
				return Chemin;
			}else{
				Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "NET USE * /DELETE /YES"});
				Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", script});
				Chemin = new File("\\\\"+value.getIp()+"\\c$");
				if(Chemin.exists()){
					return Chemin;
				}else{
					Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "NET USE * /DELETE /YES"});
					Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", script});
					Chemin = new File("\\\\"+value.getIp()+"\\c$");
					if(Chemin.exists()){
						return Chemin;
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		return null;
	}


	@Asynchronous
	public void InitLogCCJourEx(){

		try{
			List<Atm> AllAtms = repportManager.findActiveAtm(Boolean.FALSE);
			String dateForme = DateFormatUtils.format(new Date(),"ddMMyyyy");
			for(Atm val : AllAtms){
				File file = AccessDirectorie(val);
				if(file != null){
					String filename ="\\\\"+val.getIp()+"\\c$\\protopas\\WORK\\CCJourEx.sjp";
					file = new File(filename);
					if(file.exists() && file.isFile()){
						// Rename
						file.renameTo(new File(filename+"_"+dateForme));
						//Create
						file = new File(filename);
						file.createNewFile();
						//Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
						//writer.close();
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}


}