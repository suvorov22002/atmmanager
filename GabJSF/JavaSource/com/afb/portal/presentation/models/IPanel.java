/**
 * 
 */
package com.afb.portal.presentation.models;



/**
 * Interface des panneaux d'affichage de la zone client
 * @author Francis K
 * @version 1.0
 */
public interface IPanel extends IViewComponent {
	
	/**
	 * Methode d'obtention du nom du Panneau
	 * @return	Nom du Panneau
	 */
	public String getName();

	/**
	 * Methode permettant d'obtenir le Titre de la boite
	 * @return	Titre de la Boite
	 */
	public String getTitle();
	
	/**
	 * Methode permettant d'obtenir l'icone de la boite
	 * @return	Icone de la boite
	 */
	public String getIcon();
	
	/**
	 * Chemins vers le fichier contenant la definition de la Boite
	 */
	public String getFileDefinition();
		
	/**
	 * Methode permettant de verifier si la boite est ouverte
	 * @return	Etat d'ouverture de la boite
	 */
	public boolean isOpen();
	
	/**
	 * Methode permettant d'obtenir le chemin de definition de la boite de dialogue d'erreur
	 * @return	Chemin de definition de la boite de dialogue d'erreur
	 */
	public String getErrorDialogDefinition();
	
	/**
	 * Methode permettant d'obtenir le chemin de definition de la boite de dialogue d'information
	 * @return	Chemin de definition de la boite de dialogue d'information
	 */
	public String getInformationDialogDefinition();
	
	/**
	 * boite de dialogue de confirmation
	 * @return
	 */
	public String getConfirmationDialogDefinition();
	
	/**
	 * 	
	 * @return
	public ControlReportViewer getReportViewer();
	 */
}
