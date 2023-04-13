package com.afb.portal.buisness.monitoring.tpe.worker;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.time.DateUtils;

import com.afb.portal.buisness.monitoring.worker.NetworkPing;
import com.afb.portal.buisness.monitoring.worker.shared.IMonitoringTPEWorker;
import com.afb.portal.buisness.monitoring.worker.shared.IRepportManager;
import com.afb.portal.dao.shared.IAtmDAOLocal;
import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.parameter.TypeIncident;
import com.afb.portal.jpa.gab.statistisques.Incident;

/**
 * 
 * @author Owner
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
public class MonitoringTPEWorker implements IMonitoringTPEWorker {

	
	public static final String moduleCode = "GABM_001";


	public static final String moduleName ="MonitoringATM";


	public static final String from = "alerteTPE@afrilandfirstbank.com";


	public static final Integer rupture = 100000;

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
	
	private TimerTask task;
	private Timer timer;
	
	/**
	 * 
	 */
	public  void creerTimer(){

		try{

			// boucle
			int sec = 60;
			int min = 25;
			
			if(timer != null)timer.cancel();
			if(task != null)task.cancel();

			task = new TimerTask(){
				@Override
				public void run(){
					try {
						process();
					}catch(Exception e){
						e.printStackTrace();
					}
				}	
			};

			timer = new Timer(true);
			timer.scheduleAtFixedRate(task, DateUtils.addMinutes(new Date(),5), min*sec*1000);

		}catch(Exception e){
			// TODO: handle exception
			e.printStackTrace();
			creerTimer();
		}
	}

	/**
	 * 
	 */
	public  void process(){

		try{
			
			// chargment du fichier d erreur repportManager.findActiveAtm(Boolean.FALSE);
			TypeIncident typInci = null;
			List<Atm> Atms = repportManager.findActiveTPE(Boolean.FALSE);

			// Archivage
			typInci = repportManager.findTypeIncident(Incident.ConnexionReseau);
			//System.out.println("---------MonitoringTPEWorker-----------"+Atms.size());
			// parcour
			for(Atm value :Atms){

				// libelle = ""; 
				//libelleAdmin = "";

				if(value.getIp() != null && !value.getIp().trim().isEmpty()){
					// Controle du Ping
					boolean ping =  NetworkPing.isReachableByPing(value.getIp());
					Incident inci = repportManager.findIncident(value, typInci);
					if(ping == false ){
						if(inci == null){
							inci = new Incident();
							inci.setAtm(value);
							inci.setTypeIncident(typInci); 
							inci.setNomberNonOK(inci.getNomberNonOK() + 1);
							inci.setMotnomberNonOK(inci.getMotnomberNonOK() + 1);
							dao.save(inci);
						}else{
							inci.setMotnomberNonOK(inci.getMotnomberNonOK() + 1);
							inci.setNomberNonOK(inci.getNomberNonOK() + 1);
							dao.update(inci);
						}
					}else{
						if(inci == null){
							inci = new Incident();
							inci.setAtm(value);
							inci.setTypeIncident(typInci); 
							inci.setNomber(inci.getNomber() + 1);
							inci.setMotnomberOK(inci.getMotnomberOK() + 1);
							dao.save(inci);
						}else{
							inci.setNomber(inci.getNomber() + 1);
							inci.setMotnomberOK(inci.getMotnomberOK() + 1);
							dao.update(inci);
						}
					}
				}

			}

		}catch(Exception ex){
			ex.printStackTrace();
		}

	}


}
