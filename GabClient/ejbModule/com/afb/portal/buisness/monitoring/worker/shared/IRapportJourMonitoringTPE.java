package com.afb.portal.buisness.monitoring.worker.shared;

import java.util.Date;

import javax.ejb.Remote;

import afb.dsi.dpd.portal.jpa.entities.User;

@Remote
public interface IRapportJourMonitoringTPE {

	public void creerTimer();
	
	// public  void process();
	
	public  void process(User user);
	
	
	public void processTransactions(User user,Date datedebut, Date datefin);
	
	public void processTelecollecte(User user,Date datedebut, Date datefin);
	
}
