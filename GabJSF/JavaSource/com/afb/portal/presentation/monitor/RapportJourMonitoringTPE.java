package com.afb.portal.presentation.monitor;

import afb.dsi.dpd.portal.jpa.entities.User;

import com.afb.portal.presentation.models.AbstractDialog;
import com.afb.portal.presentation.tools.ExceptionHelper;
import com.afb.portal.presentation.tools.ViewHelper;

/**
 * 
 * @author Owner
 *
 */
public class RapportJourMonitoringTPE extends AbstractDialog{

	
	@Override
	public void initComponents(){
		
		// TODO Auto-generated method stub
		try{
			User user = ViewHelper.getSessionUser();
			if(user != null)ViewHelper.iRapportJourMonitoringTPE.process(user);
			
			// Information
			information = true;
			// Message d'information
			ExceptionHelper.showInformation("abstractdialog.operation.ok", this);
			
		}catch(Exception e){
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
		
	
	@Override
	public String getFileDefinition() {
		// TODO Auto-generated method stub
		return "RapportJourMonitoringTPE";
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "RapportJourMonitoringTPE";
	}

	@Override
	public String getChildDialogDefinition() {
		// TODO Auto-generated method stub
		return "RapportJourMonitoringTPE";
	}


	@Override
	protected void buildCurrentObject() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void disposeResourceOnClose() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void validate() {
		// TODO Auto-generated method stub
		
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
