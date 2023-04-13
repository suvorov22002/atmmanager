package com.afb.portal.jpa.gab.parameter;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Classe ParameterSauv
 * @author Francis
 * @version 1.0
 */
@Entity(name = "ParameterSauv")
@Table(name = "ATM_PARAMSAUV")
public class ParameterSauv implements Serializable{

	/**
	 * ID Genere
	 */
	private static final long serialVersionUID = 1L;
	
	
	public static final String SAUVENGARDE = "SAUV_MONI";
	
	/**
	 * Identifiant
	 */
	@Id
	//@SequenceGenerator(name="Seq_ParameterSauv", sequenceName="SEQ_ATM_PARAMSAUV", allocationSize = 1, initialValue = 60)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Seq_ParameterSauv")
    @Column(name = "ID")
	private String id = SAUVENGARDE;
		
	/**
	 * libelle
	 */
	@Column(name = "libelle")
	private String libelle ;
	
	/**
	 * 
	 */
	@Column(name = "heure")
	private String heure ;
	
	/**
	 * Code
	 */
	@Column(name = "DESCRIPTION")
	private String descrption ;
	
	/**
	 * 
	 */
	@Column
	private Boolean lundi = Boolean.TRUE;
	
	/**
	 * 
	 */
	@Column
	private Boolean mardi = Boolean.TRUE;
	
	/**
	 * 
	 */
	@Column
	private Boolean mercredi = Boolean.TRUE;
	
	/**
	 * 
	 */
	@Column
	private Boolean jeudi = Boolean.TRUE;
	
	/**
	 * 
	 */
	@Column
	private Boolean vendredi = Boolean.TRUE;
	
	/**
	 * 
	 */
	@Column
	private Boolean samedi = Boolean.TRUE;
	
	/**
	 * 
	 */
	@Column
	private Boolean dimanche = Boolean.TRUE;
	
	/**
	 * Version de l'enregistrement
	 */
	@Version
	@Column(name = "VERSION")
	private Integer version = 0;

	/**
	 * ParameterSauv
	 */
	public ParameterSauv() {
		super();
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the libelle
	 */
	public String getLibelle() {
		return libelle;
	}

	/**
	 * @param libelle the libelle to set
	 */
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	/**
	 * @return the heure
	 */
	public String getHeure() {
		return heure;
	}

	/**
	 * @param heure the heure to set
	 */
	public void setHeure(String heure) {
		this.heure = heure;
	}

	/**
	 * @return the descrption
	 */
	public String getDescrption() {
		return descrption;
	}

	/**
	 * @param descrption the descrption to set
	 */
	public void setDescrption(String descrption) {
		this.descrption = descrption;
	}

	/**
	 * @return the lundi
	 */
	public Boolean getLundi() {
		return lundi;
	}

	/**
	 * @param lundi the lundi to set
	 */
	public void setLundi(Boolean lundi) {
		this.lundi = lundi;
	}

	/**
	 * @return the mardi
	 */
	public Boolean getMardi() {
		return mardi;
	}

	/**
	 * @param mardi the mardi to set
	 */
	public void setMardi(Boolean mardi) {
		this.mardi = mardi;
	}

	/**
	 * @return the mercredi
	 */
	public Boolean getMercredi() {
		return mercredi;
	}

	/**
	 * @param mercredi the mercredi to set
	 */
	public void setMercredi(Boolean mercredi) {
		this.mercredi = mercredi;
	}

	/**
	 * @return the jeudi
	 */
	public Boolean getJeudi() {
		return jeudi;
	}

	/**
	 * @param jeudi the jeudi to set
	 */
	public void setJeudi(Boolean jeudi) {
		this.jeudi = jeudi;
	}

	/**
	 * @return the vendredi
	 */
	public Boolean getVendredi() {
		return vendredi;
	}

	/**
	 * @param vendredi the vendredi to set
	 */
	public void setVendredi(Boolean vendredi) {
		this.vendredi = vendredi;
	}

	/**
	 * @return the samedi
	 */
	public Boolean getSamedi() {
		return samedi;
	}

	/**
	 * @param samedi the samedi to set
	 */
	public void setSamedi(Boolean samedi) {
		this.samedi = samedi;
	}

	/**
	 * @return the dimanche
	 */
	public Boolean getDimanche() {
		return dimanche;
	}

	/**
	 * @param dimanche the dimanche to set
	 */
	public void setDimanche(Boolean dimanche) {
		this.dimanche = dimanche;
	}

	/**
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	
}
