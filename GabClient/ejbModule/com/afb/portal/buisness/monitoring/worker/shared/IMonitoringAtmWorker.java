package com.afb.portal.buisness.monitoring.worker.shared;

import javax.ejb.Remote;

@Remote
public interface IMonitoringAtmWorker {

	public void process();
	
	public void creerTimer();
	
}
