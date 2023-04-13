package com.afb.portal.buisness.monitoring.worker.shared;

import java.util.Date;

import javax.ejb.Remote;

@Remote
public interface ISauvegardeMonitoringWorker {

	public void creerTimer();
	
	public   void process(Date datedebut , Date datefin);
	
}
