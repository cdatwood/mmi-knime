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
package com.mmiagency.knime.nodes.moz.linkmetrics;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class MozLinkMetricsNodeConfiguration {


	public MozLinkMetricsNodeConfiguration() {
   	
	}

	// URL
	private final SettingsModelString m_url = getUrlColumnSettingsModel();	
    public static SettingsModelString getUrlColumnSettingsModel() {return new SettingsModelString("url", "");}
	public SettingsModelString getUrl() {return m_url;}

	// Scope
	private final SettingsModelString m_scope = getScopeSettingsModel();	
    public static SettingsModelString getScopeSettingsModel() {return new SettingsModelString("scope", "");}
	public SettingsModelString getScope() {return m_scope;}

	// Sort
	private final SettingsModelString m_sort = getSortSettingsModel();	
    public static SettingsModelString getSortSettingsModel() {return new SettingsModelString("sort", "");}
	public SettingsModelString getSort() {return m_sort;}

	// Max Results
	private final SettingsModelIntegerBounded m_maxResults = getMaxResultsSettingsModel();	
    public static SettingsModelIntegerBounded getMaxResultsSettingsModel() {return new SettingsModelIntegerBounded("noOfRows", 100, 1, Integer.MAX_VALUE);}
	public SettingsModelIntegerBounded getMaxResults() {return m_maxResults;}

	// Delay Between Calls
	private final SettingsModelDoubleBounded m_delayBetweenCalls = getDelayBetweenCallsSettingsModel();	
    public static SettingsModelDoubleBounded getDelayBetweenCallsSettingsModel() {return new SettingsModelDoubleBounded("delayBetweenCalls", 10, 0, 60);}
	public SettingsModelDoubleBounded getDelayBetweenCalls() {return m_delayBetweenCalls;}
	
	
    public void saveSettingsTo(final NodeSettingsWO settings) {
        m_url.saveSettingsTo(settings);
        m_scope.saveSettingsTo(settings);
        m_sort.saveSettingsTo(settings);
        m_maxResults.saveSettingsTo(settings);
        m_delayBetweenCalls.saveSettingsTo(settings);
    }

    public void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        m_url.loadSettingsFrom(settings);
        m_scope.loadSettingsFrom(settings);
        m_sort.loadSettingsFrom(settings);
        m_maxResults.loadSettingsFrom(settings);
        m_delayBetweenCalls.loadSettingsFrom(settings);
    }

    public void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
    	m_url.validateSettings(settings);
    	m_scope.validateSettings(settings);
    	m_sort.validateSettings(settings);
    	m_maxResults.validateSettings(settings);
    	m_delayBetweenCalls.validateSettings(settings);
    }
  
}

