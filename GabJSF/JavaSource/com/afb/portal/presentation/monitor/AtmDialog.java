package com.afb.portal.presentation.monitor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import afb.dsi.dpd.portal.jpa.entities.Branch;

import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.equipment.AtmStatus;
import com.afb.portal.jpa.gab.parameter.GroupeSauv;
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
public class AtmDialog extends AbstractDialog{

	/**
	 * Objet courant de la fenetre
	 */
	private Atm currentObject = null;

	/**
	 * Items
	 */
	private List<SelectItem> items = new ArrayList<SelectItem>(); 

	/**
	 * Items
	 */
	private List<SelectItem> itemsBranch = new ArrayList<SelectItem>(); 

	/**
	 * Items
	 */
	private List<SelectItem> itemsSauv = new ArrayList<SelectItem>(); 


	private Map<String,Branch> mapAgence = new HashMap<String, Branch>();


	private Map<String,GroupeSauv> mapgrpSauv = new HashMap<String, GroupeSauv>();

	private String txtBranch ;

	private String txtGroupe;


	/**
	 * Constructeur du Modele
	 * @param mode	Mode d'ouverture de la Boite
	 * @param currentObject	Objet Courant de la Boite
	 * @param parent	Composant Parent
	 */
	public AtmDialog(DialogFormMode mode, Atm currentObject, IViewComponent parent) {

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
	 * @return the txtBranch
	 */
	public String getTxtBranch() {
		return txtBranch;
	}



	/**
	 * @param txtBranch the txtBranch to set
	 */
	public void setTxtBranch(String txtBranch) {
		this.txtBranch = txtBranch;
	}



	/**
	 * @return the txtGroupe
	 */
	public String getTxtGroupe() {
		return txtGroupe;
	}



	/**
	 * @param txtGroupe the txtGroupe to set
	 */
	public void setTxtGroupe(String txtGroupe) {
		this.txtGroupe = txtGroupe;
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
		return "/views/parameter/AtmDialog.xhtml";

	}

	/* (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IDialog#getTitle()
	 */
	@Override
	public String getTitle() {

		// On retourne le Titre
		return "AtmDialog.title";
	}


	@Override
	protected void validate(){

		this.currentObject.setGroupeSauv(mapgrpSauv.get(txtGroupe));
		this.currentObject.setAgence(mapAgence.get(txtBranch));
		this.currentObject.setTypeAtm(AtmStatus.ATM);

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
		itemsBranch.clear();
		itemsSauv.clear();
		mapAgence.clear();
		mapgrpSauv.clear();
	}

	/**
	 * 
	 */
	@Override
	public void initComponents() {
		// TODO Auto-generated method stub

		try{

			mapAgence = new HashMap<String, Branch>();
			mapgrpSauv = new HashMap<String, GroupeSauv>();

			//  Chargement Type de Carte
			items = new ArrayList<SelectItem>();
			/** for(AtmStatus b : AtmStatus.values()){
				items.add(new SelectItem(b, b.name()));
			}*/
			items.add(new SelectItem(AtmStatus.ATM.name(),AtmStatus.ATM.name()));

			itemsBranch = new ArrayList<SelectItem>();
			for(Branch b : ViewHelper.dao.filter(Branch.class,null,null,null,null,0,-1)){
				itemsBranch.add(new SelectItem(b, b.getName()));
				mapAgence.put(b.getId().toString(),b);
			}

			itemsSauv = new ArrayList<SelectItem>();
			for(GroupeSauv b : ViewHelper.dao.filter(GroupeSauv.class,null,null,null,null,0,-1)){
				itemsSauv.add(new SelectItem(b, b.getLibelle()));
				mapgrpSauv.put(b.getId().toString(),b);
			}

			if(DialogFormMode.UPDATE.equals(this.mode)){
				currentObject = ViewHelper.dao.findByPrimaryKey(Atm.class, currentObject.getTid(),null);
				if(currentObject.getAgence() != null )txtBranch = currentObject.getAgence().getId().toString();
				if(currentObject.getGroupeSauv() != null ) txtGroupe = currentObject.getGroupeSauv().getId().toString();
			}else{
				currentObject = new Atm();
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
	 * @return the itemsSauv
	 */
	public List<SelectItem> getItemsSauv() {
		return itemsSauv;
	}


	/**
	 * @param itemsSauv the itemsSauv to set
	 */
	public void setItemsSauv(List<SelectItem> itemsSauv) {
		this.itemsSauv = itemsSauv;
	}


	/**
	 * @return the itemsBranch
	 */
	public List<SelectItem> getItemsBranch() {
		return itemsBranch;
	}


	/**
	 * @param itemsBranch the itemsBranch to set
	 */
	public void setItemsBranch(List<SelectItem> itemsBranch) {
		this.itemsBranch = itemsBranch;
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
	public void setCurrentObject(Atm currentObject) {
		this.currentObject = currentObject;
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
	public Atm getCurrentObject() {
		// TODO Auto-generated method stub
		return this.currentObject;
	}

}
