package com.afb.portal.presentation.monitor;

import java.util.Date;

import com.afb.portal.presentation.models.AbstractPanel;
import com.afb.portal.presentation.tools.ExceptionHelper;
import com.afb.portal.presentation.tools.ViewHelper;

/**
 * Boite de Dialogue de Creation dun parametre
 * @author Francis
 * @version 1.0
 */
public class SauvegardeDialog extends AbstractPanel{

	private Date  datedebut;

	private Date datefin;

	public SauvegardeDialog() {
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




	/* (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IDialog#getFileDefinition()
	 */
	@Override
	public String getFileDefinition() {

		// On retourne le chemin du fichier de definition
		return "/views/parameter/SauvegardeDialog.xhtml";

	}

	/* (non-Javadoc)
	 * @see com.afrikbrain.pme.genezis.presentation.models.IDialog#getTitle()
	 */
	@Override
	public String getTitle() {

		// On retourne le Titre
		return "SauvegardeDialog.tittle";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.AbstractPanel#disposeResourceOnClose()
	 */
	@Override
	protected void disposeResourceOnClose() {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.AbstractPanel#initComponents()
	 */
	@Override
	public void initComponents() {
		// TODO Auto-generated method stub

		try{


		}catch(Exception e){

			e.printStackTrace();
			// Etat d'erreur
			this.error = true;
			// Traitement de l'exception
			ExceptionHelper.threatException(e);

		}

	}


	public void processSauvegarde(){

		try{	

			ViewHelper.ISauvegardeMonitoringWorker.process(datedebut, datefin);
			
			// Information
			information = true;
			// Message d'information
			ExceptionHelper.showInformation("abstractdialog.operation.ok", this);

		}catch(Exception e){
			e.printStackTrace();
			this.error = true;
			ExceptionHelper.threatException(e);
		}

	}


	@Override
	public String getChildDialogDefinition() {
		// TODO Auto-generated method stub
		return "";
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SauvegardeDialog";
	}

}
