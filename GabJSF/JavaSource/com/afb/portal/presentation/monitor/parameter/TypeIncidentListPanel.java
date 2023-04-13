package com.afb.portal.presentation.monitor.parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Asynchronous;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import afb.dsi.dpd.portal.jpa.entities.User;

import com.afb.portal.jpa.gab.parameter.TypeIncident;
import com.afb.portal.presentation.models.AbstractPanel;
import com.afb.portal.presentation.models.DialogFormMode;
import com.afb.portal.presentation.models.IDialog;
import com.afb.portal.presentation.tools.ExceptionHelper;
import com.afb.portal.presentation.tools.ViewHelper;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

/**
 * TypeIncidentListPanel
 * @author Owner
 * @version 1.0
 */
public class TypeIncidentListPanel extends AbstractPanel{

	private String txtparam;
	
	private TypeIncidentDialog  dialog;
	
	private UserIncidentDialog  userIncidentDialog;
	
	private String txtuserIncidentDialog = "/views/parameter/UserIncidentDialog.xhtml";
	
	private List<TypeIncident> values = new ArrayList<TypeIncident>();
	
	/**
	 * 
	 */
	private List<User> allUsers = new ArrayList<User>();
	
	/**
	 * Map d employee
	 */
	private Map<String, User> mapEmployee = new HashMap<String, User>();

	/**
	 * contructeur
	 */
	public TypeIncidentListPanel() {
		super();
	}

	/**
	 * @return the txtparam
	 */
	public String getTxtparam() {
		return txtparam;
	}

	/**
	 * @param txtparam the txtparam to set
	 */
	public void setTxtparam(String txtparam) {
		this.txtparam = txtparam;
	}

	
	
	/**
	 * @return the userIncidentDialog
	 */
	public UserIncidentDialog getUserIncidentDialog() {
		return userIncidentDialog;
	}

	/**
	 * @param userIncidentDialog the userIncidentDialog to set
	 */
	public void setUserIncidentDialog(UserIncidentDialog userIncidentDialog) {
		this.userIncidentDialog = userIncidentDialog;
	}

	/**
	 * @return the txtuserIncidentDialog
	 */
	public String getTxtuserIncidentDialog() {
		return txtuserIncidentDialog;
	}

	/**
	 * @param txtuserIncidentDialog the txtuserIncidentDialog to set
	 */
	public void setTxtuserIncidentDialog(String txtuserIncidentDialog) {
		this.txtuserIncidentDialog = txtuserIncidentDialog;
	}

	/**
	 * @return the dialog
	 */
	public TypeIncidentDialog getDialog() {
		return dialog;
	}

	/**
	 * @param dialog the dialog to set
	 */
	public void setDialog(TypeIncidentDialog dialog) {
		this.dialog = dialog;
	}

	/**
	 * @return the values
	 */
	public List<TypeIncident> getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(List<TypeIncident> values) {
		this.values = values;
	}

	/**
	 * fichier de definition
	 */
	@Override
	public String getFileDefinition() {

		// On retourne le chemin du fichier de definition
		return "/views/parameter/TypeIncidentListPanel.xhtml";
	}

	/**
	 * Nom
	 */
	@Override
	public String getName() {
		// On retourne le nom
		return "typeIncidentListPanel";
	}

	/**
	 * tittre
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "TypeIncidentListPanel.title";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.IViewComponent#getChildDialogDefinition()
	 */
	@Override
	public String getChildDialogDefinition() {

		// On retourne le chemin du fichier de definition
		return "/views/parameter/TypeIncidentDialog.xhtml";
		
	}

	/**
	 * 
	 */
	@Override
	public String getIcon(){

		// Chemin de l'Image
		return ViewHelper.getSessionSkinURLStatic() + "/Images/CountryListPanelLogo.png";
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.AbstractPanel#disposeResourceOnClose()
	 */
	@Override
	protected void disposeResourceOnClose() {

		super.disposeResourceOnClose();		
		dialog = null;
		txtparam ="";
		values.clear();
		
	}
		
	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.AbstractPanel#initComponents()
	 */
	@Override
	protected void initComponents() {
				
		try {
			
			// Date Comptable
			super.initComponents();
			values = new ArrayList<TypeIncident>();
			txtuserIncidentDialog = "/views/parameter/UserIncidentDialog.xhtml";
			
			//chargemenrUser();
			
		}catch(Exception e){

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
	
	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.AbstractPanel#validateSubDialogClosure(com.afb.portal.presentation.models.IDialog, com.afb.portal.presentation.models.DialogFormMode, boolean)
	 */
	@Override
	public void validateSubDialogClosure(IDialog child, DialogFormMode mode,
			boolean wellClose) {
		
		// Si la fenetre s'est bien fermée
		if(!wellClose) return;

		// Obtention de l'Objet Courant
		TypeIncident currentObject = child.getCurrentObject();

		// Si l'Objet courant est null
		if(currentObject == null) return;

		// Si on est en mode creation
		if(mode.equals(DialogFormMode.CREATE)) {

			// Ajout dans la liste
			this.values.add(currentObject);

		} else if(mode.equals(DialogFormMode.UPDATE)) {

			// Recherche de l'Objet
			int index = this.values.indexOf(currentObject);

			// Si l'objet n'existe pas
			if(index < 0) return;

			// On met a jour
			this.values.set(index, currentObject);

		}	
				
	}
	
	
	/**
	 * Methode de recherche
	 */
	public void processSearch() {

		try {

			RestrictionsContainer rc = RestrictionsContainer.getInstance();
			rc.add(Restrictions.like("libelle","%"+txtparam+"%",MatchMode.ANYWHERE));
			
			// Methode de filtre
			this.values = ViewHelper.dao.filter(TypeIncident.class,null,rc,null,null,0,-1);

			// Si la liste est nulle
			if(this.values == null) this.values = new ArrayList<TypeIncident>();
			
			if(allUsers.isEmpty()){
				chargemenrUser();
			}

		} catch (Exception e) {

			// Etat d'erreur
			this.error = true;

			// Traitement de l'exception
			ExceptionHelper.threatException(e);
		}
		
	}

	/**
	 * Processus de Creation d'un Parametre
	 */
	public void processCreate() {

		try {
			
			// Instanciation du Panneau de pays
			dialog = new TypeIncidentDialog(DialogFormMode.CREATE, new TypeIncident(), this);
			// Ouverture
			//this.dialog.setOpen(true);
			this.dialog.open();
			
		} catch (Exception e) {

			// Etat d'erreur
			this.error = true;

			// Traitement de l'exception
			ExceptionHelper.threatException(e);
		}

	}

	/**
	 * Processus de Consultation d'un Pays
	 */
	public void processUpdate() {

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
		if(index < 0 || index >= this.values.size()) {

			// On sort
			return;
		}

		TypeIncident value = this.values.get(index);

		try {

			// Instanciation 
			dialog = new TypeIncidentDialog(DialogFormMode.UPDATE, value, this);

			// Ouverture
			dialog.open();

		} catch (Exception e) {

			// Etat d'erreur
			this.error = true;

			// Traitement de l'exception
			ExceptionHelper.threatException(e);
		}
	}
	
	/**
	 * Processus de Consultation d'un Pays
	 */
	public void processUpdateUser() {

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
		if(index < 0 || index >= this.values.size()) {

			// On sort
			return;
		}

		TypeIncident value = this.values.get(index);

		try {

			// Instanciation 
			userIncidentDialog = new UserIncidentDialog(DialogFormMode.UPDATE, value, this);
			userIncidentDialog.setMapEmployee(new HashMap<String, User>(mapEmployee));
			userIncidentDialog.setAllUsers(new ArrayList<User>(allUsers));
			// Ouverture
			userIncidentDialog.open();
			chargemenrUser();

		} catch (Exception e) {

			// Etat d'erreur
			this.error = true;

			// Traitement de l'exception
			ExceptionHelper.threatException(e);
		}
	}
	
	
}
