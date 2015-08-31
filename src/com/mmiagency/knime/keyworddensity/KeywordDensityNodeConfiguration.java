package com.mmiagency.knime.keyworddensity;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import twitter4j.Location;
import twitter4j.Trend;

public class KeywordDensityNodeConfiguration {

	static final String FIELD_LABEL_URL_COLUMN = "URL Column Name";
	static final String FIELD_LABEL_EXCLUDE = "Exclude Terms";
	
	static final String FIELD_KEY_URL_COLUMN = "url";
	static final String FIELD_KEY_EXCLUDE = "exclude";

	static final String FIELD_DEFAULT_URL_COLUMN = "url";
	static final String FIELD_DEFAULT_EXCLUDE = "";

	private final SettingsModelString m_url = getUrlColumnSettingsModel();	
	private final SettingsModelString m_exclude = getExcludeSettingsModel();	
	
	public KeywordDensityNodeConfiguration() {
		
	}
	
    public static SettingsModelString getUrlColumnSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_URL_COLUMN, FIELD_DEFAULT_URL_COLUMN);   
    }
    
    public static SettingsModelString getExcludeSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_EXCLUDE, FIELD_DEFAULT_EXCLUDE);   
    }
    
    public SettingsModelString getUrl() {
    	return m_url;
    }
    
    public SettingsModelString getExclude() {
    	return m_exclude;
    }
    
    public void saveSettingsTo(final NodeSettingsWO settings) {
        m_url.saveSettingsTo(settings);
        m_exclude.saveSettingsTo(settings);
    }
    
    public void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        m_url.loadSettingsFrom(settings);
        m_exclude.loadSettingsFrom(settings);
    }
    
    public void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        m_url.validateSettings(settings);
        m_exclude.validateSettings(settings);
    }
    
}
