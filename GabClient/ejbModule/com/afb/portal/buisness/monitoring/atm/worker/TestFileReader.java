package com.afb.portal.buisness.monitoring.atm.worker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.afb.portal.buisness.monitoring.worker.NetworkPing;

public class TestFileReader {

	public static void main(String[] args){
		
		boolean ping =  NetworkPing.isReachableByPing("172.21.120.11");
		
		System.out.println("---libelleAdmin---"+ping);
		
	}
	
	
	/**
	 * @param args
	 */
	public  void mainLance() {
		// TODO Auto-generated method stub
				
		try{
			
			File Chemin = null;
			String ip ="192.168.41.30";
			
			String script = "net use * \\\\"+ip.trim()+"\\c$ /USER:administrateur password /PERSISTENT:NO";
			Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", script});
			
			SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
			String journal = formater.format(new Date())+".jrn";
			String libelleAdmin = "";
			String repImage = formater.format(new Date());
			
			// Autorisation sur le Gab 	
			//AccessDirectory.procces(value.getIp(), value.getLogin(),value.getPsw());
			
			Chemin = new File("\\\\"+ip+"\\c$\\JOURNAL");
			System.out.println("---Chemin---"+Chemin.getAbsolutePath());
			if(Chemin.isDirectory()){
				Chemin = new File("\\\\"+ip+"\\c$\\JOURNAL\\"+journal);
				System.out.println("---Chemin1---"+Chemin.getAbsolutePath());
				if(!Chemin.exists()){
					libelleAdmin = libelleAdmin + "- le Fichier Journal n'a pas été crée"+"\n"; 								
				}else{
					if(Chemin.length() == 0){
						libelleAdmin = libelleAdmin + "- le Fichier Journal crée est Vide "+"\n";
					}else if(Chemin.length() > 0){
						libelleAdmin = libelleAdmin + "- Journal OK "+"\n"; 
					}
				}
			}else if(!Chemin.exists()){
				libelleAdmin = libelleAdmin + "- Le repertoire des Fichiers Journaux n'est pas accessible "+"\n";
			}
			
			System.out.println("---libelleAdmin---"+libelleAdmin);
			
			Chemin = new File("\\\\"+ip+"\\c$\\VIDEOARCHIV");
			System.out.println("---Chemin-V--"+Chemin.getAbsolutePath());
			if(Chemin.isDirectory()){
				Chemin = new File("\\\\"+ip+"\\c$\\VIDEOARCHIV\\"+repImage);
				System.out.println("---Chemin-V1--"+Chemin.getAbsolutePath());
				if(!Chemin.exists()){
					libelleAdmin = libelleAdmin + "- le Repertoire des Images n'a pas été crée "+"\n"; 
				}else{
					int tail = Chemin.listFiles().length;
					if(tail == 0){
						libelleAdmin = libelleAdmin + "- Le Repertoire des Images crée est Vide "+"\n"; 
					}else if(tail > 0){
						libelleAdmin = libelleAdmin + "- Images OK "+"\n"; 
					}
				}
			}else{
				libelleAdmin = libelleAdmin + "- Le repertoire des Images n'est pas accessible "+"\n";
			}
			
			
			System.out.println("---libelleAdmin---"+libelleAdmin);
			
			script = "NET USE * /DELETE";
			Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", script});
			
		}catch (NullPointerException e){
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}

	}

}
