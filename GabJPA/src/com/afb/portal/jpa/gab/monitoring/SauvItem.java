package com.afb.portal.jpa.gab.monitoring;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.afb.portal.jpa.gab.equipment.Atm;

/**
 * SauvItem
 * @author Francis  
 * @version 1.0
 */
@Entity(name = "SauvItem")
@Table(name = "ATM_SauvItem")
public class SauvItem implements Serializable{

	/**
	 * ID Genere
	 */
	private static final long serialVersionUID = 1L;
		   
	/**
	 * Identifiant
	 */
	@Id
	@SequenceGenerator(name="Seq_SauvItem", sequenceName="SEQ_ATM_SauvItem", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Seq_SauvItem")
    @Column(name = "ID")
	private Long id;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ATM")
	private Atm atm;
	
	@Column(name = "journal")
	private Boolean journal = Boolean.TRUE;
	
	@Column(name = "images")
	private Boolean images = Boolean.TRUE;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "d_date")
	private Date date = new Date();
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sauv")
	private Sauvegarde sauvegarde;
		
	@Column(name = "chemin")
	private String chemin;
			
	/**
	 * Version
	 */
	@Version
	@Column(name ="VERSION")
	private Integer version = 0;

	
	
	public SauvItem() {
		super();
	}

	/**
	 * 
	 * @param atm
	 * @param sauvegarde
	 * @param chemin
	 */
	public SauvItem(Atm atm, Sauvegarde sauvegarde, String chemin) {
		super();
		this.atm = atm;
		this.sauvegarde = sauvegarde;
		this.chemin = chemin;
	}


	/**
	 * 
	 * @param atm
	 * @param journal
	 * @param images
	 * @param sauvegarde
	 * @param chemin
	 */
	public SauvItem(Atm atm, Boolean journal, Boolean images,
			Sauvegarde sauvegarde, String chemin) {
		super();
		this.atm = atm;
		this.journal = journal;
		this.images = images;
		this.sauvegarde = sauvegarde;
		this.chemin = chemin;
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
	 * @return the chemin
	 */
	public String getChemin() {
		return chemin;
	}

	/**
	 * @param chemin the chemin to set
	 */
	public void setChemin(String chemin) {
		this.chemin = chemin;
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
	 * @return the images
	 */
	public Boolean getImages() {
		return images;
	}

	/**
	 * @param images the images to set
	 */
	public void setImages(Boolean images) {
		this.images = images;
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
	 * @return the sauvegarde
	 */
	public Sauvegarde getSauvegarde() {
		return this.sauvegarde;
	}

	/**
	 * @param sauvegarde the sauvegarde to set
	 */
	public void setSauvegarde(Sauvegarde sauvegarde) {
		this.sauvegarde = sauvegarde;
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
