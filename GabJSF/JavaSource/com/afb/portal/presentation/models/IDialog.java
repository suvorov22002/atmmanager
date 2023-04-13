/**
 * 
 */
package com.afb.portal.presentation.models;

/**
 * Interface de definition des boites de dialogue
 * @author Francis K
 * @version 1.0
 */
public interface IDialog  extends IViewComponent {
	
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
	 * Methode permettant de fermer la boite anormalement
	 */
	public void cancel();
	
	/**
	 * Methode permettant de savoir si la boite est ouverte en mode consultation
	 * @return	Etat d'ouverture de la boite en mode consultation
	 */
	public boolean isConsultation();
	
	/**
	 * Methode permettant de savoir si la boite est ouverte en mode Mise a jour
	 * @return	Etat d'ouverture de la boite en mode mise a jour
	 */
	public boolean isUpdate();
	
	/**
	 * Methode permettant de savoir si la boite est ouverte en mode Creation
	 * @return	Etat d'ouverture de la boite en mode Creation
	 */
	public boolean isCreate();
	
	/**
	 * Methode d'obtention de l'Objet Courant de la boite
	 * @param <T>	Parametre de Type
	 * @return	Objet courant de la boite
	 */
	public <T> T getCurrentObject();
	
	/**
	 * Methode de verification de l'Objet Courant
	 * @return	Etat de Verification
	 */
	public boolean checkBuildedCurrentObject();

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
}
