package com.afb.portal.presentation.monitor.parameter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.afb.portal.jpa.gab.parameter.TypeEquipement;
import com.afb.portal.presentation.models.AbstractPanel;
import com.afb.portal.presentation.models.DialogFormMode;
import com.afb.portal.presentation.models.IDialog;
import com.afb.portal.presentation.tools.ExceptionHelper;
import com.afb.portal.presentation.tools.ViewHelper;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

/**
 * TypeEquipementListPanel
 * @author Owner
 * @version 1.0
 */
public class TypeEquipementListPanel extends AbstractPanel{

	private String txtparam;
		
	private TypeEquipementDialog  typeEquipementDialog;
	
	private List<TypeEquipement> typeEquipements = new ArrayList<TypeEquipement>();

	/**
	 * contructeur
	 */
	public TypeEquipementListPanel() {
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
	 * @return the typeEquipementDialog
	 */
	public TypeEquipementDialog getTypeEquipementDialog() {
		return typeEquipementDialog;
	}

	/**
	 * @param typeEquipementDialog the typeEquipementDialog to set
	 */
	public void setTypeEquipementDialog(TypeEquipementDialog typeEquipementDialog) {
		this.typeEquipementDialog = typeEquipementDialog;
	}

	/**
	 * @return the typeEquipements
	 */
	public List<TypeEquipement> getTypeEquipements() {
		return typeEquipements;
	}

	/**
	 * @param typeEquipements the typeEquipements to set
	 */
	public void setTypeEquipements(List<TypeEquipement> typeEquipements) {
		this.typeEquipements = typeEquipements;
	}

	/**
	 * fichier de definition
	 */
	@Override
	public String getFileDefinition() {

		// On retourne le chemin du fichier de definition
		return "/views/parameter/TypeEquipementListPanel.xhtml";
	}

	/**
	 * Nom
	 */
	@Override
	public String getName() {
		// On retourne le nom
		return "typeEquipementListPanel";
	}

	/**
	 * tittre
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "TypeEquipementListPanel.title";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.IViewComponent#getChildDialogDefinition()
	 */
	@Override
	public String getChildDialogDefinition() {

		// On retourne le chemin du fichier de definition
		return "/views/parameter/TypeEquipementDialog.xhtml";
		
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
		typeEquipementDialog = null;
		txtparam ="";
		typeEquipements.clear();
		
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
			typeEquipements = new ArrayList<TypeEquipement>();
			
		}catch(Exception e){

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
		TypeEquipement currentObject = child.getCurrentObject();

		// Si l'Objet courant est null
		if(currentObject == null) return;

		// Si on est en mode creation
		if(mode.equals(DialogFormMode.CREATE)) {

			// Ajout dans la liste
			this.typeEquipements.add(currentObject);

		} else if(mode.equals(DialogFormMode.UPDATE)) {

			// Recherche de l'Objet
			int index = this.typeEquipements.indexOf(currentObject);

			// Si l'objet n'existe pas
			if(index < 0) return;

			// On met a jour
			this.typeEquipements.set(index, currentObject);

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
			this.typeEquipements = ViewHelper.dao.filter(TypeEquipement.class,null,rc,null,null,0,-1);

			// Si la liste est nulle
			if(this.typeEquipements == null) this.typeEquipements = new ArrayList<TypeEquipement>();

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
			typeEquipementDialog = new TypeEquipementDialog(DialogFormMode.CREATE, new TypeEquipement(), this);
			// Ouverture
			this.typeEquipementDialog.setOpen(true);
			this.typeEquipementDialog.open();
			
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
		if(index < 0 || index >= this.typeEquipements.size()) {

			// On sort
			return;
		}

		TypeEquipement value = this.typeEquipements.get(index);

		try {

			// Instanciation 
			typeEquipementDialog = new TypeEquipementDialog(DialogFormMode.UPDATE, value, this);

			// Ouverture
			typeEquipementDialog.open();

		} catch (Exception e) {

			// Etat d'erreur
			this.error = true;

			// Traitement de l'exception
			ExceptionHelper.threatException(e);
		}
	}

	
}
