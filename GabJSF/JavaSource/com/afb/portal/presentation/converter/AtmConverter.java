package com.afb.portal.presentation.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.presentation.tools.ViewHelper;

/**
 * 
 * @author Owner
 * 
 */
public class AtmConverter implements Converter{
	
	/*
	 * (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String submittedValue) {
		
		// la valeur est nulle on sort
		if(submittedValue == null || submittedValue.trim().isEmpty())	return null; 
		
		// Recherche de l'objet possedant l'id passe en parametre
		Atm val = ViewHelper.atm.findByPrimaryKey(Atm.class, Long.valueOf(submittedValue), null);
		
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
		if(arg2 instanceof Atm){
			
			// Recuperation de l'utilisateur
			Atm val = (Atm)arg2;
			
			// on retourne le nom de l'utilisateur
			return val.getTid().toString();
		}
		
		// On Retourne null
		return null;
		
	}

}
