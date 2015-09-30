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
package com.mmiagency.knime.nodes.moz.connector;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.mmiagency.knime.nodes.util.Util;
import com.mmiagency.knime.nodes.moz.data.MozApiConnection;

/**
 * @author Phuc Truong
 */
public class MozAPIConnectorConfiguration {

    private static final String ACCESS_ID = "apiAccessID";

    private static final String SECRET_KEY = "apiSecretKey";

    private String m_apiAccessID;

    private String m_apiSecretKey;

    /**
     * @param settings The settings to load from
     * @throws InvalidSettingsException If the settings are invalid
     */
    public void loadInModel(final NodeSettingsRO settings) throws InvalidSettingsException {
        m_apiAccessID = settings.getString(ACCESS_ID);
        m_apiSecretKey = settings.getString(SECRET_KEY);
    }

    /**
     * @param settings The settings to load from
     */
    public void loadInDialog(final NodeSettingsRO settings) {
        m_apiAccessID = settings.getString(ACCESS_ID, null);
        m_apiSecretKey = settings.getString(SECRET_KEY, null);
    }

    /**
     * @param settings The settings to save to
     */
    public void save(final NodeSettingsWO settings) {
        settings.addString(ACCESS_ID, m_apiAccessID);
        settings.addString(SECRET_KEY, m_apiSecretKey);
    }

    /**
     * @return A TwiiterApiConnection based on the current settings
     * @throws InvalidSettingsException If the current settings are invalid
     */
    public MozApiConnection createMozApiConnection() throws InvalidSettingsException {
        if (Util.isBlankOrNull(m_apiAccessID)) throw new InvalidSettingsException("Access ID is missing");
        if (Util.isBlankOrNull(m_apiSecretKey)) throw new InvalidSettingsException("Secret Key is missing");
        
        return new MozApiConnection(m_apiAccessID, m_apiSecretKey);
    }

    /**
     * @return the apiAccessID
     */
    public String getAccessID() {
        return m_apiAccessID;
    }

    /**
     * @param apiAccessID the apiAccessID to set
     */
    public void setAccessID(final String apiAccessID) {
        m_apiAccessID = apiAccessID;
    }

    /**
     * @return the apiSecretKey
     */
    public String getSecretKey() {
        return m_apiSecretKey;
    }

    /**
     * @param apiSecretKey the apiSecretKey to set
     */
    public void setSecretKey(final String apiSecretKey) {
        m_apiSecretKey = apiSecretKey;
    }

}
