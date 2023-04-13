/**
 * 
 */
package com.afb.portal.dao.shared;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import com.afb.portal.jpa.gab.equipment.Atm;
import com.yashiro.persistence.utils.dao.IJPAGenericDAO;


/**
 * Interface DAO Locale de gestion des Parametrages generaux
 * @author Francis K
 * @version 1.0
 */
@Local
public interface IAtmDAOLocal extends IJPAGenericDAO {
	
	/**
	 * Nom du Service DAO de gestion des Parametrages generaux
	 */
	public static final String SERVICE_NAME = "AtmDAO";
	
	
	public EntityManager getEntityManager();
	
	
	public  List<Atm> findActiveAtm();
	
	public  List<Atm> findActiveTPE();
	
}
