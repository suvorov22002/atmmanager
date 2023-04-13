package com.afb.portal.buisness.atmManage.parameter;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.afb.portal.buisness.monitoring.worker.shared.IRepportManager;
import com.afb.portal.buisness.parameter.shared.IGabManager;
import com.afb.portal.dao.shared.IAtmDAOLocal;
import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.equipment.AtmStatus;
import com.yashiro.persistence.utils.dao.tools.OrderContainer;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

public class ServiceStarted {
	
	public static IAtmDAOLocal dao = null; 
	
	public static IRepportManager repportManager = null; 
	
	public static IGabManager atm = null; 
		
		
}
