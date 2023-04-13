/**
 * 
 */
package com.afb.portal.presentation.models;

/**
 * Interface representant la racine des composants des modeles de presentantion
 * @author Francis K
 * @version 1.0
 */
public interface IViewComponent {
	
	/**
	 * Methode d'ouverture du Composant
	 */
	public void open();
	
	/**
	 * Methode de fermeture du Composant
	 */
	public void close();
	
	/**
	 * Methode permettant de valider la fermeture d'une boite de dialogue fille du Composant
	 * @param child	Boite de dialogue fille du Composant
	 * @param mode	Mode d'ouverture de la boite fille
	 */
	public void validateSubDialogClosure(IDialog child, DialogFormMode mode, boolean wellClose);
	
	/**
	 * Methode permettant d'obtenir le chemin vers le fichier de definition de la boite de dialogue fille
	 * @return	Chemin vers le fichier de definition de la boite de dialogue fille
	 */
	public String getChildDialogDefinition();
	
	/**
	 * Methode de validation de la boite de dialogue de confirmation
	 */
	public void validateConfirmationDialog();
	
	/**
	 * Methode d'action lors de la fermeture de la boite de dialogue d'information
	 */
	public void actionOnInformationDialogClose();
	
	/**
	 * Obtention de l'ID de la zone a rerender
	 * @return ID de la zone a rerender
	 */
	public String getViewAreaToRerender();
	
	/**
	 * Methode d'obtention du Script JS a executer a la fermeture de la boite d'information
	 * @return	Script
	 */
	public String getJSScriptOnComplete();
	
}
