package com.afb.portal.buisness.monitoring.worker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.time.DateUtils;

import afb.dsi.dpd.portal.jpa.entities.DataSystem;

import com.afb.portal.buisness.monitoring.worker.shared.IRepportManager;
import com.afb.portal.buisness.monitoring.worker.shared.ISynchroManager;
import com.afb.portal.dao.shared.IAtmDAOLocal;
import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.equipment.AtmStatus;
import com.afb.portal.jpa.gab.parameter.GabParameter;


/**
 * 
 * @author Owner
 * @version 1.0
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
public class SynchroManager implements ISynchroManager{
	
	/**
	 * Service DAO
	 */
	@EJB
	private IAtmDAOLocal dao;

	/**
	 * Service Repport
	 */
	@EJB
	private IRepportManager repportManager;
	
	private DataSystem system = null;
	
	private TimerTask task;
	private Timer timer;

	/**
	 * creerTimer
	 */
	public void creerTimer(){

		try{

			// boucle
			int sec = 60;
			int min = 30;
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

			timer = new Timer(true);
			timer.scheduleAtFixedRate(task, DateUtils.addMinutes(new Date(),1) , min*sec*1000);

		}catch(Exception e){
			e.printStackTrace();
			creerTimer();
		}

	}
	

	public void process(){

		try {
			
			if(system == null )system = repportManager.findDataSystem("SMVISTA");
			
			//processMAJTPES();
			processADDTPE();
			processGabs();
			processMAJTPES();
			//processTPES();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}


	public void processGabs(){

		try {
			//System.out.println("------processGabs------");
			ConnexionSVFE svfe = new ConnexionSVFE();
			svfe.openSVFEConnection(system);
			String sqlparam = "select pid,remote_address,tid,street,city from def_tab,tcp_tab" +
			" where tcp_tab.pid=def_tab.id order  by tid ";
			ResultSet rs = svfe.Execute(sqlparam);
			while(rs.next()){
				Atm atm = new Atm();
				atm.setTypeAtm(AtmStatus.ATM);
				atm.setTid(rs.getString("tid"));
				atm.setPid(rs.getString("pid"));
				atm.setIp(rs.getString("remote_address"));
				atm.setAdresse(rs.getString("city"));
				atm.setNom(rs.getString("street"));
				//
				Atm test = dao.findByPrimaryKey(Atm.class, atm.getTid(), null);
				if(test == null){
					dao.save(atm);
				}
			}
			svfe.closeSVFEConnection();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 
	 */
	public void processMAJTPES(){

		try{

			List<Atm> Atms = repportManager.findActiveTPE(Boolean.FALSE);
			ConnexionSVFE svfe = new ConnexionSVFE();
			svfe.openSVFEConnection(system);
			for(Atm atm :Atms){
				ResultSet rs = null;
				try {
					String sqlparam = "select pid,merch_number,merch_name,term_id_number,merch_cat,addr2 from pos_def_tab p, merch_tab m" +
					" where m.merch_id=p.merch_number and term_id_number='"+atm.getTid()+"'order by merch_number";
					rs = svfe.Execute(sqlparam);
					if(rs.next()){
						atm.setNom(rs.getString("merch_name"));
						dao.update(atm);	
					}
				}catch (Exception e){
				}		
			}

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}


	/**
	 * 
	 */
	public void processADDTPE(){

		try{

			//System.out.println("------processADDTPE------");
			ConnexionSVFE svfe = new ConnexionSVFE();
			svfe.openSVFEConnection(system);
			String sqlparam = "select pid,merch_number,merch_name,term_id_number,merch_cat,addr2 from pos_def_tab p, merch_tab m" +
			" where m.merch_id=p.merch_number order by merch_number";
			ResultSet rs = svfe.Execute(sqlparam);
			while(rs.next()){
				Atm atm = new Atm();
				atm.setTypeAtm(AtmStatus.TPE);
				atm.setAdresse(rs.getString("addr2"));
				atm.setTid(rs.getString("term_id_number"));
				atm.setPid(rs.getString("pid"));
				atm.setIp("");
				atm.setNom(rs.getString("merch_name"));
				//
				Atm test = dao.findByPrimaryKey(Atm.class, atm.getTid(), null);
				if(test == null){
					dao.save(atm);
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void processTPES(){

		try {


			GabParameter p = repportManager.findParameter("REP_FILE_TPE");
			//System.out.println("------processTPES------"+p);
			if(p != null){

				ConnexionSVFE svfe = new ConnexionSVFE();
				svfe.openSVFEConnection(system);

				File fichiers = new File(p.getValue());

				File[] files = fichiers.listFiles();
				for(File uploadedFile : files){

					// Chargement du Fichier
					//System.out.println("--uploadedFile--"+uploadedFile.getName());
					BufferedReader br = null;
					String line = "";
					br = new BufferedReader(new FileReader(uploadedFile));

					while((line = br.readLine()) != null){
						String[] r = line.split(";");

						Atm atm = new Atm();
						atm.setTypeAtm(AtmStatus.TPE);

						atm.setTid(r[0]);
						atm.setPid(r[0]);
						atm.setIp("");
						atm.setNom(r[1]);
						//
						Atm test = dao.findByPrimaryKey(Atm.class, atm.getTid(), null);
						if(test == null){
							dao.save(atm);
						}else{
							ResultSet rs = null;
							try{
								String sqlparam = "select pid,merch_number,merch_name,term_id_number,merch_cat,addr1 from pos_def_tab p, merch_tab m" +
								" where m.merch_id=p.merch_number and term_id_number='"+r[0]+"'order by merch_number";
								rs = svfe.Execute(sqlparam);
							}catch(Exception e){}

							if(rs != null){
								atm.setAdresse(rs.getString("addr2"));
								dao.update(test);	
							}
						}
					}

				}

				svfe.closeSVFEConnection();

			}

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
