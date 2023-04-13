package com.afb.portal.buisness.monitoring.atm.worker;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.parameter.GroupeSauv;

/**
 * CopyImageLog
 * @author Owner
 * @version 1.0
 */
public class CopyImageLog implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SENT_DIR = "SAUVEGARDE";
	
	private File srcFile;
	
	private String atmName;
	
	private GroupeSauv sauv;
	
	private Atm atm;

	/**
	 * CopyImageLog
	 * @param srcFile
	 * @param atmName
	 * @param sauv
	 */
	public CopyImageLog(File srcFile,String atmName,GroupeSauv sauv,Atm atm){
		this.srcFile = srcFile;
		this.atmName = atmName;
		this.sauv = sauv;
		this.atm = atm;
	}
	
	
	/**
	 * 
	 * @param pathFromProperties
	 * @param srcFile
	 * @return
	 */
	public Boolean CopyDirectory(){

		try{

			File dir = new File(sauv.getChemin());

			// Formatage du Chemin
			SimpleDateFormat formaterYear = new SimpleDateFormat("yyyy");
			//SimpleDateFormat formaterDate = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat formaterMonth = new SimpleDateFormat("MMMMM");
			Date aujourdhui = DateUtils.addDays(new Date(), -1);
			String annee = formaterYear.format(aujourdhui);
			String mois = formaterMonth.format(aujourdhui);
			String jour = "IMAGES";

			String chemin = SENT_DIR.concat(File.separator).concat(atmName).concat(File.separator).concat(annee).concat(File.separator).concat(mois).concat(File.separator).concat(jour); 
			//File destFile = new File(dir, srcFile.getName());
			//File parentDir = destFile.getParentFile();
			File sentDir = new File(dir, chemin);
			//if(!sentDir.isDirectory()) sentDir.mkdirs();
			sentDir.mkdirs();
			FileUtils.copyDirectoryToDirectory(srcFile, sentDir);
			//CSftp.send(srcFile.getAbsolutePath(), sauv.getLogin(), sauv.getIp(), chemin, sauv.getPsw());

		} catch (Exception e1){
			e1.printStackTrace();
			//AccessDirectorieFileIMAGE(atm);
			//return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	

	public static boolean AccessDirectorieFileIMAGE(Atm value){

		try{

			String ip = value.getIp();
			String login = value.getLogin(); 
			String password = value.getPsw();
			String script = "net use * \\\\"+ip.trim()+"\\c$ /USER:administrateur password /PERSISTENT:NO";
			if(password != null && login != null && !login.isEmpty() && !password.isEmpty()){
				script = "net use * \\\\"+ip.trim()+"\\c$ /USER:"+login.trim()+" "+password.trim()+" /PERSISTENT:NO";
			}

			Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", script});
			File Chemin = new File("\\\\"+value.getIp()+"\\c$\\JOURNAL");
			if(Chemin.exists()){
				return true;
			}else{
				Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", script});
				Chemin = new File("\\\\"+value.getIp()+"\\c$\\JOURNAL");
				if(Chemin.exists()){
					return true;
				}else{
					Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", script});
					Chemin = new File("\\\\"+value.getIp()+"\\c$\\JOURNAL");
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
	
	
}
