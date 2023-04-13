package com.afb.portal.buisness.monitoring.birtday;

import javax.ejb.Remote;

/**
 * Interface EJB gestion des dates d anniversaire des clients de cartes Prapayer
 * @author Owner
 * @version 1.0
 */
@Remote
public interface IBirthdayManager {

	/**
	 * Service Metier de gestion des utilisateurs
	 */
	public static final String SERVICE_NAME = "BirthdayManager";

	public void process();
	
}
