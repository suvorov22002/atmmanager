package com.afb.portal.jpa.gab.parameter;

import java.io.Serializable;

/**
 * AtmIncident
 * @author Owner
 * @version 1.0
 */
public class AtmIncident implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String tid = "";

	private String atmName = "";

	private String codeInc = "";

	private String incName ="";

	private Integer noombre = 0;

	private Integer noombreAlerte = 0;

	
	public AtmIncident(String tid, String atmName, String codeInc,
			String incName, Integer noombre, Integer noombreAlerte) {
		super();
		this.tid = tid;
		this.atmName = atmName;
		this.codeInc = codeInc;
		this.incName = incName;
		this.noombre = noombre;
		this.noombreAlerte = noombreAlerte;
	}


	public AtmIncident() {
		super();
	}


	/**
	 * @return the tid
	 */
	public String getTid() {
		return tid;
	}


	/**
	 * @param tid the tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}


	/**
	 * @return the atmName
	 */
	public String getAtmName() {
		return atmName;
	}


	/**
	 * @param atmName the atmName to set
	 */
	public void setAtmName(String atmName) {
		this.atmName = atmName;
	}


	/**
	 * @return the codeInc
	 */
	public String getCodeInc() {
		return codeInc;
	}


	/**
	 * @param codeInc the codeInc to set
	 */
	public void setCodeInc(String codeInc) {
		this.codeInc = codeInc;
	}


	/**
	 * @return the incName
	 */
	public String getIncName() {
		return incName;
	}


	/**
	 * @param incName the incName to set
	 */
	public void setIncName(String incName) {
		this.incName = incName;
	}


	/**
	 * @return the noombre
	 */
	public Integer getNoombre() {
		return noombre;
	}


	/**
	 * @param noombre the noombre to set
	 */
	public void setNoombre(Integer noombre) {
		this.noombre = noombre;
	}


	/**
	 * @return the noombreAlerte
	 */
	public Integer getNoombreAlerte() {
		return noombreAlerte;
	}


	/**
	 * @param noombreAlerte the noombreAlerte to set
	 */
	public void setNoombreAlerte(Integer noombreAlerte) {
		this.noombreAlerte = noombreAlerte;
	}	

}
