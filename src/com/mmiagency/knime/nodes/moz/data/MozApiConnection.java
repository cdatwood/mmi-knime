package com.mmiagency.knime.nodes.moz.data;

import java.io.IOException;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.ModelContentWO;
import org.knime.core.util.KnimeEncryption;

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
        this(model.getString(API_ACCESS_ID), model.getString(API_SECRET_KEY));
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
        if (!(obj instanceof Authenticator)) return false;
        
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
