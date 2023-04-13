package com.afb.portal.presentation.monitor.parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import com.afb.portal.jpa.gab.parameter.Equipement;
import com.afb.portal.jpa.gab.parameter.TypeEquipement;
import com.afb.portal.presentation.models.AbstractDialog;
import com.afb.portal.presentation.models.DialogFormMode;
import com.afb.portal.presentation.models.IViewComponent;
import com.afb.portal.presentation.tools.ExceptionHelper;
import com.afb.portal.presentation.tools.ViewHelper;

/**
 * Classe Equipement
 * @author Owner
 * @version 1.0
 */
public class EquipementDialog extends AbstractDialog{

	/**
	 * Objet courant de la fenetre
	 */
	private Equipement currentObject = null;
	
	/**
	 * TypeEquipement
	 */
	private String typeEquipement;
		
	/**
	 * Items
	 */
	private List<SelectItem> items = new ArrayList<SelectItem>(); 
	
	
	private Map<String, TypeEquipement> mapType = new HashMap<String, TypeEquipement>();
	
		
	/**
	 * Constructeur du Modele
	 * @param mode	Mode d'ouverture de la Boite
	 * @param currentObject	Objet Courant de la Boite
	 * @param parent Equipement
	 */
	public EquipementDialog(DialogFormMode mode, Equipement currentObject, IViewComponent parent){
		
		// Appel Parent
		super();
		
		// Initialisation des Parametres
		this.mode = mode;
		this.currentObject = currentObject;
		this.parent = parent;
		
		// Initialisation des Composants
		initComponents();
	}
	
	/**
	 * @return the items
	 */
	public List<SelectItem> getItems() {
		return items;
	}



	/**
	 * @param items the items to set
	 */
	public void setItems(List<SelectItem> items) {
		this.items = items;
	}



	/**
	 * @param currentObject the currentObject to set
	 */
	public void setCurrentObject(Equipement currentObject) {
		this.currentObject = currentObject;
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
		return "/views/parameter/EquipementDialog.xhtml";
		
	}

	/* (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IDialog#getTitle()
	 */
	@Override
	public String getTitle() {
		
		// On retourne le Titre
		return "EquipementDialog.title";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.AbstractDialog#validate()
	 */
	@Override
	protected void validate() {
		
		this.currentObject.setTypeEquipement(mapType.get(typeEquipement));
		
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
		items.clear();
		mapType.clear();
	}

	/**
	 * 
	 */
	@Override
	public void initComponents() {
		// TODO Auto-generated method stub
		// currentObject = new Equipement();
		
		try{

			//  Chargement Type de Carte
			items = new ArrayList<SelectItem>();
			for(TypeEquipement b : ViewHelper.dao.filter(TypeEquipement.class,null,null,null,null,0,-1)){
				items.add(new SelectItem(b.getId(), b.getLibelle()));
				mapType.put(b.getId().toString(), b);
			}
			
			if(DialogFormMode.UPDATE.equals(this.mode)){
				currentObject = ViewHelper.dao.findByPrimaryKey(Equipement.class,currentObject.getId(),null);
				typeEquipement =  currentObject.getTypeEquipement().getId().toString();
			}
				
			
		}catch(Exception e){

			e.printStackTrace();
			// Etat d'erreur
			this.error = true;
			// Traitement de l'exception
			ExceptionHelper.threatException(e);

		}
		
	}

	
	/**
	 * @return the typeEquipement
	 */
	public String getTypeEquipement() {
		return typeEquipement;
	}

	/**
	 * @param typeEquipement the typeEquipement to set
	 */
	public void setTypeEquipement(String typeEquipement) {
		this.typeEquipement = typeEquipement;
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
	public Equipement getCurrentObject() {
		// TODO Auto-generated method stub
		return this.currentObject;
	}

	
}
