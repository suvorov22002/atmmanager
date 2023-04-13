package com.afb.portal.jpa.gab.parameter;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import afb.dsi.dpd.portal.jpa.entities.User;

import com.yashiro.persistence.utils.annotations.validator.SizeDAOValidator;
import com.yashiro.persistence.utils.annotations.validator.SizeDAOValidators;
import com.yashiro.persistence.utils.dao.constant.DAOMode;

/**
 * Classe des parametres TypeIncident
 * @author Francis
 * @version 1.0
 */
@SizeDAOValidators({
	@SizeDAOValidator(mode = DAOMode.SAVE,   expr = "from TypeIncident g where (g.libelle = ${libelle})", max = 0, message = "TypeIncident.libelle.exist"),
	@SizeDAOValidator(mode = DAOMode.UPDATE, expr = "from TypeIncident g where (g.id = ${id})", min = 1, message = "TypeIncident.update.id.notexist"),
	@SizeDAOValidator(mode = DAOMode.UPDATE,   expr = "from TypeIncident g where (g.id != ${id}) and (g.libelle = ${libelle})", max = 0, message = "TypeIncident.libelle.exist")
})
@Entity(name = "TypeIncident")
@Table(name = "ATM_TypeIncident")
public class TypeIncident implements Serializable{

	/**
	 * ID Genere
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Identifiant
	 */
	@Id
	@SequenceGenerator(name="Seq_TypeIncident", sequenceName="SEQ_ATM_TypeIncident", allocationSize = 1, initialValue = 60)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Seq_TypeIncident")
    @Column(name = "ID")
	private Long id;
			
	
	/**
	 * Code
	 */
	@Column(name = "CODE")
	private String code ;
	
	/**
	 * Code
	 */
	@Column(name = "descrption")
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
	 * Statut
	 */
	@Column(name = "journal")
	private Boolean journal = Boolean.FALSE;
	
	/**
	 * journalError
	 */
	@Column(name = "journalError")
	private Boolean journalError = Boolean.FALSE;
	
	/**
	 * codeErreur1
	 */
	@Column(name = "codeErreur1")
	private String codeErreur1 ="";
	
	/**
	 * codeErreur2
	 */
	@Column(name = "codeErreur2")
	private String codeErreur2 ="";
	
	/**
	 * Solution
	 */
	@Column(name = "probleme")
	private String probleme ="";
	
	/**
	 * Solution
	 */
	@Column(name = "solution")
	private String solution ="";
	
	/**
	 * Message
	 */
	@Column(name = "MESSAGE")
	private String message ="";
	
	
	@Temporal(TemporalType.DATE)
	@Column(name = "D_date")
	private Date date = new Date();
	
	/**
	 * 
	 */
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "ATM_TINCIDENT_USER",
			joinColumns = {@JoinColumn(name = "TINCD_ID")},
			inverseJoinColumns = {@JoinColumn(name = "USER_ID")}
	)
	private Set<User> users = new HashSet<User>();
	
	/**
	 * Version de l'enregistrement
	 */
	@Version
	@Column(name = "VERSION")
	private Integer version = 0;
	
	/**
	 *  TypeAlerte
	 */
	public TypeIncident() {
		super();
	}
	
	
	
	/**
	 * @return the probleme
	 */
	public String getProbleme() {
		return probleme;
	}



	/**
	 * @param probleme the probleme to set
	 */
	public void setProbleme(String probleme) {
		this.probleme = probleme;
	}



	/**
	 * @return the journal
	 */
	public Boolean getJournal() {
		return journal;
	}



	/**
	 * @param journal the journal to set
	 */
	public void setJournal(Boolean journal) {
		this.journal = journal;
	}



	/**
	 * @return the journalError
	 */
	public Boolean getJournalError() {
		return journalError;
	}



	/**
	 * @param journalError the journalError to set
	 */
	public void setJournalError(Boolean journalError) {
		this.journalError = journalError;
	}



	/**
	 * @return the codeErreur1
	 */
	public String getCodeErreur1() {
		return codeErreur1;
	}



	/**
	 * @param codeErreur1 the codeErreur1 to set
	 */
	public void setCodeErreur1(String codeErreur1) {
		this.codeErreur1 = codeErreur1;
	}



	/**
	 * @return the codeErreur2
	 */
	public String getCodeErreur2() {
		return codeErreur2;
	}



	/**
	 * @param codeErreur2 the codeErreur2 to set
	 */
	public void setCodeErreur2(String codeErreur2) {
		this.codeErreur2 = codeErreur2;
	}



	/**
	 * @return the solution
	 */
	public String getSolution() {
		return solution;
	}



	/**
	 * @param solution the solution to set
	 */
	public void setSolution(String solution) {
		this.solution = solution;
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
	
	
	
}
