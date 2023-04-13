package com.afb.portal.jpa.gab.parameter;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Classe des parametres de Carte pension et salaire
 * @author Francis
 * @version 1.0
 */
@Entity(name = "TypeAlerte")
@Table(name = "ATM_TYPALERT")
public class TypeAlerte implements Serializable{

	/**
	 * ID Genere
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Identifiant
	 */
	@Id
	@SequenceGenerator(name="Seq_TypeAlerte", sequenceName="SEQ_ATM_TYPALERT", allocationSize = 1, initialValue = 60)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Seq_TypeAlerte")
    @Column(name = "ID")
	private Long id;
			
	/**
	 * Code
	 */
	@Column(name = "DESC")
	private String descrption ;
	
	/**
	 * Code
	 */
	@Column(name = "LIBELLE")
	private String libelle;
	
	/**
	 * Statut
	 */
	@Column(name = "STATUS")
	private Boolean status = Boolean.TRUE;
	
	/**
	 * Statut
	 */
	@Column(name = "AEMAIL")
	private Boolean alerteEmail = Boolean.TRUE;
		
	/**
	 * Statut
	 */
	@Column(name = "ASMS")
	private Boolean alerteSms = Boolean.TRUE;
	
	/**
	 * Message
	 */
	@Column(name = "MESSAGE")
	private String message ="";
	
	/**
	 * Version de l'enregistrement
	 */
	@Version
	@Column(name = "VERSION")
	private Integer version = 0;
	
	/**
	 *  TypeAlerte
	 */
	public TypeAlerte() {
		super();
	}
	
	
	/**
	 * @return the status
	 */
	public Boolean getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Boolean status) {
		this.status = status;
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
	 * @return the alerteEmail
	 */
	public Boolean getAlerteEmail() {
		return alerteEmail;
	}

	/**
	 * @param alerteEmail the alerteEmail to set
	 */
	public void setAlerteEmail(Boolean alerteEmail) {
		this.alerteEmail = alerteEmail;
	}

	/**
	 * @return the alerteSms
	 */
	public Boolean getAlerteSms() {
		return alerteSms;
	}

	/**
	 * @param alerteSms the alerteSms to set
	 */
	public void setAlerteSms(Boolean alerteSms) {
		this.alerteSms = alerteSms;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
