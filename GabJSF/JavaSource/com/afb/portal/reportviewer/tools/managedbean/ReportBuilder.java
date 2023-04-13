package com.afb.portal.reportviewer.tools.managedbean;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashMap;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Classe permettant de construire les etats
 * @author Francis K
 * @version 1.0
 */
public class ReportBuilder {

	
	/**
	 * Methode de construction de l'etat
	 * @param reportName
	 * @param map
	 * @param maCollection
	 * @return
	 * @throws Exception
	 */
    private static JasperPrint printReport(String reportName, HashMap<Object, Object> map, Collection<?> maCollection) throws Exception {
		
		// Construction de la source de donnees de l'etat
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(maCollection);
		
		// Construction de l'etat
		return JasperFillManager.fillReport(reportName, map, dataSource);

    }
	
	/**
	 * Methode d'exportation de l'etat en fichier html
	 * @param reportName URL du fichier jasper 
	 * @param map Parametres de l'etat
	 * @param maCollection Source de donnees de l'etat
	 * @param destFileName Fichier de destination
	 * @throws Exception
	 */
	public static void exportReportToHtmlFile(String reportName, HashMap<Object, Object> map, Collection<?> maCollection, String destFileName) throws Exception {
		
		// Construction du JasperPrint
		JasperPrint jp = printReport(reportName, map, maCollection);
		
		// Export de l'etat vers le fichier html specifie
		JasperExportManager.exportReportToHtmlFile(jp, destFileName);
	}
	
	/**
	 * Methode d'exportation de l'etat dans un flux d'octets de sortie pour fichiers html
	 * @param reportName URL du fichier jasper 
	 * @param map Parametres de l'etat
	 * @param maCollection Source de donnees de l'etat
	 * @param os Flux de sortie
	 * @throws Exception
	 */
	public static byte[] getReportHtmlBytes(String reportName, HashMap<Object, Object> map, Collection<?> maCollection) throws Exception {

		// Initialisation du tableau de byte a retourner
		byte[] data = null;
		
		// Export de l'etat dans un fichier html
		exportReportToHtmlFile(reportName, map, maCollection, "report.html");
		
		// Si le fichier n'existe pas on sort
		if(!new File("report.html").exists()) return data;
		
		// Obtention d'un InputStream du le fichier html genere
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("report.html")));
		
		// Initilisation d'un flux de sortie
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		// Initialisation d'un Byte
		int b;
		
		// Parcours du fichier et ecriture dans le flux de sortie
		while((b = bis.read()) != -1) baos.write(b);
		
		// On referme
		baos.flush();
		
		// Construction du tableau de byte a partir du flux de sortie
		data = baos.toByteArray();
		
		// Fermeture des flux
		bis.close();
		//baos.close();
		
		// Retourne le tableau de bytes
		return data;
	}

	/**
	 * Methode de generation d'un tableau d'octets de l'etat
	 * @param reportName URL du fichier jasper 
	 * @param map Parametres de l'etat
	 * @param maCollection Source de donnees de l'etat
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] getReportPDFBytes(String reportName, HashMap<Object, Object> map, Collection<?> maCollection) throws Exception {
		
		// Construction du JasperPrint
		JasperPrint jp = printReport(reportName, map, maCollection);
		
		// Construction du tableau de bytes
		return JasperExportManager.exportReportToPdf(jp);
	}
	
}
