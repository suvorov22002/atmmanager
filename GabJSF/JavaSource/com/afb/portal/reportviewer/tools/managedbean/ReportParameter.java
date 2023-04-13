/**
 * 
 */
package com.afb.portal.reportviewer.tools.managedbean;



/**
 * Classe representant un Parametre d'Etat
 * @author Francis K
 * @version 1.0
 */
public class ReportParameter implements Comparable<ReportParameter> {
	
	/**
	 * Nom du Parametre
	 */
	private String name = "";
	
	/**
	 * Valeur du Parametre
	 */
	private Object value;
	
	/**
	 * Constructeur par defaut
	 */
	public ReportParameter() {}
	
	/**
	 * Constructeur avec initialisation des Parametres
	 * @param name	Nom du Parametre
	 * @param value	Valeur du Parametre
	 */
	public ReportParameter(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * Methode d'obtention du Nom du Parametre
	 * @return Nom du Parametre
	 */
	public String getName() {
		return name;
	}

	/**
	 * Methode de mise a jour du Nom du Parametre
	 * @param name Nom du Parametre
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Methode d'obtention de la Valeur du Parametre
	 * @return Valeur du Parametre
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Methode de mise a jour de la Valeur du Parametre
	 * @param value Valeur du Parametre
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		// Si le parametre est null
		if(obj == null) return false;
		
		// Si le parametre n'est pas de l'instance
		if(!(obj instanceof ReportParameter)) return false;
		
		// On caste
		ReportParameter parameter = (ReportParameter) obj;
		
		// Si le name du parametre est vide
		if(parameter.name == null || parameter.name.trim().length() == 0) return false;
		
		// Si le name du parametre en cours est vide
		if(name == null || name.trim().length() == 0) return false;
		
		// On retourne la comparaison des names
		return name.equals(parameter.name);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(ReportParameter parameter) {
		
		// Si le parametre est null
		if(parameter == null) return -1;
		
		// Si le code du parametre est vide
		if(parameter.name == null || parameter.name.trim().length() == 0) return -1;
		
		// Si le code du parametre en cours est vide
		if(name == null || name.trim().length() == 0) return 1;
		
		// On retourne la comparaison des names
		return name.compareTo(parameter.name);
	}
}
