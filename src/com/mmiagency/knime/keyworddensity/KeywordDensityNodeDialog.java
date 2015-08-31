package com.mmiagency.knime.keyworddensity;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
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

        addDialogComponent(new DialogComponentColumnNameSelection(
        		KeywordDensityNodeConfiguration.getUrlColumnSettingsModel(),
        		KeywordDensityNodeConfiguration.FIELD_LABEL_URL_COLUMN, 
        		0, true,
        		new DataValueColumnFilter(StringValue.class)));
        
        addDialogComponent(new DialogComponentString(
        		KeywordDensityNodeConfiguration.getExcludeSettingsModel(),
        		KeywordDensityNodeConfiguration.FIELD_LABEL_EXCLUDE, true, 30));        
    }
}

