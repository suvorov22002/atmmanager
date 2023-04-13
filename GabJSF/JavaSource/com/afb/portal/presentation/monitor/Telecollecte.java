package com.afb.portal.presentation.monitor;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import afb.dsi.dpd.portal.jpa.entities.User;

import com.afb.portal.presentation.models.AbstractDialog;
import com.afb.portal.presentation.models.DialogFormMode;
import com.afb.portal.presentation.models.IDialog;
import com.afb.portal.presentation.tools.ExceptionHelper;
import com.afb.portal.presentation.tools.ViewHelper;

public class Telecollecte extends AbstractDialog{

	private Date datedebut = DateUtils.addDays(new Date(), -1);
	private Date datefin = new Date();

	@Override
	public void initComponents(){

		datedebut = DateUtils.addDays(new Date(), -1);
		datefin = new Date();
	}
	
	
	public void process(){

		// TODO Auto-generated method stub
		try{
			User user = ViewHelper.getSessionUser();
			if(user != null)ViewHelper.telecollecte.process(user,datedebut,datefin);

			// Information
			information = true;
			// Message d'information
			ExceptionHelper.showInformation("abstractdialog.operation.ok", this);

		}catch(Exception e){
			// TODO: handle exception
			e.printStackTrace();
		}
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


	@Override
	public String getFileDefinition() {
		// TODO Auto-generated method stub
		return "/views/parameter/TelecollecteDialog.xhtml";
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Telecollecte";
	}

	@Override
	public String getChildDialogDefinition() {
		// TODO Auto-generated method stub
		return "Telecollecte";
	}

	@Override
	protected void disposeResourceOnClose() {
		// TODO Auto-generated method stub
		return;
	}
	
	@Override
	public void validateSubDialogClosure(IDialog child, DialogFormMode mode,
			boolean wellClose) {
		// TODO Auto-generated method stub
		return;
	}


	@Override
	protected void buildCurrentObject() {
		// TODO Auto-generated method stub
		return;
	}


	@Override
	protected void validate() {
		// TODO Auto-generated method stub
		return;
	}


	@Override
	public boolean checkBuildedCurrentObject() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public <T> T getCurrentObject() {
		// TODO Auto-generated method stub
		return null;
	}


}
