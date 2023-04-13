/**
 * 
 */
package com.afb.portal.buisness.parameter.shared;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import afb.dsi.dpd.portal.jpa.entities.Branch;

import com.afb.portal.jpa.gab.parameter.AtmIncident;
import com.afb.portal.jpa.gab.parameter.GabParameter;
import com.afb.portal.jpa.gab.parameter.IncidentAtm;
import com.yashiro.persistence.utils.dao.tools.PropertyContainer;

/**
 * Interface Metier de gestion des Parametrages generaux
 * @author Francis K
 * @version 1.0
 */
@Remote
public interface IGabManager{

	/**
	 * Service Metier de gestion des utilisateurs
	 */
	public static final String SERVICE_NAME = "GabManager";
	
	
	//public void creerTimerAlerteMonitoring();
	//public void creerTimerMonitoringAtmWorker();
	//public void creerTimerRapportJourMonitoring();
	//public void creerTimerSauvegardeMonitoringWorker();
	
	public GabParameter findParameter(String value);
	
	public void InitEJB();
	
	/**
	 * 
	 * @param detedebut
	 * @param detefin
	 * @param codeinc
	 * @return
	 */
	public List<AtmIncident> filterAtmIncident(Date detedebut, Date detefin , String codeinc);
	
	/**
	 * 
	 * @param detedebut
	 * @param detefin
	 * @param codeAtm
	 * @return
	 */
	public List<IncidentAtm> filterIncidentAtm(Date detedebut, Date detefin , String codeAtm);
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	public GabParameter saveParameter(GabParameter entity);
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	public GabParameter updateParameter(GabParameter entity);
	
	/**
	 * 
	 * @param txtCode
	 * @return
	 */
	public GabParameter filterParameter(String txtCode);
	
	/**
	 * 
	 * @param txtCode
	 * @param txtparam
	 * @param txtdecription
	 * @return
	 */
	public List<GabParameter> filterParameter(String txtCode,String txtparam,String txtdecription);

	/**
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param entityID
	 * @param properties
	 * @return
	 */
	public <T> T findByPrimaryKey(Class<T> entityClass, Object entityID,PropertyContainer properties);
	
	
	/**
	 * filter Branch
	 * @return
	 */
	public List<Branch> filterBranch();
	
	/**
	 * 
	 */
	public void openDELTAConnection();
	
	/**
	 * 
	 */
	public void openSVFEConnection();
	
	/**
	 * 
	 */
	public void openSVBOConnection();

}
