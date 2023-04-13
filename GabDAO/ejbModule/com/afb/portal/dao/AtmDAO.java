/**
 * 
 */
package com.afb.portal.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.afb.portal.dao.shared.IAtmDAOLocal;
import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.equipment.AtmStatus;
import com.yashiro.persistence.utils.dao.JPAGenericDAORulesBased;

/**
 * Implementation du service DAO Local de gestion des Parametrages generaux
 * @author Francis K
 * @version 1.0
 */
@Stateless(name = IAtmDAOLocal.SERVICE_NAME, mappedName = IAtmDAOLocal.SERVICE_NAME, description = "Service DAO Local")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AtmDAO extends JPAGenericDAORulesBased implements IAtmDAOLocal {
	
	/**
	 * Le gestionnaire d'entites
	 */
	@PersistenceContext(unitName = "GabEAR")
	private EntityManager entityManager;

	/**
	 * Le Logger
	 */
	private static Log logger = LogFactory.getLog(AtmDAO.class);
		
	/*
	 * (non-Javadoc)
	 * @see com.yashiro.persistence.utils.dao.IJPAGenericDAO#getEntityManager()
	 */
	@Override
	public EntityManager getEntityManager() {
		
		// Un log
		logger.trace("[getEntityManager]");
		
		// On retourne le gestionnaire d'entites
		return entityManager;
	}
	
	/**
	 * findActiveAtm
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized  List<Atm> findActiveAtm(){
			
		Query query = entityManager.createQuery("select a from Atm a where status=:status and typeAtm=:typeAtm ");
		query.setParameter("status", Boolean.TRUE);
		query.setParameter("typeAtm", AtmStatus.ATM);
		return  query.getResultList();
		
	}

	/**
	 * findActiveTPE
	 */
	@SuppressWarnings("unchecked")
	public synchronized List<Atm> findActiveTPE(){
		
		Query query = entityManager.createQuery("select a from Atm a where status=:status and typeAtm=:typeAtm and ip is not null");
		query.setParameter("status", Boolean.TRUE);
		query.setParameter("typeAtm", AtmStatus.TPE);
		return  query.getResultList();
		
	}
	
}
