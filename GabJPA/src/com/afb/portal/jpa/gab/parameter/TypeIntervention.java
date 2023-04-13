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

import com.yashiro.persistence.utils.annotations.validator.SizeDAOValidator;
import com.yashiro.persistence.utils.annotations.validator.SizeDAOValidators;
import com.yashiro.persistence.utils.dao.constant.DAOMode;

/**
 * TypeIntervention
 * @author Francis  
 * @version 1.0
 */
@SizeDAOValidators({
	@SizeDAOValidator(mode = DAOMode.SAVE,   expr = "from TypeIntervention g where (g.libelle = ${libelle})", max = 0, message = "TypeIntervention.libelle.exist"),
	@SizeDAOValidator(mode = DAOMode.UPDATE, expr = "from TypeIntervention g where (g.id = ${id})", min = 1, message = "TypeIntervention.update.id.notexist"),
	@SizeDAOValidator(mode = DAOMode.UPDATE,   expr = "from TypeIntervention g where (g.id != ${id}) and (g.libelle = ${libelle})", max = 0, message = "TypeIntervention.libelle.exist")
})
@Entity(name = "TypeIntervention")
@Table(name = "ATM_TIntervention")
public class TypeIntervention implements Serializable{

	/**
	 * ID Genere
	 */
	private static final long serialVersionUID = 1L;
		   
	/**
	 * Identifiant
	 */
	@Id
	@SequenceGenerator(name="Seq_TypeIntervention", sequenceName="SEQ_ATM_TIntervention", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Seq_TypeIntervention")
    @Column(name = "ID")
	private Long id;
	
	
	/**
	 * libelle 
	 */
	@Column(name = "LIBELLE")
	private String libelle;
	
	/**
	 * libelle 
	 */
	@Column(name = "descrip")
	private String description;
		
	/**
	 * Version
	 */
	@Version
	@Column(name ="VERSION")
	private Integer version = 0;

	
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		TypeIntervention other = (TypeIntervention) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
