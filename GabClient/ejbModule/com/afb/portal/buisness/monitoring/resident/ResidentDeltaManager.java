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

/**
 * ResidentDeltaManager
 * @author Owner
 * @version 1.0
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED) 
public class ResidentDeltaManager implements IResidentDeltaManager{

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

	public static final String moduleName ="MonitoringATM_RESIDENT";

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
			parameter = repportManager.findParameter("RESIDENT_TIME");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					val = Integer.valueOf(parameter.getValue()); 
				}
			}

			if(val == 0)val = 1 ;
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
			parameter = repportManager.findParameter("RESIDENT_MSG");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					message = parameter.getValue(); 
				}
			}
			
			String messageLost = "";
			parameter = repportManager.findParameter("RESIDENT_MSG_LOST");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					messageLost = parameter.getValue(); 
				}
			}

			// Telephone 
			String telephone = "";
			parameter = repportManager.findParameter("RESIDENT_TEL");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					telephone = parameter.getValue(); 
				}
			}

			// EMAIL
			String email = "";
			parameter = repportManager.findParameter("RESIDENT_EMAIL");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					email = parameter.getValue(); 
				}
			}

			//SMS
			String sms = "";
			parameter = repportManager.findParameter("RESIDENT_SMS");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					sms = parameter.getValue(); 
				}
			}
			
			String smsLost = "";
			parameter = repportManager.findParameter("RESIDENT_SMS_LOST");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					smsLost = parameter.getValue(); 
				}
			}
			
			// envoi de Mail
			String subject = "Alerte Monitoring du Résident Monétique & Amplitude";
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
			String sql = "select op_stat,proc_stat  from host_stat_tab  ";
			ResultSet rs = svfe.Execute(sql);
			SMSWeb smsWeb = null;
			Boolean resident = Boolean.TRUE;
			//Boolean lancer = Boolean.FALSE;
			while(rs.next()){
				if(rs.getInt("op_stat") != 1 || rs.getInt("proc_stat") != 2 ){
					resident = Boolean.FALSE;
				}
			}
			
			// Check 
			if(Boolean.FALSE.equals(resident)){

				Boolean LISTEN = Boolean.FALSE;
				Boolean ESTABLISHED = Boolean.FALSE;
				Monitoring mon = null;
				// Test si la liaison est toujours OK avec le resident Delta
				// Test du Resident Delta
				String srcAmp = "";
				parameter = repportManager.findParameter("DELTA");
				if(parameter != null ){
					if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty()){
						srcAmp = parameter.getValue(); 
					}
				}
				
				/**String srcSVFE = "";
				parameter = repportManager.findParameter("SVFE");
				if(parameter != null ){
					if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
						srcSVFE = parameter.getValue(); 
					}
				}*/
				
				List<String> list = executeDelta(srcAmp, "./status_online");
				for(String val : list){
					if(!val.trim().isEmpty()){
						if(val.contains("LISTEN")){
							LISTEN = Boolean.TRUE;
						}
						if(val.contains("ESTABLISHED")){
							ESTABLISHED = Boolean.TRUE;
						}
					}
				}
				
				// le serveur Amplitude n' escoute pas 
				if(Boolean.FALSE.equals(LISTEN) && Boolean.FALSE.equals(ESTABLISHED) ){
					
					//lancer = Boolean.TRUE;
					// Demarage du Resident cote Amplitude 
					// Stop Resident Amplitude
					//executeDelta(srcAmp, "stop_online");

					// Start Resident Amplitude
					list = executeDelta(srcAmp, "./start_online");
					mon = new Monitoring(MonitoringType.RESIDENT_AMP,"RESIDENT_AMP",host, "RESIDENT_AMP",telephone,email);
					dao.save(mon);
					
					Thread.sleep(10*60*1000);
					
					list = executeDelta(srcAmp, "./status_online");
					for(String val : list){
						if(!val.trim().isEmpty()){
							if(val.contains("LISTEN")){
								LISTEN = Boolean.TRUE;
							}else if(val.contains("ESTABLISHED")){
								ESTABLISHED = Boolean.TRUE;
							}
						}
					}
					
					/**if(Boolean.TRUE.equals(LISTEN) && Boolean.FALSE.equals(ESTABLISHED)){
						// Demarage du resident Monetique
						executeSVFE(srcSVFE, "tcpdevice_reload -d 6000");
						mon = new Monitoring(MonitoringType.RESIDENT_MONETIQUE,"RESIDENT_MONETIQUE",host, "RESIDENT_MONETIQUE",telephone,email);
						dao.save(mon);
					}*/
										
				}else if(Boolean.TRUE.equals(LISTEN) && Boolean.FALSE.equals(ESTABLISHED)){
					/**lancer = Boolean.TRUE;
					// Demarage du resident Monetique
					executeSVFE(srcSVFE, "tcpdevice_reload -d 6000");
					mon = new Monitoring(MonitoringType.RESIDENT_MONETIQUE,"RESIDENT_MONETIQUE",host, "RESIDENT_MONETIQUE",telephone,email);
					dao.save(mon);*/
				}

				// Envoi des Alerte
				// SMS
				for(String phone : telephone.split(",")){
					if(!phone.trim().isEmpty()){
						smsWeb = new SMSWeb(moduleCode, moduleName, "ROOT", sms, phone);
						dao.save(smsWeb);
					}
				}
				// Email					
				messageCorps = message;
				SendFileEmail.SendMail("","", from, mailsTo,mailsToCopy, subject, messageCorps);

			}
			
			// Test si tout est OK
			/**if(Boolean.TRUE.equals(lancer)){
				rs = svfe.Execute(sql);
				resident = Boolean.TRUE;
				while(rs.next()){
					if(rs.getInt("op_stat") != 1 || rs.getInt("proc_stat") != 2 ){
						resident = Boolean.FALSE;
					}
				}
				if(Boolean.FALSE.equals(resident)){
					// Envoi des Alerte
					// SMS
					for(String phone : telephone.split(",")){
						if(!phone.trim().isEmpty()){
							smsWeb = new SMSWeb(moduleCode, moduleName, "ROOT", smsLost, phone);
							dao.save(smsWeb);
						}
					}
					// Email					
					messageCorps = messageLost;
					SendFileEmail.SendMail("","", from, mailsTo,mailsToCopy, subject, messageCorps);
				}
			}*/
			
			svfe.closeSVFEConnection();
			init();

		}catch (Exception ex){
			ex.printStackTrace();
		}

	}
	
	
	public List<String> executeDelta(String txt , String script){
		String[] tab = txt.split(",");
		user = tab[0]; pwd = tab[1]; host = tab[2]; port = Integer.valueOf(tab[3]);
		ShellExecuter shell = new ShellExecuter(user, pwd, host, port);
		shell.init();
		List<String> list = shell.executeDelta(script);
		shell.exit();
		return list;		
	}
	
	public List<String> executeSVFE(String txt , String script){
		String[] tab = txt.split(",");
		user = tab[0]; pwd = tab[1]; host = tab[2]; port = Integer.valueOf(tab[3]);
		ShellExecuter shell = new ShellExecuter(user, pwd, host, port);
		shell.init();
		List<String> list = shell.executesvfe(script);
		shell.exit();
		return list;		
	}
	

}
