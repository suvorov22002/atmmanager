package com.afb.portal.presentation.monitor.parameter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.afb.portal.jpa.gab.parameter.GabParameter;
import com.afb.portal.presentation.models.AbstractPanel;
import com.afb.portal.presentation.models.DialogFormMode;
import com.afb.portal.presentation.models.IDialog;
import com.afb.portal.presentation.tools.ExceptionHelper;
import com.afb.portal.presentation.tools.ViewHelper;
import com.yashiro.persistence.utils.dao.tools.encrypter.Encrypter;

/**
 * Classe de creation des parametres
 * @author Francis
 * @version 1.0
 */
public class ParameterListPanel extends AbstractPanel{


	private String txtCode;

	private String txtparam;

	private String txtdecription;

	private ParameterDialog  parameterDialog;

	private List<GabParameter> parameters = new ArrayList<GabParameter>();

	/**
	 * contructeur
	 */
	public ParameterListPanel() {
		super();
	}

	/**
	 * @return the txtCode
	 */
	public String getTxtCode() {
		return txtCode;
	}

	/**
	 * @param txtCode the txtCode to set
	 */
	public void setTxtCode(String txtCode) {
		this.txtCode = txtCode;
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
	 * @return the txtdecription
	 */
	public String getTxtdecription() {
		return txtdecription;
	}

	/**
	 * @param txtdecription the txtdecription to set
	 */
	public void setTxtdecription(String txtdecription) {
		this.txtdecription = txtdecription;
	}



	/**
	 * @return the parameters
	 */
	public List<GabParameter> getParameters() {
		return parameters;
	}



	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(List<GabParameter> parameters) {
		this.parameters = parameters;
	}



	/**
	 * @return the parameterDialog
	 */
	public ParameterDialog getParameterDialog() {
		return parameterDialog;
	}



	/**
	 * fichier de definition
	 */
	@Override
	public String getFileDefinition() {

		// On retourne le chemin du fichier de definition
		return "/views/parameter/ParameterListPanel.xhtml";
	}

	/**
	 * Nom
	 */
	@Override
	public String getName() {
		// On retourne le nom
		return "parameterListPanel";
	}

	/**
	 * tittre
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "parameterListPanel.title";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.IViewComponent#getChildDialogDefinition()
	 */
	@Override
	public String getChildDialogDefinition() {

		// On retourne le chemin du fichier de definition
		return "/views/parameter/ParameterDialog.xhtml";

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
		parameterDialog = null;
		txtCode ="";
		txtdecription = "";
		txtparam ="";
		parameters.clear();

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
			parameters = new ArrayList<GabParameter>();

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
		GabParameter p = child.getCurrentObject();

		// Si l'Objet courant est null
		if(p == null) return;
		
		if(p.getCode().equalsIgnoreCase("SVFE")){
			p.setValue(Encrypter.getInstance().encryptText(p.getValue()));
		}
		if(p.getCode().equalsIgnoreCase("SVBO")){
			p.setValue(Encrypter.getInstance().encryptText(p.getValue()));
		}
		if(p.getCode().equalsIgnoreCase("SVCG")){
			p.setValue(Encrypter.getInstance().encryptText(p.getValue()));
		}
		if(p.getCode().equalsIgnoreCase("DELTA")){
			p.setValue(Encrypter.getInstance().encryptText(p.getValue()));
		}

		// Si on est en mode creation
		if(mode.equals(DialogFormMode.CREATE)) {

			// Ajout dans la liste
			this.parameters.add(p);

		} else if(mode.equals(DialogFormMode.UPDATE)) {

			// Recherche de l'Objet
			int index = this.parameters.indexOf(p);

			// Si l'objet n'existe pas
			if(index < 0) return;

			// On met a jour
			this.parameters.set(index, p);

		}	

	}


	/**
	 * Methode de recherche
	 */
	public void processSearch() {

		try {

			// Methode de filtre
			this.parameters = ViewHelper.atm.filterParameter(txtCode, txtparam, txtdecription);

			for(GabParameter p : this.parameters){
				if(p.getCode().equalsIgnoreCase("SVFE")){
					p.setValue(Encrypter.getInstance().encryptText(p.getValue()));
				}
				if(p.getCode().equalsIgnoreCase("SVBO")){
					p.setValue(Encrypter.getInstance().encryptText(p.getValue()));
				}
				if(p.getCode().equalsIgnoreCase("SVCG")){
					p.setValue(Encrypter.getInstance().encryptText(p.getValue()));
				}
				if(p.getCode().equalsIgnoreCase("DELTA")){
					p.setValue(Encrypter.getInstance().encryptText(p.getValue()));
				}
			}

			// Si la liste est nulle
			if(this.parameters == null) this.parameters = new ArrayList<GabParameter>();

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
			parameterDialog = new ParameterDialog(DialogFormMode.CREATE, new GabParameter(), this);
			// Ouverture
			this.parameterDialog.setOpen(true);
			this.parameterDialog.open();

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
		if(index < 0 || index >= this.parameters.size()) {

			// On sort
			return;
		}

		GabParameter p = this.parameters.get(index);

		try {

			// Instanciation 
			p = ViewHelper.dao.findByPrimaryKey(GabParameter.class, p.getId(), null);
			/**if(p.getCode().equalsIgnoreCase("SVFE")){
				p.setValue(Encrypter.getInstance().decryptText(p.getValue()));
			}
			if(p.getCode().equalsIgnoreCase("SVBO")){
				p.setValue(Encrypter.getInstance().decryptText(p.getValue()));
			}
			if(p.getCode().equalsIgnoreCase("SVCG")){
				p.setValue(Encrypter.getInstance().decryptText(p.getValue()));
			}
			if(p.getCode().equalsIgnoreCase("DELTA")){
				p.setValue(Encrypter.getInstance().decryptText(p.getValue()));
			}*/
			parameterDialog = new ParameterDialog(DialogFormMode.UPDATE, p, this);

			// Ouverture
			parameterDialog.open();

		} catch (Exception e) {

			// Etat d'erreur
			this.error = true;

			// Traitement de l'exception
			ExceptionHelper.threatException(e);
		}
	}


}
