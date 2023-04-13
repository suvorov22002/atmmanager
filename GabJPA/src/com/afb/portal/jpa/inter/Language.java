package com.afb.portal.jpa.inter;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumeration des Langues disponibles
 * @author Francis K
 * @version 1.0
 */
public enum Language {
	
	/**
	 * Francais par defaut
	 */
	fr("Language.fr"),
	
	/**
	 * Anglais par defaut
	 */
	en("Language.en"),
	
	/**
	 * Francais de France
	 */
	fr_FR("Language.fr_FR"),
	
	/**
	 * Anglais d'Angleterre
	 */
	en_EN("Language.en_EN");
	
	/**
	 * Valeur de la constante (Code)
	 */
	private final String value;
	
	/**
	 * Liste des Langues disponibles
	 */
	private static final List<Language> enabled = new ArrayList<Language>();
	
	static {
		
		// On ajoute
		enabled.add(Language.fr);
		enabled.add(Language.fr_FR);
		enabled.add(Language.en);
		enabled.add(Language.en_EN);
		
	}
	
	/**
	 * Constructeur avec initialisation des parametres
	 * @param value	Valeur de l'etat
	 */
	private Language(String value) {
		
		// Initialisation de la valeur
		this.value = value;
	}

	/**
	 * Obtention de la valeur de l'etat
	 * @return	Valeur de l'etat
	 */
	public String value() {
        return value;
    }
	
	public String getValue() {
        return value;
    }
	
	/**
	 * Methode d'obtention des Langues disponibles
	 * @return	Liste des langues disponibles
	 */
	public static List<Language> enabledLanguages() {
		
		// On retourne la liste
		return enabled;
		
	}
	
	/**
	 * Methode d'obtention du Code Langue
	 * @return	Code Langue
	 */
	public String getLanguageCode() {
		
		// Obtention du Code Langue
		return this.toString().split("_")[0];
		
	}

	/**
	 * Methode d'obtention du Code Pays
	 * @return	Code Langue
	 */
	public String getLanguageContry() {
		
		// Tableau Chaine Language
		String[] strLanguage = this.toString().split("_");
		
		// Si le tableau a pour taille 2
		if(strLanguage.length > 1) return strLanguage[1];
		
		// On retourne le Vide
		else return "";
	}
}
