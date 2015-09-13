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
package com.mmiagency.knime.twitter.trends;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public class TwitterTrendsNodeConfiguration {
	
    private static final String CFG_WOEID = "woeid";
    private static final String CFG_EXCLUDE = "exclude";

    private static final int DEFAULT_WOEID = 1;
    
    private int m_woeid = DEFAULT_WOEID;
	private String m_exclude;

	public TwitterTrendsNodeConfiguration() {
		
	}
	
    /**
     * @param settings The settings to load from
     * @throws InvalidSettingsException If the settings are invalid
     */
    public void loadInModel(final NodeSettingsRO settings) throws InvalidSettingsException {
    	m_woeid = settings.getInt(CFG_WOEID);
    	m_exclude = settings.getString(CFG_EXCLUDE);
    	if (m_woeid <= 0) {
            throw new InvalidSettingsException("WOEID is mandatory");
    	}
    }

    /**
     * @param settings The settings to load from
     */
    public void loadInDialog(final NodeSettingsRO settings) {
    	m_woeid = settings.getInt(CFG_WOEID, DEFAULT_WOEID);
    	m_exclude = settings.getString(CFG_EXCLUDE, "");
    }

    /**
     * @param settings The settings to save to
     */
    public void save(final NodeSettingsWO settings) {
    	settings.addInt(CFG_WOEID, m_woeid);    	
        settings.addString(CFG_EXCLUDE, m_exclude);
    }
	

	/**
	 * @return the m_woeid
	 */
	public int getWoeid() {
		return m_woeid;
	}

	/**
	 * @param m_woeid the m_woeid to set
	 */
	public void setWoeid(int m_woeid) {
		this.m_woeid = m_woeid;
	}

	/**
	 * @return the m_exclude
	 */
	public String getExclude() {
		return m_exclude;
	}

	/**
	 * @param m_exclude the m_exclude to set
	 */
	public void setExclude(String m_exclude) {
		this.m_exclude = m_exclude;
	}
	
	
}
