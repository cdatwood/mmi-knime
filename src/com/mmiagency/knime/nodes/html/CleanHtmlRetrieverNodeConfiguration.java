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
package com.mmiagency.knime.nodes.html;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.util.StringHistory;

public class CleanHtmlRetrieverNodeConfiguration {
	static final String FIELD_LABEL_URL_COLUMN = "URL Column Name (Required)";
	static final String FIELD_LABEL_CONTENT_COLUMN = "Content Column Name (Optional)";
	static final String FIELD_LABEL_OUTPUT_COLUMN = "Output Column Name (Default is \"XHTML\")";
	static final String FIELD_LABEL_XML = "Output result as XML (Will output as String if unchecked)";
	static final String FIELD_LABEL_USER_AGENT = "User Agent";
	static final String FIELD_LABEL_RETRIES = "Number of retries (per URL)";
	static final String FIELD_LABEL_ABSOLUTE_URLS = "Make absolute URLs";

	static final String FIELD_KEY_URL_COLUMN = "url";
	static final String FIELD_KEY_CONTENT_COLUMN = "content";
	static final String FIELD_KEY_OUTPUT_COLUMN = "output";
	static final String FIELD_KEY_XML = "xml";
	static final String FIELD_KEY_USER_AGENT = "userAgent";
	static final String FIELD_KEY_RETRIES = "retries";
	static final String FIELD_KEY_ABSOLUTE_URLS = "absoluteUrls";

	static final String FIELD_DEFAULT_URL_COLUMN = "url";
	static final String FIELD_DEFAULT_CONTENT_COLUMN = "";
	static final String FIELD_DEFAULT_OUTPUT_COLUMN = "XHTML";
	static final boolean FIELD_DEFAULT_XML = true;
	static final String FIELD_DEFAULT_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:40.0) Gecko/20100101 Firefox/40.0";
	static final String FIELD_DEFAULT_RETRIES = "3";
	static final boolean FIELD_DEFAULT_ABSOLUTE_URLS = false;

	private static final StringHistory USER_AGENT_HISTORY = StringHistory.getInstance(CleanHtmlRetrieverNodeConfiguration.class.getCanonicalName());

	private final Map<String, String> m_encodingMap = new HashMap<String, String>();

	private final SettingsModelString m_url = getUrlColumnSettingsModel();
	private final SettingsModelString m_content = getContentColumnSettingsModel();
	private final SettingsModelString m_output = getOutputColumnSettingsModel();
	private final SettingsModelBoolean m_xml = getXmlSettingsModel();
	private final SettingsModelString m_userAgent = getUserAgentSettingsModel();
	private final SettingsModelString m_retries = getRetriesSettingsModel();
	private final SettingsModelBoolean m_absoluteUrls = getAbsoluteUrlsSettingsModel();

	public CleanHtmlRetrieverNodeConfiguration() {
		// initialize list of available encoding
		Map<String, Charset> map = Charset.availableCharsets();
		for (Iterator<String> i = map.keySet().iterator(); i.hasNext();) {
			String key = i.next();
			m_encodingMap.put(key.toLowerCase(), key);
		}
		
    	if (USER_AGENT_HISTORY.getHistory().length == 0) {
    		USER_AGENT_HISTORY.add(FIELD_DEFAULT_USER_AGENT);
    	}
	}

    public static SettingsModelString getUrlColumnSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_URL_COLUMN, "");   
    }
    
    public static SettingsModelString getContentColumnSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_CONTENT_COLUMN, FIELD_DEFAULT_CONTENT_COLUMN);   
    }
    
    public static SettingsModelString getOutputColumnSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_OUTPUT_COLUMN, FIELD_DEFAULT_OUTPUT_COLUMN);   
    }
    
    public static SettingsModelBoolean getXmlSettingsModel() {
    	return new SettingsModelBoolean(FIELD_KEY_XML, FIELD_DEFAULT_XML);
    }
    
    public static SettingsModelString getUserAgentSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_USER_AGENT, FIELD_DEFAULT_USER_AGENT);   
    }
    
    public static SettingsModelString getRetriesSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_RETRIES, FIELD_DEFAULT_RETRIES);   
    }
    
    public static SettingsModelBoolean getAbsoluteUrlsSettingsModel() {
    	return new SettingsModelBoolean(FIELD_KEY_ABSOLUTE_URLS, FIELD_DEFAULT_ABSOLUTE_URLS);
    }

    public static List<String> getRetriesOptions() {
    	return Arrays.asList(new String[]{"1","2","3","4","5","6","7","8","9","10"});
    }

    public static StringHistory getUserAgentHistory() {
    	return USER_AGENT_HISTORY;
    }

    public SettingsModelString getUrl() {
    	return m_url;
    }
    
    public SettingsModelString getContent() {
    	return m_content;
    }
    
    public SettingsModelString getOutput() {
    	return m_output;
    }
    
    public SettingsModelBoolean getXml() {
    	return m_xml;
    }
    
    public Map<String, String> getEncodingMap() {
    	return m_encodingMap;
    }

    public SettingsModelString getUserAgent() {
    	return m_userAgent;
    }
    
    public SettingsModelString getRetries() {
    	return m_retries;
    }
    
    public SettingsModelBoolean getAbsoluteUrls() {
    	return m_absoluteUrls;
    }
    
    public void saveSettingsTo(final NodeSettingsWO settings) {
        m_url.saveSettingsTo(settings);
        m_content.saveSettingsTo(settings);
        m_output.saveSettingsTo(settings);
        m_xml.saveSettingsTo(settings);
        m_userAgent.saveSettingsTo(settings);
        m_retries.saveSettingsTo(settings);
        m_absoluteUrls.saveSettingsTo(settings);
        
        if (!m_userAgent.getStringValue().isEmpty()) {
        	USER_AGENT_HISTORY.add(m_userAgent.getStringValue());
        }
    }
    
    public void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        m_url.loadSettingsFrom(settings);
        m_content.loadSettingsFrom(settings);
        m_output.loadSettingsFrom(settings);
        m_xml.loadSettingsFrom(settings);
        m_userAgent.loadSettingsFrom(settings);
        m_retries.loadSettingsFrom(settings);
        m_absoluteUrls.loadSettingsFrom(settings);
        
        if (!m_userAgent.getStringValue().isEmpty()) {
        	USER_AGENT_HISTORY.add(m_userAgent.getStringValue());
        }
    }
    
    public void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        m_url.validateSettings(settings);
        m_content.validateSettings(settings);
        m_output.validateSettings(settings);
        m_xml.validateSettings(settings);
        m_userAgent.validateSettings(settings);
        m_retries.validateSettings(settings);
        m_absoluteUrls.validateSettings(settings);
        
        if (!m_userAgent.getStringValue().isEmpty()) {
        	USER_AGENT_HISTORY.add(m_userAgent.getStringValue());
        }
    }
    
}
