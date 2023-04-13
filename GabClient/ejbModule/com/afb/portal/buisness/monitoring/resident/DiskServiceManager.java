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

/**
 * 
 * @author Owner
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED) 
public class DiskServiceManager  implements IDiskServiceManager {

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
			parameter = repportManager.findParameter("DISK_TIME");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					val = Integer.valueOf(parameter.getValue()); 
				}
			}
			if(val == 0) val = 2 ;
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

			System.out.println(val+"------init---DiskServiceManager----------------"+starts);

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
			parameter = repportManager.findParameter("DISK_MSG");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					message = parameter.getValue(); 
				}
			}
			//System.out.println("---------DISK_MSG----------------"+message);

			// Telephone 
			String telephone = "";
			parameter = repportManager.findParameter("DISK_TEL");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					telephone = parameter.getValue(); 
				}
			}
			//System.out.println("---------DISK_TEL----------------"+telephone);

			// EMAIL
			String email = "";
			parameter = repportManager.findParameter("DISK_EMAIL");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					email = parameter.getValue(); 
				}
			}
			//System.out.println("---------DISK_EMAIL----------------"+email);

			//SMS
			String sms = "";
			parameter = repportManager.findParameter("DISK_SMS");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					sms = parameter.getValue(); 
				}
			}
			//System.out.println("---------DISK_SMS----------------"+sms);

			//Script de lancement services VISA
			String tail = "";
			parameter = repportManager.findParameter("DISK_TAIL");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					tail = parameter.getValue(); 
				}
			}
			//System.out.println("---------DISK_TAIL----------------"+tail);
			
			//Script de lancement services VISA
			String disk = "";
			parameter = repportManager.findParameter("DISK");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					disk = parameter.getValue(); 
				}
			}

			
			for(String code : disk.split(",")){
				
				System.out.println(code+"---------DISK_TAIL----------------"+tail);
				
				//parametre de connexion au serveur 
				String txtsvfe = "";
				parameter = repportManager.findParameter(code);
				if(parameter != null ){
					if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
						txtsvfe = parameter.getValue(); 
					}
				}
				String[] tabsvfe = txtsvfe.split(",");
				user = tabsvfe[0]; pwd = tabsvfe[1]; host = tabsvfe[2]; port = Integer.valueOf(tabsvfe[3]);
				ShellExecuter shell = new ShellExecuter(user, pwd, host, port);
				shell.init();
				List<String> list = shell.execute("df -h");
				for(String v : list){
					System.out.println(code+"---------DISK_TAIL----------------"+v);
				}
				checkSpace(list, tail, email, message, sms, code, telephone);
				shell.exit();
				
			}
			
			
			/**
			txtsvfe = "";
			parameter = repportManager.findParameter("SVBO");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					txtsvfe = parameter.getValue(); 
				}
			}
			tabsvfe = txtsvfe.split(",");
			user = tabsvfe[0]; pwd = tabsvfe[1]; host = tabsvfe[2]; port = Integer.valueOf(tabsvfe[3]);
			shell = new ShellExecuter(user, pwd, host, port);
			shell.init();
			list = shell.execute("df -h");
			checkSpace(list, tail, email, message, sms, "SVBO", telephone);
			shell.exit();

			// Relance des Services
			txtsvfe = "";
			parameter = repportManager.findParameter("SVCG");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					txtsvfe = parameter.getValue(); 
				}
			}
			tabsvfe = txtsvfe.split(",");
			user = tabsvfe[0]; pwd = tabsvfe[1]; host = tabsvfe[2]; port = Integer.valueOf(tabsvfe[3]);
			shell = new ShellExecuter(user, pwd, host, port);
			shell.init();
			list = shell.execute("df -h");
			checkSpace(list, tail, email, message, sms, "SVCG", telephone);
			shell.exit();*/

			init();

		}catch (Exception ex){
			ex.printStackTrace();
		}

	}

	
	private boolean control(String chaine , Integer val){
		boolean trouv = false ;
		Integer num = new Integer(val);
		while(!trouv){
			trouv = control(chaine, String.valueOf(num)+"%");
			//System.out.println(chaine+"---1--------"+String.valueOf(num)+"%"+"----control------------"+trouv);
			if(num == 100) return trouv;
			if(!trouv && num < 100){
				num++;
			}
		}
		return trouv;
	}

	
	private boolean control(String chaine , String val){
		if(chaine.contains(val))return true;
		return false;
	}

	
	private String check(List<String> liste,String tail){
		String space = "";
		for(String v :  liste){
			//System.out.println("-------------Exe----------------"+v);
			if(v!= null && !v.trim().isEmpty()){
				if(control(v, Integer.valueOf(tail))) space = space+v+"\n";
			}
		}
		return space;
	}

	private void checkSpace(List<String> liste, String tail,String email,String message,String sms, String disk,String telephone){

		//System.out.println("-----------------checkSpace-----------------"+disk);
		String space = check(liste, tail);
		
		//System.out.println("-----------------space-----------------"+space);

		if(!space.trim().isEmpty()){
			// envoi de Mail
			String subject = "Alerte Monitoring Espace disk "+disk;
			String messageCorps = null;
			String from = "alerteGab@afrilandfirstbank.com"; 

			List<String> mailsTo = new ArrayList<String>();
			for(String mail : email.split(",")){
				mailsTo.add(mail);
			}
			List<String> mailsToCopy = new ArrayList<String>();

			/// EMAIl
			messageCorps = message.replaceFirst("@", disk);
			SendFileEmail.SendMail("","", from, mailsTo,mailsToCopy, subject, messageCorps+"/n"+space);

			// SMS
			for(String phone : telephone.split(",")){
				String txt = sms.replaceFirst("@", disk+"/n"+space);
				SMSWeb smsWeb = new SMSWeb(moduleCode, moduleName, "ROOT", txt, phone);
				dao.save(smsWeb);
			}

			// Log de l incident
			Monitoring mon = new Monitoring();
			mon = new Monitoring(MonitoringType.DISK_SPACE,disk,host,disk,telephone,email);
			dao.save(mon);
		}

	}

}
