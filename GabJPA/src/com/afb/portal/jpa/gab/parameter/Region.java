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

import org.hibernate.validator.NotNull;

import com.afb.portal.jpa.gab.equipment.Atm;
import com.yashiro.persistence.utils.annotations.validator.SizeDAOValidator;
import com.yashiro.persistence.utils.annotations.validator.SizeDAOValidators;
import com.yashiro.persistence.utils.dao.constant.DAOMode;

/**
 * Region
 * @author Francis  
 * @version 1.0
 */
@SizeDAOValidators({
	@SizeDAOValidator(mode = DAOMode.SAVE,   expr = "from Region g where (g.code = ${code})", max = 0, message = "Region.code.exist"),
	@SizeDAOValidator(mode = DAOMode.SAVE,   expr = "from Region g where (g.libelle = ${libelle})", max = 0, message = "Region.libelle.exist"),
	@SizeDAOValidator(mode = DAOMode.UPDATE, expr = "from Region g where (g.id = ${id})", min = 1, message = "Region.update.id.notexist"),
	@SizeDAOValidator(mode = DAOMode.UPDATE,   expr = "from Region g where (g.id != ${id}) and (g.code = ${code})", max = 0, message = "Region.code.exist"),
	@SizeDAOValidator(mode = DAOMode.UPDATE,   expr = "from Region g where (g.id != ${id}) and (g.libelle = ${libelle})", max = 0, message = "Region.libelle.exist")
})
@Entity(name = "Region")
@Table(name = "ATM_RAGION")
public class Region implements Serializable{

	/**
	 * ID Genere
	 */
	private static final long serialVersionUID = 1L;
		   
	/**
	 * Identifiant
	 */
	@Id
	@SequenceGenerator(name="Seq_Region", sequenceName="SEQ_ATM_RAGION", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Seq_Region")
    @Column(name = "ID")
	private Long id;
	
	/**O
	 * Code de region
	 */
	@Column(name = "CODE")
	private String code;
		
	/**
	 * libelle de region
	 */
	@Column(name = "LIBELLE")
	@NotNull(message="libelle.notNul.region")
	private String libelle;
	
	/**
	 * liste des atms
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy="region")
	private Set<Atm> atms = new HashSet<Atm>();
	
	/**
	 * Version
	 */
	@Version
	@Column(name ="VERSION")
	private Integer version = 0;

	
	
	public Region() {
		super();
	}

	/**
	 * 
	 * @param code
	 * @param libelle
	 */
	public Region(String code, String libelle) {
		super();
		this.code = code;
		this.libelle = libelle;
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
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		Region other = (Region) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
	
}
