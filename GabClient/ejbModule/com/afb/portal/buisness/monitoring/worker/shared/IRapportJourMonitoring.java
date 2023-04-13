package com.afb.portal.buisness.monitoring.worker.shared;

import javax.ejb.Remote;

import afb.dsi.dpd.portal.jpa.entities.User;

@Remote
public interface IRapportJourMonitoring {

	public void creerTimer();
	
	public  void process(User user);
	
	//public  void process();
}
