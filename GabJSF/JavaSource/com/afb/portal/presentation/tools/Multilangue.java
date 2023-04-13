/**
 * 
 */
package com.afb.portal.presentation.tools;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import com.afb.portal.jpa.inter.Language;

/**
 * Classe de gestion du Multilangue
 * @author Francis K
 * @version 1.0
 */
public class Multilangue {

	/**
	 * Locale par defaut
	 */
	private static Locale DEFAULTLOCALE = new Locale(Language.fr.toString());

	/**
	 * Methode d'obtention d'un message internationnalise a partir de son code
	 * @param resourceID	Code du message
	 * @param parameters	Parametres du message
	 * @return				Message internationnalise
	 */
	public static String getMessage(String resourceID, Object...parameters) {

		// Le FacesContext
		FacesContext context = FacesContext.getCurrentInstance();

		// Obtention du nom du Bundle de Message
		String messageBundleName = context.getApplication().getMessageBundle();

		// Locale à utiliser
		Locale locale = getLocale(context);

		// Obtention du ClassLoader
		ClassLoader loader = getClassLoader();

		// S'il n'ya pas de Bundle
		if(messageBundleName == null || messageBundleName.trim().length() == 0) return resourceID;

		// On retourne le message
		return getString(messageBundleName, locale, resourceID, loader, parameters);
	}

	/**
	 * Methode d'obtention d'un message internationnalise a partir de son code
	 * @param messaegBundleName	Nom du MessageBundle
	 * @param resourceID	Code du message
	 * @param parameters	Parametres du message
	 * @return				Message internationnalise
	 */
	public static String getMessage(String messageBundleName, String resourceID, Object...parameters) {

		// Obtention du nom du Bundle de Message
		String localMessageBundleName = messageBundleName;

		// Le FacesContext
		FacesContext context = FacesContext.getCurrentInstance();

		// Si le Nom du messageBundle en parametre est null
		if(localMessageBundleName == null || localMessageBundleName.trim().length() == 0) {

			// Obtention du nom du Bundle de Message de l'application
			localMessageBundleName = context.getApplication().getMessageBundle();
		}

		// Locale à utiliser
		Locale locale = getLocale(context);

		// Obtention du ClassLoader
		ClassLoader loader = getClassLoader();

		// S'il n'ya pas de Bundle
		if(localMessageBundleName == null || localMessageBundleName.trim().length() == 0) return resourceID;

		// On retourne le message
		return getString(localMessageBundleName, locale, resourceID, loader, parameters);
	}

	/**
	 * Methode effective d'internationnalisation
	 * @param bundleName	Nom du Bundle de message
	 * @param resourceID	Code du message
	 * @param loader	ClassLoader a utiliser
	 * @param params	Parametres du message
	 * @return	Message internationnalise
	 */
	public static String getString(String bundleName, Locale locale, String resourceID, ClassLoader loader, Object...params) {

		/// Le resourceBundle
		ResourceBundle resourceBundle = null;

		try {

			// Initialisation
			resourceBundle = ResourceBundle.getBundle(bundleName, locale, loader);

			// Le message
			String message = resourceBundle.getString(resourceID);

			// Un formatter
			MessageFormat formatter = new MessageFormat(message, locale);

			// Message formatte
			message = formatter.format(params);

			// Obtention du message
			return message;

		} catch (Exception e) {

			// On retourne le code
			return resourceID;
		}

	}

	/**
	 * Methode d'obtention de la locale du Contexte d'un Utilisateur
	 * @param context	Contexte JSf
	 * @return	Locale
	 */
	public static Locale getLocale(FacesContext context) {

		// Locale a retourner
		Locale locale = null;

		// Racine de l'arbre des composants
		UIViewRoot viewRoot = context.getViewRoot();

		// Si l'arbre n'est pas null
		if (viewRoot != null) {

			// On obtient la locale
			locale = viewRoot.getLocale();

			// Si la locale est nulle
			if(locale == null) return DEFAULTLOCALE;

			// Sinon
			else return locale;
		}

		// Sinon
		else {

			// Obtention de la MAP de session
			Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();

			// Si la MAP est nulle
			if(sessionMap == null || sessionMap.size() == 0) return DEFAULTLOCALE;

			// Recherche de la locale dans la MAP
			locale = (Locale) sessionMap.get(ViewHelper.SESSIONLOCALE_PARAMETER_NAME);

			// Si la locale est nulle
			if(locale == null) return DEFAULTLOCALE;

			// Sinon
			else return locale;
		}
	}

	/**
	 * Methode d'obtention du ClassLoader
	 * @return	le ClassLoader
	 */
	public static ClassLoader getClassLoader() {

		// Recherche du ClassLoader du Thread Courant
		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		// Si ce ClassLoader est null (On Charge le ClassLoader Systeme)
		if (loader == null) loader = ClassLoader.getSystemClassLoader();

		// On retourne la ClassLoader
		return loader;
	}
}
