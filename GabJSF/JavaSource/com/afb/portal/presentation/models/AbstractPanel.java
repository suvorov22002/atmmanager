/**
 * 
 */
package com.afb.portal.presentation.models;

import java.util.TimeZone;

import org.richfaces.model.selection.Selection;
import org.richfaces.model.selection.SimpleSelection;

import com.afb.portal.presentation.tools.ViewHelper;

/**
 * Classe abstraite de modele des panneaux
 * @author Francis K
 * @version 2.0
 */
public abstract class AbstractPanel implements IPanel {

	/**
	 * Valeur de la recherche par defaut
	 */
	protected String defaultSearchValue = "NO_SPECIFIED_SEARCH_VALUE";

	/**
	 * Modele de Lignes selectionnees
	 */
	protected Selection selection;

	/**
	 * Etat d'ouverture du panneau
	 */
	protected boolean open = false;

	/**
	 * Etat d'inclusion de la boite de dialogue d'erreur
	 */
	protected boolean error = false;

	/**
	 * Etat d'inclusion de la boite de dialogue d'information
	 */
	protected boolean information = false;

	/**
	 * Etat d'inclusion de la boite de dialogue d'impression
	 */
	protected boolean print = false;
	
	/**
	 * 
	 * @return
	 */
	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

	/* (non-Javadoc)
	 * @see com.afrikbrain.ebanking.lite.view.models.IPanel#close()
	 */
	@Override
	public void close() {

		// On positione l'etat d'ouverture a false
		this.open = false;

		// Liberation des ressources
		disposeResourceOnClose();
	}

	/* (non-Javadoc)
	 * @see com.afrikbrain.ebanking.lite.view.models.IPanel#isOpen()
	 */
	@Override
	public boolean isOpen() {

		// On retourne Open
		return open;
	}

	/* (non-Javadoc)
	 * @see com.afrikbrain.ebanking.lite.view.models.IPanel#open()
	 */
	@Override
	public void open() {

		// On positione l'etat d'ouverture a false
		this.open = true;

		// Initialisation des Composants
		initComponents();
	}

	/**
	 * Methode d'obtention du Modele de Lignes selectionnees
	 * @return Modele de Lignes selectionnees
	 */
	public Selection getSelection() {
		return selection;
	}

	/**
	 * Methode de mise a jour du modele de Lignes selectionnees
	 * @param selection Modele de Lignes selectionnees
	 */
	public void setSelection(Selection _selection) {
		selection = _selection;
	}

	@Override
	public String getIcon() {

		// Chemin de l'Image
		return ViewHelper.getSessionSkinURLStatic() + "/Images/CountryListPanelLogo.png";
	}

	/**
	 * Methode de liberation des ressource a la fermeture
	 */
	protected void disposeResourceOnClose() {

		// Si la selection n'est pas nulle
		if(selection != null) {

			// Si c'est une selection simple
			if(selection instanceof SimpleSelection) {

				// On vide
				((SimpleSelection)selection).clear();

			}

			// On annule la selection
			selection = null;
		}
	}

	/**
	 * Methode d'obtention de la Valeur de la recherche par defaut
	 * @return Valeur de la recherche par defaut
	 */
	public String getDefaultSearchValue() {
		return defaultSearchValue;
	}

	/**
	 * Methode de mise a jour de la Valeur de la recherche par defaut
	 * @param defaultSearchValue Valeur de la recherche par defaut
	 */
	public void setDefaultSearchValue(String defaultSearchValue) {
		this.defaultSearchValue = defaultSearchValue;
	}

	/**
	 * Methode d'initialisation des Composant de la fenetre
	 */
	protected void initComponents() {}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.IViewComponent#validateSubDialogClosure(com.afb.portal.presentation.models.IDialog, com.afb.portal.presentation.models.DialogFormMode, boolean)
	 */
	@Override
	public void validateSubDialogClosure(IDialog child, DialogFormMode mode, boolean wellClose) {}

	/**
	 * Methode d'obtention du nom de la queue de requete
	 * @return queue de requete
	 */
	public String getRequestQueue() {

		// On retourne le nom de la queue
		return "PanelRequestQueue";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IPanel#getErrorDialogDefinition()
	 */
	public String getErrorDialogDefinition() {

		// On annule
		error = false;
		
		// Le Chemin
		//String path = error ? "/views/home/ErrorDialog.xhtml" : "/views/home/EmptyPage.xhtml";
		
		String path = "/views/home/ErrorDialog.xhtml";

		// On annule
		error = false;

		// On retourne le chemin
		return path;

	}

	/*
	 * (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IViewComponent#getReportViewerDialogDefinition()
	 */
	public String getReportViewerDialogDefinition() {

		// Le Chemin
		String path = print ? "/views/home/ReportViewer.xhtml" : "/views/home/EmptyPage.xhtml";

		// On retourne le chemin
		return path;

	}

	/*
	 * (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IPanel#getInformationDialogDefinition()
	 */
	public String getInformationDialogDefinition() {

		// On annule
		information = false;
		
		// Le Chemin
		// String path = information ? "/views/home/InformationDialog.xhtml" : "/views/home/EmptyPage.xhtml";
		
		String path = "/views/home/InformationDialog.xhtml";

		// On annule
		information = false;

		// On retourne le chemin
		return path;

	}

	/**
	 * Methode d'obtention du chemin vers le fichier de definition de la boite de dialogue d'attente
	 * @return Boite de dialogue d'attente
	 */
	public String getWaitDialogDefinition() {

		// On retourne le chemin du fichier
		return isOpen() ? "/views/home/WaitDialog.xhtml" : "/views/home/EmptyPage.xhtml";
	}

	/**
	 * 
	 */
	public String getConfirmationDialogDefinition() {

		// Le Chemin
		// String path = information ? "/views/home/ConfirmationDialog.xhtml" : "/views/home/EmptyPage.xhtml";
		
		// On annule
		information = false;
		
		String path =  "/views/home/ConfirmationDialog.xhtml";

		// On annule
		information = false;

		// On retourne le chemin
		return path;

	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.IViewComponent#actionOnInformationDialogClose()
	 */
	@Override
	public void actionOnInformationDialogClose() {}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.IViewComponent#getViewAreaToRerender()
	 */
	@Override
	public String getViewAreaToRerender() {

		// La zone Cliente
		return ClientArea.staticGetID();
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.IViewComponent#getJSScriptOnComplete()
	 */
	@Override
	public String getJSScriptOnComplete() {

		// Un Script Vide
		return "";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.IViewComponent#validateConfirmationDialog()
	 */
	@Override
	public void validateConfirmationDialog(){}

}
