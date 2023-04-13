package com.afb.portal.jpa.gab.parameter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import com.afb.portal.jpa.gab.equipment.Atm;
import com.yashiro.persistence.utils.annotations.validator.SizeDAOValidator;
import com.yashiro.persistence.utils.annotations.validator.SizeDAOValidators;
import com.yashiro.persistence.utils.dao.constant.DAOMode;

/**
 * TypeIntervention
 * @author Francis  
 * @version 1.0
 */
@SizeDAOValidators({
	@SizeDAOValidator(mode = DAOMode.SAVE,   expr = "from GroupeSauv g where (g.libelle = ${libelle})", max = 0, message = "GroupeSauv.libelle.exist"),
	@SizeDAOValidator(mode = DAOMode.UPDATE, expr = "from GroupeSauv g where (g.id = ${id})", min = 1, message = "TypeIntervention.update.id.notexist"),
	@SizeDAOValidator(mode = DAOMode.UPDATE,   expr = "from GroupeSauv g where (g.id != ${id}) and (g.libelle = ${libelle})", max = 0, message = "TypeIntervention.libelle.exist")
})
@Entity(name = "GroupeSauv")
@Table(name = "ATM_GroupeSauv")
public class GroupeSauv implements Serializable{

	/**
	 * ID Genere
	 */
	private static final long serialVersionUID = 1L;
		   
	/**
	 * Identifiant
	 */
	@Id
	@SequenceGenerator(name="Seq_GroupeSauv", sequenceName="SEQ_ATM_GroupeSauv", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Seq_GroupeSauv")
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
	@Column(name = "chemin")
	private String chemin;
	
	/**
	 * libelle 
	 */
	@Column(name = "descrip")
	private String description;
	
	
	/**
	 * Ip
	 */
	@Column(name = "ATM_IP")
	private String ip;
	
	/**
	 * Utilisateur
	 */
	@Column(name = "ATM_USR")
	private String login;
	
	/**
	 * password
	 */
	@Column(name = "ATM_PSW")
	private String psw;
	
	/**
	 * groupe de Sauvegarde
	 */
	@OneToMany(fetch = FetchType.EAGER,mappedBy="groupeSauv")
	private Set<Atm> atms = new HashSet<Atm>();
		
	/**
	 * Version
	 */
	@Version
	@Column(name ="VERSION")
	private Integer version = 0;

	
	
	public GroupeSauv() {
		super();
	}

	
	
	/**
	 * @return the atms
	 */
	public Set<Atm> getAtms() {
		return atms;
	}



	/**
	 * @param atms the atms to set
	 */
	public void setAtms(Set<Atm> atms) {
		this.atms = atms;
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
