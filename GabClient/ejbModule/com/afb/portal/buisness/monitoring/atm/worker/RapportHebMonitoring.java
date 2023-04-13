package com.afb.portal.buisness.monitoring.atm.worker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.time.DateUtils;

import com.afb.portal.buisness.atmManage.parameter.ServiceStarted;
import com.afb.portal.buisness.monitoring.worker.AccessDirectory;
import com.afb.portal.buisness.monitoring.worker.ConnexionSVFE;
import com.afb.portal.buisness.monitoring.worker.NetworkPing;
import com.afb.portal.buisness.monitoring.worker.shared.IRapportHebMonitoring;
import com.afb.portal.jpa.alerte.SendFileEmail;
import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.monitoring.RapportElement;
import com.afb.portal.jpa.gab.parameter.GabParameter;
import com.afb.portal.jpa.gab.parameter.TypeIncident;


/**
 * 
 * @author Owner
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class RapportHebMonitoring  implements IRapportHebMonitoring{

	public static final Integer rupture = 100000;

	private Boolean lance = Boolean.FALSE;

	private String hour = "";

	private String lashour = "";

	/**
	 * creerTimer
	 */
	//@Asynchronous
	public void creerTimer(){

		try{

			// boucle
			int sec = 60;
			int min = 15;

			TimerTask task = new TimerTask(){
				@Override
				public void run(){
					try {
						runMonitoring();
					}catch(Exception e){
						e.printStackTrace();
					}
				}	
			};

			Timer timer = new Timer(true);
			timer.scheduleAtFixedRate(task, DateUtils.addMinutes(new Date(),1) , min*sec*1000);

		}catch(Exception e){
			// TODO: handle exception
			e.printStackTrace();
			creerTimer();
		}

	}


	public void runMonitoring(){

		try{
			
			lance = Boolean.FALSE;
			String times =  ServiceStarted.repportManager.findTimeMonitoring();
			String mois = new SimpleDateFormat("HH:mm:ss").format(new Date());
			//System.out.println("----RapportJourMonitoring--------"+mois);

			GregorianCalendar calendar = new GregorianCalendar();

			if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){

				if(times != null){
					for(String time : times.split(",")){
						if(mois.startsWith(time)){
							lance = Boolean.TRUE; 
							hour = time;
						}
					}

					if(lashour.trim().isEmpty() && Boolean.TRUE.equals(lance)){
						lashour = hour;
						process();
					}else if( Boolean.TRUE.equals(lance) && !lashour.startsWith(hour)){
						lashour = hour;
						process();
					}
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
	public  void process(){

		try{

			// /System.out.println("----RapportJourMonitoring---OK-----");

			List<RapportElement> collections = new ArrayList<RapportElement>();
			collections = processRapport();
			Collections.sort(collections);
			int i = 1;
			int nbok = 0;
			int nbnonok = 0;
			int nbnon = 0;
			for(RapportElement el : collections){
				el.setColor(String.valueOf(i));
				i++;
				if(el.getEtat().equalsIgnoreCase("OK"))nbok++;
				else if(el.getEtat().equalsIgnoreCase("NON OK")) nbnonok++;
				else nbnon++;
			}

			String mois = new SimpleDateFormat("ddMMyyyyHHMMss").format(new Date());
			HashMap<Object, Object> param = new HashMap<Object, Object>();
			param.put("ok", String.valueOf(nbok));
			param.put("nonok", String.valueOf(nbnonok));
			param.put("nbnon", String.valueOf(nbnon));
			String outFileName ="RapportMonitoring"+mois+".pdf";
			ServiceStarted.repportManager.ExportReportPDF(outFileName, param, collections, "RapportMonitoring.jasper");

			// envoi de Mail
			String heure = new SimpleDateFormat("dd/MM/yyyy HH:MM:ss").format(new Date());
			String subject = "Rapport de Monitoring des Gabs Afriland First Bank du "+heure;
			String messageCorps ="Bonjour ci joint le Rapport de Monitoring des Gabs du "+heure+" Merci ..";
			String from = "alerteGab@afrilandfirstbank.com"; 

			List<String> mailsTo = new ArrayList<String>();
			GabParameter parameter = ServiceStarted.repportManager.findParameter("MAILS_REPORT_JOUR");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					for(String mail : parameter.getValue().split(",")){
						mailsTo.add(mail);
					}
				}
			}
			SendFileEmail.SendMail(ServiceStarted.repportManager.getReportsDir()+File.separator+"RapportMonitoring"+mois+".pdf", from, mailsTo,null, subject, messageCorps);

		} catch (Exception e) {
			// TODO Auto-generated catch block
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

	/**
	 * 
	 * @return
	 */
	public List<RapportElement> processRapport(){

		List<RapportElement> collections = new ArrayList<RapportElement>();

		try{

			ConnexionSVFE svfe = new ConnexionSVFE();
			svfe.openSVFEConnection(ServiceStarted.repportManager.findDataSystem("SMVISTA"));
			List<Atm> Atms = ServiceStarted.repportManager.findActiveAtm();
			RapportElement element = new RapportElement();
			String libelleAdmin = "";
			String sql = "";
			ResultSet rs ;
			int ii = 1;

			// parcour
			for(Atm value :Atms){

				int n1 = 5000;int n2 = 10000;
				element = new RapportElement();
				element.setAge(value.getAgence().getCode());
				element.setAtm(value.getNom());
				element.setColor(String.valueOf(ii));
				ii++;
				element.setEtat("OK");

				// consultion du solde
				sql ="select tid, street,city,c1_start_bills,c2_start_bills,c3_start_bills,c4_start_bills, c_c1bills,c_c2bills,c_c3bills,c_c4bills,c_c1brej,c_c2brej,c_c3brej,c_c4brej "
					+ "from ctr_TAB a , def_tab b "
					+ "where a.pid=b.id and tid='"+value.getTid()+"' and c1_start_bills is not null";
				rs = svfe.Execute(sql);

				Integer c1_start_bills = 0;
				Integer c2_start_bills = 0;
				Integer c3_start_bills = 0;
				Integer c4_start_bills = 0;
				Integer c_c1bills = 0;
				Integer c_c2bills = 0;
				Integer c_c3bills = 0;
				Integer c_c4bills = 0;
				Integer c_c1brej = 0;
				Integer c_c2brej = 0;
				Integer c_c3brej = 0;
				Integer c_c4brej = 0;

				Integer SoldeInitial = 0;
				Integer SoldeRestant = 0;
				Boolean ruptureFond = Boolean.FALSE;
				Boolean horService = Boolean.FALSE;

				if(rs.next()){

					// debut
					c1_start_bills = rs.getInt("c1_start_bills");
					c2_start_bills = rs.getInt("c2_start_bills");
					c3_start_bills = rs.getInt("c3_start_bills");
					c4_start_bills = rs.getInt("c4_start_bills");

					// reste
					c_c1bills = rs.getInt("c_c1bills");
					c_c2bills = rs.getInt("c_c2bills");
					c_c3bills = rs.getInt("c_c3bills");
					c_c4bills = rs.getInt("c_c4bills");

					// Rejet 
					c_c1brej = rs.getInt("c_c1brej");
					c_c2brej = rs.getInt("c_c2brej");
					c_c3brej = rs.getInt("c_c3brej");
					c_c4brej = rs.getInt("c_c4brej");

					// calcul
					SoldeInitial =  (c1_start_bills*n2)+(c2_start_bills*n1)+(c3_start_bills*n2)+(c4_start_bills*n1);
					SoldeRestant = ((c1_start_bills-c_c1bills-c_c1brej)*n2)+((c2_start_bills-c_c2bills-c_c2brej)*n1)+((c3_start_bills-c_c3bills-c_c3brej)*n2)+((c4_start_bills-c_c4bills-c_c4brej)*n1);
					if(SoldeRestant < 0) SoldeRestant = 0;

					// set Value
					if(SoldeRestant <= rupture  && SoldeInitial > 0 ){		
						libelleAdmin =libelleAdmin + " - Rupture de fonds "+"\n";
						element.setEtat("NON");
						ruptureFond = Boolean.TRUE;
					}
				}

				// controle de letat du ATM si l ATM communique avec le serveru monetique
				sql="select * from V_WEB_ATM_STATUS where pid='"+value.getPid()+"' and STATUS_MSG like '%CLOSED%' ";
				rs = svfe.Execute(sql);
				if(!rs.next()){
					if(!rs.getString("STATUS_MSG").equalsIgnoreCase("CLOSED (IN SUPERVISOR MODE)") && SoldeInitial > 0 ){	
						libelleAdmin =libelleAdmin + "- Gab Hors Service"+"\n";
						horService = Boolean.TRUE;
						element.setEtat("NON OK");
					}
				}

				// Controle du Ping
				boolean ping =  NetworkPing.isReachableByPing(value.getAdresse());
				if(ping == false && Boolean.FALSE.equals(horService) && Boolean.FALSE.equals(ruptureFond)){
					libelleAdmin = libelleAdmin + "- Rupture de la connexion reseau ; "+"\n";
					element.setEtat("NON");
				}

				if(Boolean.FALSE.equals(ruptureFond)){
					// calcul
					c_c1bills = c1_start_bills - c_c1bills;
					c_c2bills = c2_start_bills - c_c2bills;
					c_c3bills = c3_start_bills - c_c3bills;
					c_c4bills = c4_start_bills - c_c4bills;

					// Controle Bourrage dans le Gab 
					sql="select * from V_ATM_DISABLE where tid='"+value.getTid()+"' and status_descx like '%bill%dispenser%'";
					ResultSet rscpt = svfe.Execute(sql);
					int nbr = 0;
					String lib ="";
					String lib1 ="";
					String lib2 ="";
					String lib3 ="";
					String lib4 ="";
					String libcass = value.getLibcassete();
					while(rscpt.next()){					
						if(rscpt.getString("status_id").equalsIgnoreCase("First bill dispenser down") && ControlCassette(libcass, "1")){
							nbr ++;	lib1 = "1";
						}
						if(rscpt.getString("status_id").equalsIgnoreCase("Fourth bill dispenser down")){
							nbr ++;	lib4 = "4";
						}
						if(rscpt.getString("status_id").equalsIgnoreCase("Second bill dispenser down")){
							nbr ++;	lib2 = "2";
						}
						if(rscpt.getString("status_id").equalsIgnoreCase("Third bill dispenser down")){
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

					if(nbr > 0 || c_c1bills == 0 || c_c2bills == 0 || c_c3bills == 0 || c_c4bills == 0 ){
						if(nbr == value.getNomnbreCass().intValue() && Boolean.FALSE.equals(ruptureFond) ){
							libelleAdmin = libelleAdmin + "- Bourrage "+"\n";
							element.setEtat("NON OK");
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
				if(rs.next() && Boolean.FALSE.equals(ruptureFond) && Boolean.FALSE.equals(horService) ){
					//controle = false;
					libelleAdmin = libelleAdmin + "- Clavier EPP Bloqué dans le Gab"+"\n"; 
					element.setEtat("NON OK");
				}
				// fin Clavier EPP  && Boolean.FALSE.equals(horService)

				// journal Gab 
				SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
				String journal = formater.format(new Date())+".jrn";
				String repImage = formater.format(new Date());
				File Chemin = null;
				boolean journalOK = false;
				boolean transactionAtm = false;
				String dateFormat = new SimpleDateFormat("yyyyMMdd").format(new Date());

				// Transaction
				sql = "select count(*) as nbr from CURR_TRANS where ATMID='"+value.getTid().trim()+"' and UDATE='"+dateFormat+"'";
				rs = svfe.Execute(sql);
				if(rs.next()){
					int nbr = rs.getInt("nbr");
					if(nbr > 0)transactionAtm = true;
				}

				// Autorisation sur le Gab 	
				AccessDirectory.procces(value.getIp(), value.getLogin(),value.getPsw());
				// Fin 

				if(Boolean.FALSE.equals(horService) ){
					try{
						Chemin = new File("\\\\"+value.getAdresse()+"\\c$\\JOURNAL");
						if(Chemin.isDirectory()){
							Chemin = new File("\\\\"+value.getAdresse()+"\\c$\\JOURNAL\\"+journal);
							if(!Chemin.exists()){
								if(transactionAtm == false){
									libelleAdmin = libelleAdmin + "- le Fichier Journal n'a pas été crée"+"\n"; 
									element.setEtat("NON");
								}else{
									if(Boolean.FALSE.equals(ruptureFond)) libelleAdmin = libelleAdmin + "- Aucune Transaction Sur le Gab "+"\n"; 
								}								
							}else{
								if(Chemin.length() == 0){
									libelleAdmin = libelleAdmin + "- le Fichier Journal crée est Vide "+"\n";
									element.setEtat("NON");
								}else if(Chemin.length() > 0){
									libelleAdmin = libelleAdmin + "- Journal OK "+"\n"; 
								}
							}
						}else if(!Chemin.exists()){
							libelleAdmin = libelleAdmin + "- Le repertoire des Fichiers Journaux n'est pas accessible "+"\n";
							element.setEtat("NON");
						}
					}catch (NullPointerException e){
						e.printStackTrace();
					}catch (Exception e){
						e.printStackTrace();
					}

					// Autorisation sur le Gab 	
					AccessDirectory.procces(value.getIp(), value.getLogin(),value.getPsw());
					// Fin 

					try{
						Chemin = new File("\\\\"+value.getAdresse()+"\\c$\\VIDEOARCHIV");
						if(Chemin.isDirectory()){
							Chemin = new File("\\\\"+value.getAdresse()+"\\c$\\VIDEOARCHIV\\"+repImage);
							if(!Chemin.exists()){
								if(transactionAtm == false){
									libelleAdmin = libelleAdmin + "- le Repertoire des Images n'a pas été crée "+"\n"; 
									element.setEtat("NON");
								}
							}else{
								int tail = Chemin.listFiles().length;
								if(tail == 0){
									libelleAdmin = libelleAdmin + "- Le Repertoire des Images crée est Vide "+"\n"; 
									element.setEtat("NON");
								}else if(tail > 0){
									libelleAdmin = libelleAdmin + "- Images OK "+"\n"; 
								}
							}
						}else{
							libelleAdmin = libelleAdmin + "- Le repertoire des Images n'est pas accessible "+"\n";
						}
					}catch(NullPointerException e){
						e.printStackTrace();
					}catch(Exception e){
						e.printStackTrace();
					}

					// Autorisation sur le Gab 	
					AccessDirectory.procces(value.getIp(), value.getLogin(),value.getPsw());
					// Fin 

					// detection des Erreur dans le fichier journal
					if(journalOK && Boolean.FALSE.equals(horService)){

						List<TypeIncident> incidentJounal =  ServiceStarted.repportManager.findTypeIncident(Boolean.TRUE);
						Chemin = new File("\\\\"+value.getAdresse()+"\\c$\\JOURNAL\\"+journal);
						String txtdebut="";
						txtdebut = new SimpleDateFormat("HH:mm:ss").format(new Date()).substring(0,2).trim();
						int debut = Integer.valueOf(txtdebut);
						if(debut > 5 ){
							debut = debut - 2;
							txtdebut = String.valueOf(debut);
							if(txtdebut.length() == 1) txtdebut = "0"+txtdebut;
						}

						try{
							InputStream ips = new FileInputStream(Chemin); 
							InputStreamReader ipsr = new InputStreamReader(ips);
							BufferedReader br = new BufferedReader(ipsr);
							String ligne;
							while ((ligne = br.readLine())!= null){
								if(ligne.startsWith(txtdebut)){
									for(TypeIncident inci : incidentJounal){
										if(ligne.contains(inci.getCodeErreur1()) || ligne.contains(inci.getCodeErreur2() )){
											if(!libelleAdmin.contains(inci.getProbleme()))libelleAdmin = libelleAdmin + "- "+inci.getProbleme()+"\n"; 
											if(Boolean.TRUE.equals(inci.getJournalError())){
												element.setEtat("NON OK");
											}else if(Boolean.TRUE.equals(inci.getJournalError())){
												element.setEtat("NON");
											}
										}
									}
								}
							}
							br.close(); 
						}catch(Exception e){
							e.printStackTrace();
						}

					}
					if(libelleAdmin.trim().isEmpty()) libelleAdmin = "";
					element.setErreur(libelleAdmin);
					collections.add(element);	

				}

			}

		}catch(SQLException ex){
			ex.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}

		return collections;

	}

}