package com.afb.portal.presentation.monitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.equipment.AtmStatus;
import com.afb.portal.presentation.models.AbstractPanel;
import com.afb.portal.presentation.models.DialogFormMode;
import com.afb.portal.presentation.models.IDialog;
import com.afb.portal.presentation.tools.ExceptionHelper;
import com.afb.portal.presentation.tools.ViewHelper;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

/**
 * Classe de creation des parametres
 * @author Francis
 * @version 1.0
 */
public class TPEListPanel extends AbstractPanel{

	/**
	 * txtparam
	 */
	private String txtparam;
	
	/**
	 * txtparam
	 */
	private String tid;
	
	/**
	 * txtparam
	 */
	private String ville;
	
	
	/**
	 * txtparam
	 */
	private String ip;
	
	
	/**
	 * txtparam
	 */
	private String telephone;
	
	
	/**
	 * txtparam
	 */
	private String promoteur;

	/**
	 * AtmDialog
	 */
	private TPEDialog  atmDialog;

	/**
	 * AtmDialog
	 */
	// private AtmUserDialog  atmUserDialog;

	/**
	 * 
	 */
	//private String txtatmUserDialog = "/views/parameter/AtmUserDialog.xhtml";

	/**
	 * values
	 */
	private List<Atm> values = new ArrayList<Atm>();

	/**
	 * Items
	 */
	//private List<SelectItem> itemsBranch = new ArrayList<SelectItem>(); 

	/**
	 * 
	 */
	//private Branch agence;

	//private AtmStatus typeAtm;

	/**
	 * Items
	 */
	//private List<SelectItem> items = new ArrayList<SelectItem>(); 

	/**
	 * contructeur
	 */
	public TPEListPanel() {
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
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return the promoteur
	 */
	public String getPromoteur() {
		return promoteur;
	}

	/**
	 * @param promoteur the promoteur to set
	 */
	public void setPromoteur(String promoteur) {
		this.promoteur = promoteur;
	}

	/**
	 * @return the atmDialog
	 */
	public TPEDialog getAtmDialog() {
		return atmDialog;
	}

	/**
	 * @param atmDialog the atmDialog to set
	 */
	public void setAtmDialog(TPEDialog atmDialog) {
		this.atmDialog = atmDialog;
	}

	/**
	 * @return the values
	 */
	public List<Atm> getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(List<Atm> values) {
		this.values = values;
	}


	/**
	 * @return the tid
	 */
	public String getTid() {
		return tid;
	}

	/**
	 * @param tid the tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}

	/**
	 * @return the ville
	 */
	public String getVille() {
		return ville;
	}

	/**
	 * @param ville the ville to set
	 */
	public void setVille(String ville) {
		this.ville = ville;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * fichier de definition
	 */
	@Override
	public String getFileDefinition() {

		// On retourne le chemin du fichier de definition
		return "/views/parameter/TPEListPanel.xhtml";
	}

	/**
	 * Nom
	 */
	@Override
	public String getName() {
		// On retourne le nom
		return "tPEListPanel";
	}

	/**
	 * tittre
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "AtmListPanel.title";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.IViewComponent#getChildDialogDefinition()
	 */
	@Override
	public String getChildDialogDefinition() {

		// On retourne le chemin du fichier de definition
		return "/views/parameter/TPEDialog.xhtml";

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
	
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.AbstractPanel#initComponents()
	 */
	@Override
	protected void initComponents(){

		try {

			//  Chargement Type de Carte
			/**items = new ArrayList<SelectItem>();
			for(AtmStatus b : AtmStatus.values()){
				items.add(new SelectItem(b, b.name()));
			}*/

			// txtatmUserDialog = "/views/parameter/AtmUserDialog.xhtml";
			// Date Comptable
			//super.initComponents();

			/**
			itemsBranch = new ArrayList<SelectItem>();
			for(Branch b : ViewHelper.dao.filter(Branch.class,null,null,null,null,0,-1)){
				itemsBranch.add(new SelectItem(b, b.getName()));
			}*/

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
		Atm currentObject = child.getCurrentObject();

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
			if(txtparam != null && !txtparam.trim().isEmpty())rc.add(Restrictions.like("nom","%"+txtparam+"%",MatchMode.ANYWHERE));
			if(ip != null && !ip.trim().isEmpty())rc.add(Restrictions.like("ip","%"+ip+"%",MatchMode.ANYWHERE));
			if(ville != null && !ville.trim().isEmpty())rc.add(Restrictions.like("adresse","%"+ville+"%",MatchMode.ANYWHERE));
			if(tid != null && !tid.trim().isEmpty())rc.add(Restrictions.like("tid","%"+tid+"%",MatchMode.ANYWHERE));
			if(telephone != null && !telephone.trim().isEmpty())rc.add(Restrictions.like("telephone","%"+telephone+"%",MatchMode.ANYWHERE));
			if(promoteur != null && !promoteur.trim().isEmpty())rc.add(Restrictions.like("promoteur","%"+promoteur+"%",MatchMode.ANYWHERE));
			rc.add(Restrictions.eq("typeAtm",AtmStatus.TPE));
			// rc.add(Restrictions.like("nom","%"+txtparam+"%",MatchMode.ANYWHERE));
			this.values = ViewHelper.dao.filter(Atm.class,null,rc,null,null,0,-1);
			// Si la liste est nulle
			if(this.values == null) this.values = new ArrayList<Atm>();

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
	public void processCreate(){

		try {

			// Instanciation du Panneau de pays
			atmDialog = new TPEDialog(DialogFormMode.CREATE, new Atm(), this);
			// Ouverture
			// this.atmDialog.setOpen(true);
			this.atmDialog.open();

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
	public void processUpdate(){

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

		Atm value = this.values.get(index);

		try {

			// Instanciation 
			atmDialog = new TPEDialog(DialogFormMode.UPDATE, value, this);

			// Ouverture
			atmDialog.open();

		} catch (Exception e) {

			// Etat d'erreur
			this.error = true;

			// Traitement de l'exception
			ExceptionHelper.threatException(e);
		}

	}

}
