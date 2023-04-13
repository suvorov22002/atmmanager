package com.afb.portal.jpa.gab.equipment;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import afb.dsi.dpd.portal.jpa.entities.Branch;
import afb.dsi.dpd.portal.jpa.entities.User;

import com.afb.portal.jpa.gab.parameter.GroupeAlerte;
import com.afb.portal.jpa.gab.parameter.GroupeSauv;
import com.afb.portal.jpa.gab.parameter.Region;
import com.yashiro.persistence.utils.annotations.validator.SizeDAOValidator;
import com.yashiro.persistence.utils.annotations.validator.SizeDAOValidators;
import com.yashiro.persistence.utils.dao.constant.DAOMode;

/**
 * Atm
 * @author Francis
 * @version 1.0
 */
@SizeDAOValidators({
	@SizeDAOValidator(mode = DAOMode.SAVE,   expr = "from Atm g where (g.tid = ${tid})", max = 0, message = "Atm.tid.exist")
})
@NamedQuery(name="findActive",query="SELECT a FROM Atm a WHERE a.status = :status and a.typeAtm =:typeAtm")
@Entity(name = "Atm")
@Table(name = "ATM_GAB")
public class Atm implements Serializable{

	/**
	 * ID Genere
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Identifiant
	 */
	@Id
	@Column(name = "TID")
	private String tid;
	
	
	@Column(name = "PID")
	private String pid;
	
	@Column(name = "NumSerie")
	private String NumSerie = "000000000";
		
	@Enumerated(EnumType.STRING)
	@Column
	private AtmStatus typeAtm;

	/**
	 * Nom
	 */
	@Column(name = "ATM_NAME")
	private String nom;

	/**
	 * Adresse
	 */
	@Column(name = "ATM_ADR")
	private String adresse;

	/**
	 * Agence
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BRCH_ID")
	private Branch agence;
	
	/**
	 * 
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "groupe")
	private GroupeSauv groupeSauv;
	
	/**
	 * NombreCassete
	 */
	@Column(name = "ATM_CASST")
	private Integer nomnbreCass = 4;
	
	/**
	 * Solde Minimun
	 */
	@Column(name = "ATM_SMIN")
	private Integer sodleMin = 1000000;

	/**
	 * Solde Critique
	 */
	@Column(name = "ATM_SCRITQ")
	private Integer sodlecritique =2000000;

	/**
	 * Solde Initiale
	 */
	@Column(name = "ATM_SINIT")
	private Integer soldeInitial =0;

	/**
	 * Solde Restant
	 */
	@Column(name = "ATM_SREST")
	private Integer soldeRestant = 0;

	/**
	 * Solde Consome
	 */
	@Column(name = "ATM_SCONS")
	private Integer soldeconsome;

	/**
	 * Ip
	 */
	@Column(name = "ATM_IP")
	private String ip;

	/**
	 * Libelle
	 */
	@Column(name = "ATM_LIB")
	private String libelle;

	/**
	 * Date
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "ATM_DAT")
	private Date date = new Date();

	/**
	 * Heure
	 */
	@Column(name = "ATM_HEU")
	private String heure;
	
	/**
	 * Heure
	 */
	@Column(name = "ATM_LIBCASS")
	private String libcassete = "1,2,3,4";

	/**
	 * Region
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "REG_ID")
	private Region region;

	/**
	 * Statut de l ATM
	 */
	@Column(name = "ATM_STS")
	private Boolean status = Boolean.FALSE;
	
	/**
	 * Utilisateur
	 */
	@Column(name = "ATM_USR")
	private String login ="administrateur";
	
	/**
	 * password
	 */
	@Column(name = "ATM_PSW")
	private String psw ="password";
	
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
	
	@Column(name = "ATM_TELLPROMO")
	private String telephone;
	
	@Column(name = "ATM_PROMOT")
	private String promoteur;
	
	/**
	 * Gab d un Utilisateur
	 */
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
			name = "ATM_ALERTE_GRP",
			joinColumns = {@JoinColumn(name = "ATM_ID")},
			inverseJoinColumns = {@JoinColumn(name = "GROUP_ID")}
	)
	private Set<GroupeAlerte> groupes = new HashSet<GroupeAlerte>();
	
	
	/**
	 * Gab d un Utilisateur
	 */
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "ATM_ALERTE_USER",
			joinColumns = {@JoinColumn(name = "ATM_ID")},
			inverseJoinColumns = {@JoinColumn(name = "USER_ID")}
	)
	private Set<User> users = new HashSet<User>();
	
	/**
	 * Version
	 */
	@Version
	@Column(name ="VERSION")
	private Integer version = 0;

	/**
	 * 
	 */
	public Atm() {
		super();
	}
	
	/**
	 * @param tid
	 * @param nom
	 * @param adresse
	 * @param agence
	 * @param sodleMin
	 * @param sodlecritique
	 * @param soldeInitial
	 * @param soldeRestant
	 * @param soldeconsome
	 * @param ip
	 * @param libelle
	 * @param date
	 * @param heure
	 * @param region
	 * @param status
	 * @param login
	 * @param psw
	 */
	public Atm(String tid, String nom, String adresse, Branch agence,
			Integer sodleMin, Integer sodlecritique, Integer soldeInitial,
			Integer soldeRestant, Integer soldeconsome, String ip,
			String libelle, Date date, String heure, Region region,
			Boolean status, String login, String psw) {
		super();
		this.tid = tid;
		this.nom = nom;
		this.adresse = adresse;
		this.agence = agence;
		this.sodleMin = sodleMin;
		this.sodlecritique = sodlecritique;
		this.soldeInitial = soldeInitial;
		this.soldeRestant = soldeRestant;
		this.soldeconsome = soldeconsome;
		this.ip = ip;
		this.libelle = libelle;
		this.date = date;
		this.heure = heure;
		this.region = region;
		this.status = status;
		this.login = login;
		this.psw = psw;
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
	 * @return the libcassete
	 */
	public String getLibcassete() {
		return libcassete;
	}

	/**
	 * @param libcassete the libcassete to set
	 */
	public void setLibcassete(String libcassete) {
		this.libcassete = libcassete;
	}

	/**
	 * @return the numSerie
	 */
	public String getNumSerie() {
		return NumSerie;
	}

	/**
	 * @param numSerie the numSerie to set
	 */
	public void setNumSerie(String numSerie) {
		NumSerie = numSerie;
	}

	/**
	 * @return the nomnbreCass
	 */
	public Integer getNomnbreCass() {
		return nomnbreCass;
	}

	/**
	 * @param nomnbreCass the nomnbreCass to set
	 */
	public void setNomnbreCass(Integer nomnbreCass) {
		this.nomnbreCass = nomnbreCass;
	}

	/**
	 * @return the pid
	 */
	public String getPid() {
		return pid;
	}

	/**
	 * @param pid the pid to set
	 */
	public void setPid(String pid) {
		this.pid = pid;
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
	 * @return the groupeSauv
	 */
	public GroupeSauv getGroupeSauv() {
		return groupeSauv;
	}

	/**
	 * @param groupeSauv the groupeSauv to set
	 */
	public void setGroupeSauv(GroupeSauv groupeSauv) {
		this.groupeSauv = groupeSauv;
	}

	/**
	 * @return the typeAtm
	 */
	public AtmStatus getTypeAtm() {
		return typeAtm;
	}

	/**
	 * @param typeAtm the typeAtm to set
	 */
	public void setTypeAtm(AtmStatus typeAtm) {
		this.typeAtm = typeAtm;
	}

	/**
	 * @return the tid
	 */
	public String getTid() {
		return tid;
	}

	/**
	 * @param tid the tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom.trim();
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the adresse
	 */
	public String getAdresse() {
		return adresse;
	}

	/**
	 * @param adresse the adresse to set
	 */
	public void setAdresse(String adresse) {
		this.adresse = adresse;
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
	 * @return the sodleMin
	 */
	public Integer getSodleMin() {
		return sodleMin;
	}

	/**
	 * @param sodleMin the sodleMin to set
	 */
	public void setSodleMin(Integer sodleMin) {
		this.sodleMin = sodleMin;
	}

	/**
	 * @return the sodlecritique
	 */
	public Integer getSodlecritique() {
		return sodlecritique;
	}

	/**
	 * @param sodlecritique the sodlecritique to set
	 */
	public void setSodlecritique(Integer sodlecritique) {
		this.sodlecritique = sodlecritique;
	}

	/**
	 * @return the soldeInitial
	 */
	public Integer getSoldeInitial() {
		return soldeInitial;
	}

	/**
	 * @param soldeInitial the soldeInitial to set
	 */
	public void setSoldeInitial(Integer soldeInitial) {
		this.soldeInitial = soldeInitial;
	}

	/**
	 * @return the soldeRestant
	 */
	public Integer getSoldeRestant() {
		return soldeRestant;
	}

	/**
	 * @param soldeRestant the soldeRestant to set
	 */
	public void setSoldeRestant(Integer soldeRestant) {
		this.soldeRestant = soldeRestant;
	}

	/**
	 * @return the soldeconsome
	 */
	public Integer getSoldeconsome() {
		return soldeconsome;
	}

	/**
	 * @param soldeconsome the soldeconsome to set
	 */
	public void setSoldeconsome(Integer soldeconsome) {
		this.soldeconsome = soldeconsome;
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
	 * @return the region
	 */
	public Region getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(Region region) {
		this.region = region;
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
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the psw
	 */
	public String getPsw() {
		return psw;
	}

	/**
	 * @param psw the psw to set
	 */
	public void setPsw(String psw) {
		this.psw = psw;
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
	 * @return the groupes
	 */
	public Set<GroupeAlerte> getGroupes() {
		return groupes;
	}

	/**
	 * @param groupes the groupes to set
	 */
	public void setGroupes(Set<GroupeAlerte> groupes) {
		this.groupes = groupes;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tid == null) ? 0 : tid.hashCode());
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
		Atm other = (Atm) obj;
		if (tid == null) {
			if (other.tid != null)
				return false;
		} else if (!tid.equals(other.tid))
			return false;
		return true;
	}
	
	
}
