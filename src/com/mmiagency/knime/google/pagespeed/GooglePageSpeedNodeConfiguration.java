package com.mmiagency.knime.google.pagespeed;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class GooglePageSpeedNodeConfiguration {

    // static variables for setting dialog labels, internal key for settings and default values
    static final String REST_URL = "https://www.googleapis.com/pagespeedonline/v2/runPagespeed?url=";
    
	static final String FIELD_LABEL_URL_COLUMN = "URL Column Name";
	static final String FIELD_LABEL_API_KEY = "Google API Key";
	static final String FIELD_LABEL_FILTER_THIRD_PARTY_RESOURCES = "Filter Third Party Resources";
	static final String FIELD_LABEL_LOCALE = "Locale";
	static final String FIELD_LABEL_STRATEGY = "Strategy";
	
	static final String FIELD_KEY_URL_COLUMN = "urlColumn";
	static final String FIELD_KEY_API_KEY = "apiKey";
	static final String FIELD_KEY_FILTER_THIRD_PARTY_RESOURCES = "filterThirdPartyResources";
	static final String FIELD_KEY_LOCALE = "locale";
	static final String FIELD_KEY_STRATEGY = "strategy";
	
	static final String FIELD_DEFAULT_URL_COLUMN = "url";
	static final String FIELD_DEFAULT_LOCALE = "en_US";
	static final String FIELD_DEFAULT_STRATEGY = "desktop";
	static final String[] FIELD_OPTIONS_STRATEGY = new String[]{"desktop", "mobile"};

	// settings for storing and values evaluation
	private final SettingsModelString m_url = getUrlColumnSettingsModel();	
	private final SettingsModelString m_apikey = getApiKeySettingsModel();
	private final SettingsModelBoolean m_filterThirdPartyResources = getFilterThirdPartyResourcesSettingsModel();
	private final SettingsModelString m_locale = getLocaleSettingsModel();
	private final SettingsModelString m_strategy = getStrategySettingsModel();
	
	public GooglePageSpeedNodeConfiguration() {
		
	}
	
    // static methods for creating settings models
    public static SettingsModelString getUrlColumnSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_URL_COLUMN, "");   
    }
    
    public static SettingsModelString getApiKeySettingsModel() {
    	return new SettingsModelString(FIELD_KEY_API_KEY,"");   
    }
    
    public static SettingsModelBoolean getFilterThirdPartyResourcesSettingsModel() {
    	return new SettingsModelBoolean(FIELD_KEY_FILTER_THIRD_PARTY_RESOURCES, false);
    }
    
    public static SettingsModelString getLocaleSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_LOCALE, FIELD_DEFAULT_LOCALE);
    }
    
    public static SettingsModelString getStrategySettingsModel() {
    	return new SettingsModelString(FIELD_KEY_STRATEGY, FIELD_DEFAULT_STRATEGY);
    }

    public SettingsModelString getUrl() {
    	return m_url;	
    }
	public SettingsModelString getApiKey() {
		return m_apikey;
	}
	public SettingsModelBoolean getFilterThirdPartyResources() {
		return m_filterThirdPartyResources;
	}
	public SettingsModelString getLocale() {
		return m_locale;
	}
	public SettingsModelString getStrategy() {
		return m_strategy;
	}
    
    
    public void saveSettingsTo(final NodeSettingsWO settings) {

        // save user settings to the config object.
        m_url.saveSettingsTo(settings);
        m_apikey.saveSettingsTo(settings);
        m_filterThirdPartyResources.saveSettingsTo(settings);
        m_locale.saveSettingsTo(settings);
        m_strategy.saveSettingsTo(settings);
    }

    public void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        // load (valid) settings from the config object.
        // It can be safely assumed that the settings are valided by the 
        // method below.        
        m_url.loadSettingsFrom(settings);
        m_apikey.loadSettingsFrom(settings);
        m_filterThirdPartyResources.loadSettingsFrom(settings);
        m_locale.loadSettingsFrom(settings);
        m_strategy.loadSettingsFrom(settings);
    }

    public void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        // check if the settings could be applied to our model
        // e.g. if the count is in a certain range (which is ensured by the
        // SettingsModel).
        // Do not actually set any values of any member variables.

        m_url.validateSettings(settings);
        m_apikey.validateSettings(settings);
        m_filterThirdPartyResources.validateSettings(settings);
        m_locale.validateSettings(settings);
        m_strategy.validateSettings(settings);
    }
}
