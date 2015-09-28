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
package com.mmiagency.knime.nodes.moz.urlmetrics;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.util.StringHistory;

import com.mmiagency.knime.nodes.moz.data.MozApiConnection;
import com.mmiagency.knime.nodes.util.Util;

public class MozUrlMetricsNodeConfiguration {

	static final String FIELD_LABEL_URL_COLUMN = "URL Column Name";

	static final String FIELD_KEY_URL_COLUMN = "urlColumn";

	static final String FIELD_DEFAULT_URL_COLUMN = "url";

	private final SettingsModelString m_url = getUrlColumnSettingsModel();	

	public MozUrlMetricsNodeConfiguration() {
    	
	}
	
    public static SettingsModelString getUrlColumnSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_URL_COLUMN, "");   
    }
	
	public SettingsModelString getUrl() {
		return m_url;
	}
	
    public void saveSettingsTo(final NodeSettingsWO settings) {

        m_url.saveSettingsTo(settings);
    }

    public void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        m_url.loadSettingsFrom(settings);
    }

    public void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

    	m_url.validateSettings(settings);
    }
    
}