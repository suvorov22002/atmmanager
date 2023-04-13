/**
 * 
 */
package com.afb.portal.buisness.atmManage.parameter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import afb.dsi.dpd.portal.jpa.entities.Branch;
import afb.dsi.dpd.portal.jpa.entities.DataSystem;

import com.afb.portal.buisness.monitoring.atm.worker.AlerteMonitoring;
import com.afb.portal.buisness.monitoring.atm.worker.MonitoringAtmWorker;
import com.afb.portal.buisness.monitoring.atm.worker.RapportJourMonitoring;
import com.afb.portal.buisness.monitoring.atm.worker.SauvegardeMonitoringWorker;
import com.afb.portal.buisness.parameter.shared.IGabManager;
import com.afb.portal.dao.shared.IAtmDAOLocal;
import com.afb.portal.jpa.gab.parameter.AtmIncident;
import com.afb.portal.jpa.gab.parameter.GabParameter;
import com.afb.portal.jpa.gab.parameter.IncidentAtm;
import com.afb.portal.jpa.gab.statistisques.Incident;
import com.yashiro.persistence.utils.annotations.AllowedRole;
import com.yashiro.persistence.utils.annotations.ProtectedClass;
import com.yashiro.persistence.utils.dao.tools.AliasesContainer;
import com.yashiro.persistence.utils.dao.tools.PropertyContainer;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

/**
 * Implementation du service Metier
 * @author Francis K
 * @version 1.0
 */
@ProtectedClass(system = "GabManager", methods = {} )
@Stateless(name = IGabManager.SERVICE_NAME, mappedName = IGabManager.SERVICE_NAME, description = "Implementation du service metier")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class GabManager implements IGabManager{

	
	/**
     * creerTimerAlerteMonitoring
     */
	public void creerTimerAlerteMonitoring(){
		try{									
			int sec = 60;int min = 2;
			TimerTask task = new TimerTask(){
				@Override
				public void run(){
					try {processAlerteMonitoring();	}catch(Exception e){e.printStackTrace();}
				}	
			};
			Timer timer = new Timer(true);
			timer.scheduleAtFixedRate(task, DateUtils.addMinutes(new Date(),1) , min*sec*1000);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void processAlerteMonitoring(){
		new AlerteMonitoring().process();
	}
	
	
	
	/**
     * creerTimerMonitoringAtmWorker
     */
	public void creerTimerMonitoringAtmWorker(){
		try{									
			int sec = 60;int min = 2;
			TimerTask task = new TimerTask(){
				@Override
				public void run(){
					try {processMonitoringAtmWorker();	}catch(Exception e){e.printStackTrace();}
				}	
			};
			Timer timer = new Timer(true);
			timer.scheduleAtFixedRate(task, DateUtils.addMinutes(new Date(),1) , min*sec*1000);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void processMonitoringAtmWorker(){
		new MonitoringAtmWorker().process();
	}
	
	
	/**
     * creerTimerRapportJourMonitoring
     */
	public void creerTimerRapportJourMonitoring(){
		try{									
			int sec = 60;int min = 1;
			TimerTask task = new TimerTask(){
				@Override
				public void run(){
					try {processRapportJourMonitoring();	}catch(Exception e){e.printStackTrace();}
				}	
			};
			Timer timer = new Timer(true);
			timer.scheduleAtFixedRate(task, DateUtils.addMinutes(new Date(),1) , min*sec*1000);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void processRapportJourMonitoring(){
		new RapportJourMonitoring().runMonitoring();
	}
	
	
	/**
     * creerTimerAlerteMonitoring
     */
	public void creerTimerSauvegardeMonitoringWorker(){
		try{									
			int sec = 60;int min = 1;
			TimerTask task = new TimerTask(){
				@Override
				public void run(){
					try {processSauvegardeMonitoringWorker();	}catch(Exception e){e.printStackTrace();}
				}	
			};
			Timer timer = new Timer(true);
			timer.scheduleAtFixedRate(task, DateUtils.addMinutes(new Date(),1) , min*sec*1000);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void processSauvegardeMonitoringWorker(){
		new SauvegardeMonitoringWorker().check();
	}
	
	// @EJB 
	// private IAlerteMonitoring alerteMonitoring;

	// @EJB 
	// private IMonitoringAtmWorker monitoringAtmWorker;

	// @EJB 
	// private IRapportJourMonitoring rapportJourMonitoring;

	/**
	 * Service DAO
	 */
	@EJB
	private IAtmDAOLocal ManagerDAO;


	/**
	 * Objet Connexion à Delta
	 */
	private  Connection con;


	/**
	 * Objet Connexion à Delta
	 */
	private  Statement statement;


	// @PostConstruct
	public void InitEJB(){
		// alerteMonitoring.process();
		// monitoringAtmWorker.process();
		//rapportJourMonitoring.runMonitoring();
	}

	/**
	 * 
	 */
	public List<AtmIncident> filterAtmIncident(Date detedebut, Date detefin , String codeinc){

		List<AtmIncident> values = new ArrayList<AtmIncident>();
		AtmIncident item = null;
		// Construction du Container de restrictions
		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.between("date",detedebut, detefin));
		rc.add(Restrictions.eq("typeIncident.code", codeinc));
		
		AliasesContainer all = AliasesContainer.getInstance();
		all.add("typeIncident.code","typeIncident.code");
		List<Incident> liste = ManagerDAO.filter(Incident.class, all, rc, null, null, 0, -1);
		for(Incident in : liste){
			if(values.isEmpty()){
				item = new AtmIncident(in.getAtm().getTid(), in.getAtm().getNom(), in.getTypeIncident().getCode(), in.getTypeIncident().getLibelle(), 1, in.getNomber());
				values.add(item);
			}else{
				boolean trouv = false;
				for(AtmIncident elm : values){
					if(elm.getTid().equalsIgnoreCase(in.getAtm().getTid()) && elm.getCodeInc().equalsIgnoreCase(in.getTypeIncident().getCode())){
						trouv = true;
						elm.setNoombre(elm.getNoombre()+1);
						elm.setNoombreAlerte(elm.getNoombreAlerte()+in.getNomber());
					}
				}
				if(trouv == false){
					item = new AtmIncident(in.getAtm().getTid(), in.getAtm().getNom(), in.getTypeIncident().getCode(), in.getTypeIncident().getLibelle(), 1, in.getNomber());
					values.add(item);
				}
			}
		}

		return values;
		
	}

	
	/**
	 * findParameter
	 * @param value
	 * @return
	 */
	public GabParameter findParameter(String value){
		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("code",value));
		List<GabParameter>  systems = ManagerDAO.filter(GabParameter.class,null,rc,null,null,0,-1);
		if(!systems.isEmpty()){
			return systems.get(0);
		}
		return null;
	}

	/**
	 * filterIncidentAtm
	 * @param detedebut
	 * @param detefin
	 * @param codeAtm
	 * @return
	 */
	public List<IncidentAtm> filterIncidentAtm(Date detedebut, Date detefin , String codeAtm){

		List<IncidentAtm> values = new ArrayList<IncidentAtm>();
		IncidentAtm item = null;
		// Construction du Container de restrictions
		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.between("date",detedebut, detefin));
		rc.add(Restrictions.eq("atm.tid", codeAtm));
		
		AliasesContainer all = AliasesContainer.getInstance();
		all.add("atm.tid","atm.tid");
		List<Incident> liste = ManagerDAO.filter(Incident.class, all, rc, null, null, 0, -1);
		for(Incident in : liste){
			if(values.isEmpty()){
				item = new IncidentAtm(in.getAtm().getTid(), in.getAtm().getNom(), in.getTypeIncident().getCode(), in.getTypeIncident().getLibelle(), 1, in.getNomber());
				values.add(item);
			}else{
				boolean trouv = false;
				for(IncidentAtm elm : values){
					if(elm.getTid().equalsIgnoreCase(in.getAtm().getTid()) && elm.getCodeInc().equalsIgnoreCase(in.getTypeIncident().getCode())){
						trouv = true;
						elm.setNoombre(elm.getNoombre()+1);
						elm.setNoombreAlerte(elm.getNoombreAlerte()+in.getNomber());
					}
				}
				if(trouv == false){
					item = new IncidentAtm(in.getAtm().getTid(), in.getAtm().getNom(), in.getTypeIncident().getCode(), in.getTypeIncident().getLibelle(), 1, in.getNomber());
					values.add(item);
				}
			}
		}

		return values;
	}


	/**
	 * Methode permettant de construire le Conteneur de restriction commun aux parametrage
	 * @param codePattern	Pattern de code
	 * @param designationPattern	Pattern de designation
	 * @return
	 */
	@SuppressWarnings("unused")
	private RestrictionsContainer buildAbstractParameterBaseRestrictionContainer(String codePattern, String designationPattern) {

		// Construction du Container de restrictions
		RestrictionsContainer restrictionsContainer = RestrictionsContainer.getInstance();

		// Si le code est positionne
		if(codePattern != null && codePattern.trim().length() > 0) restrictionsContainer.add(Restrictions.like("code", codePattern + "%"));

		// Si la designation est positionne
		if(designationPattern != null && designationPattern.trim().length() > 0) restrictionsContainer.add(Restrictions.like("designation", designationPattern + "%"));

		// On retourne le conteneur
		return restrictionsContainer;
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.buisness.parameter.shared.IParameterManager#findByPrimaryKey(java.lang.Class, java.lang.Object, com.yashiro.persistence.utils.dao.tools.PropertyContainer)
	 */
	@Override
	public <T> T findByPrimaryKey(Class<T> entityClass, Object entityID,PropertyContainer properties){

		// Appel de la DAO
		return ManagerDAO.findByPrimaryKey(entityClass, entityID, properties);

	}

	/**
	 * Separateur
	 * @param mont
	 * @return
	 */
	public static String Separator(Double mont){
		String result ="";
		String val = String.valueOf(mont.intValue());
		int i = 0;
		int j = val.length();
		int p = 0;
		while(val.length() > i){
			if(p == 3){
				result = "  "+result;				
				p = 0;
			}
			result = val.substring(j-1,j)+result;	
			p++;
			i++;
			j--;
		}
		return result;
	}


	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.buisness.parameter.shared.ICreditManager#filterBranch()
	 */
	public List<Branch> filterBranch(){
		return ManagerDAO.filter(Branch.class, null, null, null, null, 0, -1);
	}


	/**
	 * Methode de formataged une chaine
	 * @param chaine
	 * @param number
	 * @return
	 */
	public String Format(String chaine , int number){

		String charr = "0";
		String value ="";
		int size = 0;

		if(chaine == null){
			while(size < number){
				value = charr + value;
				size++;
			}
			return value;
		}else if(chaine.equalsIgnoreCase("")){
			while(size < number){
				value = charr + value;
				size++;
			}
			return value;
		}else{
			charr = "0";
			size = chaine.trim().length();
			if(size < number){
				while(size < number){
					value = charr + value;
					size++;
				}
			}
			return value+chaine;
		}
	}


	// find datasystem
	public DataSystem findDataSystem(String system){

		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("code", system));
		List<DataSystem> sys = ManagerDAO.filter(DataSystem.class, null, rc, null, null, 0, -1);

		if(sys == null  || sys.isEmpty() ) return null;
		return sys.get(0);	

	}

	/**
	 * 
	 * @param system
	 * @return
	 */
	public DataSystem saveDataSystem(DataSystem system){
		return ManagerDAO.save(system);		
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.buisness.parameter.shared.IPensionManager#openDELTAConnection()
	 */
	public void openDELTAConnection(){

		try{

			// System
			DataSystem system = findDataSystem("DELTA-V10");

			if(system == null){
				String code = "DELTA-V10";
				String name = "Amplitude V-10";
				String description = "Chaine de Connexion Amplitude";
				String providerClassName = "com.informix.jdbc.IfxDriver";
				String dbConnectionString = "jdbc:informix-sqli://192.168.11.26:1536/recette:informixserver=ol_afbrec";
				String dbUserName = "trans";
				String dbPassword = "trans123";
				system = new DataSystem(code, name, description, providerClassName, dbConnectionString, dbUserName, dbPassword);
				system = ManagerDAO.save(system);
			}

			if(system != null ){
				Class.forName(system.getProviderClassName()).newInstance();
				this.con = DriverManager.getConnection(system.getDbConnectionString(), system.getDbUserName(), system.getDbPassword());
				this.statement = this.con.createStatement();
			}

		}catch (Exception e) {
			// TODO: handle exception
			this.con = null;
		}

	}


	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.buisness.parameter.shared.IPensionManager#openSVFEConnection()
	 */
	public void openSVFEConnection(){

		try{

			// System
			DataSystem system = findDataSystem("SMVISTA_BK");
			if(system == null){
				String code = "SMVISTA_BK";
				String name = "Smart Vista FO BK";
				String description = "Chaine de Connexion a SmartVista";
				String providerClassName = "oracle.jdbc.driver.OracleDriver";
				String dbConnectionString = "jdbc:oracle:thin:@172.21.254.31:1521:svfe";
				String dbUserName = "svista";
				String dbPassword = "svista1";
				system = new DataSystem(code, name, description, providerClassName, dbConnectionString, dbUserName, dbPassword);
				system = ManagerDAO.save(system);
			}

			if(system != null ){
				Class.forName(system.getProviderClassName()).newInstance();
				this.con = DriverManager.getConnection(system.getDbConnectionString(),system.getDbUserName(),system.getDbPassword());
				this.statement = this.con.createStatement();
			}

		}catch(Exception e){
			// TODO: handle exception

		}

	}


	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.buisness.parameter.shared.IPensionManager#openSVBOConnection()
	 */
	public void openSVBOConnection(){

		try{
			// System
			DataSystem system = findDataSystem("SMVISTA_BO_BK");
			if(system == null){
				String code = "SMVISTA_BO_BK";
				String name = "Smart Vista FO BK";
				String description = "Chaine de Connexion a SmartVista";
				String providerClassName = "oracle.jdbc.driver.OracleDriver";
				String dbConnectionString = "jdbc:oracle:thin:@172.21.254.31:1521:svbo";
				String dbUserName = "svista";
				String dbPassword = "svista1";
				system = new DataSystem(code, name, description, providerClassName, dbConnectionString, dbUserName, dbPassword);
				system = ManagerDAO.save(system);
			}

			if(system != null ){
				Class.forName(system.getProviderClassName()).newInstance();
				this.con = DriverManager.getConnection(system.getDbConnectionString(),system.getDbUserName(),system.getDbPassword());
				this.statement = this.con.createStatement();
			}

		}catch(Exception e){
			// TODO: handle exception

		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.buisness.parameter.shared.IPensionManager#closeConnection()
	 */
	public  void closeConnection(){

		try {
			if(this.con != null) this.con.close();
		}catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.buisness.parameter.shared.ICreditManager#filterParameter(java.lang.String)
	 */
	public GabParameter filterParameter(String txtCode){

		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("code",txtCode));	
		List<GabParameter> params = ManagerDAO.filter(GabParameter.class, null, rc, null, null, 0, -1);
		if(params.isEmpty()) return null;
		return params.get(0);

	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.buisness.parameter.shared.ICreditManager#filterParameter(java.lang.String, java.lang.String, java.lang.String)
	 */
	@AllowedRole(name = "filterParameter", displayName = "Rechercher un Parametre")
	@Override
	public List<GabParameter> filterParameter(String txtCode,String txtparam,String txtdecription){

		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		if(txtCode != null && !txtCode.trim().isEmpty() )rc.add(Restrictions.like("code", "%"+txtCode+"%",MatchMode.ANYWHERE ));	
		if(txtparam != null && !txtparam.trim().isEmpty() )rc.add(Restrictions.like("parameter", "%"+txtparam+"%",MatchMode.ANYWHERE  ));
		if(txtdecription != null && !txtdecription.trim().isEmpty() )rc.add(Restrictions.like("descrption", "%"+txtdecription+"%" ,MatchMode.ANYWHERE ));
		return ManagerDAO.filter(GabParameter.class, null, rc, null, null, 0, -1);

	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.buisness.parameter.shared.ICreditManager#filterCompte(java.lang.String)
	 */
	public List<String> filterCompte(String client , String age){

		// variable
		List<String> results = new ArrayList<String>();


		try{

			// Connexion
			openDELTAConnection();
			if(con == null) return results;
			String sql = "select age,ncp,clc from bkcom  where ncp[1,7]='"+client+"' and ife='N' and cfe='N' ";
			ResultSet rs = con.createStatement().executeQuery(sql);
			while(rs.next()){
				if(rs.getString("ncp").substring(7,10).equalsIgnoreCase("105") || rs.getString("ncp").substring(7,10).equalsIgnoreCase("100") || rs.getString("ncp").substring(7,10).equalsIgnoreCase("101")){
					results.add(rs.getString("age")+rs.getString("ncp")+rs.getString("clc"));
				}
			}
			// fermeture
			closeConnection();

		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}

		return results;

	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.buisness.parameter.shared.ICreditManager#filterCompte(java.lang.String)
	 */
	public List<String> filterAgenceClient(String client){

		// variable
		List<String> results = new ArrayList<String>();

		try{

			// Connexion
			openDELTAConnection();
			if(con == null) return results;
			String sql = "select distinct age from bkcom  where ncp[1,7]='"+client+"'";
			ResultSet rs = con.createStatement().executeQuery(sql);
			while(rs.next()){
				results.add(rs.getString("age"));
			}
			// fermeture
			closeConnection();

		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return results;

	}

	@Override
	public GabParameter saveParameter(GabParameter entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GabParameter updateParameter(GabParameter entity) {
		// TODO Auto-generated method stub
		return null;
	}


}