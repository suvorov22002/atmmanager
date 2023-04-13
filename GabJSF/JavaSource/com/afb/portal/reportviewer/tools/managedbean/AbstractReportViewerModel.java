/**
 * 
 */
package com.afb.portal.reportviewer.tools.managedbean;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe representant le modele du Slide d'affichage des etats
 * @author Francis K
 * @version 1.0
 */
public abstract class AbstractReportViewerModel {
	
	/**
	 * Chemin vers le fichier de report
	 */
	protected String reportFileName = "";
	
	/**
	 * Source de données de l'Etat
	 */
	protected Collection<?> principalDataSource = null;
	
	/**
	 * Classe de l'Objet cible de l'Etat
	 */
	protected Class<?> principalDataSourceTargetClass = null;
	
	/**
	 * Map des Parametres de l'Etat
	 */
	protected Map<Object, Object> parameters = null;
	
	/**
	 * Constructeur de graphique
	 */
	protected AbstractChartBuilder chartBuilder = null;
	
	/**
	 * Nom du Parametre portant l'Image
	 */
	protected String graphicParameterName = "";
	
	/**
	 * Nom du parametre spécifiant si on affiche l'en-tete
	 */
	protected String printHeaderParameterName = "";
	
	/**
	 * Nom du parametre spécifiant si on affiche le pied
	 */
	protected String printFooterParameterName = "";

	/**
	 * Nom du parametre spécifiant si on affiche le Texte
	 */
	protected String printTextParameterName = "";
	
	/**
	 *  Nom du Parametre determinant l'affichage du titre
	 */
	protected String printTitleParameterName = "";
	
	
	/**
	 * Etat d'affichage du Graphique
	 */
	protected Boolean printGraphic = true;
	
	/**
	 * Etat d'affichage du Texte
	 */
	protected Boolean printDetail = true;

	/**
	 * Etat d'affichage du Titre
	 */
	protected Boolean printFlyLeaf = true;
	
	/**
	 * Etat d'affichage du Header
	 */
	protected Boolean printHeader = true;

	/**
	 * Etat d'affichage du Graphique
	 */
	protected Boolean printFooter = true;

	/**
	 * Repertoire de base des Etats
	 */
	protected String reportRootDiretory = "";
	
	/**
	 * Constructeur par defaut
	 */
	public AbstractReportViewerModel() {}
	
	/**
	 * Constructeur avec initialisation des Parametres
	 * @param reportFileName	Chemin vers le fichier de report
	 * @param principalDataSource	Source de données de l'Etat
	 * @param parameters	Map des Parametres de l'Etat
	 * @param graphicParameterName	Nom du Parametre portant l'Image
	 * @param printHeaderParameterName	Nom du parametre spécifiant si on affiche l'en-tete
	 * @param printFooterParameterName	Nom du parametre spécifiant si on affiche le pied
	 */
	public AbstractReportViewerModel(String reportFileName, Collection<?> principalDataSource, Map<Object, Object> parameters, String graphicParameterName, String printHeaderParameterName, String printFooterParameterName) {
		this.reportFileName = reportFileName;
		this.principalDataSource = principalDataSource;
		this.parameters = parameters;
		this.graphicParameterName = graphicParameterName;
		this.printHeaderParameterName = printHeaderParameterName;
		this.printFooterParameterName = printFooterParameterName;
	}
	
	/**
	 * Initialisation des Parametres
	 */
	protected void initComponents() {
		
		// Si le Nom du Parametre est null
		if(graphicParameterName == null) graphicParameterName = "";
		
		// Si la Map de Parametre est nulle
		if(parameters == null) parameters = new HashMap<Object, Object>();
		
		// Si le Constructeur d'Image est renseigne
		if(this.chartBuilder != null) {
			
			// On positionne la source de données
			this.chartBuilder.setPrincipalDataSource(principalDataSource);
			
			// Si l'Image existe
			if(this.graphicParameterName != null && this.graphicParameterName.trim().length() > 0) {

				// Image
				Image img = this.chartBuilder.buidChart();
				
				// Si l'Image est non nulle
				if(img != null) {
					
					// On Génère l'Image
					this.parameters.put(graphicParameterName, img);
					
					// On initialise l'affichage
					printGraphic = true;
					
				} else {

					// On initialise l'affichage
					printGraphic = false;
					
					// On supprime l'entree de l'Image
					parameters.remove(graphicParameterName.trim());
					
					// On annule le nom du Parametre Image
					graphicParameterName = "";
				}
			}
		}
	}
	
	/**
	 * Methode d'obtention du Chemin vers le fichier de report
	 * @return Chemin vers le fichier de report
	 */
	public String getReportFileName() {
		return this.reportRootDiretory + reportFileName;
	}
	
	/**
	 * Methode de mise a jour du Chemin vers le fichier de report
	 * @param reportFileName Chemin vers le fichier de report
	 */
	public void setReportFileName(String reportFileName) {
		this.reportFileName = reportFileName;
	}
	
	/**
	 * Methode d'obtention de la Source de données de l'Etat
	 * @return Source de données de l'Etat
	 */
	public Collection<?> getPrincipalDataSource() {
		return principalDataSource;
	}
	
	/**
	 * Methode de mise a jour de la Source de données de l'Etat
	 * @param principalDataSource Source de données de l'Etat
	 */
	public void setPrincipalDataSource(Collection<?> principalDataSource) {
		this.principalDataSource = principalDataSource;
	}
	
	/**
	 * Methode de mise a jour des Map des Parametres de l'Etat
	 * @param parameters Map des Parametres de l'Etat
	 */
	public void setParameters(Map<Object, Object> parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Methode d'obtention du Nom du Parametre portant l'Image
	 * @return Nom du Parametre portant l'Image
	 */
	public String getGraphicParameterName() {
		return graphicParameterName;
	}

	/**
	 * Methode de mise a jour du Nom du Parametre portant l'Image
	 * @param graphicParameterName Nom du Parametre portant l'Image
	 */
	public void setGraphicParameterName(String graphicParameterName) {
		this.graphicParameterName = graphicParameterName;
	}

	/**
	 * Methode d'obtention du Nom du parametre spécifiant si on affiche l'en-tete
	 * @return Nom du parametre spécifiant si on affiche l'en-tete
	 */
	public String getPrintHeaderParameterName() {
		return printHeaderParameterName;
	}

	/**
	 * Methode de mise a jour du Nom du parametre spécifiant si on affiche l'en-tete
	 * @param printHeaderParameterName Nom du parametre spécifiant si on affiche l'en-tete
	 */
	public void setPrintHeaderParameterName(String printHeaderParameterName) {
		this.printHeaderParameterName = printHeaderParameterName;
	}

	/**
	 * Methode d'obtention du Nom du parametre spécifiant si on affiche le pied
	 * @return Nom du parametre spécifiant si on affiche le pied
	 */
	public String getPrintFooterParameterName() {
		return printFooterParameterName;
	}
	
	/**
	 * Methode de mise a jour du Nom du parametre spécifiant si on affiche le pied
	 * @param printFooterParameterName Nom du parametre spécifiant si on affiche le pied
	 */
	public void setPrintFooterParameterName(String printFooterParameterName) {
		this.printFooterParameterName = printFooterParameterName;
	}

	/**
	 * Methode d'obtention du Nom du Parametre determinant l'affichage du titre
	 * @return Nom du Parametre determinant l'affichage du titre
	 */
	public String getPrintTitleParameterName() {
		return printTitleParameterName;
	}

	/**
	 * Methode de mise a jour du Nom du Parametre determinant l'affichage du titre
	 * @param printTitleParameterName Nom du Parametre determinant l'affichage du titre
	 */
	public void setPrintTitleParameterName(String printTitleParameterName) {
		this.printTitleParameterName = printTitleParameterName;
	}

	/**
	 * Methode d'obtention de l'Etat d'affichage du Graphique
	 * @return Etat d'affichage du Graphique
	 */
	public Boolean getPrintGraphic() {
		return printGraphic;
	}

	/**
	 * Methode de mise a jour de l'Etat d'affichage du Graphique
	 * @param printGraphic Etat d'affichage du Graphique
	 */
	public void setPrintGraphic(Boolean printGraphic) {
		this.printGraphic = printGraphic;
	}

	/**
	 * Methode d'obtention du Nom du parametre spécifiant si on affiche le Texte
	 * @return Nom du parametre spécifiant si on affiche le Texte
	 */
	public String getPrintTextParameterName() {
		return printTextParameterName;
	}

	/**
	 * Methode de mise a jour du Nom du parametre spécifiant si on affiche le Texte
	 * @param printTextParameterName Nom du parametre spécifiant si on affiche le Texte
	 */
	public void setPrintTextParameterName(String printTextParameterName) {
		this.printTextParameterName = printTextParameterName;
	}
	
	/**
	 * Methode d'obtention de l'Etat d'affichage du Texte
	 * @return Etat d'affichage du Texte
	 */
	public Boolean getPrintDetail() {
		return printDetail;
	}

	/**
	 * Methode de mise a jour de l'Etat d'affichage du Texte
	 * @param printText Etat d'affichage du Texte
	 */
	public void setPrintDetail(Boolean printText) {
		this.printDetail = printText;
	}

	/**
	 * Methode d'obtention de l'Etat d'affichage du Titre
	 * @return Etat d'affichage du Titre
	 */
	public Boolean getPrintFlyLeaf() {
		return printFlyLeaf;
	}

	/**
	 * Methode de mise a jour de l'Etat d'affichage du Titre
	 * @param printTitle Etat d'affichage du Titre
	 */
	public void setPrintFlyLeaf(Boolean printTitle) {
		this.printFlyLeaf = printTitle;
	}

	/**
	 * Methode d'obtention de l'Etat d'affichage du Header
	 * @return Etat d'affichage du Header
	 */
	public Boolean getPrintHeader() {
		return printHeader;
	}
	
	/**
	 * Methode de mise a jour de l'Etat d'affichage du Header
	 * @param printHeader Etat d'affichage du Header
	 */
	public void setPrintHeader(Boolean printHeader) {
		this.printHeader = printHeader;
	}

	/**
	 * Methode d'obtention de l'Etat d'affichage du Graphique
	 * @return Etat d'affichage du Graphique
	 */
	public Boolean getPrintFooter() {
		return printFooter;
	}

	/**
	 * Methode de mise a jour de l'Etat d'affichage du Graphique
	 * @param printFooter Etat d'affichage du Graphique
	 */
	public void setPrintFooter(Boolean printFooter) {
		this.printFooter = printFooter;
	}
	
	/**
	 * Methode d'obtention du Constructeur de graphique
	 * @return Constructeur de graphique
	 */
	public AbstractChartBuilder getChartBuilder() {
		return chartBuilder;
	}
	
	/**
	 * Methode de mise a jour du Constructeur de graphique
	 * @param chartBuilder Constructeur de graphique
	 */
	public void setChartBuilder(AbstractChartBuilder chartBuilder) {
		this.chartBuilder = chartBuilder;
	}
	
	/**
	 * Methode permettant de savoir si le Panneau de gestion d'affichage du Graphique est activé
	 * @return	Etat d'affichage du Panneau de gestion d'affichage du Graphique est activé
	 */
	public boolean isGraphicPanelVisible() {
		
		// Si le Nom du Parametre portant l'image est vide
		if(graphicParameterName == null || graphicParameterName.trim().length() == 0) return false;
		
		// On retourne true
		return true;
	}
	
	/**
	 * Methode de construction d'une collection de Parametre d'Etat
	 * @return	collection de Parametre d'Etat
	 */
	public Collection<ReportParameter> getReportParameters() {
		
		// Liste de Parametres
		List<ReportParameter> rParameters = new ArrayList<ReportParameter>();
		
		// Si la Map des Parametres est vide
		if(parameters == null || parameters.size() == 0) return rParameters;
		
		// Parcours
		for (Object key : parameters.keySet()) {
			
			// Ajout
			rParameters.add(new ReportParameter(key.toString(), parameters.get(key)));
		}
		
		// On retourne la Liste
		return rParameters;
	}
	
	/**
	 * Retourne la liste des parametres de l'etat
	 * @return Liste des parametres
	 */
	public Map<Object, Object> getParameters() {
		return parameters;
	}

	/**
	 * Metode permettant de recharger l'Etat
	 */
	public abstract void reloadReport();
}
