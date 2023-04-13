package com.afb.portal.jpa.exception;


/**
 * Classe representant l'exception de base
 * @author Francis K
 * @version 1.0
 */
public class PortalException extends RuntimeException {


	/**
	 * ID Genere Par Eclipse
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructeur par defaut
	 */
	public PortalException() {
		
		// Initialisation du Parent
		super("PortalException.generic.exception");
	}
	
	/**
	 * Constructeur avec initialisation du message
	 * @param message	Message initial
	 */
	public PortalException(String message) {
		
		// Initialisation du Parent
		super(message);
	}

	/**
	 * Constructeur avec initialisation du message et de la cause
	 * @param message	Message initial
	 * @param cause Cause de l'exception
	 */
	public PortalException(String message, Throwable cause) {
		
		// Initialisation du Parent
		super(message, cause);
	}
	
}
