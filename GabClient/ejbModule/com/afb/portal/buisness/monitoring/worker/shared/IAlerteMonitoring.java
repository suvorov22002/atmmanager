package com.afb.portal.buisness.monitoring.worker.shared;

import javax.ejb.Remote;

@Remote
public interface IAlerteMonitoring {
	
	/**
	 * Service Metier de gestion des utilisateurs
	 */
	public static final String SERVICE_NAME = "AlerteMonitoring";

	public void process();
	
	public void creerTimer();
	
}
