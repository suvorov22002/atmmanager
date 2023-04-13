package com.afb.portal.buisness.monitoring.trans;

import java.util.Date;

import javax.ejb.Remote;

import afb.dsi.dpd.portal.jpa.entities.User;

@Remote
public interface ITelecollecte {

	public void creerTimer();

	public void process(User user,Date datedebut, Date datefin);
	
	public void process();

}
