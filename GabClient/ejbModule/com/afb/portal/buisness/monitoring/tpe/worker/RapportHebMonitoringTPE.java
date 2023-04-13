package com.afb.portal.buisness.monitoring.tpe.worker;

import java.io.File;
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
import java.util.concurrent.TimeUnit;

import javax.ejb.AccessTimeout;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.time.DateUtils;

import com.afb.portal.buisness.atmManage.parameter.ServiceStarted;
import com.afb.portal.buisness.monitoring.worker.ConnexionSVFE;
import com.afb.portal.buisness.monitoring.worker.shared.IRapportHebMonitoringTPE;
import com.afb.portal.jpa.alerte.SendFileEmail;
import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.monitoring.RapportElement;
import com.afb.portal.jpa.gab.parameter.GabParameter;
import com.afb.portal.jpa.gab.parameter.TypeIncident;
import com.afb.portal.jpa.gab.statistisques.Incident;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class RapportHebMonitoringTPE implements IRapportHebMonitoringTPE{

	public static final String from = "alerteTPE@afrilandfirstbank.com";

	/**
	 * Service Repport
	 */
	//@EJB
	//private IRepportManager repportManager;


	public static final Integer rupture = 100000;

	private Boolean lance = Boolean.FALSE;

	private String hour = "";

	private String lashour = "";

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
			timer.scheduleAtFixedRate(task, DateUtils.addMinutes(new Date(),5) , min*sec*1000);

		}catch(Exception e){
			// TODO: handle exception
			e.printStackTrace();
			creerTimer();
		}

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@AccessTimeout(unit=TimeUnit.MINUTES, value=30)
	public void runMonitoring(){

		try{
			lance = Boolean.FALSE;
			String times =  ServiceStarted.repportManager.findTimeMonitoringTPE();
			String mois = new SimpleDateFormat("HH:mm:ss").format(new Date());

			GregorianCalendar calendar = new GregorianCalendar();
			
			//System.out.println("---Rapport Heb ----Day "+calendar.get(Calendar.DAY_OF_WEEK)+"-----FRIDAY-------"+Calendar.FRIDAY);
			
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
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@AccessTimeout(unit=TimeUnit.MINUTES, value=30)
	public  void process(){

		try{

			nbOK = 0d;
			nbNONOK = 0d;
			
			List<RapportElement> collections = new ArrayList<RapportElement>();
			collections = processRapport();
			Collections.sort(collections);
			if(collections.isEmpty())return ;
			int i = 1;
			for(RapportElement el : collections){
				el.setColor(String.valueOf(i));i++;
			}

			Double total = nbNONOK + nbOK;
			Double snbNONOK = (nbNONOK / total) * 100 ;
			Double snbOK  = 100 - snbNONOK;
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String mois = new SimpleDateFormat("ddMMyyyyHHMMss").format(new Date());
			HashMap<Object, Object> param = new HashMap<Object, Object>();
			param.put("nbOK", String.valueOf(nbOK));
			param.put("nbNONOK", String.valueOf(nbNONOK));
			param.put("snbOK", String.valueOf(snbOK));
			param.put("snbNONOK", String.valueOf(snbNONOK));
			
			param.put("tittre", "REPPORT HEBDOMADAIRE DE MONITORING DES TPES");
			param.put("sousTittre", "DU "+format.format(DateUtils.addDays(new Date(),-8))+" AU "+format.format(new Date()));
			String outFileName ="RapportHebMonitoringTPE"+mois+".pdf";
			ServiceStarted.repportManager.ExportReportPDF(outFileName, param, collections, "RapportMonitoringTPE.jasper");

			// envoi de Mail
			String heure = new SimpleDateFormat("dd/MM/yyyy HH:MM:ss").format(new Date());
			String subject = "Rapport Hebdomadaire de Monitoring des TPEs du "+heure;
			String messageCorps ="Bonjour ci joint le Rapport de Monitoring des TPES du "+format.format(DateUtils.addDays(new Date(),-8))+" au "+format.format(new Date())+" Merci ..";
			//String from = "alerteGab@afrilandfirstbank.com"; 

			List<String> mailsTo = new ArrayList<String>();
			GabParameter parameter = ServiceStarted.repportManager.findParameter("MAILS_MONITORING_TPE");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					for(String mail : parameter.getValue().split(",")){
						mailsTo.add(mail);
					}
				}
			}
			List<String> mailscc = new ArrayList<String>();
			if(!mailsTo.isEmpty())SendFileEmail.SendMail(outFileName,ServiceStarted.repportManager.getReportsDir()+File.separator+outFileName, from, mailsTo,mailscc, subject, messageCorps);
			
		}catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	Double nbOK = 0d;
	Double nbNONOK = 0d;
	
	/**
	 * 
	 * @return
	 */
	public List<RapportElement> processRapport(){

		List<RapportElement> collections = new ArrayList<RapportElement>();

		try{
			
			// Controle de l heure
			if(Boolean.FALSE.equals(ServiceStarted.repportManager.checkTime())) return new ArrayList<RapportElement>();

			ConnexionSVFE svfe = new ConnexionSVFE();
			svfe.openSVFEConnection(ServiceStarted.repportManager.findDataSystem("SMVISTA"));
			List<Atm> Atms = ServiceStarted.repportManager.findActiveTPE();
			RapportElement element = new RapportElement();
			//String libelleAdmin = "";
			//String sql = "";
			//ResultSet rs ;
			int ii = 1;
			
			TypeIncident typInci = ServiceStarted.repportManager.findTypeIncident(Incident.ConnexionReseau);

			// parcour
			for(Atm value :Atms){

				// Transaction  Gab 
				if(value.getTid() != null && !value.getTid().trim().isEmpty()){
					
					//libelleAdmin = "";
					element = new RapportElement();
					element.setAge(value.getTid());
					element.setAtm(value.getNom());
					element.setColor(String.valueOf(ii));
					ii++;
					//element.setEtat("OK");

					//SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
					Date curentDate = new Date();
					
					/**
					String datedebut = formater.format(DateUtils.addDays(curentDate,-8));
					String datefin = formater.format(curentDate);

					// Transaction
					sql = "select count(*) as nbr from CURR_TRANS where ATMID='"+value.getTid().trim()+"' and UDATE between '"+datedebut+"' and '"+datefin+"'";
					rs = svfe.Execute(sql);
					int nbr = 0;
					if(rs.next()){
						nbr = rs.getInt("nbr");
					}

					if(nbr == 0){
						element.setStatus("Aucune Transaction");
						libelleAdmin = libelleAdmin + "- Aucune Transaction."+"\n";	
						element.setEtat("AT");
						
					}else{
						element.setEtat("T");
						element.setStatus("Avec des Transactions ");
						if(nbr == 1)libelleAdmin = libelleAdmin + "- "+nbr+" Transaction."+"\n";
						else libelleAdmin = libelleAdmin + "- "+nbr+" Transactions."+"\n";
					}*/
					
					List<Incident> incis = ServiceStarted.repportManager.findIncident(value, typInci,DateUtils.addDays(curentDate,-8),curentDate);
					if(!incis.isEmpty()){
						element.setEtat("NON OK");
						element.setErreur("NON OK");
						nbOK++;
					}else{
						element.setEtat("OK");
						element.setErreur("OK");
						nbOK++;
					}
					collections.add(element);
				}

			}

			svfe.closeSVFEConnection();
			
		}catch(SQLException ex){
			ex.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}

		return collections;

	}
	
}
