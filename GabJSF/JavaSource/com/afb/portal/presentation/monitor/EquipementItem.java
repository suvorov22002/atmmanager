package com.afb.portal.presentation.monitor;

import java.io.Serializable;

import com.afb.portal.jpa.gab.parameter.Equipement;

/**
 * 
 * @author Owner
 *
 */
public class EquipementItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Boolean active = Boolean.FALSE;
	
	private Equipement type ;

	public EquipementItem() {
		super();
	}

	public EquipementItem(Boolean active, Equipement type) {
		super();
		this.active = active;
		this.type = type;
	}
	
	

	public EquipementItem(Equipement type) {
		super();
		this.type = type;
	}

	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * @return the type
	 */
	public Equipement getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Equipement type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		EquipementItem other = (EquipementItem) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	
	
}
