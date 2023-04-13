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
@Entity(name = "GabParameter")
@Table(name = "ATM_PARAM")
public class GabParameter implements Serializable{

	/**
	 * ID Genere
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Identifiant
	 */
	@Id
	@SequenceGenerator(name="Seq_GabParameter", sequenceName="SEQ_ATM_PARAM", allocationSize = 1, initialValue = 60)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Seq_GabParameter")
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
	@Column(name = "DESCRIPTION")
	private String descrption ;
	
	/**
	 * Code
	 */
	@Column(name = "PARAMM")
	private String value;
	
	/**
	 * Version de l'enregistrement
	 */
	@Version
	@Column(name = "VERSION")
	private Integer version = 0;

	/**
	 * @param code
	 * @param descrption
	 * @param parameter
	 */
	public GabParameter(String code, String descrption, String parameter) {
		super();
		this.code = code;
		this.descrption = descrption;
		this.value = parameter;
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
	 * 
	 */
	public GabParameter() {
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
	 * @return the parameter
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param parameter the parameter to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}
