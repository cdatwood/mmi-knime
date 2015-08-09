package com.mmiagency.knime.w3c.htmlvalidator;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class W3cHtmlValidatorNodeConfiguration {

	static final String FIELD_LABEL_VALIDATOR_URL = "HTML Validator URL";
	static final String FIELD_LABEL_URL_COLUMN = "URL Column Name";
	static final String FIELD_LABEL_SHOW_OUTLINE = "Show Outline";

	static final String FIELD_KEY_VALIDATOR_URL = "htmlValidatorUrl";
	static final String FIELD_KEY_URL_COLUMN = "urlColumn";
	static final String FIELD_KEY_SHOW_OUTLINE = "showOutline";

	static final String FIELD_DEFAULT_VALIDATOR_URL = "https://validator.w3.org/nu/";
	static final String FIELD_DEFAULT_URL_COLUMN = "url";
	static final boolean FIELD_DEFAULT_SHOW_OUTLINE = false;

	private final SettingsModelString m_validatorUrl = getValidatorUrlSettingsModel();	
	private final SettingsModelString m_url = getUrlColumnSettingsModel();	
	private final SettingsModelBoolean m_showOutline = getShowOutlineSettingsModel();	

	public W3cHtmlValidatorNodeConfiguration() {
		
	}
	
    public static SettingsModelString getValidatorUrlSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_VALIDATOR_URL, FIELD_DEFAULT_VALIDATOR_URL);   
    }
    public static SettingsModelString getUrlColumnSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_URL_COLUMN, FIELD_DEFAULT_URL_COLUMN);   
    }
    public static SettingsModelBoolean getShowOutlineSettingsModel() {
    	return new SettingsModelBoolean(FIELD_KEY_SHOW_OUTLINE, FIELD_DEFAULT_SHOW_OUTLINE);   
    }
	
	public SettingsModelString getValidatorUrl() {
		return m_validatorUrl;
	}
	
	public SettingsModelString getUrl() {
		return m_url;
	}
	
	public SettingsModelBoolean getShowOutline() {
		return m_showOutline;
	}

    public void saveSettingsTo(final NodeSettingsWO settings) {

        m_validatorUrl.saveSettingsTo(settings);
        m_url.saveSettingsTo(settings);
        m_showOutline.saveSettingsTo(settings);

    }

    public void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        m_validatorUrl.loadSettingsFrom(settings);
        m_url.loadSettingsFrom(settings);
        m_showOutline.loadSettingsFrom(settings);

    }

    public void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {

        m_validatorUrl.validateSettings(settings);
    	m_url.validateSettings(settings);
        m_showOutline.validateSettings(settings);
    	
    }
    
}
