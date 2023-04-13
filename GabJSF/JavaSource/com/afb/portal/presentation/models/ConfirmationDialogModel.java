package com.afb.portal.presentation.models;

import java.util.ArrayList;
import java.util.List;

import com.afb.portal.presentation.tools.Multilangue;

/**
 * Boite de dialogue de confirmations
 * @author Francis
 * @version 1.0
 */
public class ConfirmationDialogModel {

	 public static int INFORMATION_DIALOG = 0;
		
		public static int WARNING_DIALOG = 1;
		
		public static int ERROR_DIALOG = -1;
			
		public static int YES_ANSWER = 1;
		
		public static int NO_ANSWER = 0;
		
		public static int CANCEL_ANSWER = -1;
		
		public static int YESNO_DIALOG = 2;
		
		public static int YESNOCANCEL_DIALOG = 3;

		/**
		 * Liste des modeles a informer lors de la fermeture de la boite de dialogue d'information
		 */
		private List<IViewComponent> onCloseListeners = new ArrayList<IViewComponent>();
		
		private IViewComponent parent;
		
		/**
		 * Message d'information
		 */
		private String message = "";
		
		/**
		 * Chaine de rerender
		 */
		private String rerender = "";

		/**
		 * Script a executer
		 */
		private String javaScript = "";
		
		/**
		 * Titre de la boite d'informations
		 */
		private String title = "ehr.confirmation.dialog.title";
		
		/**
		 * Etat d'ouverture de la boite
		 */
		private boolean open;
		
		/**
		 * Icone de la boite de message
		 */
		private String dialogBoxImage = "WarningDialogBoxIcon.png";
		
		/**
		 * Determine si le bouton NO doit etre affiche
		 */
		private boolean renderNoButton = false;
		
		/**
		 * Determine si le bouton CANCEL doit etre affiche
		 */
		private boolean renderCancelButton = false;
		
		/**
		 * Decision (bouton sur lequel on a clique)
		 */
		private int decision = YES_ANSWER;
		
		/**
		 * Constructeur par defaut
		 */
		public ConfirmationDialogModel() {}
		
		/**
		 * Constructeur avec initialisation du panneau appelant
		 * @param parent
		 */
		public ConfirmationDialogModel(IViewComponent parent) {
			this.parent = parent;
		}
		
		
		public String getDialogBoxImage() {
			return dialogBoxImage;
		}

		public void setDialogBoxImage(String dialogBoxImage) {
			this.dialogBoxImage = dialogBoxImage;
		}

		public boolean isRenderNoButton() {
			return renderNoButton;
		}

		public void setRenderNoButton(boolean renderNoButton) {
			this.renderNoButton = renderNoButton;
		}

		public boolean isRenderCancelButton() {
			return renderCancelButton;
		}

		public void setRenderCancelButton(boolean renderCancelButton) {
			this.renderCancelButton = renderCancelButton;
		}

		public int getDecision() {
			return decision;
		}

		public void setDecision(int decision) {
			this.decision = decision;
		}

		/**
		 * Methode d'obtention du Titre de la boite d'informations
		 * @return Titre de la boite d'informations
		 */
		public String getTitle() {
			return title;
		}
		
		/**
		 * Methode permettant d'obtenir le nom du fichier de definition
		 * @return	Nom du fichier de definition
		 */
		public String getFileDefinition() {
			
			// On retourne le chemin
			return "/views/home/ConfirmationDialog.xhtml";
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
			this.renderNoButton = false;
			this.renderCancelButton = false;
			dialogBoxImage = "DialogInformationIcon.png";

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

					// Si le buffer n'est plus vide
					if(scriptBuffer.length() > 0) buffer.append(";");
									
					// Si l'ID n'est pas vide
					if(onCloseListener.getViewAreaToRerender() != null && onCloseListener.getViewAreaToRerender().trim().length() > 0) buffer.append(onCloseListener.getViewAreaToRerender().trim());
					if(onCloseListener.getJSScriptOnComplete() != null && onCloseListener.getJSScriptOnComplete().trim().length() > 0 && scriptBuffer.length() > 0) scriptBuffer.append(onCloseListener.getJSScriptOnComplete().trim());
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
		 * Methode du bouton NO
		 */
		public void yes() {
			// MAJ de la decision
			this.decision = YES_ANSWER;
			
			// Execution de la fermeture
			close();
			
		}
		
		/**
		 * Methode du bouton NO
		 */
		public void no() {
			// MAJ de la decision
			this.decision = NO_ANSWER;
			
			// Execution de la fermeture
			close();
			
		}
		
		/**
		 * Methode du bouton CANCEL
		 */
		public void cancel() {
			// MAJ de la decision
			this.decision = CANCEL_ANSWER;
			
			// Fermeture
			close();
			
		}
		
		/**
		 * Methode d'affichage du Message
		 * @param title	Titre de la boite
		 * @param message	Message a afficher
		 */
		/**
		public void showInformationMessage(String titre, String message, Object...parameters) {

			// Initialisation
			javaScript = "";
			
			// On positionne le titre
			this.title = Multilangue.getMessage(titre);
			
			// On positionne le message
			this.message = Multilangue.getMessage(message, parameters);

			// On ouvre
			this.open = true;
		}*/
		
		/**
		 * Methode d'affichage du Message
		 * @param message	Message a afficher
		 */
		public void showInformationMessage(String message, Object...parameters) {

			// Initialisation
			javaScript = "";
			
			// On positionne le titre
			this.title = Multilangue.getMessage(title);
			
			// On positionne le message
			this.message = Multilangue.getMessage(message, parameters);
			
			// On ouvre
			this.open = true;
		}
		
		/**
		 * Methode d'affichage du Message
		 * @param message	Message a afficher
		 */
		public void showInformationMessage(String message1,String message2, Object...parameters) {

			// Initialisation
			javaScript = "";
			
			// On positionne le titre
			this.title = Multilangue.getMessage(title);
			
			// On positionne le message
			this.message = message1 + Multilangue.getMessage(message2, parameters);
			
			// On ouvre
			this.open = true;
		}
		
		/**
		 * Methode d'affichage du Message
		 * @param message	Message a afficher
		 */
		public void showInformationMessage(String message, int dialogType, Object...parameters) {
			
			// MAJ des valeurs d'affichage de la boite de dialogue
			updateValuesFromOpenMode(dialogType);
			
			// Appel de la methode sans titre
			showInformationMessage(message, parameters);
			
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
		
		private void updateValuesFromOpenMode(int openMode){
			if(openMode == INFORMATION_DIALOG){
				this.renderNoButton = false;
				this.renderCancelButton = false;
				dialogBoxImage = "DialogInformationIcon.png";
				this.title = "ehr.informationDialog.title";
				
			}if(openMode == WARNING_DIALOG){
				this.renderNoButton = false;
				this.renderCancelButton = false;
				dialogBoxImage = "Warning.gif";
				//dialogBoxImage = "WarningDialogBoxIcon.png";
				this.title = "ehr.warningDialog.title";
				
			}if(openMode == ERROR_DIALOG){
				this.renderNoButton = false;
				this.renderCancelButton = false;
				dialogBoxImage = "ErrorDialogBoxIcon.png";
				this.title = "ehr.errorDialog.title";
				
			}else if(openMode == YESNO_DIALOG){
				this.renderNoButton = true;
				this.renderCancelButton = false;
				dialogBoxImage = "QuestionDialogBoxIcon.png";
				this.title = "ehr.questionDialog.title";
				
			}else if(openMode == YESNOCANCEL_DIALOG){
				this.renderNoButton = true;
				this.renderCancelButton = true;
				dialogBoxImage = "QuestionDialogBoxIcon.png";
				this.title = "ehr.questionDialog.title";
				
			}

		}


		public IViewComponent getParent() {
			return parent;
		}


		public void setParent(IViewComponent parent) {
			this.parent = parent;
		}
	
}
