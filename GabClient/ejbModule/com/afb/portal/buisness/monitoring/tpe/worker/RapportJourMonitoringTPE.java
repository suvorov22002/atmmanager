package com.afb.portal.buisness.monitoring.tpe.worker;

import java.io.File;
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
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.time.DateUtils;

import afb.dsi.dpd.portal.jpa.entities.User;

import com.afb.portal.buisness.monitoring.worker.ConnexionSVFE;
import com.afb.portal.buisness.monitoring.worker.NetworkPing;
import com.afb.portal.buisness.monitoring.worker.shared.IRapportJourMonitoringTPE;
import com.afb.portal.buisness.monitoring.worker.shared.IRepportManager;
import com.afb.portal.dao.shared.IAtmDAOLocal;
import com.afb.portal.jpa.alerte.SendFileEmail;
import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.monitoring.RapportElement;
import com.afb.portal.jpa.gab.parameter.GabParameter;
import com.afb.portal.jpa.gab.parameter.TypeIncident;
import com.afb.portal.jpa.gab.statistisques.Incident;


/**
 * RapportJourMonitoring
 * @author Owner
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class RapportJourMonitoringTPE implements IRapportJourMonitoringTPE {

	public String from = "alertetpe@afrilandfirstbank.com";

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
	public static final Double fixing = 70d;

	private Boolean lance = Boolean.FALSE;
	private String hour = "";
	private String lashour = "";
	public  Double nbOK = 0d;
	public  Double nbNONOK = 0d;
	public  Double nbVert = 0d;
	public  Double nbVertCiel = 0d;
	public  Double nbBleu = 0d;

	private TimerTask task;
	private Timer timer;

	/**
	 * creerTimer
	 */
	public void creerTimer(){

		try{

			this.lashour = "";
			this.lance = Boolean.FALSE;

			// boucle
			int sec = 60;
			int min = 1;
			if(this.timer != null)this.timer.cancel();
			if(this.task != null)this.task.cancel();

			this.task = new TimerTask(){
				@Override
				public void run(){
					try{
						runMonitoring();
					}catch(Exception e){
						e.printStackTrace();
					}
				}	
			};

			this.timer = new Timer(true);
			this.timer.schedule(this.task, DateUtils.addMinutes(new Date(),1) , min*sec*1000);

		}catch(Exception e){
			// TODO: handle exception
			e.printStackTrace();
			creerTimer();
		}

	}

	public void runMonitoring(){

		try{

			this.lance = Boolean.FALSE;
			String times =  this.repportManager.findTimeMonitoringTPE();
			String mois = new SimpleDateFormat("HH:mm:ss").format(new Date());

			//times = "12:45,17:45,112:40";

			//System.out.println(times+"----RapportJourMonitoring----TPE----"+mois);
			if(times != null){
				for(String time : times.split(",")){
					if(mois.startsWith(time)){
						this.lance = Boolean.TRUE; 
						this.hour = time;
					}
				}

				if(this.lashour.trim().isEmpty() && Boolean.TRUE.equals(this.lance)){
					this.lashour = this.hour;
					process();
				}else if( Boolean.TRUE.equals(this.lance) && !this.lashour.startsWith(this.hour)){
					this.lashour = this.hour;
					process();
				}
			}

			//process();

		}catch(Exception e){
			// TODO: handle exception
			e.printStackTrace();
		}

	}


	/**
	 * 
	 */
	@Asynchronous
	public void process(User user){

		try{

			this.nbOK = 0d;
			this.nbNONOK = 0d;
			this.nbVert = 0d;
			this.nbVertCiel = 0d;
			this.nbBleu = 0d;

			//System.out.println("---Rapport Journalier de Monitoring---TPE---GO-");
			List<RapportElement> collections = new ArrayList<RapportElement>();
			collections = processRapport();
			Collections.sort(collections);
			//System.out.println("----RapportJourMonitoring--------"+collections.size());
			if(collections.isEmpty())return ;

			int i = 1;

			//System.out.println("---Rapport Journalier de Monitoring---"+collections.size());
			for(RapportElement el : collections){
				el.setColor(String.valueOf(i));i++;
			}
			Double total = this.nbNONOK + this.nbOK+this.nbVert+this.nbVertCiel+this.nbBleu;
			Double snbNONOK = (this.nbNONOK / total);
			Double PnbVert = (this.nbVert / total);
			Double PnbVertCiel = (this.nbVertCiel / total);
			Double PnbBleu = (this.nbBleu / total);
			Double snbOK  = 1 - snbNONOK;
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String mois = new SimpleDateFormat("ddMMyyyyHHMMss").format(new Date());
			HashMap<Object, Object> param = new HashMap<Object, Object>();
			param.put("nbOK", this.nbOK);
			param.put("nbNONOK", this.nbNONOK);
			param.put("nbVert", this.nbVert);
			param.put("nbVertCiel", this.nbVertCiel);
			param.put("nbBleu", this.nbBleu);
			param.put("PnbVert", PnbVert);
			param.put("PnbVertCiel", PnbVertCiel);
			param.put("PnbBleu", PnbBleu);
			param.put("snbOK", snbOK);
			param.put("snbNONOK", snbNONOK);
			param.put("nbT", total);

			param.put("tittre", "REPPORT DE MONITORING DES TPES");
			param.put("sousTittre", "DU "+format.format(new Date()));
			String outFileName ="RapportJourMonitoringTPE"+mois+".pdf";
			this.repportManager.ExportReportPDF(outFileName, param, collections, "RapportMonitoringTPE_V2.jasper");

			// envoi de Mail
			String heure = new SimpleDateFormat("dd/MM/yyyy HH:MM:ss").format(new Date());
			String subject = "Rapport de Monitoring des TPEs du "+heure;
			String messageCorps ="Bonjour ci joint le Rapport de Monitoring des TPES du "+heure+" Merci ..";
			//String from = "alerteTEP@afrilandfirstbank.com"; 

			List<String> mailsTo = new ArrayList<String>();
			//GabParameter parameter = repportManager.findParameter("MAILS_MONITORING_TPE");
			if(user != null ){
				if(user.getEmail() != null && !user.getEmail() .trim().isEmpty() ){
					for(String mail : user.getEmail().split(",")){
						mailsTo.add(mail.trim());
					}
				}
			}
			//
			List<String> mailscc = new ArrayList<String>();
			if(!mailsTo.isEmpty())SendFileEmail.SendMail(outFileName,this.repportManager.getReportsDir()+File.separator+outFileName, this.from, mailsTo,mailscc, subject, messageCorps);

			// Save
			// this.dao.saveList(collections,true);

		}catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}


	/**
	 * 
	 */
	public void process(){

		try{

			this.nbOK = 0d;
			this.nbNONOK = 0d;
			this.nbVert = 0d;
			this.nbVertCiel = 0d;
			this.nbBleu = 0d;

			//System.out.println("---Rapport Journalier de Monitoring---TPE---GO-");
			List<RapportElement> collections = new ArrayList<RapportElement>();
			collections = processRapport();
			Collections.sort(collections);
			//System.out.println("----RapportJourMonitoring--------"+collections.size());
			if(collections.isEmpty())return ;

			int i = 1;

			//System.out.println("---Rapport Journalier de Monitoring---"+collections.size());
			for(RapportElement el : collections){
				el.setColor(String.valueOf(i));i++;
			}
			Double total = this.nbNONOK + this.nbOK+this.nbVert+this.nbVertCiel+this.nbBleu;
			Double snbNONOK = (this.nbNONOK / total);
			Double PnbVert = (this.nbVert / total);
			Double PnbVertCiel = (this.nbVertCiel / total);
			Double PnbBleu = (this.nbBleu / total);
			Double snbOK  = 1 - snbNONOK;
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String mois = new SimpleDateFormat("ddMMyyyyHHMMss").format(new Date());
			HashMap<Object, Object> param = new HashMap<Object, Object>();
			param.put("nbOK", this.nbOK);
			param.put("nbNONOK", this.nbNONOK);
			param.put("nbVert", this.nbVert);
			param.put("nbVertCiel", this.nbVertCiel);
			param.put("nbBleu", this.nbBleu);
			param.put("PnbVert", PnbVert);
			param.put("PnbVertCiel", PnbVertCiel);
			param.put("PnbBleu", PnbBleu);
			param.put("snbOK", snbOK);
			param.put("snbNONOK", snbNONOK);
			param.put("nbT", total);

			param.put("tittre", "REPPORT DE MONITORING DES TPES");
			param.put("sousTittre", "DU "+format.format(new Date()));
			String outFileName ="RapportJourMonitoringTPE"+mois+".pdf";
			this.repportManager.ExportReportPDF(outFileName, param, collections, "RapportMonitoringTPE_V2.jasper");

			// envoi de Mail
			String heure = new SimpleDateFormat("dd/MM/yyyy HH:MM:ss").format(new Date());
			String subject = "Rapport de Monitoring des TPEs du "+heure;
			String messageCorps ="Bonjour ci joint le Rapport de Monitoring des TPES du "+heure+" Merci ..";
			//String from = "alerteTEP@afrilandfirstbank.com"; 

			List<String> mailsTo = new ArrayList<String>();
			//mailsTo.add("francis_konchou@afrilandfirstbank.com");
			//mailsTo.add("aumer_soufo@afrilandfirstbank.com");
			//mailsTo.add("fabrice_kamga@afrilandfirstbank.com");
			//mailsTo.add("francis_djeumeni@afrilandfirstbank.com");
			//mailsTo.add("habib_noubissi@afrilandfirstbank.com");
			//mailsTo.add("achille_tsala@afrilandfirstbank.com");
			//mailsTo.add("jp_yamcheu@afrilandfirstbank.com");

			GabParameter parameter = this.repportManager.findParameter("MAILS_MONITORING_TPE");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					for(String mail : parameter.getValue().split(",")){
						mailsTo.add(mail);
					}
				}
			}
			//
			List<String> mailscc = new ArrayList<String>();
			if(!mailsTo.isEmpty())SendFileEmail.SendMail(outFileName,this.repportManager.getReportsDir()+File.separator+outFileName, this.from, mailsTo,mailscc, subject, messageCorps);

			// Save
			this.dao.saveList(collections,true);

		}catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}


	/**
	 * 
	 */
	@Asynchronous
	public void processTransactions(User user,Date datedebut, Date datefin){

		try{

			this.nbOK = 0d;
			this.nbNONOK = 0d;
			this.nbVert = 0d;
			this.nbVertCiel = 0d;
			this.nbBleu = 0d;

			List<RapportElement> collections = new ArrayList<RapportElement>();
			collections = processRapportTransactions(datedebut, datefin);
			Collections.sort(collections);
			if(collections.isEmpty())return ;

			int i = 1;

			for(RapportElement el : collections){
				el.setColor(String.valueOf(i));i++;
			}
			Double total = this.nbNONOK + this.nbOK+this.nbVert+this.nbVertCiel+this.nbBleu;
			Double snbNONOK = (this.nbNONOK / total);
			Double PnbVert = (this.nbVert / total);
			Double PnbVertCiel = (this.nbVertCiel / total);
			Double PnbBleu = (this.nbBleu / total);
			Double snbOK  = 1 - snbNONOK;
			//SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String mois = new SimpleDateFormat("ddMMyyyyHHMMss").format(new Date());
			HashMap<Object, Object> param = new HashMap<Object, Object>();
			param.put("nbOK", this.nbOK);
			param.put("nbNONOK", this.nbNONOK);
			param.put("nbVert", this.nbVert);
			param.put("nbVertCiel", this.nbVertCiel);
			param.put("nbBleu", this.nbBleu);
			param.put("PnbVert", PnbVert);
			param.put("PnbVertCiel", PnbVertCiel);
			param.put("PnbBleu", PnbBleu);
			param.put("snbOK", snbOK);
			param.put("snbNONOK", snbNONOK);
			param.put("nbT", total);

			param.put("tittre", "REPPORT DES TRANSACTIONS 1F SUR LES TPES");
			param.put("sousTittre", "DU "+new SimpleDateFormat("dd/MM/yyyy").format(datedebut)+ " AU "+new SimpleDateFormat("dd/MM/yyyy").format(datefin));
			String outFileName ="RapportTransactions"+mois+".pdf";
			this.repportManager.ExportReportPDF(outFileName, param, collections, "RapportTransactions.jasper");

			// envoi de Mail
			String heure = " du "+new SimpleDateFormat("dd/MM/yyyy").format(datedebut)+ " au "+new SimpleDateFormat("dd/MM/yyyy").format(datefin);
			String subject = "Rapport des Transactions 1F sur les TPEs du "+heure;
			String messageCorps ="Bonjour ci joint le Rapport des Transactions 1F sur les TPES du "+heure+" Merci ..";

			List<String> mailsTo = new ArrayList<String>();
			if(user != null ){
				if(user.getEmail() != null && !user.getEmail() .trim().isEmpty() ){
					for(String mail : user.getEmail().split(",")){
						mailsTo.add(mail.trim());
					}
				}
			}else{
				GabParameter parameter = this.repportManager.findParameter("MAILS_MONITORING_TRANS");
				if(parameter != null ){
					if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
						for(String mail : parameter.getValue().split(",")){
							mailsTo.add(mail);
						}
					}
				}
			}

			List<String> mailscc = new ArrayList<String>();
			if(!mailsTo.isEmpty())SendFileEmail.SendMail(outFileName,this.repportManager.getReportsDir()+File.separator+outFileName, this.from, mailsTo,mailscc, subject, messageCorps);

			// Save
			this.dao.saveList(collections,true);

		}catch(Exception e){
			e.printStackTrace();
		}

	}


	/**
	 * 
	 */
	@Asynchronous
	public void processTelecollecte(User user,Date datedebut, Date datefin){

		try{

			this.nbOK = 0d;
			this.nbNONOK = 0d;
			this.nbVert = 0d;
			this.nbVertCiel = 0d;
			this.nbBleu = 0d;

			List<RapportElement> collections = new ArrayList<RapportElement>();
			collections = processRapportTelecollecte(datedebut, datefin);
			Collections.sort(collections);
			if(collections.isEmpty())return ;

			int i = 1;
			for(RapportElement el : collections){
				el.setColor(String.valueOf(i));i++;
			}
			Double total = this.nbNONOK + this.nbOK+this.nbVert+this.nbVertCiel+this.nbBleu;
			Double snbNONOK = (this.nbNONOK / total);
			Double PnbVert = (this.nbVert / total);
			Double PnbVertCiel = (this.nbVertCiel / total);
			Double PnbBleu = (this.nbBleu / total);
			Double snbOK  = 1 - snbNONOK;
			//SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String mois = new SimpleDateFormat("ddMMyyyyHHMMss").format(new Date());
			HashMap<Object, Object> param = new HashMap<Object, Object>();
			param.put("nbOK", this.nbOK);
			param.put("nbNONOK", this.nbNONOK);
			param.put("nbVert", this.nbVert);
			param.put("nbVertCiel", this.nbVertCiel);
			param.put("nbBleu", this.nbBleu);
			param.put("PnbVert", PnbVert);
			param.put("PnbVertCiel", PnbVertCiel);
			param.put("PnbBleu", PnbBleu);
			param.put("snbOK", snbOK);
			param.put("snbNONOK", snbNONOK);
			param.put("nbT", total);

			param.put("tittre", "REPPORT DES TELECOLLECTES SUR TPE");
			param.put("sousTittre", "DU "+new SimpleDateFormat("dd/MM/yyyy").format(datedebut)+ " AU "+new SimpleDateFormat("dd/MM/yyyy").format(datefin));
			String outFileName ="RapportTelecollecte"+mois+".pdf";
			this.repportManager.ExportReportPDF(outFileName, param, collections, "RapportTelecollecte.jasper");

			// envoi de Mail
			String heure = " du "+new SimpleDateFormat("dd/MM/yyyy").format(datedebut)+ " au "+new SimpleDateFormat("dd/MM/yyyy").format(datefin);
			String subject = "Rapport des Télécollectes sur TPE "+heure;
			String messageCorps ="Bonjour ci joint le Rapport des Télécollectes sur les TPES du "+heure+" Merci ..";

			List<String> mailsTo = new ArrayList<String>();
			if(user != null ){
				if(user.getEmail() != null && !user.getEmail() .trim().isEmpty() ){
					for(String mail : user.getEmail().split(",")){
						mailsTo.add(mail.trim());
					}
				}
			}else{
				GabParameter parameter = this.repportManager.findParameter("MAILS_MONITORING_TELE");
				if(parameter != null ){
					if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
						for(String mail : parameter.getValue().split(",")){
							mailsTo.add(mail);
						}
					}
				}
			}

			List<String> mailscc = new ArrayList<String>();
			if(!mailsTo.isEmpty())SendFileEmail.SendMail(outFileName,this.repportManager.getReportsDir()+File.separator+outFileName, this.from, mailsTo,mailscc, subject, messageCorps);
			this.dao.saveList(collections,true);
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @return
	 */
	public List<RapportElement> processRapportTelecollecte(Date datedebut ,Date datefin){

		List<RapportElement> collections = new ArrayList<RapportElement>();

		try{

			String sql = "";
			ResultSet rs ;
			String dateAfter = new SimpleDateFormat("yyyyMMdd").format(datedebut);
			String dateBefore = new SimpleDateFormat("yyyyMMdd").format(datefin);
			ConnexionSVFE svfe = new ConnexionSVFE();
			svfe.openSVFEConnection(this.repportManager.findDataSystem("SMVISTA"));
			List<Atm> Atms = this.repportManager.findActiveTPE(Boolean.TRUE);
			RapportElement element = new RapportElement();
			int ii = 1;
			Map<String,Integer> MapAtmTelecollecte = new HashMap<String, Integer>();
			sql = "select term_id_number,count(*) as nbr from CURR_TRANS,pos_def_tab where ATMID=term_id_number and trans_type='779'  and UDATE between '"+dateAfter+"' and '"+dateBefore+"' group by term_id_number ";
			rs = svfe.Execute(sql);
			while(rs.next()){
				MapAtmTelecollecte.put(rs.getString("term_id_number"),rs.getInt("nbr"));
			}

			// parcour
			for(Atm value :Atms){
				element = new RapportElement();
				element.setAge(value.getTid());
				element.setAtm(value.getNom());
				element.setVille(value.getAdresse());
				element.setColor(String.valueOf(ii));
				element.setTelephone(value.getTelephone());
				element.setPromoteur(value.getPromoteur());
				element.setIp(value.getIp());
				element.setAtmPid(value.getPid());
				element.setAtmTid(value.getTid());
				element.setType(value.getTypeAtm().toString());
				element.setLibatm(value.getLibelle());
				if(MapAtmTelecollecte.containsKey(value.getTid().trim())){
					element.setAtm(element.getAtm() + " (Télécollecte OK) ");
					element.setStatus("KO");
					element.setEtat("OK");
					element.setErreur("VF");
					this.nbVert = this.nbVert + 1;
				}else{
					element.setAtm(element.getAtm() + " (Télécollecte KO) ");
					element.setStatus("KO");
					element.setEtat("KO");
					element.setErreur("RG");
					this.nbNONOK = this.nbNONOK + 1;
				}
				collections.add(element);
				ii++;
			}
			svfe.closeSVFEConnection();
		}catch(SQLException ex){
			ex.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return collections;
	}


	/**
	 * 
	 * @return
	 */
	public List<RapportElement> processRapportTransactions(Date datedebut ,Date datefin){

		List<RapportElement> collections = new ArrayList<RapportElement>();

		try{

			String sql = "";
			ResultSet rs ;
			String dateAfter = new SimpleDateFormat("yyyyMMdd").format(datedebut);
			String dateBefore = new SimpleDateFormat("yyyyMMdd").format(datefin);
			//System.out.println(dateAfter+"---Rapport Journalier de Monitoring--TPE-"+dateBefore);
			ConnexionSVFE svfe = new ConnexionSVFE();
			svfe.openSVFEConnection(this.repportManager.findDataSystem("SMVISTA"));
			List<Atm> Atms = this.repportManager.findActiveTPE(Boolean.FALSE);
			RapportElement element = new RapportElement();
			int ii = 1;
			Map<String,Integer> MapAtmTrans = new HashMap<String, Integer>();
			sql = "select term_id_number,count(*) as nbr from CURR_TRANS,pos_def_tab where ATMID=term_id_number and resp ='-1' and trans_type='774'  and sysamt=1  and UDATE between '"+dateAfter+"' and '"+dateBefore+"' group by term_id_number ";
			rs = svfe.Execute(sql);
			while(rs.next()){
				MapAtmTrans.put(rs.getString("term_id_number"),rs.getInt("nbr"));
			}
			
			// parcour
			for(Atm value :Atms){
				element = new RapportElement();
				element.setAge(value.getTid());
				element.setAtm(value.getNom());
				element.setVille(value.getAdresse());
				element.setColor(String.valueOf(ii));
				element.setTelephone(value.getTelephone());
				element.setPromoteur(value.getPromoteur());
				element.setIp(value.getIp());
				element.setAtmPid(value.getPid());
				element.setAtmTid(value.getTid());
				element.setType(value.getTypeAtm().toString());
				element.setLibatm(value.getLibelle());
				if(MapAtmTrans.containsKey(value.getTid().trim())){
					element.setStatus("OK");
					element.setErreur("VF");
					element.setEtat("OK");
					this.nbVert = this.nbVert + 1;
				}else{
					element.setStatus("KO");
					element.setErreur("RG");
					element.setEtat("KO");
					this.nbNONOK = this.nbNONOK + 1;
				}
				collections.add(element);
				ii++;
			}
			svfe.closeSVFEConnection();
		}catch(SQLException ex){
			ex.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return collections;
	}


	/**
	 * 
	 * @return
	 */
	public List<RapportElement> processRapport(){

		List<RapportElement> collections = new ArrayList<RapportElement>();

		try{

			// Controle de l heure
			//if(Boolean.FALSE.equals(RepportManager.checkTime())) return new ArrayList<RapportElement>();

			String sql = "";
			ResultSet rs ;
			Date date = DateUtils.addDays(new Date(), -1); 
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
				date = DateUtils.addDays(new Date(), -3); 
			}
			if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
				date = DateUtils.addDays(new Date(), -2); 
			}

			String dateAfter = new SimpleDateFormat("yyyyMMdd").format(date);
			String dateBefore = new SimpleDateFormat("yyyyMMdd").format(new Date());
			//System.out.println(dateAfter+"---Rapport Journalier de Monitoring--TPE-"+dateBefore);
			ConnexionSVFE svfe = new ConnexionSVFE();
			svfe.openSVFEConnection(this.repportManager.findDataSystem("SMVISTA"));
			List<Atm> Atms = this.repportManager.findActiveTPE(Boolean.FALSE);
			RapportElement element = new RapportElement();
			int ii = 1;

			TypeIncident typInci = this.repportManager.findTypeIncident(Incident.ConnexionReseau);

			Map<String,Integer> MapAtmTrans = new HashMap<String, Integer>();
			sql = "select term_id_number,count(*) as nbr from CURR_TRANS,pos_def_tab where ATMID=term_id_number and resp ='-1' and UDATE between '"+dateAfter+"' and '"+dateBefore+"' group by term_id_number ";
			rs = svfe.Execute(sql);
			while(rs.next()){
				MapAtmTrans.put(rs.getString("term_id_number"),rs.getInt("nbr"));
			}

			Map<String,Integer> MapAtmTransLost = new HashMap<String, Integer>();
			sql = "select term_id_number,count(*) as nbr from CURR_TRANS,pos_def_tab where ATMID=term_id_number and resp != '-1' and UDATE between '"+dateAfter+"' and '"+dateBefore+"' group by term_id_number ";
			rs = svfe.Execute(sql);
			while(rs.next()){
				MapAtmTransLost.put(rs.getString("term_id_number"),rs.getInt("nbr"));
			}

			Map<String,Integer> MapAtmTelecollecte = new HashMap<String, Integer>();
			sql = "select term_id_number,count(*) as nbr from CURR_TRANS,pos_def_tab where ATMID=term_id_number and trans_type='779'  and UDATE between '"+dateAfter+"' and '"+dateBefore+"' group by term_id_number ";
			rs = svfe.Execute(sql);
			while(rs.next()){
				MapAtmTelecollecte.put(rs.getString("term_id_number"),rs.getInt("nbr"));
			}

			Date curentDate = new Date();

			// parcour
			for(Atm value :Atms){

				// Transaction  Gab 
				if(value.getIp() != null && !value.getIp().trim().isEmpty()){

					//System.out.println("---Rapport Journalier de Monitoring--TPE-"+value.getNom());

					//libelleAdmin = "";
					element = new RapportElement();
					element.setAge(value.getTid());
					element.setAtm(value.getNom());
					element.setVille(value.getAdresse());
					element.setColor(String.valueOf(ii));
					element.setTelephone(value.getTelephone());
					element.setPromoteur(value.getPromoteur());
					element.setIp(value.getIp());
					//element.setEtat("OK");

					element.setAtmPid(value.getPid());
					element.setAtmTid(value.getTid());
					element.setType(value.getTypeAtm().toString());
					element.setLibatm(value.getLibelle());
					element.setStatus("OK");
					ii++;
					//SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");


					boolean transactionAtm = false;
					//boolean transactionAtmLost = false;
					Integer nbok = 0;
					Integer nblost = 0;
					if(MapAtmTrans.containsKey(value.getTid().trim())){
						transactionAtm = true;
						nbok = MapAtmTrans.get(value.getTid().trim());
					}

					if(MapAtmTelecollecte.containsKey(value.getTid().trim())){
						transactionAtm = true;
					}else{
						element.setAtm(element.getAtm() + " (Télécollecte KO) ");
						element.setStatus("KO");
					}

					if(MapAtmTransLost.containsKey(value.getTid().trim())){
						//transactionAtmLost = true;
						nblost = MapAtmTransLost.get(value.getTid().trim());
					}

					// Libelle TransactionLost 
					Integer tot = nblost + nbok;
					if(tot == 0) tot = 1;
					Integer pourlost = Double.valueOf(Math.round((nblost / tot)*100)).intValue();
					if( pourlost > 1 ){
						element.setAtm(element.getAtm() + " ("+pourlost+"% Echec)");
						element.setStatus("KO");
					}

					/**sql = "select count(*) as nbr from CURR_TRANS where ATMID='"+value.getTid().trim()+"' and UDATE='"+dateFormat+"'";
					rs = svfe.Execute(sql);
					if(rs.next()){
						int nbr = rs.getInt("nbr");
						if(nbr > 0)transactionAtm = true;
					}*/

					/**boolean ping =  NetworkPing.isReachableByPing(value.getIp());
					List<Incident> incis = repportManager.findIncident(value, typInci,curentDate,curentDate);
					if(ping == false && transactionAtm == false ){
						if(!incis.isEmpty()){
							Incident in = incis.get(0);
							Integer total = in.getMotnomberNonOK() + in.getMotnomberOK();
							if(total <= 0) total = 1 ;
							Double val = Double.valueOf((in.getMotnomberNonOK() / total) * 100 );
							if(val > fixing){
								element.setEtat("KO");
								element.setErreur("KO");
								nbNONOK++;
							}else{
								element.setEtat("OK");
								element.setErreur("OK");
								nbOK++;
							}
							in.setMotnomberNonOK(0);
							in.setMotnomberOK(0);
							dao.update(in);
						}else{
							element.setEtat("KO");
							element.setErreur("KO");
							nbNONOK++;
						}						
					}else{
						element.setEtat("OK");
						element.setErreur("OK");
						nbOK++;						
					}*/



					boolean ping =  NetworkPing.isReachableByPing(value.getIp());
					List<Incident> incis = this.repportManager.findIncident(value, typInci,curentDate,curentDate);
					boolean incident = checkIncident(incis);

					if(ping == false && transactionAtm == false ){
						// PIN KO TRANSACTION NON ENREGISTREE SUR UNE PERIODE DE 24H : ROUGE
						if(incident == false){
							element.setEtat("KO");
							element.setErreur("RG");
							this.nbNONOK++;
						}else{
							element.setErreur("VC");
							element.setEtat("OK");
							this.nbVertCiel++;
						}
					}else if(ping == false && transactionAtm == true ){
						// PIN KO  LA DERNIERE TRANSACTION ENREGISTREE SUR UN DELAIS DE 24 H : BLEU
						if(incident == false){
							element.setErreur("VB");
							element.setEtat("OK");
							this.nbBleu++;
						}else{
							element.setErreur("VF");
							element.setEtat("OK");
							this.nbVert++;
						}
					}else if(ping == true && transactionAtm == false ){
						// PIN OK TRX NON ENREGISTREE SUR UN DELAIS 24H  : VERT CIEL
						element.setErreur("VC");
						element.setEtat("OK");
						this.nbVertCiel++;						
					}else if(ping == true && transactionAtm == true ){
						// PIN OK TRX ENREGISTREE SUR UN DELAIS SUR UN DELAIS 24H  : VERT FONCE
						element.setErreur("VF");
						element.setEtat("OK");
						this.nbVert++;						
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


	public boolean checkIncident(List<Incident> incis){

		if(!incis.isEmpty()){
			Incident in = incis.get(0);
			Integer total = in.getMotnomberNonOK() + in.getMotnomberOK();
			if(total == 0) total = 1 ;
			if(total < 0) total = 1 ;
			Double val = Double.valueOf((in.getMotnomberNonOK() / total) * 100 );
			in.setMotnomberNonOK(0);
			in.setMotnomberOK(0);
			this.dao.update(in);
			if(val > fixing){
				return false;
			}
			return true;
		}		
		return false;

	}


}