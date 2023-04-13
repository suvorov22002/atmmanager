package com.afb.portal.jpa.gab.parameter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import afb.dsi.dpd.portal.jpa.entities.User;

/**
 * GroupeAlerte
 * @author Owner
 * @version 1.0
 */
@Entity(name = "GroupeAlerte")
@Table(name = "ATM_GPAlerte")
public class GroupeAlerte implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
			
	/**
	 * Identifiant
	 */
	@Id
	@SequenceGenerator(name="Seq_GroupeAlerte", sequenceName="SEQ_ATM_GPAlerte", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Seq_GroupeAlerte")
    @Column(name = "ID")
	private Long id;
	
	
	/**
	 * libelle 
	 */
	@Column(name = "LIBELLE")
	private String libelle;
	
	/**
	 * libelle 
	 */
	@Column(name = "descrip")
	private String description;
	
	/**
	 * Statut
	 */
	@Column(name = "actif")
	private Boolean admin = Boolean.TRUE;
	
	/**
	 * Parametrage des Types d alerte pour un Utilisateur X
	 */
	@OneToMany(fetch = FetchType.EAGER, mappedBy="groupeAlerte")
	private Set<Personne> userAlertes = new HashSet<Personne>();
	
	/**
	 * Gab d un Utilisateur
	 */
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "ATM_GROUP_USER",
			joinColumns = {@JoinColumn(name = "GROUP_ID")},
			inverseJoinColumns = {@JoinColumn(name = "USER_ID")}
	)
	private Set<User> users = new HashSet<User>();
	
	/**
	 * 
	 */
	public GroupeAlerte() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * @return the users
	 */
	public Set<User> getUsers() {
		return users;
	}


	/**
	 * @param users the users to set
	 */
	public void setUsers(Set<User> users) {
		this.users = users;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @return the admin
	 */
	public Boolean getAdmin() {
		return admin;
	}


	/**
	 * @param admin the admin to set
	 */
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	/**
	 * @return the userAlertes
	 */
	public Set<Personne> getUserAlertes() {
		return userAlertes;
	}

	/**
	 * @param userAlertes the userAlertes to set
	 */
	public void setUserAlertes(Set<Personne> userAlertes) {
		this.userAlertes = userAlertes;
	}	
	
}
