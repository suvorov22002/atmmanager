package com.afb.portal.buisness.monitoring.resident;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.time.DateUtils;

import afb.dsi.dpd.portal.jpa.entities.DataSystem;

import com.afb.portal.buisness.monitoring.worker.ConnexionSVFE;
import com.afb.portal.buisness.monitoring.worker.shared.IRepportManager;
import com.afb.portal.dao.shared.IAtmDAOLocal;
import com.afb.portal.jpa.alerte.SendFileEmail;
import com.afb.portal.jpa.gab.parameter.GabParameter;
import com.afb.portal.jpa.gab.parameter.Monitoring;
import com.afb.portal.jpa.gab.parameter.MonitoringType;
import com.afb.portal.webservcice.entities.SMSWeb;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED) 
public class NetworkServiceManager implements INetworkServiceManager{

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

	public static final String moduleCode = "GABM_001";

	public static final String moduleName ="MonitoringATM";

	private TimerTask task = null;

	private Timer timer = null;

	private DataSystem system = null;

	private Date starts;

	private GabParameter parameter;

	/**
	 * creerTimer
	 */
	public void process(){

		try{						
			// Heure de lancement 
			init();
		}catch(Exception e){
			e.printStackTrace();
			process();
		}
	}


	public void init(){

		try{						

			Integer val = 0;
			parameter = repportManager.findParameter("NETWORK_TIME");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					val = Integer.valueOf(parameter.getValue()); 
				}
			}

			if(val == 0)val = 2 ;
			// Heure de lancement 
			if(timer != null )timer.cancel();
			if(task != null )task.cancel();
			starts = new Date();
			task = new TimerTask(){
				@Override
				public void run(){
					try{ processWorker(); }catch(Exception e){e.printStackTrace();}
				}	
			};
			timer = new Timer(true);
			timer.schedule(task,DateUtils.addMinutes(starts, val),val*60*1000);
			//System.out.println(val+"------init---NETWORK----------------"+starts);
		}catch(Exception e){
			e.printStackTrace();
			process();
		}
	}

	private String user ="trans"; // username for remote host
	private String pwd ="trans123"; // password of the remote host
	private String host = "172.21.60.200"; // remote host address
	private int port = 22;

	/**
	 * process
	 */
	public void processWorker(){

		try{

			// Message 
			String message = "";
			parameter = repportManager.findParameter("NETWORK_MSG");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					message = parameter.getValue(); 
				}
			}

			// Telephone 
			String telephone = "";
			parameter = repportManager.findParameter("NETWORK_TEL");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					telephone = parameter.getValue(); 
				}
			}

			// EMAIL
			String email = "";
			parameter = repportManager.findParameter("NETWORK_EMAIL");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					email = parameter.getValue(); 
				}
			}

			//SMS
			String sms = "";
			parameter = repportManager.findParameter("NETWORK_SMS");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					sms = parameter.getValue(); 
				}
			}

			String msgLost = "";
			parameter = repportManager.findParameter("NETWORK_MSG_LOST");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					msgLost = parameter.getValue(); 
				}
			}

			//Script de lancement services VISA
			String script = "";
			parameter = repportManager.findParameter("NETWORK_SCRIPT");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					script = parameter.getValue(); 
				}
			}

			//parametre de connexion au serveur 
			String txtsvfe = "";
			parameter = repportManager.findParameter("SVFE");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					txtsvfe = parameter.getValue(); 
				}
			}
			String[] tabsvfe = txtsvfe.split(",");
			user = tabsvfe[0]; pwd = tabsvfe[1]; host = tabsvfe[2]; port = Integer.valueOf(tabsvfe[3]);

			String reseau = "";
			parameter = repportManager.findParameter("NETWORK");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					reseau = parameter.getValue(); 
				}
			}

			// envoi de Mail
			String subject = "Alerte Monitoring des Reseaux VISA et MasterCard";
			String messageCorps = null;
			String from = "alerteGab@afrilandfirstbank.com"; 
			List<String> mailsTo = new ArrayList<String>();
			for(String mail : email.split(",")){
				mailsTo.add(mail);
			}
			List<String> mailsToCopy = new ArrayList<String>();

			// Data System
			system = repportManager.findDataSystem("SMVISTA");
			ConnexionSVFE svfe = new ConnexionSVFE();
			svfe.openSVFEConnection(system);			
			String sql = "select RID,op_stat,proc_stat  from ntwk_stat  ";
			ResultSet rs = svfe.Execute(sql);
			SMSWeb smsWeb = null;
			Boolean lancer = Boolean.FALSE;
			
			while(rs.next()){
				if(rs.getInt("op_stat") != 0 || rs.getInt("proc_stat") != 1 ){

					/// parcour des differents reseaux 
					for(String code : reseau.split(",")){
						String codeReseau = code.split("-")[0];
						String nomReseau = code.split("-")[1];
						String txt = "";
						for(String phone : telephone.split(",")){
							if(rs.getString("RID").equalsIgnoreCase(codeReseau)){
								txt = sms.replaceFirst("@",nomReseau);
								smsWeb = new SMSWeb(moduleCode, moduleName, "ROOT", txt, phone);
								lancer = Boolean.TRUE;
								dao.save(smsWeb);
							}
						}
						if(rs.getString("RID").equalsIgnoreCase(codeReseau)){
							messageCorps = message.replaceFirst("@",nomReseau);
							SendFileEmail.SendMail("","", from, mailsTo,mailsToCopy, subject, messageCorps);
						}
						Monitoring mon = new Monitoring();
						if(rs.getString("RID").equalsIgnoreCase(codeReseau)){
							mon = new Monitoring(MonitoringType.NETWORK,nomReseau,host, nomReseau,telephone,email);
							dao.save(mon);
						}

						// Relance des Services
						ShellExecuter shell = new ShellExecuter(user, pwd, host, port);
						smsWeb = null;
						if(rs.getString("RID").equalsIgnoreCase(codeReseau)){
							shell.init();
							List<String> list = shell.executesvfe(script+" "+codeReseau);
							shell.exit();
							String result = "";
							for(String val : list){
								if(!val.trim().isEmpty()){
									result = result +""+ val.trim(); 
								}
							}
							for(String phone : telephone.split(",")){
								txt = script+" "+codeReseau+" Result: "+result;
								smsWeb = new SMSWeb(moduleCode, moduleName, "ROOT", txt, phone);
								dao.save(smsWeb);
							}
							messageCorps = script+" "+codeReseau+" Result: "+result;
							SendFileEmail.SendMail("","", from, mailsTo,mailsToCopy, subject, messageCorps);
						}						
					}
				}
			}

			// Check a nouveua si tout est OK
			if(Boolean.TRUE.equals(lancer)){
				rs = svfe.Execute(sql);
				while(rs.next()){
					if(rs.getInt("op_stat") != 0 || rs.getInt("proc_stat") != 1 ){
						/// parcour des differents reseaux 
						for(String code : reseau.split(",")){
							String codeReseau = code.split("-")[0];
							String nomReseau = code.split("-")[1];
							String txt = null;
							for(String phone : telephone.split(",")){
								if(rs.getString("RID").equalsIgnoreCase(codeReseau)){
									txt = msgLost.replaceFirst("@",nomReseau);
									smsWeb = new SMSWeb(moduleCode, moduleName, "ROOT", txt, phone);
									lancer = Boolean.TRUE;
									dao.save(smsWeb);
								}
							}
							if(rs.getString("RID").equalsIgnoreCase(codeReseau)){
								messageCorps = msgLost.replaceFirst("@", nomReseau);
								SendFileEmail.SendMail("","", from, mailsTo,mailsToCopy, subject, messageCorps);
							}
						}
					}
				}
			}
			
			svfe.closeSVFEConnection();
			init();

		}catch(Exception ex){
			ex.printStackTrace();
			init();
		}

	}

}
