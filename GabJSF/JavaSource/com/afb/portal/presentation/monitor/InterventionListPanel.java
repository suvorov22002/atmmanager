package com.afb.portal.presentation.monitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.hibernate.criterion.Restrictions;

import afb.dsi.dpd.portal.jpa.entities.Branch;

import com.afb.portal.jpa.gab.monitoring.Intervention;
import com.afb.portal.jpa.gab.parameter.TypeIntervention;
import com.afb.portal.presentation.models.AbstractPanel;
import com.afb.portal.presentation.models.DialogFormMode;
import com.afb.portal.presentation.models.IDialog;
import com.afb.portal.presentation.models.reportViewer.ReportViewer;
import com.afb.portal.presentation.tools.ExceptionHelper;
import com.afb.portal.presentation.tools.ViewHelper;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

/**
 * Intervention
 * @author Owner
 * @version 1.0
 */
public class InterventionListPanel extends AbstractPanel{

	/**
	 * 
	 */
	private ReportViewer reportViewer;

	/**
	 * datedebut
	 */
	private Date datedebut;

	/**
	 * datefin
	 */
	private Date datefin;

	/**
	 * InterventionDialog
	 */
	private InterventionDialog  dialog;

	/**
	 * values
	 */
	private List<Intervention> values = new ArrayList<Intervention>();

	/**
	 * Items
	 */
	private List<SelectItem> itemsBranch = new ArrayList<SelectItem>(); 

	/**
	 * 
	 */
	private String agence;


	private String type;

	/**
	 * Items
	 */
	private List<SelectItem> items = new ArrayList<SelectItem>(); 

	/**
	 * 
	 */
	private Map<String, TypeIntervention> mapTypeInt = new HashMap<String, TypeIntervention>();

	/**
	 * 
	 */
	private Map<String, Branch> mapAgence = new HashMap<String, Branch>();

	/**
	 * contructeur
	 */
	public InterventionListPanel() {
		super();
	}

	/**
	 * @return the datedebut
	 */
	public Date getDatedebut() {
		return datedebut;
	}

	/**
	 * @param datedebut the datedebut to set
	 */
	public void setDatedebut(Date datedebut) {
		this.datedebut = datedebut;
	}

	/**
	 * @return the datefin
	 */
	public Date getDatefin() {
		return datefin;
	}

	/**
	 * @param datefin the datefin to set
	 */
	public void setDatefin(Date datefin) {
		this.datefin = datefin;
	}

	/**
	 * @return the dialog
	 */
	public InterventionDialog getDialog() {
		return dialog;
	}

	/**
	 * @param dialog the dialog to set
	 */
	public void setDialog(InterventionDialog dialog) {
		this.dialog = dialog;
	}

	/**
	 * @return the values
	 */
	public List<Intervention> getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(List<Intervention> values) {
		this.values = values;
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
	 * @return the agence
	 */
	public String getAgence() {
		return agence;
	}

	/**
	 * @param agence the agence to set
	 */
	public void setAgence(String agence) {
		this.agence = agence;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * fichier de definition
	 */
	@Override
	public String getFileDefinition() {

		// On retourne le chemin du fichier de definition
		return "/views/parameter/InterventionListPanel.xhtml";
	}

	/**
	 * Nom
	 */
	@Override
	public String getName() {
		// On retourne le nom
		return "interventionListPanel";
	}

	/**
	 * tittre
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "InterventionListPanel.title";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.IViewComponent#getChildDialogDefinition()
	 */
	@Override
	public String getChildDialogDefinition() {

		// On retourne le chemin du fichier de definition
		return "/views/parameter/InterventionDialog.xhtml";

	}

	/**
	 * 
	 */
	@Override
	public String getIcon() {

		// Chemin de l'Image
		return ViewHelper.getSessionSkinURLStatic() + "/Images/CountryListPanelLogo.png";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.AbstractPanel#disposeResourceOnClose()
	 */
	@Override
	protected void disposeResourceOnClose() {
		// TODO Auto-generated method stub
		super.disposeResourceOnClose();
		items.clear();
		itemsBranch.clear();
		mapAgence.clear();
		mapTypeInt.clear();
		items.clear();
		itemsBranch.clear();
		dialog = null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.AbstractPanel#initComponents()
	 */
	@Override
	protected void initComponents(){

		try {

			// Date Comptable
			super.initComponents();

			itemsBranch = new ArrayList<SelectItem>();
			mapAgence = new HashMap<String, Branch>();
			itemsBranch.add(new SelectItem(null, ""));
			for(Branch b : ViewHelper.dao.filter(Branch.class,null,null,null,null,0,-1)){
				itemsBranch.add(new SelectItem(b.getId().toString(), b.getName()));
				mapAgence.put(b.getId().toString(),b);
			}

			items = new ArrayList<SelectItem>();
			mapTypeInt = new HashMap<String, TypeIntervention>();
			items.add(new SelectItem(null, ""));
			for(TypeIntervention b : ViewHelper.dao.filter(TypeIntervention.class,null,null,null,null,0,-1)){
				items.add(new SelectItem(b.getId().toString(), b.getLibelle()));
				mapTypeInt.put(b.getId().toString(),b);
			}

		}catch(Exception e){
			// Etat d'erreur
			this.error = true;
			// Traitement de l'exception
			ExceptionHelper.threatException(e);
		}

	}

	@Override
	public void validateSubDialogClosure(IDialog child, DialogFormMode mode,
			boolean wellClose) {

		// Si la fenetre s'est bien fermée
		if(!wellClose) return;

		// Obtention de l'Objet Courant
		Intervention currentObject = child.getCurrentObject();

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

			Branch agence = mapAgence.get(this.agence);
			TypeIntervention type = mapTypeInt.get(this.type);

			RestrictionsContainer rc = RestrictionsContainer.getInstance();
			rc.add(Restrictions.between("date", datedebut,datefin));
			if(agence != null ) rc.add(Restrictions.eq("agence",agence));
			if(type != null ) rc.add(Restrictions.eq("type",type));	
			rc.add(Restrictions.eq("status",Boolean.TRUE));	
			
			//AliasesContainer all = AliasesContainer.getInstance();
			// all.add("atm.agence","atm.agence");
			
			//LoaderModeContainer lmc = LoaderModeContainer.getInstance();
			//lmc.add("atm", FetchMode.JOIN);
			
			this.values = ViewHelper.dao.filter(Intervention.class,null,rc,null,null,0,-1);
			// Si la liste est nulle
			if(this.values == null) this.values = new ArrayList<Intervention>();

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
			dialog = new InterventionDialog(DialogFormMode.CREATE, new Intervention(), this);
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

		Intervention value = this.values.get(index);

		try {

			// Instanciation 
			value = ViewHelper.dao.findByPrimaryKey(Intervention.class, value.getId(), null);
			dialog = new InterventionDialog(DialogFormMode.UPDATE, value, this);

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
	public void processPrint() {

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

		Intervention value = this.values.get(index);

		try {

			// Instanciation  
			value = ViewHelper.dao.findByPrimaryKey(Intervention.class, value.getId(), null);
			
			// Affichage du rapport de traitement 
			List<Intervention> repports = new ArrayList<Intervention>();
			repports.add(value);
			reportViewer = new ReportViewer(repports,"FicheIntervention.jasper", null, "Fiche.Intervention", this,"/views/repport/ReportFicheIntervention.xhtml");
			reportViewer.viewReport();

		} catch (Exception e) {

			// Etat d'erreur
			this.error = true;

			// Traitement de l'exception
			ExceptionHelper.threatException(e);
		}

	}

	/**
	 * @return the reportViewer
	 */
	public ReportViewer getReportViewer() {
		return reportViewer;
	}

	/**
	 * @param reportViewer the reportViewer to set
	 */
	public void setReportViewer(ReportViewer reportViewer) {
		this.reportViewer = reportViewer;
	}
	
	


}
