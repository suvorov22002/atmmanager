/**
 * 
 */
package com.afb.portal.presentation.tools.listeners;

import javax.ejb.Asynchronous;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.afb.portal.buisness.atmManage.parameter.ServiceStarted;
import com.afb.portal.buisness.monitoring.birtday.IBirthdayManager;
import com.afb.portal.buisness.monitoring.resident.IDiskServiceManager;
import com.afb.portal.buisness.monitoring.resident.INetworkServiceManager;
import com.afb.portal.buisness.monitoring.resident.IResidentDeltaManager;
import com.afb.portal.buisness.monitoring.resident.IResidentServiceManager;
import com.afb.portal.buisness.monitoring.trans.ITelecollecte;
import com.afb.portal.buisness.monitoring.trans.ITransactions;
import com.afb.portal.buisness.monitoring.worker.shared.IAlerteMonitoring;
import com.afb.portal.buisness.monitoring.worker.shared.IMonitoringAtmWorker;
import com.afb.portal.buisness.monitoring.worker.shared.IMonitoringTPEWorker;
import com.afb.portal.buisness.monitoring.worker.shared.IRapportJourMonitoring;
import com.afb.portal.buisness.monitoring.worker.shared.IRapportJourMonitoringTPE;
import com.afb.portal.buisness.monitoring.worker.shared.IRepportManager;
import com.afb.portal.buisness.monitoring.worker.shared.ISauvegardeMonitoringWorker;
import com.afb.portal.buisness.monitoring.worker.shared.ISynchroManager;
import com.afb.portal.buisness.parameter.shared.IGabManager;
import com.afb.portal.dao.shared.IAtmDAOLocal;
import com.afb.portal.presentation.tools.ViewHelper;

/**
 * Listener du chargement du Contexte de l'application
 * @author Francis K
 * @version 2.0
 */
public class ContextLoaderListener implements ServletContextListener {

	/**
	 *  Contexte JNDI
	 */
	private Context ctx = null;

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event){

		try {

			// Si le contexte n'est pas null
			if(ctx != null) ctx.close();

		} catch (Exception e) {

			// On relance l'exception
			throw new RuntimeException("Erreur lors de la fermeture du Contexte JNDI", e);

		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event){

		try {

			// Initialisation du contexte JNDI
			ctx = new InitialContext();

		}catch(Exception e){
			// On relance l'exception
			throw new RuntimeException("Erreur lors de l'initialisation du Contexte JNDI", e);
		}

		try{

			// Service
			ViewHelper.dao = (IAtmDAOLocal) ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + IAtmDAOLocal.SERVICE_NAME + "/local");
			
			// Service
			ServiceStarted.dao = (IAtmDAOLocal) ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + IAtmDAOLocal.SERVICE_NAME + "/local");
			ServiceStarted.repportManager = (IRepportManager) ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + "RepportManager" + "/remote");
			
			ServiceStarted.atm = (IGabManager) ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + IGabManager.SERVICE_NAME + "/remote");
			
			//ViewHelper.facadeManager = (IFacadeManagerRemote) new InitialContext().lookup(PortalHelper.APPLICATION_EAR + "/" + IFacadeManagerRemote.SERVICE_NAME + "/remote");
			ViewHelper.IRepportManager = (IRepportManager) ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + "RepportManager" + "/remote");

			// Chargement du service de gestion des Utilisateurs
			ViewHelper.atm = (IGabManager) ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + IGabManager.SERVICE_NAME + "/remote");

			// ATM
			ViewHelper.IAlerteMonitoring = (IAlerteMonitoring) ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + IAlerteMonitoring.SERVICE_NAME + "/remote");
			ViewHelper.ISauvegardeMonitoringWorker = (ISauvegardeMonitoringWorker) ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + "SauvegardeMonitoringWorker" + "/remote");
			ViewHelper.IMonitoringAtmWorker = (IMonitoringAtmWorker) ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + "MonitoringAtmWorker" + "/remote");
			ViewHelper.IRapportJourMonitoring = (IRapportJourMonitoring) ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + "RapportJourMonitoring" + "/remote");

			// TPE
			ViewHelper.iMonitoringTPEWorker = (IMonitoringTPEWorker)ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + "MonitoringTPEWorker" + "/remote");
			ViewHelper.iRapportJourMonitoringTPE = (IRapportJourMonitoringTPE)ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + "RapportJourMonitoringTPE" + "/remote");
			
			ViewHelper.SynchroManager = (ISynchroManager)ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + "SynchroManager" + "/remote");
						
			ViewHelper.BirtdayManager = (IBirthdayManager)ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + IBirthdayManager.SERVICE_NAME + "/remote");
			ViewHelper.NetworkServiceManager = (INetworkServiceManager)ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + INetworkServiceManager.SERVICE_NAME + "/remote");
			ViewHelper.DiskServiceManager = (IDiskServiceManager)ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + IDiskServiceManager.SERVICE_NAME + "/remote");
			ViewHelper.ResidentServiceManager = (IResidentServiceManager)ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + "ResidentServiceManager" + "/remote");
			ViewHelper.ResidentDeltaManager = (IResidentDeltaManager)ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + "ResidentDeltaManager" + "/remote");

			ViewHelper.transactions = (ITransactions)ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + "Transactions" + "/remote");
			ViewHelper.telecollecte = (ITelecollecte)ctx.lookup(ViewHelper.EAR_APPLICATION + "/" + "Telecollecte" + "/remote");
						
			// Initiation des Timer
			Lanche();

		}catch (Exception e){
			// On relance l'exception
			throw new RuntimeException("Erreur lors du chargement des Services Métiers", e);
		}

	}


	@Asynchronous
	public void Lanche(){

		try{

			/**  */
			ViewHelper.IRepportManager.findActiveAtm(Boolean.TRUE);
			ViewHelper.IRepportManager.findActiveTPE(Boolean.TRUE);
			ViewHelper.SynchroManager.creerTimer();
			ViewHelper.IRepportManager.creerTimer();
			
			//Initiation des Timer
			ViewHelper.IAlerteMonitoring.creerTimer();
			ViewHelper.IMonitoringAtmWorker.creerTimer();
			ViewHelper.ISauvegardeMonitoringWorker.creerTimer();
			
			ViewHelper.IRapportJourMonitoring.creerTimer();
			ViewHelper.iMonitoringTPEWorker.creerTimer();
			ViewHelper.iRapportJourMonitoringTPE.creerTimer();
			
			ViewHelper.BirtdayManager.process();
			ViewHelper.NetworkServiceManager.process();
			ViewHelper.DiskServiceManager.process();
			ViewHelper.ResidentServiceManager.process();
			//ViewHelper.ResidentDeltaManager.process();
			
			//ViewHelper.transactions.creerTimer();
			//ViewHelper.telecollecte.creerTimer();
			
		}catch(Exception e){
			// On relance l'exception
			e.printStackTrace();
		}

	}

}
