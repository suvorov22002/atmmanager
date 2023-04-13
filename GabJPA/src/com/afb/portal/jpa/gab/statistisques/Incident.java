package com.afb.portal.jpa.gab.statistisques;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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

import com.afb.portal.jpa.gab.equipment.Atm;
import com.afb.portal.jpa.gab.parameter.TypeIncident;

/**
 * Incident
 * @author Incident
 * @version 1.0
 */
@Entity(name = "Incident")
@Table(name = "ATM_Incident")
public class Incident implements Serializable{

	public static final String ruptureFonds = "IN001";
	public static final String HorsService = "IN002";
	public static final String ConnexionReseau = "IN003";
	public static final String Bourrage = "IN004";
	public static final String ProblemeCaissettes = "IN005";
	public static final String ClavierEPPBloque = "IN006";
	public static final String JOURNAL = "IN007";
	public static final String VIDEO = "IN008";
	public static final String SoldeCritique = "IN009";
	public static final String SoldeMin = "IN010";
	public static final String TransactionTPE = "IN011";
	
	/**
	 * ID Genere
	 */
	private static final long serialVersionUID = 1L;
		   
	/**
	 * Identifiant
	 */
	@Id
	@SequenceGenerator(name="Seq_Incident", sequenceName="SEQ_ATM_Incident", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Seq_Incident")
    @Column(name = "ID")
	private Long id;
	
	/**
	 * atm
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ATM")
	private Atm atm;
		
	/**
	 * date
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "d_date")
	private Date date = new Date();
	
	/**
	 * 
	 */
	@Column(name = "txtdate")
	private String txtdate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
	
	/**
	 * 
	 */
	@Column(name = "txtheure")
	private String txtheure = new SimpleDateFormat("ddMMyyyyHHMMss").format(new Date());
	
	/**
	 * heure
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "d_time")
	private Date heure = new Date();
	
	/**
	 * TypeIncident
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TypeIncident")
	private TypeIncident typeIncident;
	
	/**
	 * nomber
	 */
	@Column(name = "Innomber")
	private Integer nomber = 0;
	
	/**
	 * nomber
	 */
	@Column(name = "InnomberNonOK")
	private Integer nomberNonOK = 0;
	
	/**
	 * nomber
	 */
	@Column(name = "motnomberOK")
	private Integer motnomberOK = 0;
	
	/**
	 * nomber
	 */
	@Column(name = "motnomberNonOK")
	private Integer motnomberNonOK = 0;
	
	/***
	 * 
	 */
	public Incident() {
		super();
	}

	
	
	/**
	 * @return the txtheure
	 */
	public String getTxtheure() {
		return txtheure;
	}



	/**
	 * @return the nomberNonOK
	 */
	public Integer getNomberNonOK() {
		if(nomberNonOK == null) nomberNonOK = 0;
		return nomberNonOK;
	}



	/**
	 * @param nomberNonOK the nomberNonOK to set
	 */
	public void setNomberNonOK(Integer nomberNonOK) {
		this.nomberNonOK = nomberNonOK;
	}



	/**
	 * @return the motnomberOK
	 */
	public Integer getMotnomberOK() {
		if(motnomberOK == null) motnomberOK = 0;
		return motnomberOK;
	}



	/**
	 * @param motnomberOK the motnomberOK to set
	 */
	public void setMotnomberOK(Integer motnomberOK) {
		this.motnomberOK = motnomberOK;
	}



	/**
	 * @return the motnomberNonOK
	 */
	public Integer getMotnomberNonOK() {
		if(motnomberNonOK == null) motnomberNonOK = 0;
		return motnomberNonOK;
	}



	/**
	 * @param motnomberNonOK the motnomberNonOK to set
	 */
	public void setMotnomberNonOK(Integer motnomberNonOK) {
		this.motnomberNonOK = motnomberNonOK;
	}



	/**
	 * @param txtheure the txtheure to set
	 */
	public void setTxtheure(String txtheure) {
		this.txtheure = txtheure;
	}



	/**
	 * @return the nomber
	 */
	public Integer getNomber() {
		return nomber;
	}



	/**
	 * @param nomber the nomber to set
	 */
	public void setNomber(Integer nomber) {
		this.nomber = nomber;
	}



	/**
	 * @return the typeIncident
	 */
	public TypeIncident getTypeIncident() {
		return typeIncident;
	}



	/**
	 * @param typeIncident the typeIncident to set
	 */
	public void setTypeIncident(TypeIncident typeIncident) {
		this.typeIncident = typeIncident;
	}



	/**
	 * @return the txtdate
	 */
	public String getTxtdate() {
		return txtdate;
	}



	/**
	 * @param txtdate the txtdate to set
	 */
	public void setTxtdate(String txtdate) {
		this.txtdate = txtdate;
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
	 * @return the heure
	 */
	public Date getHeure() {
		return heure;
	}

	/**
	 * @param heure the heure to set
	 */
	public void setHeure(Date heure) {
		this.heure = heure;
	}
	
}
