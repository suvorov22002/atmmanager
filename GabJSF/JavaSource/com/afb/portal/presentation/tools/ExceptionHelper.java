/**
 * 
 */
package com.afb.portal.presentation.tools;

import javax.ejb.EJBAccessException;
import javax.ejb.EJBException;
import javax.faces.context.FacesContext;

import com.afb.portal.jpa.exception.PortalException;
import com.afb.portal.presentation.models.ClientArea;
import com.afb.portal.presentation.models.IViewComponent;
import com.yashiro.persistence.utils.annotations.validator.engine.exceptions.BaseJPersistenceUtilsException;
import com.yashiro.persistence.utils.annotations.validator.engine.exceptions.DAOValidationException;

/**
 * Classe d'aide pour la gestion des exceptions
 * @author Francis K
 * @version 1.0
 */
public class ExceptionHelper {
	
	/**
	 * Methode permettant d'obtenir le message d'erreur internationnalise
	 * @param exception	Exception survenue
	 * @return	Message d'erreur internationnalise
	 */
	public static String getExceptionMessageSummary(Throwable exception) {
		
		// Si c'est une exception de sms
		if(exception instanceof EJBAccessException) {
			
			// On retourne le message internationnalise
			return Multilangue.getMessage("genezisexceptionhelper.ejbaccessexception");
			
		} else if(exception instanceof EJBException) {
			
			// On caste en EJBException
			EJBException ejbException = (EJBException) exception;
			
			// On récupère la cause
			Exception cause = ejbException.getCausedByException();
			
			// Si la cause n'est pas une instance de ImporterExceptionBase
			if(cause instanceof BaseJPersistenceUtilsException) {
				
				// On caste
				BaseJPersistenceUtilsException jpuException = (BaseJPersistenceUtilsException) cause;
				
				if(jpuException instanceof DAOValidationException) Multilangue.getMessage(jpuException.getMessage(), ((DAOValidationException)jpuException).getParameters()); 
				
				// On retourne le message internationnalise
				return Multilangue.getMessage(jpuException.getMessage());				
				
			} else if(cause instanceof PortalException) {
				
				// On caste
				@SuppressWarnings("unused")
				PortalException ebpException = (PortalException) cause;
				
				// On retourne le message internationnalise
				return Multilangue.getMessage(cause.getMessage());
				
			} else {
				
				// On retourne le message internationnalise
				return Multilangue.getMessage(cause.getMessage());
			}
			
		} else {
			
			// On retourne le message internationnalise
			return Multilangue.getMessage(exception.getMessage());
		}
	}
	
	/**
	 * Methode permettant d'obtenir la pile d'erreurs survenue
	 * @param exception	Exception survenue
	 * @return	Message Pile d'erreurs
	 */
	public static String getExceptionMessageDetails(Throwable exception) {
		
		// Le Buffer
		StringBuffer buffer = new StringBuffer();
		
		// On Y met le message internationnalise
		buffer.append(getExceptionMessageSummary(exception));

		// On Y met le message internationnalise
		buffer.append("\n");
		
		// Obtention de la pile des exception
		StackTraceElement[] elements = exception.getStackTrace();
		
		// Si la liste n'est pas vide
		if(elements != null && elements.length > 0) {
			
			// Parcours de la liste
			for(int i = 0; i < elements.length; i++) {
				
				// Un Element
				StackTraceElement element = elements[i];
				
				// Ajout du Message
				buffer.append(element.toString());
			}
		}
		
		// On retourne la chaine
		return buffer.toString();
	}
	
	/**
	 * Methode de traitement d'une exception
	 * @param exception	Exception survenue
	 */
	public static void threatException(Throwable exception) {
		
		// Chargement du Bean de gestion de la zone Cliente
		ClientArea ca = (ClientArea) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("clientArea");
		
		// Si le gestionnaire est non null
		if(ca != null) {
			
			// Ouverture de la fenetre d'erreur
			ca.getErrorDialogModel().openErrorDialog(exception);
		}
	}
	
	
	/**
	 * Methode permettant d'afficher un message d'information
	 * @param title	Titre de la fenetre
	 * @param message	Message a afficher
	 */
	public static void showError(String title, String message) {
		
		// Chargement du Bean de gestion de la zone Cliente
		ClientArea ca = (ClientArea) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("clientArea");
		
		// Si le gestionnaire est non null
		if(ca != null) {
			
			// Ouverture de la fenetre d'erreur
			ca.getInformationDialogModel().showErrorMessage(title, message);
		}
	}
	
	/**
	 * Methode permettant d'afficher un message d'information
	 * @param title	Titre de la fenetre
	 * @param message	Message a afficher
	 */
	public static void showError(String message, Object...parameters) {
		
		// Chargement du Bean de gestion de la zone Cliente
		ClientArea ca = (ClientArea) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("clientArea");
		
		// Si le gestionnaire est non null
		if(ca != null) {
			
			// Ouverture de la fenetre d'erreur
			ca.getInformationDialogModel().showErrorMessage(message, parameters);
		}
	}
	
	/**
	 * Methode permettant d'afficher un message d'information
	 * @param title	Titre de la fenetre
	 * @param message	Message a afficher
	 */
	public static void showError(String message, IViewComponent component, Object...parameters) {
		
		// Chargement du Bean de gestion de la zone Cliente
		ClientArea ca = (ClientArea) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("clientArea");
		
		// Si le gestionnaire est non null
		if(ca != null) {
			
			// Ajout du Listener
			ca.getInformationDialogModel().addListener(component);
			
			// Ouverture de la fenetre d'erreur
			ca.getInformationDialogModel().showErrorMessage(message, parameters);
		}
	}
	
	
	/**
	 * Methode permettant d'afficher un message d'information
	 * @param title	Titre de la fenetre
	 * @param message	Message a afficher
	 */
	public static void showInformation(String title, String message) {
		
		// Chargement du Bean de gestion de la zone Cliente
		ClientArea ca = (ClientArea) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("clientArea");
		
		// Si le gestionnaire est non null
		if(ca != null) {
			
			// Ouverture de la fenetre d'erreur
			ca.getInformationDialogModel().showInformationMessage(title, message);
		}
	}
	
	/**
	 * Methode permettant d'afficher un message d'information
	 * @param title	Titre de la fenetre
	 * @param message	Message a afficher
	 */
	public static void showInformation(String message, Object...parameters) {
		
		// Chargement du Bean de gestion de la zone Cliente
		ClientArea ca = (ClientArea) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("clientArea");
		
		// Si le gestionnaire est non null
		if(ca != null) {
			
			// Ouverture de la fenetre d'erreur
			ca.getInformationDialogModel().showInformationMessage(message, parameters);
		}
	}
	
	/**
	 * Methode permettant d'afficher un message d'information
	 * @param title	Titre de la fenetre
	 * @param message	Message a afficher
	 */
	public static void showInformation(String message, IViewComponent component, Object...parameters) {
		
		// Chargement du Bean de gestion de la zone Cliente
		ClientArea ca = (ClientArea) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("clientArea");
		
		// Si le gestionnaire est non null
		if(ca != null) {
			
			// Ajout du Listener
			ca.getInformationDialogModel().addListener(component);
			
			// Ouverture de la fenetre d'erreur
			ca.getInformationDialogModel().showInformationMessage(message, parameters);
		}
	}
	
	/**
	 * boite de dialogue de confirmation
	 * @param message
	 * @param parameters
	 */
    public static void showConfirmation(String message, Object...parameters) {
		
    	// Chargement du Bean de gestion de la zone Cliente
		ClientArea ca = (ClientArea) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("clientArea");
		
		// Si le gestionnaire est non null
		if(ca != null) {
			
			// Ouverture de la fenetre d'erreur
			ca.getConfirmationDialogModel().showInformationMessage(message, parameters);
		}
	}
    
    /**
	 * boite de dialogue de confirmation
	 * @param message
	 * @param parameters
	 */
    public static void showConfirmation(String message1,String message2, Object...parameters) {
		
    	// Chargement du Bean de gestion de la zone Cliente
		ClientArea ca = (ClientArea) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("clientArea");
		
		// Si le gestionnaire est non null
		if(ca != null) {
			
			// Ouverture de la fenetre d'erreur
			ca.getConfirmationDialogModel().showInformationMessage(message1,message2, parameters);
		}
	}
    
}
