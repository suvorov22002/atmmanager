package com.afb.portal.presentation.monitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.parameter.AtmIncident;
import com.afb.portal.presentation.models.AbstractPanel;

/**
 * StatGabIncidentDialog
 * @author Owner
 * @version 1.0
 */
public class StatGabIncidentDialog extends AbstractPanel{

	private Date datedebut = new Date();

	private Date datefin = new Date();

	private Atm atm = null;

	private String codeAtm = "";
	
	private List<AtmIncident> values = new ArrayList<AtmIncident>();

	/**
	 * Map d employee
	 */
	private Map<String, Atm> mapAtm = new HashMap<String, Atm>();

	/**
	 * 
	 */
	private List<Atm> allAtm = new ArrayList<Atm>();

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.IPanel#getFileDefinition()
	 */
	@Override
	public String getFileDefinition() {
		// TODO Auto-generated method stub
		return "StatGabIncidentDialog.xhtml";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.IPanel#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "StatGabIncidentDialog";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.IPanel#getTitle()
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "StatGabIncident";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.IViewComponent#getChildDialogDefinition()
	 */
	@Override
	public String getChildDialogDefinition() {
		// TODO Auto-generated method stub
		return "";
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
	 * @return the atm
	 */
	public Atm getAtm() {
		return atm;
	}

	/**
	 * @param atm the atm to set
	 */
	public void setAtm(Atm atm) {
		this.atm = atm;
	}

	/**
	 * @return the codeAtm
	 */
	public String getCodeAtm() {
		return codeAtm;
	}

	/**
	 * @param codeAtm the codeAtm to set
	 */
	public void setCodeAtm(String codeAtm) {
		this.codeAtm = codeAtm;
	}
	
	
	

}
