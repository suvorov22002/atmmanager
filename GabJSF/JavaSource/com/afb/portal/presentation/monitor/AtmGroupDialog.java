package com.afb.portal.presentation.monitor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.richfaces.model.selection.Selection;

import afb.dsi.dpd.portal.jpa.entities.Branch;
import afb.dsi.dpd.portal.jpa.entities.User;

import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.equipment.AtmStatus;
import com.afb.portal.presentation.models.AbstractPanel;
import com.afb.portal.presentation.tools.ExceptionHelper;
import com.afb.portal.presentation.tools.ViewHelper;
import com.yashiro.persistence.utils.collections.converters.ConverterUtil;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

/**
 * 
 * @author Francis
 * @version 1.0
 */
public class AtmGroupDialog extends AbstractPanel{

	/**
	 * Objet courant
	 */
	private Atm currentObject = null;

	/**
	 * Items
	 */
	private List<SelectItem> items = new ArrayList<SelectItem>(); 

	/**
	 * itemsBranch
	 */
	private List<SelectItem> itemsBranch = new ArrayList<SelectItem>(); 

	/**
	 * 
	 */
	private Branch agence;

	/**
	 * values
	 */
	private List<Atm> allatms = new ArrayList<Atm>();

	/**
	 * values
	 */
	private List<Atm> saveatms = new ArrayList<Atm>();

	/**
	 * 
	 */
	private AtmStatus typeAtm;

	/**
	 * Element de l extender selectionne
	 */
	private Selection selection;

	/**
	 * Nom de l employee
	 */
	private String txtName;

	/**
	 * Map d employee
	 */
	private Map<String, User> mapEmployee = null;

	/**
	 * 
	 */
	private List<User> managers;

	/**
	 * Constructeur du Modele
	 */
	public AtmGroupDialog() {

	}


	/**
	 * @return the agence
	 */
	public Branch getAgence() {
		return agence;
	}

	/**
	 * @param agence the agence to set
	 */
	public void setAgence(Branch agence) {
		this.agence = agence;
	}




	/**
	 * @return the allatms
	 */
	public List<Atm> getAllatms() {
		return allatms;
	}




	/**
	 * @param allatms the allatms to set
	 */
	public void setAllatms(List<Atm> allatms) {
		this.allatms = allatms;
	}




	/**
	 * @return the saveatms
	 */
	public List<Atm> getSaveatms() {
		return saveatms;
	}




	/**
	 * @param saveatms the saveatms to set
	 */
	public void setSaveatms(List<Atm> saveatms) {
		this.saveatms = saveatms;
	}




	/**
	 * @return the typeAtm
	 */
	public AtmStatus getTypeAtm() {
		return typeAtm;
	}




	/**
	 * @return the selection
	 */
	public Selection getSelection() {
		return selection;
	}


	/**
	 * @param selection the selection to set
	 */
	public void setSelection(Selection selection) {
		this.selection = selection;
	}


	/**
	 * @return the txtName
	 */
	public String getTxtName() {
		return txtName;
	}


	/**
	 * @param txtName the txtName to set
	 */
	public void setTxtName(String txtName) {
		this.txtName = txtName;
	}


	/**
	 * @return the mapEmployee
	 */
	public Map<String, User> getMapEmployee() {
		return mapEmployee;
	}


	/**
	 * @param mapEmployee the mapEmployee to set
	 */
	public void setMapEmployee(Map<String, User> mapEmployee) {
		this.mapEmployee = mapEmployee;
	}


	/**
	 * @return the managers
	 */
	public List<User> getManagers() {
		return managers;
	}


	/**
	 * @param managers the managers to set
	 */
	public void setManagers(List<User> managers) {
		this.managers = managers;
	}


	/**
	 * @return the currentObject
	 */
	public Atm getCurrentObject() {
		return currentObject;
	}


	/**
	 * @param typeAtm the typeAtm to set
	 */
	public void setTypeAtm(AtmStatus typeAtm) {
		this.typeAtm = typeAtm;
	}

	/**
	 * 
	 * @return
	 */
	public boolean checkBuildedCurrentObject() {

		if(saveatms.isEmpty()) return false;
		
		if(managers.isEmpty()) return false;
		
		// On retourne true
		return true;
	}

	/* (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IDialog#getFileDefinition()
	 */
	@Override
	public String getFileDefinition() {

		// On retourne le chemin du fichier de definition
		return "/views/parameter/AtmGroupDialog.xhtml";

	}

	/* (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IDialog#getTitle()
	 */
	@Override
	public String getTitle() {

		// On retourne le Titre
		return "AtmGroupDialog.title";
	}


	/**
	 * mthode de seelction du subjection box 
	 */	
	public List<User> chargeEmployee(Object suggest) {

		// Carractere saisie dans le subjetion box 
		String pref = (String)suggest;

		// Variable qui doit contenir la lsite des employe en fonction du carractere saisie
		ArrayList<User> result = new ArrayList<User>();

		//if(pref.length() == 1)
		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.like("name","%"+pref+"%",MatchMode.ANYWHERE));
		List<User> employees = ViewHelper.dao.filter(User.class,null,rc,null,null,0,-1);

		// Recupere la liste des employees
		Iterator<User> iterator = employees.iterator();

		while (iterator.hasNext()) {

			// Position sur le premier element de la liste
			User elem = ((User) iterator.next());

			// filtre en faonction des carrastesre sasies
			if ((elem.getName() != null && elem.getName().toLowerCase().indexOf(pref.toLowerCase()) == 0) || "".equals(pref))
			{
				// 	Ajout de de l employe selectionner dans la liste
				result.add(elem);
			}
		}

		// retourne le resultat
		return result;
	}




	/**
	 * Methode qui permet de charge un employe dans la liste
	 */
	public void processAdd(){


		try{

			// Si l employee n est pas null
			if(txtName != null && txtName.trim().length() != 0){

				// si l element n est pas dans la map
				if(!mapEmployee.containsKey(txtName)){

					return;

				}else{

					// recupere l employe de la map
					User employe = mapEmployee.get(txtName);

					// si c est employee n est vpas encore dans la liste
					if(!managers.contains(employe)){

						// Ajout dans la liste 
						this.managers.add(employe);

					}

					txtName = new String();

				}

			}

		} catch (Exception e) {

			// Etat d'erreur
			this.error = true;

			// Traitement de l'exception
			ExceptionHelper.threatException(e);
		}

	}

	/**
	 * Methode qui permet de suprimer un manager de la liste
	 */
	public void processDelete(){

		// Si la selection est nulle
		if(selection == null) {

			// On sort
			return;
		}

		// Obtention des lignes selectionnees
		Iterator<Object> iterator = selection.getKeys();

		// Index de la ligne selectionnee
		int index = 0;

		// Si l'iterateur est null
		if(iterator == null) {

			// On sort
			return;
		}

		// Si l'iterateur a un element
		if(iterator.hasNext()) {

			// Index selectionné
			index = (Integer) iterator.next();

		}

		// Si l'index n'est pas dans l'intervalle du modele
		if(index < 0 || index >= this.managers.size()) {

			// On sort
			return;
		}

		try {

			// Retire de la liste 
			this.managers.remove(index);

		} catch (Exception e) {

			// Etat d'erreur
			this.error = true;

			// Traitement de l'exception
			ExceptionHelper.threatException(e);
		}



	}

	protected void validate() {

		// Enregistrement
		for(Atm atm : saveatms){
			atm = ViewHelper.dao.update(atm);		
		}

	}

	/**
	 * buildCurrentObject
	 */
	protected void buildCurrentObject() {

		// mise a jour des employee selectionne
		for(Atm atm : saveatms){
			atm.setUsers(ConverterUtil.convertCollectionToSet(managers));			
		}

	}


	public void saveAtms() {

		try {

			// Si le Contrôle de l'Objet Courant echoue
			if(!this.checkBuildedCurrentObject()) {

				// On positionne l'etat d'info
				this.information = true;

				// On sort
				return;
			}

			// Construction de l'Objet Courant
			buildCurrentObject();

			// Validation
			validate();

			// Information
			information = true;
			// Message d'information
			ExceptionHelper.showInformation("abstractdialog.operation.ok", this);

		} catch (Exception e) {

			// Mise en place de l'etat d'inclusion du fichier d'erreur
			this.error = true;

			// Traitement de l'exception
			ExceptionHelper.threatException(e);
		}
	}

	@Override
	protected void disposeResourceOnClose() {
		// TODO Auto-generated method stub
		items.clear();
		itemsBranch.clear();
		agence = null;
		allatms.clear();
		saveatms.clear();
		typeAtm = null;
		managers.clear();
		mapEmployee.clear();
	}

	
	public void chargement() {
		// TODO Auto-generated method stub

		try{

			allatms = new ArrayList<Atm>();
			RestrictionsContainer rc = RestrictionsContainer.getInstance();
			rc.add(Restrictions.or(Restrictions.like("nom","%"+txtName+"%",MatchMode.ANYWHERE), Restrictions.like("typeAtm",typeAtm)));
			allatms = ViewHelper.dao.filter(Atm.class,null,rc,null,null,0,-1);

		}catch(Exception e){

			e.printStackTrace();
			// Etat d'erreur
			this.error = true;
			// Traitement de l'exception
			ExceptionHelper.threatException(e);

		}

	}
	
	/**
	 * 
	 */
	@Override
	public void initComponents() {
		// TODO Auto-generated method stub

		try{

			
			allatms = new ArrayList<Atm>();
			saveatms = new ArrayList<Atm>();
			managers = new ArrayList<User>();
			mapEmployee = new HashMap<String, User>();
			
			//  Chargement Type de Carte
			items = new ArrayList<SelectItem>();
			for(AtmStatus b : AtmStatus.values()){
				items.add(new SelectItem(b, b.name()));
			}

			itemsBranch = new ArrayList<SelectItem>();
			for(Branch b : ViewHelper.dao.filter(Branch.class,null,null,null,null,0,-1)){
				itemsBranch.add(new SelectItem(b, b.getName()));
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


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "AtmGroupDialog";
	}


}
