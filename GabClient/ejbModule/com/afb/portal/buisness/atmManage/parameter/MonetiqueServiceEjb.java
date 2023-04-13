package com.afb.portal.buisness.atmManage.parameter;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.criterion.Restrictions;

import afb.dsi.dpd.portal.jpa.entities.DataSystem;

import com.afb.portal.buisness.monitoring.worker.ConnexionSVFE;
import com.afb.portal.buisness.monitoring.worker.shared.IRepportManager;
import com.afb.portal.dao.shared.IAtmDAOLocal;
import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.parameter.GabParameter;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

/**
 * EJB de Monitoring des services Monétiques
 * @author Owner
 * @version 1.0
 */
@Stateless
public class MonetiqueServiceEjb implements IMonetiqueServiceEjb {
	
	
	/**
	 * Service DAO
	 */
	@EJB
	private IAtmDAOLocal dao; 
	
	/**
	 * Service Repport
	 */
	@EJB
	private IRepportManager repportManager ;
	
	/**
	 * DataSystem
	 */
	private DataSystem system = null;
	
	
	private ConnexionSVFE svfe = new ConnexionSVFE();

	// Lancer
	
	
	// RésidentMonétique
	public Boolean processResidentMonetique(){
		try{
			String sql = "";
			ResultSet rs ;
			if(system == null )system = repportManager.findDataSystem("SMVISTA");
			svfe.openSVFEConnection(system);
			sql = "select op_stat from host_stat_tab where op_stat=1  and proc_stat=2 ";
			rs = svfe.Execute(sql);
			if(rs.next()) return Boolean.TRUE;
			svfe.closeSVFEConnection();
		}catch(Exception e){
			return Boolean.FALSE;
		}
		return Boolean.FALSE;
	}
	
	
	//-	Les statuts des résidents côté SmartVista et Amplitude, de les relancer dans le cas où les statuts sont différents de la normale
	
	
	
	//-	L’état des services monétiques (atndnt_srv, nwint00, atmi00,atmi_wincor etc…)
		
	
	
	//-	L’espace disque sur le serveur monétique
	
	
	
	//-	Le statut des réseaux internationaux Visa et MasterCard.

	
	
	
	
	/**
	 * 
	 */
	public void process(){

		try{
			String sql = "";
			ResultSet rs ;
			ConnexionSVFE svfe = new ConnexionSVFE();
			if(system == null )system = repportManager.findDataSystem("SMVISTA");
			svfe.openSVFEConnection(system);
			List<Atm> Atms = repportManager.findActiveAtm(Boolean.FALSE);

			Map<String,String> MapAtmHosService = new HashMap<String, String>();
			sql = "select pid,tid,remote_address,tid,street,city from def_tab,tcp_tab  " +
			" where tcp_tab.pid=def_tab.id and tid not in (select tid  from V_ATM_WARNING ) order  by street";
			rs = svfe.Execute(sql);
			while(rs.next()){
				MapAtmHosService.put(rs.getString("tid"),rs.getString("street"));
			}
		}catch (Exception e) {
			e.printStackTrace();
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
	
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public GabParameter findParameter(String value){
		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("code",value));
		List<GabParameter>  systems = dao.filter(GabParameter.class,null,rc,null,null,0,-1);
		if(!systems.isEmpty()){
			return systems.get(0);
		}
		return null;
	}
}
