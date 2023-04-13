package com.afb.portal.buisness.monitoring.worker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.criterion.Restrictions;

import afb.dsi.dpd.portal.jpa.entities.DataSystem;

import com.afb.portal.buisness.monitoring.worker.shared.IRepportManager;
import com.afb.portal.dao.shared.IAtmDAOLocal;
import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.parameter.GabParameter;
import com.afb.portal.jpa.gab.parameter.TypeIncident;
import com.afb.portal.jpa.gab.statistisques.Incident;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

/**
 * 
 * @author Owner
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class RepportManager implements IRepportManager{


	/**
	 * Service DAO
	 */
	@EJB
	private IAtmDAOLocal dao; 

	/**
	 * Nom du repertoire de base des donnees de ATFF
	 */
	public static final String ATFF_ROOT_DATAS_DIR = "GabJSF";

	/**
	 * Nom du repertoire des donnees de la couche de presentation
	 */
	public static final String ATFF_RESOURCES_DATAS_DIR = "Resources";

	/**
	 * Nom du repertoire des Etats de ATFF
	 */
	public static final String ATFF_REPORTS_DATAS_DIR = "Reports";


	public static List<Atm> listeAtm = new ArrayList<Atm>();


	public static List<Atm> listeTPE = new ArrayList<Atm>();


	private TimerTask task;
	private Timer timer;
	
	public void creerTimer(){
		try{
			// boucle
			int sec = 60;
			int min = 40;
			
			if(timer != null)timer.cancel();
			if(task != null)task.cancel();
			
			task = new TimerTask(){
				@Override
				public void run(){
					try {
						findActiveAtm(Boolean.TRUE);
						
						findActiveTPE(Boolean.TRUE);
					}catch(Exception e){
						e.printStackTrace();
					}
				}	
			};
			timer = new Timer(true);
			timer.schedule(task, DateUtils.addMinutes(new Date(),20) , min*sec*1000);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public synchronized  List<Atm> findActiveAtm(Boolean charg){
		if(charg.equals(Boolean.TRUE) ){
			listeAtm = findActiveAtm();
		}else if(charg.equals(Boolean.FALSE) && listeAtm.isEmpty()){
			listeAtm = findActiveAtm();
		}
		return listeAtm;
	}

	public synchronized List<Atm> findActiveTPE(Boolean charg){
		if(charg.equals(Boolean.TRUE)){
			listeTPE =  findActiveTPE();
		}else if(charg.equals(Boolean.FALSE) && listeTPE.isEmpty()){
			listeTPE = findActiveTPE();
		}
		return listeTPE;
	}


	/**
	 * findActiveAtm
	 * @param value
	 * @return
	 */
	public synchronized  List<Atm> findActiveAtm(){
			
		/**
		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("status",Boolean.TRUE));
		rc.add(Restrictions.eq("typeAtm",AtmStatus.ATM));
		return  dao.filter(Atm.class,null,rc,null,null,0,-1);
		*/
		return  dao.findActiveAtm();
	}

	/**
	 * findActiveTPE
	 */
	public synchronized List<Atm> findActiveTPE(){
		/**RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("status",Boolean.TRUE));
		rc.add(Restrictions.eq("typeAtm",AtmStatus.TPE));
		rc.add(Restrictions.isNotNull("ip"));

		return dao.filter(Atm.class,null,rc,null,null,0,-1);
		*/
		return  dao.findActiveTPE();
	}


	public synchronized Boolean  checkTimeSauvegarde(){
		String txtheure = new SimpleDateFormat("HH:MM:ss").format(new Date());
		String time = txtheure.substring(0,5);
		if(time.startsWith("01:")){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}


	/**
	 * getTime
	 */
	public synchronized Boolean  checkTime(){
		Calendar calendar = Calendar.getInstance();
		String txtheure = new SimpleDateFormat("HH:mm:ss").format(new Date());
		Integer time = Integer.valueOf(txtheure.substring(0,5).replace(":",""));
		if(Calendar.SUNDAY == calendar.get(Calendar.DAY_OF_WEEK)){
			if(time > 700 && time < 1800) return Boolean.TRUE;
		}else if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
			if(time > 700 && time < 1800) return Boolean.TRUE;
		}else{
			if(time > 700 && time < 1800) return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * getTime
	 */
	public synchronized Boolean  checkTimeSolde(){
		Calendar calendar = Calendar.getInstance();
		String txtheure = new SimpleDateFormat("HH:mm:ss").format(new Date());
		Integer time = Integer.valueOf(txtheure.substring(0,5).replace(":",""));
		if(Calendar.SUNDAY == calendar.get(Calendar.DAY_OF_WEEK)){
			return Boolean.FALSE;
		}else if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
			if(time > 700 && time < 1200) return Boolean.TRUE;
		}else{
			if(time > 700 && time < 1800) return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}


	private DataSystem system = null;

	/**
	 * findDataSystem
	 */
	public void findDataSystem(){
		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("code","SMVISTA"));
		List<DataSystem>  systems = dao.filter(DataSystem.class,null,rc,null,null,0,-1);
		if(!systems.isEmpty()){
			system = systems.get(0);
		}
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public DataSystem findDataSystem(String value){
		if(system != null) return system;
		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("code",value));
		List<DataSystem>  systems = dao.filter(DataSystem.class,null,rc,null,null,0,-1);
		if(!systems.isEmpty()){
			system = systems.get(0);
			return system;
		}
		return null;
	}


	public TypeIncident findTypeIncident(String code){
		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("code",code));
		List<TypeIncident>  systems = dao.filter(TypeIncident.class,null,rc,null,null,0,-1);
		if(!systems.isEmpty()){
			return systems.get(0);
		}
		return null;
	}


	public Incident findIncident(Atm atm,TypeIncident typeIncident){
		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("atm",atm));
		rc.add(Restrictions.eq("typeIncident",typeIncident));
		rc.add(Restrictions.eq("txtdate",new SimpleDateFormat("dd-MM-yyyy").format(new Date())));
		List<Incident>  systems = dao.filter(Incident.class,null,rc,null,null,0,-1);
		if(!systems.isEmpty()){
			return systems.get(0);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.buisness.monitoring.worker.shared.IRepportManager#findIncident(com.afb.portal.jpa.gab.equipment.Atm, com.afb.portal.jpa.gab.parameter.TypeIncident, java.util.Date, java.util.Date)
	 */
	public List<Incident> findIncident(Atm atm,TypeIncident typeIncident, Date debut , Date fin){
		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("atm",atm));
		rc.add(Restrictions.eq("typeIncident",typeIncident));
		rc.add(Restrictions.between("date",debut,fin));
		return dao.filter(Incident.class,null,rc,null,null,0,-1);
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public GabParameter findParameter(String value){
		//System.out.println("------findParameter------"+dao);
		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("code",value));
		List<GabParameter>  systems = dao.filter(GabParameter.class,null,rc,null,null,0,-1);
		if(!systems.isEmpty()){
			return systems.get(0);
		}
		return null;
	}

	/**
	 * findTypeIncident
	 * @param journal
	 * @return
	 */
	public List<TypeIncident> findTypeIncident(Boolean journal){
		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("journal",journal));
		rc.add(Restrictions.eq("journalError",journal));
		return dao.filter(TypeIncident.class,null,null,null,null,0,-1);
	}


	public String findTimeSaugarde(){
		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("code","SAUV_TIME"));
		List<GabParameter>  systems = dao.filter(GabParameter.class,null,rc,null,null,0,-1);
		if(!systems.isEmpty()){
			return systems.get(0).getValue();
		}
		return null;
	}


	public String findTimeMonitoring(){
		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("code","TIME_GAB"));
		List<GabParameter>  systems = dao.filter(GabParameter.class,null,rc,null,null,0,-1);
		if(!systems.isEmpty()){
			return systems.get(0).getValue();
		}
		return null;
	}
	
	
	public String findTimeTelecollecte(){
		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("code","TIME_TRANS"));
		List<GabParameter>  systems = dao.filter(GabParameter.class,null,rc,null,null,0,-1);
		if(!systems.isEmpty()){
			return systems.get(0).getValue();
		}
		return null;
	}
	
	
	public String findTimeTransaction(){
		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("code","TIME_TELE"));
		List<GabParameter>  systems = dao.filter(GabParameter.class,null,rc,null,null,0,-1);
		if(!systems.isEmpty()){
			return systems.get(0).getValue();
		}
		return null;
	}


	public String findTimeMonitoringTPE(){
		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("code","TIME_TPE"));
		List<GabParameter>  systems = dao.filter(GabParameter.class,null,rc,null,null,0,-1);
		if(!systems.isEmpty()){
			return systems.get(0).getValue();
		}
		return null;
	}
	

	/**
	 * Retourne le chemin du repertoire des etats de E-HR
	 * @return
	 */
	public  String getReportsDir(){

		// Repertoire des etats
		return getRootDataDirectory() + File.separator + ATFF_ROOT_DATAS_DIR + File.separator + ATFF_RESOURCES_DATAS_DIR + File.separator + ATFF_REPORTS_DATAS_DIR;

	}

	public  String getRootDataDirectory(){

		// On recalcule
		String rootDataDirectory = System.getProperty("jboss.server.data.dir", ".") + File.separator + "PortalDatas";

		// On retourne
		return rootDataDirectory;
	}

	/**
	 * Generation des fichier PDF de rapport
	 */
	public void ExportReportPDF(String outFileName,HashMap<Object, Object> param,Collection<?> collections,String reportFileName){	    
		try{	

			if(param == null) param = new HashMap<Object, Object>();	
			param.put("SUB_REPORT_DIR",getReportsDir());
			String srcreportFileName = getReportsDir()+ File.separator +reportFileName;
			String srcoutFileName = getReportsDir()+ File.separator +outFileName;
			JRBeanCollectionDataSource bean = new JRBeanCollectionDataSource(collections);
			JasperPrint jasperPrint = JasperFillManager.fillReport(srcreportFileName,param,bean);			
			JasperExportManager.exportReportToPdfFile(jasperPrint, srcoutFileName);	

		}catch(JRException e){
			e.printStackTrace();
		}
	}


	/**
	 * Generation des fichier PDF de rapport
	 */
	/**
	public  void ExportReportPDF(String outFileName,List<Object> collections,HashMap<Object, Object> param,String reportFileName){	    
		try{
			String srcreportFileName = getReportsDir()+ File.separator +reportFileName;
			// HashMap hm = new HashMap();
			JRBeanCollectionDataSource bean = new JRBeanCollectionDataSource(collections);
			JasperPrint jasperPrint = JasperFillManager.fillReport(srcreportFileName,param,bean);			
			JasperExportManager.exportReportToPdfFile(jasperPrint, outFileName);	
		}catch(JRException e){
			e.printStackTrace();
		}
	}*/

	/**
	 * 
	 * @param reportName
	 * @param map
	 * @param maCollection
	 * @return
	 */
	public  JasperPrint printReport(String reportName, HashMap<Object, Object> map, Collection<?> maCollection){
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(maCollection);
		JasperPrint jp = null;
		try{
			jp = JasperFillManager.fillReport(reportName, map, dataSource);
		}catch(Exception e){
			e.printStackTrace();
		}
		return jp;
	}


}
