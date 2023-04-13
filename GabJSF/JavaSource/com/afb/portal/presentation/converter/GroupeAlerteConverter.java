package com.afb.portal.presentation.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.afb.portal.jpa.gab.parameter.GroupeAlerte;
import com.afb.portal.presentation.tools.ViewHelper;

/**
 * GroupeAlerteConverter
 * @author Francis
 * @version 1.0
 */
public class GroupeAlerteConverter implements Converter{

	/*
	 * (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String submittedValue) {
		
		// la valeur est nulle on sort
		if(submittedValue == null || submittedValue.trim().isEmpty())	return null; 
		
		// Recherche de l'objet possedant l'id passe en parametre
		GroupeAlerte val = ViewHelper.atm.findByPrimaryKey(GroupeAlerte.class, Long.valueOf(submittedValue), null);
		
		// On retourne l'objet
		return val;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2){
		
		// Si l'objet est null on sort
		if(arg2 == null) return " ";
		
		// Si l'objet est une instance de SXUser
		if(arg2 instanceof GroupeAlerte){
			
			// Recuperation de l'utilisateur
			GroupeAlerte val = (GroupeAlerte)arg2;
			
			// on retourne le nom de l'utilisateur
			return val.getId().toString();
		}
		
		// On Retourne null
		return null;
		
	}
	
}
