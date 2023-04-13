package com.afb.portal.presentation.monitor.parameter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.afb.portal.jpa.gab.parameter.Equipement;
import com.afb.portal.jpa.gab.parameter.TypeEquipement;
import com.afb.portal.presentation.models.AbstractPanel;
import com.afb.portal.presentation.models.DialogFormMode;
import com.afb.portal.presentation.models.IDialog;
import com.afb.portal.presentation.tools.ExceptionHelper;
import com.afb.portal.presentation.tools.ViewHelper;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

/**
 * EquipementListPanel
 * @author Owner
 * @version 1.0
 */
public class EquipementListPanel extends AbstractPanel {

	
	private String libelle;
	
	private TypeEquipement typeEquipement;
	
	private EquipementDialog  equipementDialog;
	
	private List<Equipement> equipements = new ArrayList<Equipement>();
	
	private List<SelectItem> valuesItems = new ArrayList<SelectItem>();

	/**
	 * contructeur
	 */
	public EquipementListPanel() {
		super();
	}

	/**
	 * @return the equipementDialog
	 */
	public EquipementDialog getEquipementDialog() {
		return equipementDialog;
	}

	/**
	 * @param equipementDialog the equipementDialog to set
	 */
	public void setEquipementDialog(EquipementDialog equipementDialog) {
		this.equipementDialog = equipementDialog;
	}

	/**
	 * @return the equipements
	 */
	public List<Equipement> getEquipements() {
		return equipements;
	}

	/**
	 * @param equipements the equipements to set
	 */
	public void setEquipements(List<Equipement> equipements) {
		this.equipements = equipements;
	}

	/**
	 * @return the libelle
	 */
	public String getLibelle() {
		return libelle;
	}

	/**
	 * @param libelle the libelle to set
	 */
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	/**
	 * @return the typeEquipement
	 */
	public TypeEquipement getTypeEquipement() {
		return typeEquipement;
	}

	/**
	 * @param typeEquipement the typeEquipement to set
	 */
	public void setTypeEquipement(TypeEquipement typeEquipement) {
		this.typeEquipement = typeEquipement;
	}

	/**
	 * @return the valuesItems
	 */
	public List<SelectItem> getValuesItems() {
		return valuesItems;
	}

	/**
	 * @param valuesItems the valuesItems to set
	 */
	public void setValuesItems(List<SelectItem> valuesItems) {
		this.valuesItems = valuesItems;
	}
	
	

	/**
	 * fichier de definition
	 */
	@Override
	public String getFileDefinition() {

		// On retourne le chemin du fichier de definition
		return "/views/parameter/EquipementListPanel.xhtml";
	}

	/**
	 * Nom
	 */
	@Override
	public String getName() {
		// On retourne le nom
		return "equipementListPanel";
	}

	/**
	 * tittre
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "EquipementListPanel.title";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.IViewComponent#getChildDialogDefinition()
	 */
	@Override
	public String getChildDialogDefinition() {

		// On retourne le chemin du fichier de definition
		return "/views/parameter/EquipementDialog.xhtml";
		
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
		equipementDialog = null;
		equipements.clear();
		
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
			equipements = new ArrayList<Equipement>();
			
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
	public void validateSubDialogClosure(IDialog child, DialogFormMode mode,boolean wellClose) {
		
		// Si la fenetre s'est bien fermée
		if(!wellClose) return;

		// Obtention de l'Objet Courant
		Equipement  Object = child.getCurrentObject();

		// Si l'Objet courant est null
		if(Object == null) return;

		// Si on est en mode creation
		if(mode.equals(DialogFormMode.CREATE)) {

			// Ajout dans la liste
			this.equipements.add(Object);

		} else if(mode.equals(DialogFormMode.UPDATE)) {

			// Recherche de l'Objet
			int index = this.equipements.indexOf(Object);

			// Si l'objet n'existe pas
			if(index < 0) return;

			// On met a jour
			this.equipements.set(index, Object);

		}	
				
	}
	
	
	/**
	 * Methode de recherche
	 */
	public void processSearch() {

		try {

			RestrictionsContainer rc = RestrictionsContainer.getInstance();
			//rc.add(Restrictions.or(Restrictions.like("libelle","%"+libelle+"%",MatchMode.ANYWHERE), Restrictions.eq("typeEquipement",typeEquipement)));
			Restrictions.like("libelle","%"+libelle+"%",MatchMode.ANYWHERE);
			this.equipements = ViewHelper.dao.filter(Equipement.class,null,rc,null,null,0,-1);

			// Si la liste est nulle
			if(this.equipements == null) this.equipements = new ArrayList<Equipement>();

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
			equipementDialog = new EquipementDialog(DialogFormMode.CREATE, new Equipement(), this);
			// Ouverture
			this.equipementDialog.open();
			
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
		if(index < 0 || index >= this.equipements.size()) {

			// On sort
			return;
		}

		Equipement value = this.equipements.get(index);

		try {

			// Instanciation 
			equipementDialog = new EquipementDialog(DialogFormMode.UPDATE, value, this);

			// Ouverture
			equipementDialog.open();

		} catch (Exception e) {

			// Etat d'erreur
			this.error = true;

			// Traitement de l'exception
			ExceptionHelper.threatException(e);
		}
	}
	
}
