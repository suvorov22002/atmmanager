package com.afb.portal.jpa.gab.parameter;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity(name = "Monitoring")
@Table(name = "ATM_MonService")
public class Monitoring implements Serializable {
	
	/**
	 * ID Genere
	 */
	private static final long serialVersionUID = 1L;
		   
	/**
	 * Identifiant
	 */
	@Id
	@SequenceGenerator(name="Seq_Monitoring", sequenceName="SEQ_ATM_MonService", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Seq_Monitoring")
    @Column(name = "ID")
	private Long id;
	
	
	@Column(name = "Mtype")
	@Enumerated(EnumType.STRING)
	private MonitoringType type ;

	@Column(name = "Mlibelle")
	private String libelle;
	
	@Column(name = "Mhost")
	private String host;
	
	@Column(name = "Mname")
	private String name;
	
	@Column(name = "Mtel")
	private String txtphone;
	
	@Column(name = "MEmail")
	private String txtemail;
	
	@Column(name="MDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date = new Date();

	public Monitoring(MonitoringType type, String libelle, String host,
			String name, Date date) {
		super();
		this.type = type;
		this.libelle = libelle;
		this.host = host;
		this.name = name;
		this.date = date;
	}

	
	
	public Monitoring(MonitoringType type, String libelle, String host,
			String name, String txtphone, String txtemail) {
		super();
		this.type = type;
		this.libelle = libelle;
		this.host = host;
		this.name = name;
		this.txtphone = txtphone;
		this.txtemail = txtemail;
	}



	public Monitoring() {
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
	 * @return the type
	 */
	public MonitoringType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(MonitoringType type) {
		this.type = type;
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
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the txtphone
	 */
	public String getTxtphone() {
		return txtphone;
	}

	/**
	 * @param txtphone the txtphone to set
	 */
	public void setTxtphone(String txtphone) {
		this.txtphone = txtphone;
	}

	/**
	 * @return the txtemail
	 */
	public String getTxtemail() {
		return txtemail;
	}

	/**
	 * @param txtemail the txtemail to set
	 */
	public void setTxtemail(String txtemail) {
		this.txtemail = txtemail;
	}
	
	
	
	
}
