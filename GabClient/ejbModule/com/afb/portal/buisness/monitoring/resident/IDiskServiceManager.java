package com.afb.portal.buisness.monitoring.resident;

import javax.ejb.Remote;

@Remote
public interface IDiskServiceManager {
	
	/**
	 * Service Metier de gestion des utilisateurs
	 */
	public static final String SERVICE_NAME = "DiskServiceManager";

	public void process();

}
