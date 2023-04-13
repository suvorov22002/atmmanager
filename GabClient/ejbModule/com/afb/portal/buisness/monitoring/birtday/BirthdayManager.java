package com.afb.portal.buisness.monitoring.birtday;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import afb.dsi.dpd.portal.jpa.entities.DataSystem;

import com.afb.portal.buisness.monitoring.worker.shared.IRepportManager;
import com.afb.portal.dao.shared.IAtmDAOLocal;
import com.afb.portal.jpa.alerte.Birthday;
import com.afb.portal.jpa.alerte.SendFileEmail;
import com.afb.portal.jpa.gab.parameter.GabParameter;
import com.afb.portal.webservcice.entities.SMSWeb;

/**
 * BirthdayManager
 * @author Owner
 * @version 1.0
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED) 
public class BirthdayManager implements IBirthdayManager{

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

	public static final String moduleName ="MonitoringATM_HBD";

	private TimerTask task = null;

	private Timer timer = null;

	private DataSystem system = null;

	private Date starts;
	
	private Connection conDB = null;
	private Statement stmDB;
	//private DataSystem dsDB = null;
	
	
	public Connection getSystemConnection(DataSystem system) throws Exception {
		Class.forName(system.getProviderClassName());
		return DriverManager.getConnection( system.getDbConnectionString(), system.getDbUserName(), system.getDbPassword() );
	}


	public ResultSet executeFilterSystemQuery(DataSystem ds, String query) throws Exception {
		ResultSet rs = null;
		conDB = getSystemConnection(ds);
		if(conDB != null){
			PreparedStatement ps = conDB.prepareStatement(query); // ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
			rs = ps.executeQuery();
		}
		return rs;
	}


	public ResultSet executeFilterSystemQuery(DataSystem ds, String query, Object[] parameters) throws Exception {

		ResultSet rs = null;
		conDB = getSystemConnection(ds);
		if(conDB != null){

			PreparedStatement ps = conDB.prepareStatement(query); //  ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY

			if(parameters != null && parameters.length > 0){

				int i = 1;
				for(Object o : parameters){
					if(o instanceof java.util.Date) ps.setDate(i, new java.sql.Date( ((java.util.Date)o).getTime() ) );
					else if(o instanceof java.sql.Date) ps.setDate(i, (java.sql.Date)o );
					else if(o instanceof Double) ps.setDouble(i, Double.valueOf( o.toString()) );
					else if(o instanceof Long) ps.setLong(i, Long.valueOf( o.toString()) );
					else if(o instanceof Integer) ps.setInt(i, Integer.valueOf( o.toString()) );
					else ps.setString(i, o.toString());
					i++;

				}

			}

			rs = ps.executeQuery();

		}

		return rs;

	}

	/**
	 * creerTimer
	 */
	public void process(){

		try{						
			// Heure de lancement 
			//init();

			starts = new Date();
			starts = DateUtils.addMinutes(starts, 2);
			task = new TimerTask(){
				@Override
				public void run(){
					try{ processWorker(); }catch(Exception e){e.printStackTrace();}
				}	
			};
			
			timer = new Timer(true);
			timer.schedule(task,starts,4*60*60*1000);
			
		}catch(Exception e){
			e.printStackTrace();
			process();
		}

	}


	protected void init(){

		try{
			getTime();
			starts = DateUtils.addMinutes(starts, 2);
			task = new TimerTask(){
				@Override
				public void run(){
					try{ processWorker(); }catch(Exception e){e.printStackTrace();}
				}	
			};
			timer = new Timer(true);
			timer.schedule(task,starts,4*60*60*1000);
		}catch(Exception e){
			e.printStackTrace();
			process();
		}

	}


	/**
	 * process
	 */
	public void processWorker(){

		try{

			// Message 
			String message = "";
			GabParameter parameter = repportManager.findParameter("BIRTHDAY_MSG");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					message = parameter.getValue(); 
				}
			}

			// Message des Stats 
			String messageStats = "";
			parameter = repportManager.findParameter("BIRTHDAY_MSG_STAT");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty()){
					messageStats = parameter.getValue(); 
				}
			}

			String messageStatsPhone = "";
			parameter = repportManager.findParameter("BIRTHDAY_MSG_STAT_PHONE");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty()){
					messageStatsPhone = parameter.getValue(); 
				}
			}

			String email = "";
			parameter = repportManager.findParameter("BIRTHDAY_MSG_STAT_EMAIL");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty()){
					email = parameter.getValue(); 
				}
			}

			// Delais d attente
			String temps = "";
			parameter = repportManager.findParameter("BIRTHDAY_DAYS");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty()){
					temps = parameter.getValue(); 
				}
			}

			String temps2 = "";
			parameter = repportManager.findParameter("BIRTHDAY_DAYS2");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty()){
					temps2 = parameter.getValue(); 
				}
			}

			String pan = "";
			parameter = repportManager.findParameter("BIRTHDAY_PAN");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty()){
					pan = parameter.getValue(); 
				}
			}

			// Data System
			system = repportManager.findDataSystem("SMVISTA_BO_BK");
			//ConnexionSVFE svfe = new ConnexionSVFE();
			//svfe.openSVFEConnection(system);
			
			String txtdate = DateFormatUtils.format(DateUtils.addDays(new Date(),+Integer.valueOf(temps)), "dd/MM/yyyy");
			String txtdate2 = DateFormatUtils.format(DateUtils.addDays(new Date(),+Integer.valueOf(temps2)), "dd/MM/yyyy");
			//String txtdate2 = DateFormatUtils.format(new Date(), "dd/MM/yyyy");
									
			String sql = " select ict.card_num, ict.mem_no,ict.expir_date, ict.crdh_name,addr_cs.phone1, addr_cs.phone3,addr4 "+
			" from addr_tab addr_cs,iss_customer_tab icstt,iss_card_tab ict,iss_ref_tab irt,card_type_tab ctt "+
			" where icstt.address_id = addr_cs.address_id and irt.iss_customer_id = icstt.customer_id " +
			" and ict.card_num = irt.iss_card_num and ctt.card_type = ict.card_int_type " +
			" and ict.card_status<>'CRST3' and ict.inst_id='0001'  " +
			" and SUBSTR(ict.card_num,1,6) in ("+pan+") and to_date(to_char(ict.expir_date,'dd/MM/yyyy'),'dd/MM/yyyy') between '"+txtdate2+"' and '"+txtdate+"'  "+
			" order by ict.card_num ";
			
			System.out.println("----------------SQL--------------"+sql);
			ResultSet rs = executeFilterSystemQuery(system, sql); //  new Object[]{pan,txtdate2,txtdate}  svfe.Execute(sql);
		   			
			Integer compteurOK = 0;
			Integer compteurLost = 0;
			while(rs.next()){
				String phone  = rs.getString("phone1");
				if(phone == null) phone  = rs.getString("phone3");
				if(phone == null) phone  = rs.getString("addr4");
				String name = rs.getString("crdh_name");
				String num = rs.getString("card_num");
				Date date = rs.getDate("expir_date");
				Birthday b = new Birthday(num, name, phone);
				//System.out.println("----------------phone---------------"+phone);
				if(phone != null && !phone.trim().isEmpty() && phone.trim().length()>= 8 ){
					int tail = num.replace(" ","").trim().length();
					if(phone.trim().length() == 8) phone = "6"+phone;
					String numCahe = tail == 16 ? num.substring(0,6).concat("XXXXXX").concat(num.substring(tail-4,tail)) : num.substring(0,6).concat("XXXXXX");
					String msg = message.replaceFirst("@",name.trim()).replaceFirst("@",numCahe.trim()).replaceFirst("@",DateFormatUtils.format(date, "dd/MM/yyyy"));
					SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", msg, phone.replace(" ",""));
					dao.save(sms);
					compteurOK++;
				}else{
					b.setSend(Boolean.FALSE);
					compteurLost++;
				}
				dao.save(b);
			}
			//svfe.closeSVFEConnection();

			// Send Message Stat ENvois 
			//if(compteurOK != 0 || compteurLost != 0){
			String date = DateFormatUtils.format(new Date(), "dd/MM/yyyy");
			String heure = DateFormatUtils.format(new Date(), "HH:mm:ss");
			Integer total = compteurOK+compteurLost;
			String msg = messageStats.replaceFirst("@",date).replaceFirst("@",heure).replaceFirst("@",compteurOK.toString()).replaceFirst("@",compteurLost.toString()).replaceFirst("@",total.toString());
			for(String phone : messageStatsPhone.split(",")){
				if(phone.trim().length() == 8) phone = "6"+phone;
				SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", msg, phone.replace(" ",""));
				dao.save(sms);
			}
			
			List<String> mailsTo = new ArrayList<String>();
			for(String mail : email.split(",")){
				mailsTo.add(mail);
			}
			List<String> mailsToCopy = new ArrayList<String>();
			String subject = "Alerte Statistiques date anniversaire cartes prapayées";
			String from = "alerteGab@afrilandfirstbank.com"; 
			String resp = SendFileEmail.SendMailProvider(from, mailsTo,mailsToCopy, subject, msg);
			if(!StringUtils.equalsIgnoreCase(resp, "200") && !StringUtils.equalsIgnoreCase(resp, "000")){
				mailsToCopy = new ArrayList<String>();
				SendFileEmail.SendMailProvider(from, mailsTo, mailsToCopy, subject, msg+"\n"+resp);
			}
			//}
			
			if(conDB != null ) conDB.close();
			if(stmDB != null ) stmDB.close();
			if(timer != null )timer.cancel();
			if(task != null )task.cancel();
			init();

		}catch(Exception ex){
			ex.printStackTrace();
		}

	}


	public Date getTime(){

		try{

			String temps = "";
			GabParameter parameter = repportManager.findParameter("BIRTHDAY_TIME");
			if(parameter != null){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty()){
					temps = parameter.getValue(); 
				}
			}
			Integer time = 0 ;
			if(!temps.trim().isEmpty())time  = Integer.valueOf(temps);
			GregorianCalendar cal = new GregorianCalendar();
			cal.set(Calendar.HOUR_OF_DAY,time);
			cal.set(Calendar.MINUTE,0);
			cal.set(Calendar.MILLISECOND,0);
			Date date = cal.getTime();
			date = DateUtils.addDays(date,1);
			starts =  date;
			System.out.println("----------------starts--------------"+starts);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return starts;

	}

}
