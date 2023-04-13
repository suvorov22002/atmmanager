package com.afb.portal.dao.exception;

import afb.dsi.dpd.portal.jpa.tools.PortalException;



/**
 * Exception levee lorsqu'une entite n'a pas ete retrouvee dans le referentiel de donnees
 * @author Francis K
 * @version 1.0
 */
public class EntityNotFoundException extends PortalException{


	/**
	 * ID Genere par Eclipse
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Code de l'entite inexistante
	 */
	private Object code;
	
	/**
	 * Constructeur avec initialisation du code de l'entite inexistante
	 * @param code	Code de l'entite inexistante
	 */
	public EntityNotFoundException(Object code) {
		
		// Initialisation parente
		super("EntityNotFoundException.message");
		
		// Initialisation de l'ID de l'Objet
		this.code = code;
	}
	
	/**
	 * Constructeur avec initialisation du message et du code de l'entite inexistante
	 * @param message Message d'erreur
	 * @param code	Code de l'entite inexistante
	 */
	public EntityNotFoundException(String message, Object code) {
		
		// Initialisation parente
		super(message);
		
		// Initialisation de l'ID de l'Objet
		this.code = code;
	}
	/**
	 * Methode d'obtention du Code de l'entite inexistante
	 * @return Code de l'entite inexistante
	 */
	public Object getCode() {
		return code;
	}

	/**
	 * Methode de mise a jour du Code de l'entite inexistante
	 * @param code Code de l'entite inexistante
	 */
	public void setCode(Object code) {
		this.code = code;
	}
	
}
