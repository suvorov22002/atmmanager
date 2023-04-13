package com.afb.portal.buisness.monitoring.atm.worker;

import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.afb.portal.buisness.monitoring.worker.ConnexionSVFE;
import com.afb.portal.buisness.monitoring.worker.shared.IRepportManager;
import com.afb.portal.buisness.monitoring.worker.shared.ISauvegardeMonitoringWorker;
import com.afb.portal.dao.shared.IAtmDAOLocal;
import com.afb.portal.jpa.alerte.SendFileEmail;
import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.monitoring.RapportElement;
import com.afb.portal.jpa.gab.monitoring.SauvItem;
import com.afb.portal.jpa.gab.monitoring.Sauvegarde;
import com.afb.portal.jpa.gab.parameter.GabParameter;
import com.afb.portal.jpa.gab.parameter.GroupeSauv;
import com.yashiro.persistence.utils.collections.converters.ConverterUtil;

/**
 * SauvegardeMonitoringWorker
 * @author Owner
 * @version 1.0
 */
@Stateless 
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SauvegardeMonitoringWorker implements ISauvegardeMonitoringWorker{

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


	public static final String SENT_DIR = "SAUVEGARDE";


	public static final String moduleCode = "GABM_001";


	public static final String moduleName ="MonitoringATM";


	private static String from = "alerteGab@afrilandfirstbank.com";

	private static Sauvegarde currentObject = new Sauvegarde();

	private Map<String, ThreadSauvegarde> mapThread = new HashMap<String, ThreadSauvegarde>();

	private int limit = 10;
	
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
						check();
					}catch(Exception e){
						e.printStackTrace();
					}
				}	
			};

			timer = new Timer(true);
			timer.schedule(task, DateUtils.addMinutes(new Date(),1) , min*sec*1000);

		}catch(Exception e){
			// TODO: handle exception
			e.printStackTrace();
			creerTimer();
		}

	}

	/**
	 * check
	 */
	public void check(){

		try{

			Boolean lance = Boolean.FALSE;
			String times =  repportManager.findTimeSaugarde();
			String mois = new SimpleDateFormat("HH:mm:ss").format(new Date());
			//System.out.println("----SauvegardeMonitoringWorker--------"+mois);

			if(times != null){
				for(String time : times.split(",")){
					if(mois.startsWith(time)){
						lance = Boolean.TRUE; 
					}
				}
				if(Boolean.TRUE.equals(lance)){
					Date dateTaitement = DateUtils.addDays(new Date(),-1);
					process(dateTaitement);
				}
			}

		}catch(Exception e){
			// TODO: handle exception
			e.printStackTrace();
			//creerTimer();
		}

	}


	public List<RapportElement> processSauvegarde(Date dateTaitement){

		try{

			//System.out.println("----processSauvegarde----11----");
			// Controle de l heure
			// if(Boolean.FALSE.equals(repportManager.checkTimeSauvegarde())) return null;

			ThreadSauvegarde threadSauvegarde;
			List<RapportElement> collections = new ArrayList<RapportElement>();
			String contenu ="";
			String sql = "";
			ResultSet rs ;
			ConnexionSVFE svfe = new ConnexionSVFE();
			String dateFormat = new SimpleDateFormat("yyyyMMdd").format(dateTaitement);
			List<Atm> Atms = repportManager.findActiveAtm(Boolean.FALSE);
			svfe.openSVFEConnection(repportManager.findDataSystem("SMVISTA"));
			GroupeSauv sauv = null;	
			SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
			String journal = formater.format(dateTaitement)+".jrn";
			String repImage = formater.format(dateTaitement);
			RapportElement element = new RapportElement();
			int ii = 1;
			currentObject = new Sauvegarde();
			currentObject.setLibelle("JOURNAL SAUVEGARDE DU "+dateFormat);
			List<SauvItem> items = new ArrayList<SauvItem>();
			SauvItem item = null;
			mapThread = new HashMap<String, ThreadSauvegarde>();


			for(Atm value :Atms){

				try{

					//System.out.println("---Atm---"+value.getNom());
					element = new RapportElement();
					if(value.getAgence() != null) element.setAge(value.getAgence().getCode());
					else element.setAge("00001");
					element.setAtm(value.getNom());
					element.setColor(String.valueOf(ii));

					if(value.getGroupeSauv() != null) sauv = value.getGroupeSauv();
					ii++;
					if(sauv != null) contenu = sauv.getChemin();
					item = new SauvItem(value,currentObject, contenu);

					// Transaction
					contenu ="";
					boolean transactionAtm = false;
					Boolean archiveJournal = Boolean.FALSE;
					Boolean archiveImage = Boolean.FALSE;
					sql = "select count(*) as nbr from CURR_TRANS where ATMID='"+value.getTid().trim()+"' and UDATE='"+dateFormat+"'";
					rs = svfe.Execute(sql);
					if(rs.next()){
						int nbr = rs.getInt("nbr");
						if(nbr > 0)transactionAtm = true;
					}


					if(transactionAtm == true){

						archiveJournal = Boolean.FALSE;
						archiveImage = Boolean.FALSE;

						if(sauv != null){
							
							if(limit ==  mapThread.size()){
								// Check les Thread qui sont fini
								ControlMap();
							}
							
							if(mapThread.size() == 0){
								String script = "NET USE * /DELETE /YES";
								Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", script});
								AccessDirectorieSAVE(sauv);
							}
							
							if(limit >  mapThread.size()){
								
								boolean accesLog = CopyFileLog.AccessDirectorieFileJOURNAL(value);
								File srcFile = new File("\\\\"+value.getIp()+"\\c$\\JOURNAL\\"+journal);
								if(srcFile.isFile()){
									archiveJournal = Boolean.TRUE;
								}
								
								//boolean accesImages = CopyImageLog.AccessDirectorieFileIMAGE(value);
								boolean accesImages = accesLog;
								srcFile = new File("\\\\"+value.getIp()+"\\c$\\VIDEOARCHIV\\"+repImage);
								if(srcFile.isDirectory()){
									archiveImage = Boolean.TRUE;
								}
								
								if((accesImages == true && Boolean.TRUE.equals(archiveImage) ) || (accesLog == true && Boolean.TRUE.equals(archiveJournal))){
									threadSauvegarde = new ThreadSauvegarde(value.getNom(), sauv, value, repImage, journal);
									threadSauvegarde.start();
									mapThread.put(value.getNom(), threadSauvegarde);
								}
								
								if(Boolean.TRUE.equals(archiveImage) && Boolean.FALSE.equals(archiveJournal)){
									contenu = contenu + "- Journal NON OK"+"\n";
									item.setJournal(Boolean.FALSE);

									contenu = contenu + "- Images OK ";
									item.setImages(Boolean.TRUE);

									element.setEtat("NON");
								}else if(Boolean.FALSE.equals(archiveImage) && Boolean.TRUE.equals(archiveJournal)){
									contenu = contenu + "- Journal OK "+"\n";
									item.setJournal(Boolean.TRUE);		
									contenu = contenu + "- Images NON OK";
									item.setImages(Boolean.FALSE);
									element.setEtat("NON");
								}else if(Boolean.TRUE.equals(archiveImage) && Boolean.TRUE.equals(archiveJournal) ){

									contenu = contenu + "- Journal OK "+"\n";
									item.setJournal(Boolean.TRUE);

									contenu = contenu + "- Images OK ";
									item.setImages(Boolean.TRUE);

									element.setEtat("OK");

								}else if(Boolean.FALSE.equals(archiveImage) && Boolean.FALSE.equals(archiveJournal) ){

									contenu = contenu + "- Journal NON OK"+"\n";
									item.setJournal(Boolean.FALSE);

									contenu = contenu + "- Images NON OK";
									item.setImages(Boolean.FALSE);

									element.setEtat("NON OK");
								}
								
							}

						}else{
							contenu = contenu + "- Journal NON OK"+"\n";
							item.setJournal(Boolean.FALSE);
							contenu = contenu + "- Images NON OK";
							item.setImages(Boolean.FALSE);
							element.setEtat("NON OK");
							element.setEtat("NON OK");						
						}

					}else{
						contenu = contenu + "- Aucune Transaction";
						item.setImages(Boolean.TRUE);
						element.setEtat("OK");
					}

					element.setErreur(contenu);
					collections.add(element);
					items.add(item);

				}catch(Exception ex){
					ex.printStackTrace();
				}

			}

			currentObject.setItems(ConverterUtil.convertCollectionToSet(items));
			svfe.closeSVFEConnection();
			return collections;

		}catch (Exception ex){
			ex.printStackTrace();
		}

		return null;

	}


	public void ControlMap(){

		try{
			
			List<String> values = new ArrayList<String>(); 
			for(String key : mapThread.keySet()){
				ThreadSauvegarde intance = mapThread.get(key);
				if(!intance.isAlive()){
					values.add(key);
				}
			}
			
			for(String key : values){
				ThreadSauvegarde intance = mapThread.get(key);
				if(intance.getArchiveImage().equals(Boolean.FALSE)){
					intance.start();
				}else if(intance.getArchiveJournal().equals(Boolean.FALSE)){
					intance.processCopyFile();
				}else{
					mapThread.remove(key);
				}
			}

			if(0 < mapThread.size()){
				long delayMillis = 5*60*1000;
				Thread.sleep(delayMillis);
				ControlMap();
			}

		}catch(Exception ex){
			ex.printStackTrace();
			ControlMap();
		}

	}
	

	private boolean AccessDirectorieSAVE(GroupeSauv value){

		try{

			String ip = value.getIp();
			String login = value.getLogin(); 
			String password = value.getPsw();
			String script = "";
			if(password != null && login != null && !login.isEmpty() && !password.isEmpty()){
				script = "net use * \\\\"+ip.trim()+"\\c$ /USER:"+login.trim()+" "+password.trim()+" /PERSISTENT:NO";
			}
			Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "NET USE * /DELETE /YES"});
			Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", script});
			File Chemin = new File(value.getChemin());
			if(Chemin.exists()){
				return true;
			}else{
				Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "NET USE * /DELETE /YES"});
				Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", script});
				Chemin = new File(value.getChemin());
				if(Chemin.exists()){
					return true;
				}else{
					Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "NET USE * /DELETE /YES"});
					Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", script});
					Chemin = new File(value.getChemin());
					if(Chemin.exists()){
						return true;
					}
				}
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}

		return false;

	}


	/**
	 * process
	 */
	public   void process(Date datedebut , Date datefin){

		try{
			Date dateTaitement = datedebut;
			SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
			String txtdatedebut = formater.format(datedebut);
			String txtdatefin = formater.format(datefin);

			while(!txtdatedebut.equalsIgnoreCase(txtdatefin)){
				process(dateTaitement);				
				dateTaitement = DateUtils.addDays(datedebut,1);
				txtdatedebut = formater.format(dateTaitement);
			}

			if(txtdatedebut.equalsIgnoreCase(txtdatefin)){
				process(dateTaitement);	
			}

		}catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	/**
	 * process
	 */
	public   void process(Date dateTaitement){

		try{

			List<RapportElement> collections = new ArrayList<RapportElement>();
			collections = processSauvegarde(dateTaitement);
			//System.out.println("----process----collections---"+collections);
			while(collections == null){
				collections = new ArrayList<RapportElement>();
				collections = processSauvegarde(dateTaitement);
			}
			if(collections.isEmpty()) return;
			Collections.sort(collections);
			//System.out.println("----process----collections---"+collections.size());
			int i = 1;
			int nbok = 0;
			int nbnonok = 0;
			int nbnon = 0;
			for(RapportElement el : collections){
				el.setColor(String.valueOf(i));
				i++;
				if(el.getEtat().equalsIgnoreCase("OK"))nbok++;
				else if(el.getEtat().equalsIgnoreCase("NON OK")) nbnonok++;
				else nbnon++;
			}

			String mois = new SimpleDateFormat("ddMMyyyyHHMMss").format(new Date());
			HashMap<Object, Object> param = new HashMap<Object, Object>();
			param.put("ok", String.valueOf(nbok));
			param.put("nonok", String.valueOf(nbnonok));
			param.put("nbnon", String.valueOf(nbnon));
			String outFileName ="RapportSauvegarde"+mois+".pdf";
			repportManager.ExportReportPDF(outFileName, param, collections, "RapportSauvegarde.jasper");

			// envoi de Mail
			String heure = new SimpleDateFormat("dd/MM/yyyy HH:MM:ss").format(new Date());
			String subject = "Rapport de Sauvegarde des Fichiers Journaux et Images des Gabs "+heure;
			String messageCorps ="Bonjour ci joint le Rapport de Sauvegarde des logs des Gabs du "+heure+" Merci ..";
			GabParameter parameter = repportManager.findParameter("MAILS_FROM");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					from = parameter.getValue(); 
				}
			}

			// Enregistrement 
			dao.save(currentObject);

			// Envoi de Mail du Rapport de Sauvegarde
			List<String> mailsTo = new ArrayList<String>();
			parameter = repportManager.findParameter("MAILS_SAUV");
			if(parameter != null ){
				if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
					for(String mail : parameter.getValue().split(",")){
						mailsTo.add(mail);
					}
				}
			}
			SendFileEmail.SendMail(outFileName,repportManager.getReportsDir()+File.separator+"RapportSauvegarde"+mois+".pdf", from, mailsTo,null, subject, messageCorps);

		}catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}


	/**
	 * 
	 * @param pathFromProperties
	 * @param srcFile
	 * @return
	 */
	public Boolean CopyDirectory(File srcFile,String atmName,GroupeSauv sauv){

		try{

			File dir = new File(sauv.getChemin());

			// Formatage du Chemin
			SimpleDateFormat formaterYear = new SimpleDateFormat("yyyy");
			//SimpleDateFormat formaterDate = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat formaterMonth = new SimpleDateFormat("MMMMM");
			Date aujourdhui = new Date();
			String annee = formaterYear.format(aujourdhui);
			String mois = formaterMonth.format(aujourdhui);
			String jour = "IMAGES";

			String chemin = SENT_DIR.concat(File.separator).concat(atmName).concat(File.separator).concat(annee).concat(File.separator).concat(mois).concat(File.separator).concat(jour); 
			//File destFile = new File(dir, srcFile.getName());
			//File parentDir = destFile.getParentFile();
			File sentDir = new File(dir, chemin);
			if(!sentDir.exists()) sentDir.mkdirs();
			FileUtils.copyDirectoryToDirectory(srcFile, sentDir);
			//CSftp.send(srcFile.getAbsolutePath(), sauv.getLogin(), sauv.getIp(), chemin, sauv.getPsw());

		} catch (Exception e1){
			e1.printStackTrace();	
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
		
	}


	/**
	 * 
	 * @param pathFromProperties
	 * @param srcFile
	 * @return
	 */
	public Boolean CopyFile(File srcFile,String atmName,GroupeSauv sauv){

		try{

			File dir = new File(sauv.getChemin());

			// Formatage du Chemin
			SimpleDateFormat formaterYear = new SimpleDateFormat("yyyy");
			//SimpleDateFormat formaterDate = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat formaterMonth = new SimpleDateFormat("MMMMM");
			Date aujourdhui = new Date();
			String annee = formaterYear.format(aujourdhui);
			String mois = formaterMonth.format(aujourdhui);
			String jour = "JOURNAL";

			String chemin = SENT_DIR.concat(File.separator).concat(atmName).concat(File.separator).concat(annee).concat(File.separator).concat(mois).concat(File.separator).concat(jour); 
			//File destFile = new File(dir, srcFile.getName());
			//File parentDir = destFile.getParentFile();
			File sentDir = new File(dir, chemin);
			if(!sentDir.exists()) sentDir.mkdirs();
			FileUtils.copyFileToDirectory(srcFile, sentDir, true);
			//CSftp.send(srcFile.getAbsolutePath(), sauv.getLogin(), sauv.getIp(), chemin, sauv.getPsw());

		} catch (Exception e1){
			e1.printStackTrace();	
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

}
