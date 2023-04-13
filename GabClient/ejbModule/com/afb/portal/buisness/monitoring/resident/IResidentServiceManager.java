package com.afb.portal.buisness.monitoring.resident;

import javax.ejb.Remote;

/**
 * Interface EJB gestion du Monitoring des service Monetiques
 * @author Owner
 * @version 1.0
 */
@Remote
public interface IResidentServiceManager {
	
	/**
	 * Service Metier de gestion des utilisateurs
	 */
	public static final String SERVICE_NAME = "ResidentServiceManager";

	public void process();

}
