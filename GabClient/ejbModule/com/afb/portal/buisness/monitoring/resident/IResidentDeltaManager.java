package com.afb.portal.buisness.monitoring.resident;

import javax.ejb.Remote;


/**
 * Interface EJB gestion du Monitoring de resident Delta
 * @author Owner
 * @version 1.0
 */
@Remote
public interface IResidentDeltaManager {

	/**
	 * Service Metier de gestion des utilisateurs
	 */
	public static final String SERVICE_NAME = "ResidentDeltaManager";

	public void process();
	
}
