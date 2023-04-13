package com.afb.portal.presentation.monitor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Asynchronous;

import org.richfaces.model.selection.Selection;

import afb.dsi.dpd.portal.jpa.entities.User;

import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.presentation.models.AbstractDialog;
import com.afb.portal.presentation.models.DialogFormMode;
import com.afb.portal.presentation.models.IViewComponent;
import com.afb.portal.presentation.tools.ExceptionHelper;
import com.afb.portal.presentation.tools.ViewHelper;
import com.yashiro.persistence.utils.collections.converters.ConverterUtil;

/**
 * Boite de Dialogue de Creation dun parametre
 * @author Francis
 * @version 1.0
 */
public class AtmUserDialog extends AbstractDialog{

	/**
	 * Objet courant de la fenetre
	 */
	private Atm currentObject = null;

	/**
	 * Nom de l employee
	 */
	private String txtName;

	/**
	 * Map d employee
	 */
	private Map<String, User> mapEmployee = new HashMap<String, User>();

	private Selection selection;

	/**
	 * 
	 */
	private List<User> managers = new ArrayList<User>();


	/**
	 * 
	 */
	private List<User> allUsers = new ArrayList<User>();


	/**
	 * Constructeur du Modele
	 * @param mode	Mode d'ouverture de la Boite
	 * @param currentObject	Objet Courant de la Boite
	 * @param parent	Composant Parent
	 */
	public AtmUserDialog(DialogFormMode mode, Atm currentObject, IViewComponent parent) {

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

		this.currentObject.setUsers(ConverterUtil.convertCollectionToSet(managers));

		// Si on est en mode Enregistrement
		// Enregistrement
		this.currentObject = ViewHelper.dao.update(currentObject);
	}

	@Override
	protected void buildCurrentObject() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void disposeResourceOnClose() {
		// TODO Auto-generated method stub
		mapEmployee.clear();
		managers.clear();
		allUsers.clear();
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
	 * @return the allUsers
	 */
	public List<User> getAllUsers() {
		return allUsers;
	}




	/**
	 * @param allUsers the allUsers to set
	 */
	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}




	/**
	 * 
	 */
	@Override
	public void initComponents() {
		// TODO Auto-generated method stub

		try{

			//allUsers = new ArrayList<User>();
			//mapEmployee = new HashMap<String, User>();
			
			currentObject = ViewHelper.dao.findByPrimaryKey(Atm.class, currentObject.getTid(),null);
			managers = ConverterUtil.convertCollectionToList(currentObject.getUsers());
			// allUsers =  ViewHelper.dao.filter(User.class,null,null,null,null,0,-1);

			// RestrictionsContainer rc = RestrictionsContainer.getInstance();
			// rc.add(Restrictions.like("name","%"+pref.toUpperCase()+"%"));
			// allUsers = ViewHelper.dao.filter(User.class,null,null,null,null,0,-1);
			// for(User elem : allUsers)mapEmployee.put(elem.getName().trim(),elem);
			// chargemenrUser();

		}catch(Exception e){

			e.printStackTrace();
			// Etat d'erreur
			this.error = true;
			// Traitement de l'exception
			ExceptionHelper.threatException(e);

		}

	}


	@Asynchronous
	public void  chargemenrUser(){
		try{

			if(allUsers == null || allUsers.isEmpty() ){

				// rc.add(Restrictions.like("name","%"+pref.toUpperCase()+"%"));
				System.out.println("---------------chargemenrUser----------------------");
				allUsers = ViewHelper.dao.filter(User.class,null,null,null,null,0,-1);
				for(User elem : allUsers)mapEmployee.put(elem.getName().trim(),elem);

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
	 * mthode de seelction du subjection box 
	 */	
	public List<User> chargeEmployee(Object suggest) {

		// mapEmployee = new HashMap<String, User>();
		// Carractere saisie dans le subjetion box 
		String pref = (String)suggest;

		// Variable qui doit contenir la lsite des employe en fonction du carractere saisie
		ArrayList<User> result = new ArrayList<User>();

		//if(pref.length() == 1)
		//RestrictionsContainer rc = RestrictionsContainer.getInstance();
		//rc.add(Restrictions.like("name","%"+pref.toUpperCase()+"%"));
		//allUsers = ViewHelper.dao.filter(User.class,null,rc,null,null,0,-1);

		// Recupere la liste des employees
		Iterator<User> iterator = allUsers.iterator();

		while (iterator.hasNext()) {

			// Position sur le premier element de la liste
			User elem = ((User) iterator.next());

			// filtre en faonction des carrastesre sasies
			if ((elem.getName() != null && elem.getName().toLowerCase().indexOf(pref.toLowerCase()) == 0) || "".equals(pref))
			{
				// 	Ajout de de l employe selectionner dans la liste
				result.add(elem);
				//mapEmployee.put(elem.getName().trim(),elem);
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

			// System.out.println("-----------txtName---------------"+txtName);
			// Si l employee n est pas null
			if(txtName != null && txtName.trim().length() != 0){

				// si l element n est pas dans la map
				String name = txtName;
				if(!mapEmployee.containsKey(name.trim())){
					// System.out.println("-----------!mapEmployee.containsKey(txtName)---------------"+txtName+"----"+mapEmployee.size());
					/**for(String val : ConverterUtil.convertCollectionToList(mapEmployee.keySet())){
						// System.out.println("------Name------------"+val+"-----");
					}*/
					return;

				}else{

					// System.out.println("-----------Put---------------"+txtName);

					// recupere l employe de la map
					User employe = mapEmployee.get(name);

					// si c est employee n est vpas encore dans la liste
					if(!managers.contains(employe)){

						// Ajout dans la liste 
						this.managers.add(employe);

					}

					// txtName = new String();

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
