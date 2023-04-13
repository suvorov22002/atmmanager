package com.afb.portal.presentation.monitor;

import afb.dsi.dpd.portal.jpa.entities.User;

import com.afb.portal.presentation.models.AbstractDialog;
import com.afb.portal.presentation.tools.ExceptionHelper;
import com.afb.portal.presentation.tools.ViewHelper;

public class RapportJourMonitoring extends AbstractDialog{

	
	@Override
	protected void buildCurrentObject() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void disposeResourceOnClose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initComponents(){
		
		// TODO Auto-generated method stub
		try{
			User user = ViewHelper.getSessionUser();
			if(user != null )ViewHelper.IRapportJourMonitoring.process(user);
			
			// Information
			information = true;
			// Message d'information
			ExceptionHelper.showInformation("abstractdialog.operation.ok", this);
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
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

	@Override
	public String getFileDefinition() {
		// TODO Auto-generated method stub
		return "RapportJourMonitoring";
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "RapportJourMonitoring";
	}

	@Override
	public String getChildDialogDefinition() {
		// TODO Auto-generated method stub
		return "RapportJourMonitoring";
	}

}
