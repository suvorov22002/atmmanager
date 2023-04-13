package com.afb.portal.buisness.monitoring.atm.worker;

import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import afb.dsi.dpd.portal.jpa.entities.DataSystem;
import afb.dsi.dpd.portal.jpa.entities.User;

import com.afb.portal.buisness.monitoring.worker.ConnexionSVFE;
import com.afb.portal.buisness.monitoring.worker.NetworkPing;
import com.afb.portal.buisness.monitoring.worker.shared.IMonitoringAtmWorker;
import com.afb.portal.buisness.monitoring.worker.shared.IRepportManager;
import com.afb.portal.dao.shared.IAtmDAOLocal;
import com.afb.portal.jpa.alerte.SendFileEmail;
import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.parameter.GabParameter;
import com.afb.portal.jpa.gab.parameter.TypeIncident;
import com.afb.portal.jpa.gab.statistisques.Incident;
import com.afb.portal.webservcice.entities.SMSWeb;
import com.yashiro.persistence.utils.collections.converters.ConverterUtil;

/**
 * 
 * @author Owner
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
public class MonitoringAtmWorker implements IMonitoringAtmWorker{

	/**
	 * Service DAO
	 */
	@EJB
	private IAtmDAOLocal dao ;

	public static final String moduleCode = "GABM_001";


	public static final String moduleName ="MonitoringATM";


	public static final String from = "alerteGab@afrilandfirstbank.com";


	public static final Integer rupture = 100000;

	/**
	 * Service Repport
	 */
	@EJB
	private IRepportManager repportManager ;
	
	private DataSystem system = null;
	
	private TimerTask task;
	private Timer timer;

	/**
	 * 
	 */
	public void creerTimer(){

		try{

			// boucle
			int sec = 60;
			int min = 30;

			if(timer != null)timer.cancel();
			if(task != null)task.cancel();
			
			task = new TimerTask(){
				@Override
				public void run(){
					try {
						process();
					}catch(Exception e){
						e.printStackTrace();
					}
				}	
			};

			timer = new Timer(true);
			timer.scheduleAtFixedRate(task, DateUtils.addMinutes(new Date(),5) , min*sec*1000);

		}catch(Exception e){
			// TODO: handle exception
			e.printStackTrace();
			creerTimer();
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
	 */
	public void process(){

		try{

			//System.out.println("----MonitoringATMWorker----ATM----");
			// Controle de l heure
			if(Boolean.FALSE.equals(repportManager.checkTime())) return;

			// chargment du fichier d erreur
			String libelleAdmin = "";
			String libelleSMS = "";
			TypeIncident typInci = null;
			String sql = "";
			ResultSet rs ;
			ConnexionSVFE svfe = new ConnexionSVFE();
			if(system == null )system = repportManager.findDataSystem("SMVISTA");
			svfe.openSVFEConnection(system);
			List<Atm> Atms = repportManager.findActiveAtm(Boolean.FALSE);

			Map<String,String> MapAtmHosService = new HashMap<String, String>();
			sql = "select pid,tid,remote_address,tid,street,city from def_tab,tcp_tab  " +
			" where tcp_tab.pid=def_tab.id and tid not in (select tid  from V_ATM_WARNING ) order  by street";
			rs = svfe.Execute(sql);
			while(rs.next()){
				MapAtmHosService.put(rs.getString("tid"),rs.getString("street"));
			}

			System.out.println("---------MonitoringAtmWorker-----------"+Atms.size());
			
			// parcour
			for(Atm value :Atms){

				//int n1 = 5000;
				//int n2 = 10000;

				// libelle = ""; 
				libelleAdmin = "";
				libelleSMS = "";
				Boolean horService = Boolean.FALSE;

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
						ruptureFond = Boolean.TRUE;
					}
				}

				// controle de letat du ATM si l ATM communique avec le serveru monetique
				sql="select * from V_WEB_ATM_STATUS where pid='"+value.getPid()+"' and STATUS_MSG like '%CLOSED%' ";
				if(rs.next()){
					if(rs.getString("STATUS_MSG").equalsIgnoreCase("CLOSED (IN SUPERVISOR MODE)")){						
						libelleSMS = value.getNom()+" est en mode Superviseur";
						for(User user : ConverterUtil.convertCollectionToList(typInci.getUsers())){
							if(user.getPhone() != null && !user.getPhone().trim().isEmpty() && !user.getPhone().startsWith("000")){
								SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", libelleSMS, user.getPhone());
								dao.save(sms);
							}	
						}
					}
				}

				// Status du Gab
				//sql="select * from V_ATM_WARNING where tid='"+value.getTid()+"'";
				//rs = svfe.Execute(sql); 
				//if(rs.next()){
				if(MapAtmHosService.containsKey(value.getTid())){	
					horService = Boolean.TRUE;
					// Archivage
					typInci = repportManager.findTypeIncident(Incident.HorsService);
					Incident inci = repportManager.findIncident(value, typInci);
					if(inci == null){
						inci = new Incident();
						inci.setAtm(value);
						inci.setTypeIncident(typInci);
						dao.save(inci);
						libelleAdmin = libelleAdmin + "- Gab Hors Service"+"\n";
					}else{
						inci.setNomber(inci.getNomber() + 1);
						dao.update(inci);
						libelleAdmin = libelleAdmin + " - Gab Hors Service"+"("+inci.getNomber().toString()+"/"+inci.getNomber().toString()+")"+"\n";
					}

					// Alerte
					libelleSMS = value.getNom()+" Hors Service";
					for(User user : ConverterUtil.convertCollectionToList(typInci.getUsers())){
						if(user.getPhone() != null && !user.getPhone().trim().isEmpty() && !user.getPhone().startsWith("000")){
							SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", libelleSMS, user.getPhone());
							dao.save(sms);
						}	
					}

					for(User user : ConverterUtil.convertCollectionToList(value.getUsers())){
						// SMS 
						if(user.getPhone() != null && !user.getPhone().trim().isEmpty() && !user.getPhone().startsWith("000")){
							SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", libelleSMS, user.getPhone());
							dao.save(sms);
						}						
					}
				}

				// Controle du Ping
				boolean ping =  NetworkPing.isReachableByPing(value.getIp());
				if(ping == false && Boolean.FALSE.equals(horService) ){
					// Archivage
					typInci = repportManager.findTypeIncident(Incident.ConnexionReseau);
					Incident inci = repportManager.findIncident(value, typInci);
					if(inci == null){
						inci = new Incident();
						inci.setAtm(value);
						inci.setTypeIncident(typInci);
						dao.save(inci);
						libelleAdmin = libelleAdmin + " - Rupture de la connexion reseau ; "+"\n";
					}else{
						inci.setNomber(inci.getNomber() + 1);
						dao.update(inci);
						libelleAdmin = libelleAdmin + " - Rupture de la connexion reseau ; "+"("+inci.getNomber().toString()+"/"+inci.getNomber().toString()+")"+"\n";
					}
				}


				if(Boolean.FALSE.equals(ruptureFond)){

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
						if(rscpt.getString("status_id").equalsIgnoreCase("Fourth bill dispenser down") && ControlCassette(libcass, "4") ){
							nbr ++;	lib4 = "4";
						}
						if(rscpt.getString("status_id").equalsIgnoreCase("Second bill dispenser down") && ControlCassette(libcass, "2") ){
							nbr ++;	lib2 = "2";
						}
						if(rscpt.getString("status_id").equalsIgnoreCase("Third bill dispenser down") && ControlCassette(libcass, "3") ){
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

							// Archivage
							typInci = repportManager.findTypeIncident(Incident.Bourrage);
							Incident inci = repportManager.findIncident(value, typInci);
							if(inci == null){
								inci = new Incident();
								inci.setAtm(value);
								inci.setTypeIncident(typInci);
								dao.save(inci);
								libelleAdmin = libelleAdmin + "-  Bourrage "+"\n";
							}else{
								inci.setNomber(inci.getNomber() + 1);
								dao.update(inci);
								libelleAdmin = libelleAdmin + " - Bourrage "+"("+inci.getNomber().toString()+"/"+inci.getNomber().toString()+")"+"\n";
							}

							libelleSMS = " Bourrage dans le "+value.getNom()+" ";
							for(User user : ConverterUtil.convertCollectionToList(typInci.getUsers())){
								if(user.getPhone() != null && !user.getPhone().trim().isEmpty() && !user.getPhone().startsWith("000")){
									SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", libelleSMS, user.getPhone());
									dao.save(sms);
								}	
							}

							for(User user : ConverterUtil.convertCollectionToList(value.getUsers())){
								// SMS 
								if(user.getPhone() != null && !user.getPhone().trim().isEmpty() && !user.getPhone().startsWith("000")){
									SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", libelleSMS, user.getPhone());
									dao.save(sms);
								}						
							}

						}else if(nbr > 1 && nbr < value.getNomnbreCass().intValue() && Boolean.FALSE.equals(ruptureFond)){

							// Archivage
							typInci = repportManager.findTypeIncident(Incident.ProblemeCaissettes);
							Incident inci = repportManager.findIncident(value, typInci);
							if(inci == null){
								inci = new Incident();
								inci.setAtm(value);
								inci.setTypeIncident(typInci);
								dao.save(inci);
								libelleAdmin = libelleAdmin + "- Probleme sur les Caissettes ("+lib+") "+"\n";
							}else{
								inci.setNomber(inci.getNomber() + 1);
								dao.update(inci);
								libelleAdmin = libelleAdmin + " - Probleme sur les Caissettes ("+lib+") "+"\n";
							}

						}else if(nbr == 1 && nbr < value.getNomnbreCass().intValue() && Boolean.FALSE.equals(ruptureFond)){

							// Archivage
							typInci = repportManager.findTypeIncident(Incident.ProblemeCaissettes);
							Incident inci = repportManager.findIncident(value, typInci);
							if(inci == null){
								inci = new Incident();
								inci.setAtm(value);
								inci.setTypeIncident(typInci);
								dao.save(inci);
								libelleAdmin = libelleAdmin + "-  Probleme sur la Caissette ("+lib+") "+"\n";
							}else{
								inci.setNomber(inci.getNomber() + 1);
								dao.update(inci);
								libelleAdmin = libelleAdmin + " - Probleme sur la Caissette ("+lib+") "+"\n";
							}

						}						
					}
					// fin Controle Bourrage dans le Gab
				}

				// Clavier EPP
				sql="select * from V_ATM_DISABLE V,ATM_DEF_VIEW A where V.PID=A.ID and V.tid='"+value.getTid()+"' and status_descx like '%Encryptor down%'";
				rs = svfe.Execute(sql);
				if(rs.next() && Boolean.FALSE.equals(ruptureFond) && Boolean.FALSE.equals(horService)){
					// Archivage
					typInci = repportManager.findTypeIncident(Incident.ClavierEPPBloque);
					Incident inci = repportManager.findIncident(value, typInci);
					if(inci == null){
						inci = new Incident();
						inci.setAtm(value);
						inci.setTypeIncident(typInci);
						dao.save(inci);
						libelleAdmin = libelleAdmin + "- Clavier EPP Bloqué dans le Gab "+"\n";
					}else{
						inci.setNomber(inci.getNomber() + 1);
						dao.update(inci);
						libelleAdmin = libelleAdmin + " - Clavier EPP Bloqué dans le Gab "+"("+inci.getNomber().toString()+"/"+inci.getNomber().toString()+")"+"\n";
					}

					libelleSMS = "Clavier EPP Bloqué dans le Gab sur le "+value.getNom()+" ";
					for(User user : ConverterUtil.convertCollectionToList(typInci.getUsers())){
						if(user.getPhone() != null && !user.getPhone().trim().isEmpty() && !user.getPhone().startsWith("000")){
							SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", libelleSMS, user.getPhone());
							dao.save(sms);
						}	
					}
				}
				// fin Clavier EPP  && Boolean.FALSE.equals(horService)

				// journal Gab 
				SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
				String journal = formater.format(new Date())+".jrn";
				String repImage = formater.format(new Date());
				File Chemin = null;
				boolean transactionAtm = false;
				String dateFormat = new SimpleDateFormat("yyyyMMdd").format(new Date());

				// Transaction
				sql = "select count(*) as nbr from CURR_TRANS where ATMID='"+value.getTid().trim()+"' and UDATE='"+dateFormat+"'";
				rs = svfe.Execute(sql);
				if(rs.next()){
					int nbr = rs.getInt("nbr");
					if(nbr > 0)transactionAtm = true;
				}

				/**String ip = value.getIp();
				String login = value.getLogin(); 
				String password = value.getPsw();
				String script = "net use * \\\\"+ip.trim()+"\\c$ /USER:administrateur password /PERSISTENT:NO";
				if(password != null && login != null && !login.isEmpty() && !password.isEmpty()){
					//script = "net use * \\\\"+ip.trim()+"\\c$ /USER:"+login.trim()+" "+password+" /PERSISTENT:NO";
				}*/

				if(Boolean.FALSE.equals(horService)){

					// Autorisation sur le Gab
					//Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", script});
					// Fin 

					try{
						Chemin = new File("\\\\"+value.getIp()+"\\c$\\JOURNAL");
						if(Chemin.isDirectory()){
							Chemin = new File("\\\\"+value.getIp()+"\\c$\\JOURNAL\\"+journal);
							if(!Chemin.exists()){
								if(transactionAtm == false){
									// Archivage
									typInci = repportManager.findTypeIncident(Incident.JOURNAL);
									Incident inci = repportManager.findIncident(value, typInci);
									if(inci == null){
										inci = new Incident();
										inci.setAtm(value);
										inci.setTypeIncident(typInci);
										dao.save(inci);
										libelleAdmin = libelleAdmin + "- le Fichier Journal n'a pas été crée "+"\n";
									}else{
										inci.setNomber(inci.getNomber() + 1);
										dao.update(inci);
										libelleAdmin = libelleAdmin + " - le Fichier Journal n'a pas été crée "+"("+inci.getNomber().toString()+"/"+inci.getNomber().toString()+")"+"\n";
									}

									libelleSMS = " le Fichier Journal n'a pas été crée sur le "+value.getNom()+" ";
									for(User user : ConverterUtil.convertCollectionToList(typInci.getUsers())){
										if(user.getPhone() != null && !user.getPhone().trim().isEmpty() && !user.getPhone().startsWith("000")){
											SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", libelleSMS, user.getPhone());
											dao.save(sms);
										}	
									}

								}								
							}else{

								if(Chemin.length() == 0){

									// Archivage
									typInci = repportManager.findTypeIncident(Incident.JOURNAL);
									Incident inci = repportManager.findIncident(value, typInci);

									if(inci == null){
										inci = new Incident();
										inci.setAtm(value);
										inci.setTypeIncident(typInci);
										dao.save(inci);
										libelleAdmin = libelleAdmin + "- Le Fichier Journal crée est Vide "+"\n";
									}else{
										inci.setNomber(inci.getNomber() + 1);
										dao.update(inci);
										libelleAdmin = libelleAdmin + " - Le Fichier Journal crée est Vide "+"("+inci.getNomber().toString()+"/"+inci.getNomber().toString()+")"+"\n";
									}

									libelleSMS = " Le Fichier Journal crée est Vide sur le "+value.getNom()+" ";
									for(User user : ConverterUtil.convertCollectionToList(typInci.getUsers())){
										if(user.getPhone() != null && !user.getPhone().trim().isEmpty() && !user.getPhone().startsWith("000")){
											SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", libelleSMS, user.getPhone());
											dao.save(sms);
										}	
									}

								}

							}
						}

					}catch (NullPointerException e){
						e.printStackTrace();
					}catch (Exception e){
						e.printStackTrace();
					}
				}



				// VIDEO Gab
				if(Boolean.FALSE.equals(horService)){

					// Autorisation sur le Gab 	
					//Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", script});
					// Fin 

					try{
						Chemin = new File("\\\\"+value.getIp()+"\\c$\\VIDEOARCHIV");
						if(Chemin.isDirectory()){
							Chemin = new File("\\\\"+value.getIp()+"\\c$\\VIDEOARCHIV\\"+repImage);
							if(!Chemin.exists()){
								if(transactionAtm == false){
									// Archivage
									typInci = repportManager.findTypeIncident(Incident.VIDEO);
									Incident inci = repportManager.findIncident(value, typInci);
									if(inci == null){
										inci = new Incident();
										inci.setAtm(value);
										inci.setTypeIncident(typInci);
										dao.save(inci);
										libelleAdmin = libelleAdmin + "- le Repertoire des Images n'a pas été crée "+"\n";
									}else{
										inci.setNomber(inci.getNomber() + 1);
										dao.update(inci);
										libelleAdmin = libelleAdmin + " - le Repertoire des Images n'a pas été crée "+"("+inci.getNomber().toString()+"/"+inci.getNomber().toString()+")"+"\n";
									}

									libelleSMS = "le Repertoire des Images n'a pas été crée sur le "+value.getNom()+" ";
									for(User user : ConverterUtil.convertCollectionToList(typInci.getUsers())){
										if(user.getPhone() != null && !user.getPhone().trim().isEmpty() && !user.getPhone().startsWith("000")){
											SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", libelleSMS, user.getPhone());
											dao.save(sms);
										}	
									}

								}
							}else{
								int tail = Chemin.listFiles().length;
								if(tail == 0){
									// Archivage
									typInci = repportManager.findTypeIncident(Incident.VIDEO);
									Incident inci = repportManager.findIncident(value, typInci);
									if(inci == null){
										inci = new Incident();
										inci.setAtm(value);
										inci.setTypeIncident(typInci);
										dao.save(inci);
										libelleAdmin = libelleAdmin + "- Le Repertoire des Images crée est Vide "+"\n";
									}else{
										inci.setNomber(inci.getNomber() + 1);
										dao.update(inci);
										libelleAdmin = libelleAdmin + " - Le Repertoire des Images crée est Vide "+"("+inci.getNomber().toString()+"/"+inci.getNomber().toString()+")"+"\n";
									}

									libelleSMS = "Le Repertoire des Images crée est Vide sur le "+value.getNom()+" ";
									for(User user : ConverterUtil.convertCollectionToList(typInci.getUsers())){
										if(user.getPhone() != null && !user.getPhone().trim().isEmpty() && !user.getPhone().startsWith("000")){
											SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", libelleSMS, user.getPhone());
											dao.save(sms);
										}	
									}

								}
							}
						}
					}catch(NullPointerException e){
						e.printStackTrace();
					}catch(Exception e){
						e.printStackTrace();
					}

					//script = "NET USE * /DELETE /YES";
					//Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", script});

				}

				if(!libelleAdmin.trim().isEmpty()){

					List<String> mailsTo = new ArrayList<String>();
					for(User user : ConverterUtil.convertCollectionToList(value.getUsers())){
						if(user.getEmail() != null && !user.getEmail().trim().isEmpty()){
							mailsTo.add(user.getEmail());
						}							
					}

					// Envoi de Mail du Rapport de Sauvegarde
					List<String> mailsToCC = new ArrayList<String>();
					GabParameter parameter = repportManager.findParameter("MAILS_REPORT_MONITORING");
					if(parameter != null ){
						if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
							for(String mail : parameter.getValue().split(",")){
								mailsToCC.add(mail);
							}
						}
					} 

					// MAIL
					String libelle ="Alerte sur le "+value.getNom();
					String resp = SendFileEmail.SendMailProvider(from, mailsTo, mailsToCC, libelle, libelleAdmin);
					if(!StringUtils.equalsIgnoreCase(resp, "200") && !StringUtils.equalsIgnoreCase(resp, "000")){
						mailsToCC = new ArrayList<String>();
						SendFileEmail.SendMailProvider(from, mailsTo, mailsToCC, libelle, libelleAdmin+"\n"+resp);
					}

				}

			}

		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException(ex.toString());
		}

	}


}
