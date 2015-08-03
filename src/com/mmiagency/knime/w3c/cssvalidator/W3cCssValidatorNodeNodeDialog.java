package com.mmiagency.knime.w3c.cssvalidator;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.util.DataValueColumnFilter;

/**
 * <code>NodeDialog</code> for the "W3cCssValidatorNode" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author MMI Agency
 */
public class W3cCssValidatorNodeNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring W3cCssValidatorNode node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    @SuppressWarnings("unchecked")
	protected W3cCssValidatorNodeNodeDialog() {
        super();
        
        addDialogComponent(new DialogComponentString(
        		W3cCssValidatorNodeNodeModel.getValidatorUrlSettingsModel(),
        		W3cCssValidatorNodeNodeModel.FIELD_LABEL_VALIDATOR_URL, true, 20));        

        addDialogComponent(new DialogComponentColumnNameSelection(
        		W3cCssValidatorNodeNodeModel.getUrlColumnSettingsModel(),
        		W3cCssValidatorNodeNodeModel.FIELD_LABEL_URL_COLUMN, 
        		0, true,
        		new DataValueColumnFilter(StringValue.class)));
        
        addDialogComponent(new DialogComponentStringSelection(
        		W3cCssValidatorNodeNodeModel.getProfileSettingsModel(),
        		W3cCssValidatorNodeNodeModel.FIELD_LABEL_PROFILE, 
        		W3cCssValidatorNodeNodeModel.FIELD_OPTIONS_PROFILE.keySet().toArray(new String[W3cCssValidatorNodeNodeModel.FIELD_OPTIONS_PROFILE.keySet().size()])));        

        addDialogComponent(new DialogComponentStringSelection(
        		W3cCssValidatorNodeNodeModel.getMediumSettingsModel(),
        		W3cCssValidatorNodeNodeModel.FIELD_LABEL_MEDIUM, 
        		W3cCssValidatorNodeNodeModel.FIELD_OPTIONS_MEDIUM.keySet().toArray(new String[W3cCssValidatorNodeNodeModel.FIELD_OPTIONS_MEDIUM.keySet().size()])));        
        
        addDialogComponent(new DialogComponentStringSelection(
        		W3cCssValidatorNodeNodeModel.getWarningsSettingsModel(),
        		W3cCssValidatorNodeNodeModel.FIELD_LABEL_WARNINGS, 
        		W3cCssValidatorNodeNodeModel.FIELD_OPTIONS_WARNINGS.keySet().toArray(new String[W3cCssValidatorNodeNodeModel.FIELD_OPTIONS_WARNINGS.keySet().size()])));        
        
        addDialogComponent(new DialogComponentStringSelection(
        		W3cCssValidatorNodeNodeModel.getVendorExtensionsSettingsModel(),
        		W3cCssValidatorNodeNodeModel.FIELD_LABEL_VENDOR_EXTENSIONS, 
        		W3cCssValidatorNodeNodeModel.FIELD_OPTIONS_VENDOR_EXTENSIONS.keySet().toArray(new String[W3cCssValidatorNodeNodeModel.FIELD_OPTIONS_VENDOR_EXTENSIONS.keySet().size()])));        
    }
}

