package com.mmiagency.knime.w3c.cssvalidator;

import java.util.HashMap;
import java.util.Map;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class W3cCssValidatorNodeConfiguration {
	
	static final String FIELD_LABEL_VALIDATOR_URL = "CSS Validator URL";
	static final String FIELD_LABEL_URL_COLUMN = "URL Column Name";
	static final String FIELD_LABEL_PROFILE = "Profile";
	static final String FIELD_LABEL_MEDIUM = "Medium";
	static final String FIELD_LABEL_WARNINGS = "Warnings";
	static final String FIELD_LABEL_VENDOR_EXTENSIONS = "Vendor Extensions";

	static final String FIELD_KEY_VALIDATOR_URL = "validatorUrl";
	static final String FIELD_KEY_URL_COLUMN = "urlColumn";
	static final String FIELD_KEY_PROFILE = "profile";
	static final String FIELD_KEY_MEDIUM = "usermedium";
	static final String FIELD_KEY_WARNINGS = "warning";
	static final String FIELD_KEY_VENDOR_EXTENSIONS = "vextwarning";

	static final String FIELD_DEFAULT_VALIDATOR_URL = "http://jigsaw.w3.org/css-validator/validator";
	static final String FIELD_DEFAULT_URL_COLUMN = "url";
	static final String FIELD_DEFAULT_PROFILE = "CSS level 3";
	static final String FIELD_DEFAULT_MEDIUM = "All";
	static final String FIELD_DEFAULT_WARNINGS = "Normal report";
	static final String FIELD_DEFAULT_VENDOR_EXTENSIONS = "Default";
	
	static final Map<String, String> FIELD_OPTIONS_PROFILE = new HashMap<String, String>();
	static final Map<String, String> FIELD_OPTIONS_MEDIUM = new HashMap<String, String>();
	static final Map<String, String> FIELD_OPTIONS_WARNINGS = new HashMap<String, String>();
	static final Map<String, String> FIELD_OPTIONS_VENDOR_EXTENSIONS = new HashMap<String, String>();
	
	private final SettingsModelString m_validatorUrl = getValidatorUrlSettingsModel();	
	private final SettingsModelString m_url = getUrlColumnSettingsModel();	
	private final SettingsModelString m_profile = getProfileSettingsModel();	
	private final SettingsModelString m_medium = getMediumSettingsModel();	
	private final SettingsModelString m_warnings = getWarningsSettingsModel();	
	private final SettingsModelString m_vendorExtensions = getVendorExtensionsSettingsModel();	
	
	public W3cCssValidatorNodeConfiguration() {
    	FIELD_OPTIONS_PROFILE.put("No special profile", "none");
    	FIELD_OPTIONS_PROFILE.put("CSS level 1", "css1");
    	FIELD_OPTIONS_PROFILE.put("CSS level 2", "css2");
    	FIELD_OPTIONS_PROFILE.put("CSS level 2.1", "css21");
    	FIELD_OPTIONS_PROFILE.put("CSS level 3", "css3");
    	FIELD_OPTIONS_PROFILE.put("SVG", "svg");
    	FIELD_OPTIONS_PROFILE.put("SVG Basic", "svgbasic");
    	FIELD_OPTIONS_PROFILE.put("SVG tiny", "svgtiny");
    	FIELD_OPTIONS_PROFILE.put("Mobile", "mobile");
    	FIELD_OPTIONS_PROFILE.put("ATSC TV profile", "atsc-tv");
    	FIELD_OPTIONS_PROFILE.put("TV profile", "tv");
    	
    	FIELD_OPTIONS_MEDIUM.put("All", "all");
    	FIELD_OPTIONS_MEDIUM.put("aural", "aural");
    	FIELD_OPTIONS_MEDIUM.put("braille", "braille");
    	FIELD_OPTIONS_MEDIUM.put("embossed", "embossed");
    	FIELD_OPTIONS_MEDIUM.put("handheld", "handheld");
    	FIELD_OPTIONS_MEDIUM.put("print", "print");
    	FIELD_OPTIONS_MEDIUM.put("projection", "projection");
    	FIELD_OPTIONS_MEDIUM.put("screen", "screen");
    	FIELD_OPTIONS_MEDIUM.put("TTY", "tty");
    	FIELD_OPTIONS_MEDIUM.put("TV", "tv");
    	FIELD_OPTIONS_MEDIUM.put("presentation", "presentation");
    	
    	FIELD_OPTIONS_WARNINGS.put("All", "2");
    	FIELD_OPTIONS_WARNINGS.put("Normal report", "1");
    	FIELD_OPTIONS_WARNINGS.put("Most important", "0");
    	FIELD_OPTIONS_WARNINGS.put("No warnings", "no");

    	FIELD_OPTIONS_VENDOR_EXTENSIONS.put("Default", "");
    	FIELD_OPTIONS_VENDOR_EXTENSIONS.put("Warnings", "true");
    	FIELD_OPTIONS_VENDOR_EXTENSIONS.put("Errors", "true");
    }

    public static SettingsModelString getValidatorUrlSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_VALIDATOR_URL, FIELD_DEFAULT_VALIDATOR_URL);   
    }
    public static SettingsModelString getUrlColumnSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_URL_COLUMN, FIELD_DEFAULT_URL_COLUMN);   
    }
    public static SettingsModelString getProfileSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_PROFILE, FIELD_DEFAULT_PROFILE);   
    }
    public static SettingsModelString getMediumSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_MEDIUM, FIELD_DEFAULT_MEDIUM);   
    }
    public static SettingsModelString getWarningsSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_WARNINGS, FIELD_DEFAULT_WARNINGS);   
    }
    public static SettingsModelString getVendorExtensionsSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_VENDOR_EXTENSIONS, FIELD_DEFAULT_VENDOR_EXTENSIONS);   
    }
    
    public SettingsModelString getValidatorUrl() {
    	return m_validatorUrl;
    }
    public SettingsModelString getUrl() {
    	return m_url;
    }
    public SettingsModelString getProfile() {
    	return m_profile;
    }
    public SettingsModelString getMedium() {
    	return m_medium;
    }
    public SettingsModelString getWarnings() {
    	return m_warnings;
    }
    public SettingsModelString getVendorExtensions() {
    	return m_vendorExtensions;
    }

    public void saveSettingsTo(final NodeSettingsWO settings) {

        m_validatorUrl.saveSettingsTo(settings);
        m_url.saveSettingsTo(settings);
        m_profile.saveSettingsTo(settings);
        m_medium.saveSettingsTo(settings);
        m_warnings.saveSettingsTo(settings);
        m_vendorExtensions.saveSettingsTo(settings);

    }

    public void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        m_validatorUrl.loadSettingsFrom(settings);
        m_url.loadSettingsFrom(settings);
        m_profile.loadSettingsFrom(settings);
        m_medium.loadSettingsFrom(settings);
        m_warnings.loadSettingsFrom(settings);
        m_vendorExtensions.loadSettingsFrom(settings);

    }

    public void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        m_validatorUrl.validateSettings(settings);
        m_url.validateSettings(settings);
        m_profile.validateSettings(settings);
        m_medium.validateSettings(settings);
        m_warnings.validateSettings(settings);
        m_vendorExtensions.validateSettings(settings);

    }    
}