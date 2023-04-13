package com.afb.portal.presentation.monitor.parameter;

import com.afb.portal.jpa.gab.parameter.TypeEquipement;
import com.afb.portal.presentation.models.AbstractDialog;
import com.afb.portal.presentation.models.DialogFormMode;
import com.afb.portal.presentation.models.IViewComponent;
import com.afb.portal.presentation.tools.ViewHelper;

/**
 * TypeEquipementDialog
 * @author Owner
 *
 */
public class TypeEquipementDialog extends AbstractDialog{

	/**
	 * Objet courant de la fenetre
	 */
	private TypeEquipement currentObject = null;
	
	/**
	 * Constructeur par defaut
	 */
	public TypeEquipementDialog() {
		
		// Appel Parent
		super();
	}
		
	/**
	 * Constructeur du Modele
	 * @param mode	Mode d'ouverture de la Boite
	 * @param currentObject	Objet Courant de la Boite
	 * @param parent	Composant Parent
	 */
	public TypeEquipementDialog(DialogFormMode mode, TypeEquipement currentObject, IViewComponent parent) {
		
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

		// On retourne true
		return true;
	}

	/* (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IDialog#getFileDefinition()
	 */
	@Override
	public String getFileDefinition() {
		
		// On retourne le chemin du fichier de definition
		return "/views/parameter/TypeEquipementDialog.xhtml";
		
	}

	/* (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IDialog#getTitle()
	 */
	@Override
	public String getTitle() {
		
		// On retourne le Titre
		return "TypeEquipementDialog.title";
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
	 * @param currentObject the currentObject to set
	 */
	public void setCurrentObject(TypeEquipement currentObject) {
		this.currentObject = currentObject;
	}

	/**
	 * 
	 */
	@Override
	public void initComponents() {
		// TODO Auto-generated method stub
		// currentObject = new TypeEquipement();
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
	public TypeEquipement getCurrentObject() {
		// TODO Auto-generated method stub
		return this.currentObject;
	}

	
}
