package com.afb.portal.presentation.monitor.parameter;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.afb.portal.jpa.gab.parameter.ParameterSauv;
import com.afb.portal.presentation.models.AbstractPanel;
import com.afb.portal.presentation.tools.ExceptionHelper;
import com.afb.portal.presentation.tools.ViewHelper;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

/**
 * ParameterSauvDialog
 * @author Owner
 * @version 1.0
 */
public class ParameterSauvDialog extends AbstractPanel{


	/**
	 * Objet courant de la fenetre
	 */
	private ParameterSauv currentObject = new ParameterSauv();
	
	/**
	 * contructeur
	 */
	public ParameterSauvDialog() {
		super();
	}
	
	

	/**
	 * @return the currentObject
	 */
	public ParameterSauv getCurrentObject() {
		return currentObject;
	}



	/**
	 * @param currentObject the currentObject to set
	 */
	public void setCurrentObject(ParameterSauv currentObject) {
		this.currentObject = currentObject;
	}



	/**
	 * fichier de definition
	 */
	@Override
	public String getFileDefinition() {

		// On retourne le chemin du fichier de definition
		return "/views/parameter/ParameterSauvDialog.xhtml";
	}

	/**
	 * Nom
	 */
	@Override
	public String getName() {
		// On retourne le nom
		return "ParameterSauvDialog";
	}

	/**
	 * tittre
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "ParameterSauvDialog.title";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.IViewComponent#getChildDialogDefinition()
	 */
	@Override
	public String getChildDialogDefinition() {

		// On retourne le chemin du fichier de definition
		return "";
		
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
			
			RestrictionsContainer rc = RestrictionsContainer.getInstance();
			rc.add(Restrictions.eq("id",ParameterSauv.SAUVENGARDE));
			
			// Methode de filtre
			List<ParameterSauv> values = ViewHelper.dao.filter(ParameterSauv.class,null,rc,null,null,0,-1);
			
			currentObject = new ParameterSauv();
			if(values != null ){
				if(!values.isEmpty()){
					currentObject = values.get(0);
				}
			}
			
		}catch(Exception e){

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
			ViewHelper.dao.update(currentObject);
			
			// Information
			information = true;
			// Message d'information
			ExceptionHelper.showInformation("abstractdialog.operation.ok", this);
			
		} catch (Exception e) {

			// Etat d'erreur
			this.error = true;

			// Traitement de l'exception
			ExceptionHelper.threatException(e);
		}

	}
	
	
	
}
