package com.afb.portal.presentation.monitor.parameter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.afb.portal.jpa.gab.parameter.GroupeAlerte;
import com.afb.portal.presentation.models.AbstractPanel;
import com.afb.portal.presentation.models.DialogFormMode;
import com.afb.portal.presentation.models.IDialog;
import com.afb.portal.presentation.tools.ExceptionHelper;
import com.afb.portal.presentation.tools.ViewHelper;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

/**
 * GroupeAlerte
 * @author Owner
 *
 */
public class GroupeAlerteListPanel extends AbstractPanel{


	private String txtparam;
	
	private GroupeAlerteDialog  dialog;
	
	private List<GroupeAlerte> values = new ArrayList<GroupeAlerte>();

	/**
	 * contructeur
	 */
	public GroupeAlerteListPanel() {
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
	 * @return the dialog
	 */
	public GroupeAlerteDialog getDialog() {
		return dialog;
	}

	/**
	 * @param dialog the dialog to set
	 */
	public void setDialog(GroupeAlerteDialog dialog) {
		this.dialog = dialog;
	}

	/**
	 * @return the values
	 */
	public List<GroupeAlerte> getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(List<GroupeAlerte> values) {
		this.values = values;
	}

	/**
	 * fichier de definition
	 */
	@Override
	public String getFileDefinition() {

		// On retourne le chemin du fichier de definition
		return "/views/parameter/GroupeAlerteListPanel.xhtml";
	}

	/**
	 * Nom
	 */
	@Override
	public String getName() {
		// On retourne le nom
		return "GroupeAlerteListPanel";
	}

	/**
	 * tittre
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "GroupeAlerteListPanel.title";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.IViewComponent#getChildDialogDefinition()
	 */
	@Override
	public String getChildDialogDefinition() {

		// On retourne le chemin du fichier de definition
		return "/views/parameter/GroupeAlerteDialog.xhtml";
		
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
			values = new ArrayList<GroupeAlerte>();
			
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
		GroupeAlerte currentObject = child.getCurrentObject();

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
			rc.add(Restrictions.like("libelle","%"+txtparam+"%"));
			
			// Methode de filtre
			this.values = ViewHelper.dao.filter(GroupeAlerte.class,null,rc,null,null,0,-1);

			// Si la liste est nulle
			if(this.values == null) this.values = new ArrayList<GroupeAlerte>();

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
			dialog = new GroupeAlerteDialog(DialogFormMode.CREATE, new GroupeAlerte(), this);
			// Ouverture
			this.dialog.setOpen(true);
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

		GroupeAlerte value = this.values.get(index);

		try {

			// Instanciation 
			dialog = new GroupeAlerteDialog(DialogFormMode.UPDATE, value, this);

			// Ouverture
			dialog.open();

		} catch (Exception e) {

			// Etat d'erreur
			this.error = true;

			// Traitement de l'exception
			ExceptionHelper.threatException(e);
		}
		
	}
	
}
