/**
 * 
 */
package com.afb.portal.presentation.models;

/**
 * Panneau par defaut
 * @author Francis K
 * @version 2.0
 */
public class DefaultPanel extends AbstractPanel {

	/* (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IPanel#getFileDefinition()
	 */
	@Override
	public String getFileDefinition() {
		
		// On retourne le chemin vers le fichier
		return "/views/home/clientarea/DefaultPanel.xhtml";
	}

	/* (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IPanel#getName()
	 */
	@Override
	public String getName() {
		
		// Nom Du panneau
		return "DefaultPanel";
	}

	/* (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IPanel#getTitle()
	 */
	@Override
	public String getTitle() {
		
		// On retourne le Titre
		return "defaultpanel.title";
	}

	@Override
	public String getChildDialogDefinition() {
		
		// On retourne le panneau par defaut
		return "/views/home/EmptyPage.xhtml";
	}

	@Override
	public void actionOnInformationDialogClose() {}
	
	@Override
	public String getViewAreaToRerender() {
		
		// La zone Cliente
		return ClientArea.staticGetID();
	}
	
	@Override
	public String getJSScriptOnComplete() {
		
		// Un Script Vide
		return "javascript:";
	}

}
