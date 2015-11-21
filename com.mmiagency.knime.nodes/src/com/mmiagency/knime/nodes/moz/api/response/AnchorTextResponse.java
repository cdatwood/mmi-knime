/*
 * ------------------------------------------------------------------------
 * Copyright by MMI Agency, Houston, Texas, USA
 * Website: http://www.mmiagency.com; Contact: 713-929-6900
 *
 * The MMI KNIME Node is Copyright (C) 2015, MMI Agency The KNIME Nodes 
 * are free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your 
 * option) any later version. 
 * 
 * The KNIME Nodes are distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details. You should have received a 
 * copy of the GNU General Public License along with the KNIME Nodes. If 
 * not, see <http://www.gnu.org/licenses/>.
 * ------------------------------------------------------------------------
 */
package com.mmiagency.knime.nodes.moz.api.response;

/**
 * 
 * A Pojo to capture the json response from
 * AnchorText Api
 * 
 * @author Radeep Solutions
 */
public class AnchorTextResponse 
{
	private String atuef;
	
	private String atuemp;
	
	private String atuep;
	
	private String atueu;
	
	private String atuf;
	
	private String atuif;
	
	private String atuimp;
	
	private String atuiu;
	
	private String atut;

	/**
	 * @return the atuef
	 */
	public String getAtuef() {
		return atuef;
	}

	/**
	 * @param atuef the atuef to set
	 */
	public void setAtuef(String atuef) {
		this.atuef = atuef;
	}

	/**
	 * @return the atuemp
	 */
	public String getAtuemp() {
		return atuemp;
	}

	/**
	 * @param atuemp the atuemp to set
	 */
	public void setAtuemp(String atuemp) {
		this.atuemp = atuemp;
	}

	/**
	 * @return the atuep
	 */
	public String getAtuep() {
		return atuep;
	}

	/**
	 * @param atuep the atuep to set
	 */
	public void setAtuep(String atuep) {
		this.atuep = atuep;
	}

	/**
	 * @return the atueu
	 */
	public String getAtueu() {
		return atueu;
	}

	/**
	 * @param atueu the atueu to set
	 */
	public void setAtueu(String atueu) {
		this.atueu = atueu;
	}

	/**
	 * @return the atuf
	 */
	public String getAtuf() {
		return atuf;
	}

	/**
	 * @param atuf the atuf to set
	 */
	public void setAtuf(String atuf) {
		this.atuf = atuf;
	}

	/**
	 * @return the atuif
	 */
	public String getAtuif() {
		return atuif;
	}

	/**
	 * @param atuif the atuif to set
	 */
	public void setAtuif(String atuif) {
		this.atuif = atuif;
	}

	/**
	 * @return the atuimp
	 */
	public String getAtuimp() {
		return atuimp;
	}

	/**
	 * @param atuimp the atuimp to set
	 */
	public void setAtuimp(String atuimp) {
		this.atuimp = atuimp;
	}

	/**
	 * @return the atuiu
	 */
	public String getAtuiu() {
		return atuiu;
	}

	/**
	 * @param atuiu the atuiu to set
	 */
	public void setAtuiu(String atuiu) {
		this.atuiu = atuiu;
	}

	/**
	 * @return the atut
	 */
	public String getAtut() {
		return atut;
	}

	/**
	 * @param atut the atut to set
	 */
	public void setAtut(String atut) {
		this.atut = atut;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AnchorTextResponse [atuef=" + atuef + ", atuemp=" + atuemp
				+ ", atuep=" + atuep + ", atueu=" + atueu + ", atuf=" + atuf
				+ ", atuif=" + atuif + ", atuimp=" + atuimp + ", atuiu="
				+ atuiu + ", atut=" + atut + "]";
	}
}
