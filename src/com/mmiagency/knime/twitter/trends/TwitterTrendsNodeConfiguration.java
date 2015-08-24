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
