/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.afb.portal.jpa.gab.monitoring;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *  
 * @author Francis
 */
@Entity(name = "RapportElement")
@Table(name = "ATM_RPPTGAB")
public class RapportElement implements Serializable, Comparable<RapportElement>{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Id auto genere
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	private String code ="";
	
	private String age ="";
	
	private String codage ="";
	
	private String libage ="";

	private String atm ="";

	private String etat ="OK";

	private String color ="";

	private String erreur ="";

	private String status ="O";

	private String ville ="";   
	
	private String ip ="";  

	private String telephone =""; 

	private String promoteur =""; 
	
	private String type = "";
	
	private String libatm ="";
	
	private String atmTid ="";
	
	private String atmPid ="";
	
	@Temporal(TemporalType.DATE)
	private Date dateCrt = new Date();
	
	private Boolean checkJournal = Boolean.FALSE;
	
	private Boolean checkImage = Boolean.FALSE;
	
	private Boolean horService = Boolean.FALSE;

	public RapportElement() {
		super();
	}
			
	/**
	 * @return the horService
	 */
	public Boolean getHorService() {
		return horService;
	}

	/**
	 * @param horService the horService to set
	 */
	public void setHorService(Boolean horService) {
		this.horService = horService;
	}

	/**
	 * @return the checkJournal
	 */
	public Boolean getCheckJournal() {
		return checkJournal;
	}

	/**
	 * @param checkJournal the checkJournal to set
	 */
	public void setCheckJournal(Boolean checkJournal) {
		this.checkJournal = checkJournal;
	}

	/**
	 * @return the checkImage
	 */
	public Boolean getCheckImage() {
		return checkImage;
	}

	/**
	 * @param checkImage the checkImage to set
	 */
	public void setCheckImage(Boolean checkImage) {
		this.checkImage = checkImage;
	}

	/**
	 * @return the codage
	 */
	public String getCodage() {
		return codage;
	}

	/**
	 * @param codage the codage to set
	 */
	public void setCodage(String codage) {
		this.codage = codage;
	}



	/**
	 * @return the libage
	 */
	public String getLibage() {
		return libage;
	}



	/**
	 * @param libage the libage to set
	 */
	public void setLibage(String libage) {
		this.libage = libage;
	}



	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}



	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}



	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}



	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}



	/**
	 * @return the libatm
	 */
	public String getLibatm() {
		return libatm;
	}



	/**
	 * @param libatm the libatm to set
	 */
	public void setLibatm(String libatm) {
		this.libatm = libatm;
	}



	/**
	 * @return the atmTid
	 */
	public String getAtmTid() {
		return atmTid;
	}



	/**
	 * @param atmTid the atmTid to set
	 */
	public void setAtmTid(String atmTid) {
		this.atmTid = atmTid;
	}



	/**
	 * @return the atmPid
	 */
	public String getAtmPid() {
		return atmPid;
	}



	/**
	 * @param atmPid the atmPid to set
	 */
	public void setAtmPid(String atmPid) {
		this.atmPid = atmPid;
	}



	/**
	 * @return the dateCrt
	 */
	public Date getDateCrt() {
		return dateCrt;
	}



	/**
	 * @param dateCrt the dateCrt to set
	 */
	public void setDateCrt(Date dateCrt) {
		this.dateCrt = dateCrt;
	}



	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}



	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}



	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}



	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}



	/**
	 * @return the promoteur
	 */
	public String getPromoteur() {
		return promoteur;
	}



	/**
	 * @param promoteur the promoteur to set
	 */
	public void setPromoteur(String promoteur) {
		this.promoteur = promoteur;
	}



	/**
	 * @return the ville
	 */
	public String getVille() {
		return ville;
	}



	/**
	 * @param ville the ville to set
	 */
	public void setVille(String ville) {
		this.ville = ville;
	}



	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the age
	 */
	public String getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(String age) {
		this.age = age;
	}



	/**
	 * @return the atm
	 */
	public String getAtm() {
		return atm;
	}



	/**
	 * @param atm the atm to set
	 */
	public void setAtm(String atm) {
		this.atm = atm;
	}



	/**
	 * @return the etat
	 */
	public String getEtat() {
		return etat;
	}


	/**
	 * @param etat the etat to set
	 */
	public void setEtat(String etat){
		this.etat = etat;
	}


	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}



	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}



	/**
	 * @return the erreur
	 */
	public String getErreur() {
		return erreur;
	}



	/**
	 * @param erreur the erreur to set
	 */
	public void setErreur(String erreur) {
		this.erreur = erreur;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result + ((atm == null) ? 0 : atm.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RapportElement other = (RapportElement) obj;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (atm == null) {
			if (other.atm != null)
				return false;
		} else if (!atm.equals(other.atm))
			return false;
		return true;
	}


	/**
	 * compareTo
	 */
	public int compareTo(RapportElement arg){

		// Comparaison sur l'Etat
		int cpt = this.getErreur().compareToIgnoreCase(arg.getErreur());
		if(cpt != 0) { return cpt; }
		
		cpt = this.getEtat().compareToIgnoreCase(arg.getEtat());
		if(cpt != 0) { return cpt; }
			
		// Comparaison sur la Ville
		cpt = this.getAtm().compareToIgnoreCase(arg.getAtm());
		if(cpt != 0) { return cpt; }
		
		// Comparaison sur la Ville
		cpt = this.getVille().compareToIgnoreCase(arg.getVille());
		if(cpt != 0) { return cpt; }

		// Les deux livres sont à la même position
		return 0;
		
	}
	
}