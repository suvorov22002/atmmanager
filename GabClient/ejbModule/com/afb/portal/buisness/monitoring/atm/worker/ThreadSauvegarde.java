package com.afb.portal.buisness.monitoring.atm.worker;

import java.io.File;

import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.parameter.GroupeSauv;

/**
 * 
 * @author Owner
 *
 */
public class ThreadSauvegarde extends Thread{

	private GroupeSauv sauv;

	private Atm atm;

	private static CopyImageLog  copyImageLog;

	private static CopyFileLog copyFileLog;

	private Boolean archiveJournal = Boolean.FALSE;
	private Boolean archiveImage = Boolean.FALSE;

	private String repImage;
	private String journal;
	
	private int lost = 0;

	public ThreadSauvegarde(String name,GroupeSauv sauv, Atm atm, String repImage, String journal) {
		super();
		setName(name);
		this.sauv = sauv;
		this.atm = atm;
		this.repImage = repImage;
		this.journal = journal;
	}


	/**
	 * @return the sauv
	 */
	public GroupeSauv getSauv() {
		return sauv;
	}


	/**
	 * @param sauv the sauv to set
	 */
	public void setSauv(GroupeSauv sauv) {
		this.sauv = sauv;
	}


	/**
	 * @return the atm
	 */
	public Atm getAtm() {
		return atm;
	}


	/**
	 * @param atm the atm to set
	 */
	public void setAtm(Atm atm) {
		this.atm = atm;
	}


	/**
	 * @return the archiveJournal
	 */
	public Boolean getArchiveJournal() {
		return archiveJournal;
	}


	/**
	 * @param archiveJournal the archiveJournal to set
	 */
	public void setArchiveJournal(Boolean archiveJournal) {
		this.archiveJournal = archiveJournal;
	}


	/**
	 * @return the archiveImage
	 */
	public Boolean getArchiveImage() {
		return archiveImage;
	}


	/**
	 * @param archiveImage the archiveImage to set
	 */
	public void setArchiveImage(Boolean archiveImage) {
		this.archiveImage = archiveImage;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		process();
	}


	public void process(){
		archiveJournal = Boolean.FALSE;
		archiveImage = Boolean.FALSE;
		lost = 0;
		processCopyFile();
		processCopyImage();
	}

	/**
	 * 
	 */
	public void processCopyFile(){

		try{
			// Autorisation sur le Gab 	
			File srcFile = new File("\\\\"+atm.getIp()+"\\c$\\JOURNAL\\"+journal);
			if(srcFile.isFile()){
				if(srcFile.length() > 0){
					System.out.println("---Copy File Log---"+atm.getNom());
					copyFileLog = new CopyFileLog(srcFile, atm.getNom(), sauv,atm);
					archiveJournal = copyFileLog.CopyFile();
					archiveJournal = Boolean.TRUE;
				} else archiveJournal = Boolean.TRUE;
			} else archiveJournal = Boolean.TRUE;
		}catch (Exception e){
			e.printStackTrace();
			archiveJournal = Boolean.FALSE;
		}	

	}

	/**
	 *  processCopyImage
	 */
	public void processCopyImage(){

		try{
			// Archivage  Images
			File srcFile = new File("\\\\"+atm.getIp()+"\\c$\\VIDEOARCHIV\\"+repImage);
			if(srcFile.isDirectory()){
				if(srcFile.listFiles().length > 0){
					System.out.println("---Copy Image Log---"+atm.getNom());
					copyImageLog = new CopyImageLog(srcFile, atm.getNom(), sauv,atm);
					archiveImage = copyImageLog.CopyDirectory();
					archiveImage = Boolean.TRUE;
				} else archiveImage = Boolean.TRUE;
			} else archiveImage = Boolean.TRUE;
		}catch (Exception e){
			lost++;
			e.printStackTrace();
			archiveImage = Boolean.FALSE;
		}

	}

}
