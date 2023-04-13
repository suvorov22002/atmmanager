package com.afb.portal.jpa.gab.monitoring;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import afb.dsi.dpd.portal.jpa.entities.Branch;
import afb.dsi.dpd.portal.jpa.entities.Town;
import afb.dsi.dpd.portal.jpa.entities.User;

import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.parameter.Equipement;
import com.afb.portal.jpa.gab.parameter.Personne;
import com.afb.portal.jpa.gab.parameter.TypeIncident;
import com.afb.portal.jpa.gab.parameter.TypeIntervention;
import com.afb.portal.jpa.gab.parameter.TypePanne;

/**
 * Intervention
 * @author Francis  
 * @version 1.0
 */
@Entity(name = "Intervention")
@Table(name = "ATM_Intervention")
public class Intervention implements Serializable{

	/**
	 * ID Genere
	 */
	private static final long serialVersionUID = 1L;
		   
	/**
	 * Identifiant
	 */
	@Id
	@SequenceGenerator(name="Seq_Intervention", sequenceName="SEQ_ATM_Intervention", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Seq_Intervention")
    @Column(name = "ID")
	private Long id;
	
	
	/**
	 * libelle 
	 */
	@Column(name = "LIBELLE" , length=1000)
	private String libelle;
	
	/**
	 * libelle 
	 */
	@Column(name = "descrip" , length=1000)
	private String description;
	
	/**
	 * libelle 
	 */
	@Column(name = "rapport" , length=1000)
	private String rapport;
	
	@Column(name = "nbrjour")
	private Integer nbrjour;
	
	/**
	 * 
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ville")
	private Town ville;
	
	/**
	 * 
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Type")
	private TypeIntervention type;
	
	/**
	 * 
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ATM")
	private Atm atm;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BRCH_ID")
	private Branch agence;
	
	
	@Temporal(TemporalType.DATE)
	@Column(name = "d_date")
	private Date date = new Date();
	
	@Temporal(TemporalType.DATE)
	@Column(name = "d_dateMise")
	private Date dateMise = new Date();
	
	
	@Column(name = "int_status")
	private Boolean status = Boolean.TRUE;
	
	
	@Column(name = "firstcass10000")
	private Integer firstcass10000 = 0;
	
	@Column(name = "secondcass10000")
	private Integer secondcass10000 = 0;
	
	@Column(name = "thirdcass5000")
	private Integer thirdcass5000 = 0;
	
	@Column(name = "fourcass5000")
	private Integer fourcass5000 = 0;
	
	
	@Column(name = "bfirstcass10000")
	private Integer bfirstcass10000 = 0;
	
	@Column(name = "bsecondcass10000")
	private Integer bsecondcass10000 = 0;
	
	@Column(name = "bthirdcass5000")
	private Integer bthirdcass5000 = 0;
	
	@Column(name = "bfourcass5000")
	private Integer bfourcass5000 = 0;
		
	/**
	 * Personnes
	 */
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
			name = "ATM_INTER_PERS",
			joinColumns = {@JoinColumn(name = "ATM_ID")},
			inverseJoinColumns = {@JoinColumn(name = "PERS_ID")}
	)
	private Set<Personne> personnes = new HashSet<Personne>();
	
	
	/**
	 * Personnes
	 */
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "ATM_INTER_USER",
			joinColumns = {@JoinColumn(name = "ATM_ID")},
			inverseJoinColumns = {@JoinColumn(name = "USER_ID")}
	)
	private Set<User> users = new HashSet<User>();
	
	/**
	 * Gab d un Utilisateur
	 */
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "ATM_INTER_EQUIP",
			joinColumns = {@JoinColumn(name = "INTER_ID")},
			inverseJoinColumns = {@JoinColumn(name = "EQUI_ID")}
	)
	private Set<Equipement> equipements = new HashSet<Equipement>();
	
	/**
	 * Gab d un Utilisateur
	 */
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
			name = "ATM_INTER_PANNES",
			joinColumns = {@JoinColumn(name = "INTER_ID")},
			inverseJoinColumns = {@JoinColumn(name = "PANNE_ID")}
	)
	private Set<TypePanne> pannes = new HashSet<TypePanne>();
	
	
	/**
	 * incidents
	 */
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "ATM_INTER_INCID",
			joinColumns = {@JoinColumn(name = "INTER_ID")},
			inverseJoinColumns = {@JoinColumn(name = "INCD_ID")}
	)
	private Set<TypeIncident> incidents = new HashSet<TypeIncident>();
	
	/**
	 * Version
	 */
	@Version
	@Column(name ="VERSION")
	private Integer version = 0;
	

	/**
	 * 
	 */
	public Intervention() {
		super();
	}

	@PrePersist
	public void validatepersit(){
		setAgence(this.atm.getAgence());
	}
	
	@PreUpdate
	public void validateupdatet(){
		setAgence(this.atm.getAgence());
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	
	
	/**
	 * @return the dateMise
	 */
	public Date getDateMise() {
		return dateMise;
	}

	/**
	 * @param dateMise the dateMise to set
	 */
	public void setDateMise(Date dateMise) {
		this.dateMise = dateMise;
	}

	/**
	 * @return the agence
	 */
	public Branch getAgence() {
		return agence;
	}

	/**
	 * @param agence the agence to set
	 */
	public void setAgence(Branch agence) {
		this.agence = agence;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	
	
	/**
	 * @return the firstcass10000
	 */
	public Integer getFirstcass10000() {
		return firstcass10000;
	}

	/**
	 * @param firstcass10000 the firstcass10000 to set
	 */
	public void setFirstcass10000(Integer firstcass10000) {
		this.firstcass10000 = firstcass10000;
	}

	/**
	 * @return the secondcass10000
	 */
	public Integer getSecondcass10000() {
		return secondcass10000;
	}

	/**
	 * @param secondcass10000 the secondcass10000 to set
	 */
	public void setSecondcass10000(Integer secondcass10000) {
		this.secondcass10000 = secondcass10000;
	}

	/**
	 * @return the thirdcass5000
	 */
	public Integer getThirdcass5000() {
		return thirdcass5000;
	}

	/**
	 * @param thirdcass5000 the thirdcass5000 to set
	 */
	public void setThirdcass5000(Integer thirdcass5000) {
		this.thirdcass5000 = thirdcass5000;
	}

	/**
	 * @return the fourcass5000
	 */
	public Integer getFourcass5000() {
		return fourcass5000;
	}

	/**
	 * @param fourcass5000 the fourcass5000 to set
	 */
	public void setFourcass5000(Integer fourcass5000) {
		this.fourcass5000 = fourcass5000;
	}

	/**
	 * @return the bfirstcass10000
	 */
	public Integer getBfirstcass10000() {
		return bfirstcass10000;
	}

	/**
	 * @param bfirstcass10000 the bfirstcass10000 to set
	 */
	public void setBfirstcass10000(Integer bfirstcass10000) {
		this.bfirstcass10000 = bfirstcass10000;
	}

	/**
	 * @return the bsecondcass10000
	 */
	public Integer getBsecondcass10000() {
		return bsecondcass10000;
	}

	/**
	 * @param bsecondcass10000 the bsecondcass10000 to set
	 */
	public void setBsecondcass10000(Integer bsecondcass10000) {
		this.bsecondcass10000 = bsecondcass10000;
	}

	/**
	 * @return the bthirdcass5000
	 */
	public Integer getBthirdcass5000() {
		return bthirdcass5000;
	}

	/**
	 * @param bthirdcass5000 the bthirdcass5000 to set
	 */
	public void setBthirdcass5000(Integer bthirdcass5000) {
		this.bthirdcass5000 = bthirdcass5000;
	}

	/**
	 * @return the bfourcass5000
	 */
	public Integer getBfourcass5000() {
		return bfourcass5000;
	}

	/**
	 * @param bfourcass5000 the bfourcass5000 to set
	 */
	public void setBfourcass5000(Integer bfourcass5000) {
		this.bfourcass5000 = bfourcass5000;
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
	 * @return the nbrjour
	 */
	public Integer getNbrjour() {
		return nbrjour;
	}

	/**
	 * @param nbrjour the nbrjour to set
	 */
	public void setNbrjour(Integer nbrjour) {
		this.nbrjour = nbrjour;
	}

	/**
	 * @return the ville
	 */
	public Town getVille() {
		return ville;
	}

	/**
	 * @param ville the ville to set
	 */
	public void setVille(Town ville) {
		this.ville = ville;
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
	 * @return the incidents
	 */
	public Set<TypeIncident> getIncidents() {
		return incidents;
	}

	/**
	 * @param incidents the incidents to set
	 */
	public void setIncidents(Set<TypeIncident> incidents) {
		this.incidents = incidents;
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
	 * @return the rapport
	 */
	public String getRapport() {
		return rapport;
	}

	/**
	 * @param rapport the rapport to set
	 */
	public void setRapport(String rapport) {
		this.rapport = rapport;
	}

	/**
	 * @return the type
	 */
	public TypeIntervention getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(TypeIntervention type) {
		this.type = type;
	}

	/**
	 * @return the atm
	 */
	public Atm getAtm() {
		return atm;
	}

	/**
	 * @param atm the atm to set
	 */
	public void setAtm(Atm atm) {
		this.atm = atm;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the personnes
	 */
	public Set<Personne> getPersonnes() {
		return personnes;
	}

	/**
	 * @param personnes the personnes to set
	 */
	public void setPersonnes(Set<Personne> personnes) {
		this.personnes = personnes;
	}

	/**
	 * @return the equipements
	 */
	public Set<Equipement> getEquipements() {
		return equipements;
	}

	/**
	 * @param equipements the equipements to set
	 */
	public void setEquipements(Set<Equipement> equipements) {
		this.equipements = equipements;
	}

	/**
	 * @return the pannes
	 */
	public Set<TypePanne> getPannes() {
		return pannes;
	}

	/**
	 * @param pannes the pannes to set
	 */
	public void setPannes(Set<TypePanne> pannes) {
		this.pannes = pannes;
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
