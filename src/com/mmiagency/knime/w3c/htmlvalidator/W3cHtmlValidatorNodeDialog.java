package com.mmiagency.knime.w3c.htmlvalidator;

import java.util.Arrays;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.util.DataValueColumnFilter;

import com.mmiagency.knime.w3c.cssvalidator.W3cCssValidatorNodeConfiguration;


/**
 * <code>NodeDialog</code> for the "W3cHtmlValidatorNode" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author MMI Agency
 */
public class W3cHtmlValidatorNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring W3cHtmlValidatorNode node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    @SuppressWarnings("unchecked")
	protected W3cHtmlValidatorNodeDialog() {
        super();
        
        addDialogComponent(new DialogComponentStringSelection(
        		W3cHtmlValidatorNodeConfiguration.getValidatorUrlSettingsModel(),
        		W3cHtmlValidatorNodeConfiguration.FIELD_LABEL_VALIDATOR_URL,        
		        Arrays.asList(W3cHtmlValidatorNodeConfiguration.getValidatorUrlHistory().getHistory()),
		        true));

        addDialogComponent(new DialogComponentColumnNameSelection(
        		W3cHtmlValidatorNodeConfiguration.getUrlColumnSettingsModel(),
        		W3cHtmlValidatorNodeConfiguration.FIELD_LABEL_URL_COLUMN, 
        		0, true,
        		new DataValueColumnFilter(StringValue.class)));

        addDialogComponent(new DialogComponentBoolean(
        		W3cHtmlValidatorNodeConfiguration.getShowOutlineSettingsModel(),
        		W3cHtmlValidatorNodeConfiguration.FIELD_LABEL_SHOW_OUTLINE));        

    }
}

