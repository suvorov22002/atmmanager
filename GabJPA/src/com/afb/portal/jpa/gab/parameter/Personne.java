package com.afb.portal.jpa.gab.parameter;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author Owner
 *
 */
@Entity(name = "Personne")
@Table(name = "ATM_Personne")
public class Personne implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Identifiant
	 */
	@Id
	@SequenceGenerator(name="Seq_Personne", sequenceName="SEQ_ATM_Personne", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Seq_Personne")
    @Column(name = "ID")
	private Long id;
	
	/**
	 * 		
	 */
	@Column(name = "nom")
	private String nom;
	
	/**
	 * 
	 */
	@Column(name = "email")
	private String email;
	
	/**
	 * 
	 */
	@Column(name = "phone")
	private String phone;
	
	/**
	 * alerteEmail
	 */
	@Column(name = "AEMAIL")
	private Boolean alerteEmail = Boolean.TRUE;
	
	/**
	 * alerteSms
	 */
	@Column(name = "ASMS")
	private Boolean alerteSms = Boolean.TRUE;
	
	
	@Column(name = "actif")
	private Boolean actif = Boolean.TRUE;
	
	/**
	 * 
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "groupe")
	private GroupeAlerte groupeAlerte;
		
	/**
	 * 
	 */
	public Personne() {
		super();
		// TODO Auto-generated constructor stub
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
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
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
	 * @return the actif
	 */
	public Boolean getActif() {
		return actif;
	}

	/**
	 * @param actif the actif to set
	 */
	public void setActif(Boolean actif) {
		this.actif = actif;
	}

	/**
	 * @return the groupeAlerte
	 */
	public GroupeAlerte getGroupeAlerte() {
		return groupeAlerte;
	}

	/**
	 * @param groupeAlerte the groupeAlerte to set
	 */
	public void setGroupeAlerte(GroupeAlerte groupeAlerte) {
		this.groupeAlerte = groupeAlerte;
	}
		
}
