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
package com.mmiagency.knime.nodes.keyworddensity;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.util.DataValueColumnFilter;

/**
 * <code>NodeDialog</code> for the "KeywordDensity" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Ed Ng
 */
public class KeywordDensityNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the KeywordDensity node.
     */
    @SuppressWarnings("unchecked")
	protected KeywordDensityNodeDialog() {
        super();
        
        addDialogComponent(new DialogComponentColumnNameSelection(
        		KeywordDensityNodeConfiguration.getUrlColumnSettingsModel(),
        		KeywordDensityNodeConfiguration.FIELD_LABEL_URL_COLUMN, 
        		0, true,
        		new DataValueColumnFilter(StringValue.class)));
        
        addDialogComponent(new DialogComponentColumnNameSelection(
        		KeywordDensityNodeConfiguration.getContentColumnSettingsModel(),
        		KeywordDensityNodeConfiguration.FIELD_LABEL_CONTENT_COLUMN, 
        		0, false, true,
        		new DataValueColumnFilter(StringValue.class)));

        addDialogComponent(new DialogComponentColumnNameSelection(
        		KeywordDensityNodeConfiguration.getExcludeColumnSettingsModel(),
        		KeywordDensityNodeConfiguration.FIELD_LABEL_EXCLUDE_COLUMN, 
        		0, false, true,
        		new DataValueColumnFilter(StringValue.class)));

        addDialogComponent(new DialogComponentString(
        		KeywordDensityNodeConfiguration.getExcludeSettingsModel(),
        		KeywordDensityNodeConfiguration.FIELD_LABEL_EXCLUDE, false, 30));        
        
        addDialogComponent(new DialogComponentBoolean(
        		KeywordDensityNodeConfiguration.getIncludeMetaKeywordsSettingsModel(), 
        		KeywordDensityNodeConfiguration.FIELD_LABEL_INCLUDE_META_KEYWORDS));
        
        addDialogComponent(new DialogComponentBoolean(
        		KeywordDensityNodeConfiguration.getIncludeMetaDescriptionSettingsModel(), 
        		KeywordDensityNodeConfiguration.FIELD_LABEL_INCLUDE_META_DESCRIPTION));
        
        addDialogComponent(new DialogComponentBoolean(
        		KeywordDensityNodeConfiguration.getIncludePageTitleSettingsModel(), 
        		KeywordDensityNodeConfiguration.FIELD_LABEL_INCLUDE_PAGE_TITLE));
    }
}

