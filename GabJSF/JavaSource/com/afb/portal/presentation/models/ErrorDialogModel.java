/**
 * 
 */
package com.afb.portal.presentation.models;

import com.afb.portal.presentation.tools.ExceptionHelper;

/**
 * Classe de modele de la boite de dialogue d'erreur
 * @author Francis K
 * @version 1.0
 */
public class ErrorDialogModel {
	
	/**
	 *  Exception survenue
	 */
	private Throwable exception = null;
	
	/**
	 * Constructeur par defaut
	 */
	public ErrorDialogModel() {}
	
	/**
	 * Constructeur avec initialisation des parametres
	 * @param exception	Exception survenue
	 */
	public ErrorDialogModel(Throwable exception) {
		
		// On positionne l'exception
		this.exception = exception;
	}
	
	/**
	 * Methode d'obtention de l'Exception survenue
	 * @return Exception survenue
	 */
	public Throwable getException() {
		return exception;
	}

	/**
	 * Methode de mise a jour de l'Exception survenue
	 * @param exception Exception survenue
	 */
	public void setException(Throwable exception) {
		this.exception = exception;
	}

	/**
	 * Mthode d'obtention du message resume
	 * @return	Message resume
	 */
	public String getExceptionMessageSumary() {
		
		// Si l'exception est nulle
		if(exception == null) return "";
		
		// On retourne le message resume
		return ExceptionHelper.getExceptionMessageSummary(exception);
	}
	
	/**
	 * Mthode d'obtention du message detaille
	 * @return	Message detaille
	 */
	public String getExceptionMessageDetails() {
		
		// Si l'exception est nulle
		if(exception == null) return "";
		
		// On retourne le message resume
		return ExceptionHelper.getExceptionMessageDetails(exception);
	}
	
	/**
	 * Methode permettant de savoir si cette boite est ouverte
	 * @return	Etat d'ouverture
	 */
	public boolean isOpen() {
		
		// On retourne l'etat
		return exception != null;
	}
	
	/**
	 * methode d'ouverture de la boite de dialogue de gestion des erreurs
	 */
	public void openErrorDialog(Throwable exception) {
		
		// On positionne l'exception
		this.exception = exception;
	
	}
	
	/**
	 * Methode de fermeture de la boite
	 */
	public void closeErrorDialog() {
		
		// on annule l'exception
		this.exception = null;
	}
}