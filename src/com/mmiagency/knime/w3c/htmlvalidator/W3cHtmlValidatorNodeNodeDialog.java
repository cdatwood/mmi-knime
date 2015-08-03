package com.mmiagency.knime.w3c.htmlvalidator;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.util.DataValueColumnFilter;


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
public class W3cHtmlValidatorNodeNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring W3cHtmlValidatorNode node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    @SuppressWarnings("unchecked")
	protected W3cHtmlValidatorNodeNodeDialog() {
        super();
        
        addDialogComponent(new DialogComponentString(
        		W3cHtmlValidatorNodeNodeModel.getValidatorUrlSettingsModel(),
        		W3cHtmlValidatorNodeNodeModel.FIELD_LABEL_VALIDATOR_URL, true, 20));        

        addDialogComponent(new DialogComponentColumnNameSelection(
        		W3cHtmlValidatorNodeNodeModel.getUrlColumnSettingsModel(),
        		W3cHtmlValidatorNodeNodeModel.FIELD_LABEL_URL_COLUMN, 
        		0, true,
        		new DataValueColumnFilter(StringValue.class)));

        addDialogComponent(new DialogComponentBoolean(
        		W3cHtmlValidatorNodeNodeModel.getShowOutlineSettingsModel(),
        		W3cHtmlValidatorNodeNodeModel.FIELD_LABEL_SHOW_OUTLINE));        

    }
}

