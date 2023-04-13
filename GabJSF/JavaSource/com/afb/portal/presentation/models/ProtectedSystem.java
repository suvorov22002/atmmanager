package com.afb.portal.presentation.models;

import java.io.Serializable;


/**
 * classe de protection des menu
 * @author Francis Konchou
 * @version 1.0
 */
public class ProtectedSystem implements Serializable{
	
	/**
	 * ID 
	 */
	private static final long serialVersionUID = 1L;
	
	
	// Credit
	public static Boolean seeAllCredit = Boolean.FALSE;
	
	public static Boolean validateCredit = Boolean.FALSE;
	
	public static Boolean CancelCredit = Boolean.FALSE;
	
	public static Boolean printCredit = Boolean.FALSE;
	
	public static Boolean IntiCredit = Boolean.FALSE;
	
	public static Boolean filterCredit = Boolean.FALSE;
	
	public static Boolean saveCredit = Boolean.FALSE;
	
	public static Boolean updateCredit = Boolean.FALSE;
	
	public static Boolean filterStatCredit = Boolean.FALSE;

	/**
	 * 
	 */
	public ProtectedSystem() {
		super();
	}

	
	
	/**
	 * @return the filterStatCredit
	 */
	public  Boolean getFilterStatCredit() {
		return filterStatCredit;
	}



	/**
	 * @param filterStatCredit the filterStatCredit to set
	 */
	public  void setFilterStatCredit(Boolean filterStatCredit) {
		ProtectedSystem.filterStatCredit = filterStatCredit;
	}

	/**
	 * @return the seeAllCredit
	 */
	public  Boolean getSeeAllCredit() {
		return seeAllCredit;
	}

	/**
	 * @param seeAllCredit the seeAllCredit to set
	 */
	public  void setSeeAllCredit(Boolean seeAllCredit) {
		ProtectedSystem.seeAllCredit = seeAllCredit;
	}

	/**
	 * @return the validateCredit
	 */
	public  Boolean getValidateCredit() {
		return validateCredit;
	}

	/**
	 * @param validateCredit the validateCredit to set
	 */
	public  void setValidateCredit(Boolean validateCredit) {
		ProtectedSystem.validateCredit = validateCredit;
	}

	/**
	 * @return the cancelCredit
	 */
	public  Boolean getCancelCredit() {
		return CancelCredit;
	}

	/**
	 * @param cancelCredit the cancelCredit to set
	 */
	public  void setCancelCredit(Boolean cancelCredit) {
		CancelCredit = cancelCredit;
	}

	/**
	 * @return the printCredit
	 */
	public  Boolean getPrintCredit() {
		return printCredit;
	}

	/**
	 * @param printCredit the printCredit to set
	 */
	public  void setPrintCredit(Boolean printCredit) {
		ProtectedSystem.printCredit = printCredit;
	}

	/**
	 * @return the intiCredit
	 */
	public  Boolean getIntiCredit() {
		return IntiCredit;
	}

	/**
	 * @param intiCredit the intiCredit to set
	 */
	public  void setIntiCredit(Boolean intiCredit) {
		IntiCredit = intiCredit;
	}

	/**
	 * @return the filterCredit
	 */
	public  Boolean getFilterCredit() {
		return filterCredit;
	}

	/**
	 * @param filterCredit the filterCredit to set
	 */
	public  void setFilterCredit(Boolean filterCredit) {
		ProtectedSystem.filterCredit = filterCredit;
	}

	/**
	 * @return the saveCredit
	 */
	public  Boolean getSaveCredit() {
		return saveCredit;
	}

	/**
	 * @param saveCredit the saveCredit to set
	 */
	public  void setSaveCredit(Boolean saveCredit) {
		ProtectedSystem.saveCredit = saveCredit;
	}

	/**
	 * @return the updateCredit
	 */
	public  Boolean getUpdateCredit() {
		return updateCredit;
	}

	/**
	 * @param updateCredit the updateCredit to set
	 */
	public  void setUpdateCredit(Boolean updateCredit) {
		ProtectedSystem.updateCredit = updateCredit;
	}

	
}
