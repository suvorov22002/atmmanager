package com.afb.portal.buisness.monitoring.resident;

import javax.ejb.Remote;

@Remote
public interface INetworkServiceManager {
	
	/**
	 * Service Metier de gestion des utilisateurs
	 */
	public static final String SERVICE_NAME = "NetworkServiceManager";

	public void process();

}
