package com.afb.portal.presentation.monitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.afb.portal.jpa.gab.parameter.AtmIncident;
import com.afb.portal.jpa.gab.parameter.TypeIncident;
import com.afb.portal.presentation.models.AbstractPanel;
import com.afb.portal.presentation.models.reportViewer.ReportViewer;
import com.afb.portal.presentation.tools.ExceptionHelper;
import com.afb.portal.presentation.tools.ViewHelper;

/**
 * StatIncidentGabDialog
 * @author Owner
 * @version 1.0
 */
public class StatIncidentGabDialog extends AbstractPanel{

	private Date datedebut = new Date();

	private Date datefin = new Date();

	private TypeIncident typInci = null;

	private String codeTypeInci = "";
	
	private List<AtmIncident> values = new ArrayList<AtmIncident>();
	
	/**
	 * Editeur d etat
	 */
	private ReportViewer reportViewer = null;

	/**
	 * Map d employee
	 */
	private Map<String, TypeIncident> mapTypeInci = new HashMap<String, TypeIncident>();

	/**
	 * 
	 */
	private List<TypeIncident> allTypeinci = new ArrayList<TypeIncident>();

	@Override
	public String getFileDefinition() {
		// TODO Auto-generated method stub
		return "/StatIncidentGabDialog.xhtml";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "StatIncidentGabDialog";
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "StatIncident";
	}

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
	 * @return the typInci
	 */
	public TypeIncident getTypInci() {
		return typInci;
	}

	/**
	 * @param typInci the typInci to set
	 */
	public void setTypInci(TypeIncident typInci) {
		this.typInci = typInci;
	}

	/**
	 * @return the codeTypeInci
	 */
	public String getCodeTypeInci() {
		return codeTypeInci;
	}

	/**
	 * @param codeTypeInci the codeTypeInci to set
	 */
	public void setCodeTypeInci(String codeTypeInci) {
		this.codeTypeInci = codeTypeInci;
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.portal.presentation.models.AbstractPanel#initComponents()
	 */
	@Override
	protected void initComponents() {

		try{

			allTypeinci = ViewHelper.dao.filter(TypeIncident.class,null,null,null,null,0,-1);
			for(TypeIncident elem : allTypeinci)mapTypeInci.put(elem.getCode(),elem);

		}catch(Exception e){
			e.printStackTrace();
			this.error = true;
			ExceptionHelper.threatException(e);
		}

	}
	
	/**
	 * 
	 */
	public void processFilter(){

		try{

			values = ViewHelper.atm.filterAtmIncident(datedebut, datefin, codeTypeInci);
			
		}catch(Exception e){
			e.printStackTrace();
			this.error = true;
			ExceptionHelper.threatException(e);
		}

	}
	

	/**
	 * mthode de seelction du subjection box 
	 */	
	public List<TypeIncident> chargeEmployee(Object suggest){

		String pref = (String)suggest;
		ArrayList<TypeIncident> result = new ArrayList<TypeIncident>();

		// Recupere la liste des employees
		Iterator<TypeIncident> iterator = allTypeinci.iterator();

		while(iterator.hasNext()){
			// Position sur le premier element de la liste
			TypeIncident elem = ((TypeIncident) iterator.next());

			// filtre en faonction des carrastesre sasies
			if ((elem.getLibelle() != null && elem.getLibelle().toLowerCase().indexOf(pref.toLowerCase()) == 0) || "".equals(pref)){
				result.add(elem);
			}
		}

		// retourne le resultat
		return result;
	}

	
	public void processPrint(){

		try{

			/**
			// Affichage du rapport de traitement 
			List<Report> repports = new ArrayList<Report>();
			String titlle = "Rapport de Traitement des Fichiers Small World";
			String subtitlle = "Détails du traitement Small World";

			report.setTitlle(titlle);
			report.setSubtitlle(subtitlle);
			report.setUser(login);
			repports.add(report);

			reportViewer = new ControlReportViewer(repports,"Traitement.jasper", null, "mnuItemSmallWorld.label", this,"/views/repport/ReportSmallWorld.xhtml");
			reportViewer.viewReport();
			return;*/

		}catch (Exception e){

			e.printStackTrace();
			// Etat d'erreur
			this.error = true;
			ExceptionHelper.threatException(e);
		
		}
		
	}


}
