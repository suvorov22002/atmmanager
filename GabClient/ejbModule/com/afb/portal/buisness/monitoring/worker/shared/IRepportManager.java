package com.afb.portal.buisness.monitoring.worker.shared;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import afb.dsi.dpd.portal.jpa.entities.DataSystem;

import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.parameter.GabParameter;
import com.afb.portal.jpa.gab.parameter.TypeIncident;
import com.afb.portal.jpa.gab.statistisques.Incident;

@Remote
public interface IRepportManager {
	
	public List<Atm> findActiveAtm(Boolean charg);
	
	public List<Atm> findActiveTPE(Boolean charg);
	
	public void findDataSystem();
	
	public List<Atm> findActiveAtm();
	
	public List<Atm> findActiveTPE();
	
	
	public void creerTimer();
	
	/**
	 * findTimeSaugarde
	 * @return
	 */
	public String findTimeSaugarde();
	
	/**
	 * 
	 * @return
	 */
	public Boolean  checkTimeSolde();
	
	public Boolean  checkTimeSauvegarde();
	
	/**
	 * 
	 * @return
	 */
	public Boolean  checkTime();
	
	/**
	 * 
	 * @return
	 */
	public  String getReportsDir();

	/**
	 * 
	 * @param outFileName
	 * @param param
	 * @param collections
	 * @param reportFileName
	 */
	public void ExportReportPDF(String outFileName,HashMap<Object, Object> param,Collection<?> collections,String reportFileName);
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public GabParameter findParameter(String value);
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public TypeIncident findTypeIncident(String code);
	
	/**
	 * 
	 * @param atm
	 * @param typeIncident
	 * @return
	 */
	public Incident findIncident(Atm atm,TypeIncident typeIncident);
	
	
	/**
	 * findIncident
	 * @param atm
	 * @param typeIncident
	 * @param debut
	 * @param fin
	 * @return
	 */
	public List<Incident> findIncident(Atm atm,TypeIncident typeIncident, Date debut , Date fin);
	
	/**
	 * 
	 * @param journal
	 * @return
	 */
	public List<TypeIncident> findTypeIncident(Boolean journal);
	
	/**
	 * 
	 * @return
	 */
	public String findTimeMonitoring();
	
	public String findTimeTelecollecte();
	
	public String findTimeTransaction();
	
	
	public String findTimeMonitoringTPE();
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public DataSystem findDataSystem(String value);
	
}
