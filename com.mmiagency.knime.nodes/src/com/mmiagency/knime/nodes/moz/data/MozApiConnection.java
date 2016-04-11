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
package com.mmiagency.knime.nodes.moz.data;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.ModelContentWO;
import com.mmiagency.knime.nodes.moz.api.authentication.Authenticator;

/**
 * Object that represents a connection to the Moz API.
 *
 * @author Phuc Truong
 */
public final class MozApiConnection {

    private static final String API_ACCESS_ID = "accessId";

    private static final String API_SECRET_KEY = "secretKey";

    private String m_apiAccessId;

    private String m_apiSecretKey;

    private Authenticator m_mozAuthenticator;

    /**
     * @param accessToken The access token
     * @param accessTokenSecret The access token secret
     * @throws InvalidSettingsException If the parameters are invalid
     */
    public MozApiConnection(final String accessToken, final String accessTokenSecret) throws InvalidSettingsException {
        m_apiAccessId = accessToken;
        m_apiSecretKey = accessTokenSecret;
        m_mozAuthenticator = new Authenticator(m_apiAccessId, m_apiSecretKey);
    }

    /**
     * @param model The model containing the connection information
     * @throws InvalidSettingsException If the model was invalid
     */
    public MozApiConnection(final ModelContentRO model) throws InvalidSettingsException {
        this(model.getString(API_ACCESS_ID, ""), model.getString(API_SECRET_KEY, ""));
    }

    /**
     * @return The moz object
     */
    public Authenticator getMozAuthenticator() {
        return m_mozAuthenticator;
    }

    /**
     * @param model The model to save the current configuration in
     */
    public void save(final ModelContentWO model) {
        model.addString(API_ACCESS_ID, m_apiAccessId);
        model.addString(API_SECRET_KEY, m_apiSecretKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;
        
        Authenticator mozAuthenticator = (Authenticator)obj;
        if (!mozAuthenticator.getAccessID().equals(mozAuthenticator.getAccessID())) return false;
        if (!mozAuthenticator.getSecretKey().equals(mozAuthenticator.getSecretKey())) return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return m_apiAccessId.hashCode() + m_apiSecretKey.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Access ID:\n" + m_apiAccessId + "\n\n");
        return sb.toString();
    }

}
