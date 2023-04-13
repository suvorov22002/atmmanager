package com.afb.portal.buisness.monitoring.resident;

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

import com.afb.portal.buisness.monitoring.worker.shared.IRepportManager;
import com.afb.portal.dao.shared.IAtmDAOLocal;
import com.afb.portal.jpa.alerte.SendFileEmail;
import com.afb.portal.jpa.gab.parameter.GabParameter;
import com.afb.portal.jpa.gab.parameter.Monitoring;
import com.afb.portal.jpa.gab.parameter.MonitoringType;
import com.afb.portal.webservcice.entities.SMSWeb;


//-	Les statuts des résidents côté SmartVista et Amplitude, de les relancer dans le cas où les statuts sont différents de la normale
//-	L’état des services monétiques (atndnt_srv, nwint00, atmi00,atmi_wincor etc…) 
//-	L’espace disque sur le serveur monétique  OK
//-	Le statut des réseaux internationaux Visa et MasterCard.  OK

/**
 * @author Owner
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED) 
public class ResidentServiceManager implements IResidentServiceManager{

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
			parameter = repportManager.findParameter("SERVICE_TIME");
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
			parameter = repportManager.findParameter("SERVICE_MSG");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					message = parameter.getValue(); 
				}
			}

			// Telephone 
			String telephone = "";
			parameter = repportManager.findParameter("SERVICE_TEL");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					telephone = parameter.getValue(); 
				}
			}

			// EMAIL
			String email = "";
			parameter = repportManager.findParameter("SERVICE_EMAIL");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					email = parameter.getValue(); 
				}
			}

			//SMS
			String sms = "";
			parameter = repportManager.findParameter("SERVICE_SMS");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					sms = parameter.getValue(); 
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
			ShellExecuter shell = new ShellExecuter(user, pwd, host, port);
			shell.init();
			List<String> list = shell.executesvfe("tpstat");
			checkSpace(list, email, message, sms, "SVFE", telephone);
			shell.exit();
						
			
			init();

		}catch (Exception ex){
			ex.printStackTrace();
		}

	}

	
	private void checkSpace(List<String> liste,String email,String message,String sms, String disk,String telephone){
		
		String ser ="";
		for(String v :liste){
			if(v.contains("NOT RUNNING")){
				ser = ser+v.trim()+"\n";
			}
		}
		
		// envoi de Mail
		String subject = "Alerte Monitoring Services Monétiques ";
		String messageCorps = null;
		String from = "alerteGab@afrilandfirstbank.com"; 

		List<String> mailsTo = new ArrayList<String>();
		for(String mail : email.split(",")){
			mailsTo.add(mail);
		}
		List<String> mailsToCopy = new ArrayList<String>();
		
		/// EMAIl
		messageCorps = message.replaceFirst("@", disk);
		SendFileEmail.SendMail("","", from, mailsTo,mailsToCopy, subject, messageCorps+"\n"+ser);
		
		// SMS
		for(String phone : telephone.split(",")){
			String txt = sms.replaceFirst("@", disk);
			SMSWeb smsWeb = new SMSWeb(moduleCode, moduleName, "ROOT", txt+"\n"+ser, phone);
			dao.save(smsWeb);
		}

		// Log de l incident
		Monitoring mon = new Monitoring();
		mon = new Monitoring(MonitoringType.DISK_SPACE,disk,host,disk,telephone,email);
		dao.save(mon);
		
	}
	
}
