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
package com.mmiagency.knime.keyworddensity;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class KeywordDensityNodeConfiguration {

	static final String FIELD_LABEL_URL_COLUMN = "URL Column Name (Required)";
	static final String FIELD_LABEL_CONTENT_COLUMN = "Content Column Name (Optional)";
	static final String FIELD_LABEL_EXCLUDE_COLUMN = "Exclude Terms Column Name (Optional, specific to URL)";
	static final String FIELD_LABEL_EXCLUDE = "Exclude Terms (Optional, applies to all URLs)";
	static final String FIELD_LABEL_INCLUDE_META_KEYWORDS = "Include meta tag keywords";
	static final String FIELD_LABEL_INCLUDE_META_DESCRIPTION = "Include meta tag description";
	static final String FIELD_LABEL_INCLUDE_PAGE_TITLE = "Include page title";
	
	static final String FIELD_KEY_URL_COLUMN = "url";
	static final String FIELD_KEY_CONTENT_COLUMN = "content";
	static final String FIELD_KEY_EXCLUDE_COLUMN = "excludeColumn";
	static final String FIELD_KEY_EXCLUDE = "exclude";
	static final String FIELD_KEY_INCLUDE_META_KEYWORDS = "includeMetaKeywords";
	static final String FIELD_KEY_INCLUDE_META_DESCRIPTION = "includeMetaDescription";
	static final String FIELD_KEY_INCLUDE_PAGE_TITLE = "includePageTitle";

	static final String FIELD_DEFAULT_URL_COLUMN = "url";
	static final String FIELD_DEFAULT_CONTENT_COLUMN = "";
	static final String FIELD_DEFAULT_EXCLUDE_COLUMN = "";
	static final String FIELD_DEFAULT_EXCLUDE = "";
	static final boolean FIELD_DEFAULT_INCLUDE_META_KEYWORDS = true;
	static final boolean FIELD_DEFAULT_INCLUDE_META_DESCRIPTION = true;
	static final boolean FIELD_DEFAULT_INCLUDE_PAGE_TITLE = true;

	private final SettingsModelString m_url = getUrlColumnSettingsModel();	
	private final SettingsModelString m_content = getContentColumnSettingsModel();	
	private final SettingsModelString m_excludeColumn = getExcludeColumnSettingsModel();	
	private final SettingsModelString m_exclude = getExcludeSettingsModel();	
	private final SettingsModelBoolean m_includeMetaKeywords = getIncludeMetaKeywordsSettingsModel();	
	private final SettingsModelBoolean m_includeMetaDescription = getIncludeMetaDescriptionSettingsModel();	
	private final SettingsModelBoolean m_includePageTitle = getIncludePageTitleSettingsModel();	
	
	public KeywordDensityNodeConfiguration() {
		
	}
	
    public static SettingsModelString getUrlColumnSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_URL_COLUMN, "");   
    }
    
    public static SettingsModelString getContentColumnSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_CONTENT_COLUMN, FIELD_DEFAULT_CONTENT_COLUMN);   
    }
    
    public static SettingsModelString getExcludeColumnSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_EXCLUDE_COLUMN, FIELD_DEFAULT_EXCLUDE_COLUMN);   
    }
    
    public static SettingsModelString getExcludeSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_EXCLUDE, FIELD_DEFAULT_EXCLUDE);   
    }
    
    public static SettingsModelBoolean getIncludeMetaKeywordsSettingsModel() {
    	return new SettingsModelBoolean(FIELD_LABEL_INCLUDE_META_KEYWORDS, FIELD_DEFAULT_INCLUDE_META_KEYWORDS);
    }
    
    public static SettingsModelBoolean getIncludeMetaDescriptionSettingsModel() {
    	return new SettingsModelBoolean(FIELD_LABEL_INCLUDE_META_DESCRIPTION, FIELD_DEFAULT_INCLUDE_META_DESCRIPTION);
    }
    
    public static SettingsModelBoolean getIncludePageTitleSettingsModel() {
    	return new SettingsModelBoolean(FIELD_KEY_INCLUDE_PAGE_TITLE, FIELD_DEFAULT_INCLUDE_PAGE_TITLE);
    }
    
    public SettingsModelString getUrl() {
    	return m_url;
    }
    
    public SettingsModelString getContent() {
    	return m_content;
    }
    
    public SettingsModelString getExcludeColumn() {
    	return m_excludeColumn;
    }

    public SettingsModelString getExclude() {
    	return m_exclude;
    }

    public SettingsModelBoolean getIncludeMetaKeywords() {
    	return m_includeMetaKeywords;
    }
    
    public SettingsModelBoolean getIncludeMetaDescription() {
    	return m_includeMetaDescription;
    }
    
    public SettingsModelBoolean getIncludePageTitle() {
    	return m_includePageTitle;
    }
    
    public void saveSettingsTo(final NodeSettingsWO settings) {
        m_url.saveSettingsTo(settings);
        m_content.saveSettingsTo(settings);
        m_exclude.saveSettingsTo(settings);
        m_excludeColumn.saveSettingsTo(settings);
        m_includeMetaKeywords.saveSettingsTo(settings);
        m_includeMetaDescription.saveSettingsTo(settings);
        m_includePageTitle.saveSettingsTo(settings);
    }
    
    public void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        m_url.loadSettingsFrom(settings);
        m_content.loadSettingsFrom(settings);
        m_exclude.loadSettingsFrom(settings);
        m_excludeColumn.loadSettingsFrom(settings);
        m_includeMetaKeywords.loadSettingsFrom(settings);
        m_includeMetaDescription.loadSettingsFrom(settings);
        m_includePageTitle.loadSettingsFrom(settings);
    }
    
    public void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        m_url.validateSettings(settings);
        m_content.validateSettings(settings);
        m_exclude.validateSettings(settings);
        m_excludeColumn.validateSettings(settings);
        m_includeMetaKeywords.validateSettings(settings);
        m_includeMetaDescription.validateSettings(settings);
        m_includePageTitle.validateSettings(settings);
    }
    
}
