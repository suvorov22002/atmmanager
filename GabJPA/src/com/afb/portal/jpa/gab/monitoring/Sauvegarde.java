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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * Sauvegarde
 * @author Francis  
 * @version 1.0
 */
@Entity(name = "Sauvegarde")
@Table(name = "ATM_Sauvegarde")
public class Sauvegarde implements Serializable{

	/**
	 * ID Genere
	 */
	private static final long serialVersionUID = 1L;
		   
	/**
	 * Identifiant
	 */
	@Id
	@SequenceGenerator(name="Seq_Sauvegarde", sequenceName="SEQ_ATM_Sauvegarde", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Seq_Sauvegarde")
    @Column(name = "ID")
	private Long id;
	
	/**
	 * libelle 
	 */
	@Column(name = "LIBELLE")
	private String libelle;
			
	/**
	 * 
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "d_date")
	private Date date = new Date();
		
	/**
	 * Personnes
	 */
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="sauvegarde")
	private Set<SauvItem> items = new HashSet<SauvItem>();
	
	/**
	 * status
	 */
	@Column
	private Boolean status = Boolean.FALSE;
	
	/**
	 * Version
	 */
	@Version
	@Column(name ="VERSION")
	private Integer version = 0;

	/**
	 * 
	 */
	public Sauvegarde() {
		super();
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
	 * @return the items
	 */
	public Set<SauvItem> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(Set<SauvItem> items) {
		this.items = items;
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
		
}
