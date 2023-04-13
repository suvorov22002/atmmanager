/**
 * 
 */
package com.afb.portal.presentation.models;

import java.util.ArrayList;
import java.util.List;

import com.afb.portal.presentation.tools.Multilangue;

/**
 * Modele de la boite de dialogue d'information
 * @author Francis K
 * @version 1.0
 */
public class InformationDialogModel {
	
	/**
	 * Liste des modeles a informer lors de la fermeture de la boite de dialogue d'information
	 */
	private List<IViewComponent> onCloseListeners = new ArrayList<IViewComponent>();
	
	/**
	 * Message d'information
	 */
	private String message = "";
	
	/**
	 * Chaine de rerender
	 */
	private String rerender = "Nothing";
	
	/**
	 * Script a executer
	 */
	private String javaScript = "";
	
	/**
	 * Titre de la boite d'informations
	 */
	private String title = "information.dialog.title";
	
	/**
	 * Etat d'ouverture de la boite
	 */
	private boolean open;
	
	/**
	 * Determine si le bouton CANCEL doit etre affiche
	 */
	private boolean Error = false;
	
	private boolean Information = false;
	
	/**
	 * Constructeur par defaut
	 */
	public InformationDialogModel() {}
	
	/**
	 * Methode d'obtention du Titre de la boite d'informations
	 * @return Titre de la boite d'informations
	 */
	public String getTitle() {
		return title;
	}
	
	
	
	/**
	 * @return the error
	 */
	public boolean isError() {
		return Error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(boolean error) {
		Error = error;
	}

	/**
	 * @return the information
	 */
	public boolean isInformation() {
		return Information;
	}

	/**
	 * @param information the information to set
	 */
	public void setInformation(boolean information) {
		Information = information;
	}

	/**
	 * Methode permettant d'obtenir le nom du fichier de definition
	 * @return	Nom du fichier de definition
	 */
	public String getFileDefinition() {
		
		// On retourne le chemin
		return "/views/home/InformationDialog.xhtml";
	}
	
	/**
	 * Methode d'obtention du Message d'information
	 * @return Message d'information
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Methode d'obtention de l'Etat d'ouverture de la boite
	 * @return Etat d'ouverture de la boite
	 */
	public boolean isOpen() {
		return open;
	}
	
	/**
	 * Methode de fermeture de la boite
	 */
	public void close() {
		
		this.open = false;
		
		// On reinitialise les champs
		title = "information.dialog.title";
		message = "";
		
		// Si la liste est vide
		if(this.onCloseListeners.size() == 0) rerender = "Nothing";
		
		else {

			// Un Buffer
			StringBuffer buffer = new StringBuffer();
			
			// Buffer de Script
			StringBuffer scriptBuffer = new StringBuffer();
			
			// Parcours
			for (IViewComponent onCloseListener : this.onCloseListeners) {
				
				// Si le buffer n'est plus vide
				if(buffer.length() > 0) buffer.append(",");
							
				// Si l'ID n'est pas vide
				if(onCloseListener.getViewAreaToRerender() != null && onCloseListener.getViewAreaToRerender().trim().length() > 0) buffer.append(onCloseListener.getViewAreaToRerender().trim());
				if(onCloseListener.getJSScriptOnComplete() != null && onCloseListener.getJSScriptOnComplete().trim().length() > 0) scriptBuffer.append(onCloseListener.getJSScriptOnComplete().trim());
			}
			
			// Rerender
			rerender = buffer.toString();
			
			// JS
			javaScript = scriptBuffer.toString();
			
		}
		
		// Signalisation
		for (IViewComponent onCloseListener : this.onCloseListeners) {
			
			// On signale
			onCloseListener.actionOnInformationDialogClose();
		}
		
		// On nettoie
		this.clearListeners();
	}
	
	/**
	 * Methode d'affichage du Message
	 * @param title	Titre de la boite
	 * @param message	Message a afficher
	 */
	public void showInformationMessage(String title, String message, Object...parameters) {
		
		// Initialisation
		javaScript = "";
		
		// On positionne le titre
		this.title = Multilangue.getMessage(title);
		
		Error = false;
		Information = true;
		
		// On positionne le message
		this.message = Multilangue.getMessage(message, parameters);
		
		if(this.message.equals(message) || this.message.contains("?") ){
			this.message = message;
		}
		
		// On ouvre
		this.open = true;
	}
	
	/**
	 * Methode d'affichage du Message
	 * @param title	Titre de la boite
	 * @param message	Message a afficher
	 */
	public void showErrorMessage(String title, String message, Object...parameters) {
		
		title = "errordialog.title";
				
		// Initialisation
		javaScript = "";
		
		Error = true;
		Information = false;
		
		// On positionne le titre
		this.title = Multilangue.getMessage(title);
		
		// On positionne le message
		this.message = Multilangue.getMessage(message, parameters);
		
		if(this.message.equals(message) || this.message.contains("?") ){
			this.message = message;
		}
		
		// On ouvre
		this.open = true;
	}
	
	
	/**
	 * Methode d'affichage du Message
	 * @param title	Titre de la boite
	 * @param message	Message a afficher
	 */
	public void showErrorMessage(String message, Object...parameters) {
		
		title = "errordialog.title";
				
		// Initialisation
		javaScript = "";
		
		Error = true;
		Information = false;
		
		// On positionne le titre
		this.title = Multilangue.getMessage(title);
		
		// On positionne le message
		this.message = Multilangue.getMessage(message, parameters);
		
		if(this.message.equals(message) || this.message.contains("?") ){
			this.message = message;
		}
		
		// On ouvre
		this.open = true;
	}
	
	/**
	 * Methode d'affichage du Message
	 * @param message	Message a afficher
	 */
	public void showInformationMessage(String message, Object...parameters) {

		// Initialisation
		javaScript = "";
		
		// On positionne le titre
		this.title = Multilangue.getMessage(title);
		
		Error = false;
		Information = true;
		
		// On positionne le message
		this.message = Multilangue.getMessage(message, parameters);
		
		if(this.message.equals(message) || this.message.contains("?") ){
			this.message = message;
		}
		
		// On ouvre
		this.open = true;
	}
	
	/**
	 * Methode d'ajout de listener
	 * @param component	Listener a ajouter
	 */
	public void addListener(IViewComponent component) {
		
		// Si le composant est null
		if(component == null) return;
		
		// Ajout
		this.onCloseListeners.add(component);
	}
	
	/**
	 * Methode de nettoyage de la liste des Listeners
	 */
	protected void clearListeners() {
		
		// On nettoie
		this.onCloseListeners.clear();
	}
	
	/**
	 * Methode permettant d'Obtenir la zone a reafficher
	 * @return Chaine representant les zones a reafficher
	 */
	public String getRerenderAreaID() {
		
		// On retourne la chaine
		return rerender;
	}
	
	/**
	 * Methode d'obtention du Script a executer
	 * @return Script a executer
	 */
	public String getJavaScript() {
		
		// On retourne la chaine de script
		return javaScript;
	}
}
