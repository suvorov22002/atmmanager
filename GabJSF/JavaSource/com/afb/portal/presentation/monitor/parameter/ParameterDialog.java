package com.afb.portal.presentation.monitor.parameter;


import com.afb.portal.jpa.gab.parameter.GabParameter;
import com.afb.portal.presentation.models.AbstractDialog;
import com.afb.portal.presentation.models.DialogFormMode;
import com.afb.portal.presentation.models.IViewComponent;
import com.afb.portal.presentation.tools.ExceptionHelper;
import com.afb.portal.presentation.tools.ViewHelper;

/**
 * Boite de Dialogue de Creation dun parametre
 * @author Francis
 * @version 1.0
 */
public class ParameterDialog extends AbstractDialog{
	
	/**
	 * Objet courant de la fenetre
	 */
	private GabParameter currentObject = null;
	
	/**
	 * Constructeur par defaut
	 */
	public ParameterDialog() {
		
		// Appel Parent
		super();
	}
		
	/**
	 * Constructeur du Modele
	 * @param mode	Mode d'ouverture de la Boite
	 * @param currentObject	Objet Courant de la Boite
	 * @param parent	Composant Parent
	 */
	public ParameterDialog(DialogFormMode mode, GabParameter currentObject, IViewComponent parent) {
		
		// Appel Parent
		super();
		
		// Initialisation des Parametres
		this.mode = mode;
		this.currentObject = currentObject;
		this.parent = parent;
		
		// Initialisation des Composants
		initComponents();
	}
	
	
	/* (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IDialog#checkBuildedCurrentObject()
	 */
	@Override
	public boolean checkBuildedCurrentObject() {

		// Si le code n'est pas renseigné
		if(currentObject.getCode() == null || currentObject.getCode().trim().length() == 0) {
			
			// Affichage de la Boite de dialogue d'information
			ExceptionHelper.showInformation("Parameter.code.empty");
			
			// On retourne false
			return false;
		}
		
		// Si la designation n'est pas renseigné
		if(currentObject.getValue() == null || currentObject.getValue().trim().length() == 0) {
			
			// Affichage de la Boite de dialogue d'information
			ExceptionHelper.showInformation("Parameter.Parameter.empty");
			
			// On retourne false
			return false;
		}
		
		// Si la designation n'est pas renseigné
		if(currentObject.getDescrption() == null || currentObject.getDescrption().trim().length() == 0) {
			
			// Affichage de la Boite de dialogue d'information
			ExceptionHelper.showInformation("Parameter.Descrption.empty");
			
			// On retourne false
			return false;
		}
		
		// On retourne true
		return true;
	}

	/* (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IDialog#getFileDefinition()
	 */
	@Override
	public String getFileDefinition() {
		
		// On retourne le chemin du fichier de definition
		return "/views/parameter/ParameterDialog.xhtml";
		
	}

	/* (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IDialog#getTitle()
	 */
	@Override
	public String getTitle() {
		
		// On retourne le Titre
		return "ParameterDialog.title";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.AbstractDialog#validate()
	 */
	@Override
	protected void validate() {
		
		// Si on est en mode Enregistrement
		if(mode.equals(DialogFormMode.CREATE)) {
			
			// Enregistrement
			this.currentObject = ViewHelper.dao.save(currentObject);
			
		} else if(mode.equals(DialogFormMode.UPDATE)) {

			// Enregistrement
			this.currentObject = ViewHelper.dao.update(currentObject);
			
		}
	}
		
	@Override
	protected void buildCurrentObject() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void disposeResourceOnClose() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	@Override
	public void initComponents() {
		// TODO Auto-generated method stub
		// currentObject = new GabParameter();
	}

	
	@Override
	public String getChildDialogDefinition() {
		// TODO Auto-generated method stub
		return "";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.IDialog#getCurrentObject()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public GabParameter getCurrentObject() {
		// TODO Auto-generated method stub
		return this.currentObject;
	}

}
