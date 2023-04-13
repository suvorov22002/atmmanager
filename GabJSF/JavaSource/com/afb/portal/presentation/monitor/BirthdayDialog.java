package com.afb.portal.presentation.monitor;

import java.util.ArrayList;
import java.util.List;

import com.afb.portal.jpa.alerte.Birthday;
import com.afb.portal.jpa.gab.parameter.GabParameter;
import com.afb.portal.presentation.models.AbstractPanel;
import com.afb.portal.presentation.tools.ViewHelper;
import com.afb.portal.webservcice.entities.SMSWeb;

/**
 * BirthdayDialog
 * @author Owner
 * @version 1.0
 */
public class BirthdayDialog extends AbstractPanel{

	// Liste
	private List<Birthday> listes  = new ArrayList<Birthday>();
	
	public static final String moduleCode = "GABM_001";

	public static final String moduleName ="MonitoringATM";


	public BirthdayDialog() {
		super();
	}

	/**
	 * @return the listes
	 */
	public List<Birthday> getListes() {
		return listes;
	}

	/**
	 * @param listes the listes to set
	 */
	public void setListes(List<Birthday> listes) {
		this.listes = listes;
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.AbstractPanel#initComponents()
	 */
	@Override
	protected void initComponents() {
		// TODO Auto-generated method stub
		super.initComponents();

		listes = ViewHelper.dao.filter(Birthday.class,null,null,null,null,0,-1);

	}

	public void saveMAJ(){

		for(Birthday val : listes )ViewHelper.dao.update(val);		
	}

	
	public void sendMessage(){
		
		// Message 
		String message = "";
		GabParameter parameter = ViewHelper.atm.findParameter("GABM_MSG");
		if(parameter != null ){
			if(parameter.getValue() != null && !parameter.getValue().trim().isEmpty() ){
				message = parameter.getValue(); 
			}
		}

		if(message.trim().isEmpty()) return;
		for(Birthday val : listes ){
			String phone = val.getPhone();
			if(phone != null && !phone.trim().isEmpty() && phone.trim().length()>=9){
				SMSWeb sms = new SMSWeb(moduleCode, moduleName, "ROOT", message, phone);
				ViewHelper.dao.save(sms);
			}
			ViewHelper.dao.update(val);		
		}
	}

	@Override
	public String getFileDefinition() {
		// TODO Auto-generated method stub
		return "/views/parameter/BirthdayDialog.xhtml";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "BirthdayDialog";
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Birthday";
	}

	@Override
	public String getChildDialogDefinition() {
		// TODO Auto-generated method stub
		return "";
	}

}
