package com.afb.portal.buisness.monitoring.trans;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.time.DateUtils;

import afb.dsi.dpd.portal.jpa.entities.User;

import com.afb.portal.buisness.monitoring.worker.shared.IRapportJourMonitoringTPE;
import com.afb.portal.buisness.monitoring.worker.shared.IRepportManager;


@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class Telecollecte implements ITelecollecte{
	

	/**
	 * Service Repport
	 */
	@EJB
	private IRepportManager repportManager;
	
	@EJB
	private IRapportJourMonitoringTPE rapportJourMonitoringTPE;
	
	private TimerTask task;
	private Timer timer;
	

	/**
	 * creerTimer
	 */
	public void creerTimer(){

		try{

			// boucle
			int sec = 60;
			int min = 1;
			if(timer != null)timer.cancel();
			if(task != null)task.cancel();

			task = new TimerTask(){
				@Override
				public void run(){
					try {
						process();
					}catch(Exception e){
						e.printStackTrace();
					}
				}	
			};

			String times =  repportManager.findTimeTelecollecte();
			//String mois = new SimpleDateFormat("HH:mm:ss").format(new Date());
			GregorianCalendar cal = new GregorianCalendar();
			String[] temps = times.split(":");
			cal.setTime(DateUtils.addDays(new Date(),+1));
			cal.set(Calendar.HOUR,Double.valueOf(temps[0]).intValue());
			cal.set(Calendar.MINUTE,Double.valueOf(temps[1]).intValue());
			cal.set(Calendar.MILLISECOND,0);
			Date date = cal.getTime();		
			
			timer = new Timer(true);
			timer.schedule(task,date, min*sec*1000);

		}catch(Exception e){
			// TODO: handle exception
			e.printStackTrace();
			creerTimer();
		}

	}


	@Override
	public void process(User user,Date datedebut, Date datefin){
		// TODO Auto-generated method stub
		rapportJourMonitoringTPE.processTelecollecte(user, datedebut, datefin);
	}
	
	

	public void process(){
		// TODO Auto-generated method stub
		Date datedebut = DateUtils.addDays(new Date(), -1);
		Date datefin = new Date();
		rapportJourMonitoringTPE.processTelecollecte(null, datedebut, datefin);
		creerTimer();
	}

}
