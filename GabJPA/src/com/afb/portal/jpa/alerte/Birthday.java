package com.afb.portal.jpa.alerte;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Birthday
 * @author Owner
 * @version 1.0
 */
@Entity(name = "Birthday")
@Table(name = "ATM_Birthday")
public class Birthday implements Serializable {

	/**
	 * ID Genere
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Identifiant
	 */
	@Id
	@Column(name = "ID")
	private String id;
	
	@Column(name = "carte")
	private String carte;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name="Bselect")
	private Boolean select = Boolean.FALSE;
	
	@Column(name="Bsend")
	private Boolean send = Boolean.TRUE;
	
	@Column(name="BDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date = new Date();
	
	@Version
	@Column(name="Bversion")
	private int version;

	/**@PrePersist
	public void birthday() {
		setId(RandomStringUtils.randomAlphanumeric(10));
	}*/
	
	public Birthday() {
		super();
	}


	public Birthday(String carte, String name, String phone) {
		super();
		setId(RandomStringUtils.randomAlphanumeric(10));
		this.carte = carte;
		this.name = name;
		this.phone = phone;
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
	 * @return the send
	 */
	public Boolean getSend() {
		return send;
	}


	/**
	 * @param send the send to set
	 */
	public void setSend(Boolean send) {
		this.send = send;
	}


	/**
	 * @return the select
	 */
	public Boolean getSelect() {
		return select;
	}

	/**
	 * @param select the select to set
	 */
	public void setSelect(Boolean select) {
		this.select = select;
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
	 * @return the carte
	 */
	public String getCarte() {
		return carte;
	}

	/**
	 * @param carte the carte to set
	 */
	public void setCarte(String carte) {
		this.carte = carte;
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
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}
	
	
	
}
