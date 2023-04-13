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

import com.yashiro.persistence.utils.annotations.validator.SizeDAOValidator;
import com.yashiro.persistence.utils.annotations.validator.SizeDAOValidators;
import com.yashiro.persistence.utils.dao.constant.DAOMode;

/**
 * Type Equipement 
 * @author Francis  
 * @version 1.0
 */
@SizeDAOValidators({
	@SizeDAOValidator(mode = DAOMode.SAVE,   expr = "from TypeEquipement g where (g.libelle = ${libelle})", max = 0, message = "TypeEquipement.libelle.exist"),
	@SizeDAOValidator(mode = DAOMode.UPDATE, expr = "from TypeEquipement g where (g.id = ${id})", min = 1, message = "TypeEquipement.update.id.notexist"),
	@SizeDAOValidator(mode = DAOMode.UPDATE,   expr = "from TypeEquipement g where (g.id != ${id}) and (g.libelle = ${libelle})", max = 0, message = "TypeEquipement.libelle.exist")
})
@Entity(name = "TypeEquipement")
@Table(name = "ATM_TEquipement")
public class TypeEquipement implements Serializable{

	/**
	 * ID Genere
	 */
	private static final long serialVersionUID = 1L;
		   
	/**
	 * Identifiant
	 */
	@Id
	@SequenceGenerator(name="Seq_TypeEquipement", sequenceName="SEQ_ATM_TEquipement", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Seq_TypeEquipement")
    @Column(name = "ID")
	private Long id;
	
	
	/**
	 * libelle 
	 */
	@Column(name = "LIBELLE")
	private String libelle;
	
	/**
	 * description 
	 */
	@Column(name = "descrip")
	private String description;
	
	
	/**
	 * Liste des Equipements
	 */
	@OneToMany(fetch = FetchType.EAGER,mappedBy="typeEquipement")
	private Set<Equipement> equipements = new HashSet<Equipement>();
		
	/**
	 * Version
	 */
	@Version
	@Column(name ="VERSION")
	private Integer version = 0;

	
	
	
	public TypeEquipement() {
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
