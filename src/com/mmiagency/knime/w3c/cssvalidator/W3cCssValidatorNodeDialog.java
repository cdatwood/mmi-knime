package com.mmiagency.knime.w3c.cssvalidator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knime.core.data.StringValue;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.util.DataValueColumnFilter;
import org.knime.core.node.util.StringHistoryPanel;

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
public class W3cCssValidatorNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring W3cCssValidatorNode node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    @SuppressWarnings("unchecked")
	protected W3cCssValidatorNodeDialog() {
        super();

        addDialogComponent(new DialogComponentStringSelection(
        		W3cCssValidatorNodeConfiguration.getValidatorUrlSettingsModel(),
        		W3cCssValidatorNodeConfiguration.FIELD_LABEL_VALIDATOR_URL, 
        		Arrays.asList(W3cCssValidatorNodeConfiguration.getValidatorUrlHistory().getHistory()),
        		true));

        addDialogComponent(new DialogComponentColumnNameSelection(
        		W3cCssValidatorNodeConfiguration.getUrlColumnSettingsModel(),
        		W3cCssValidatorNodeConfiguration.FIELD_LABEL_URL_COLUMN, 
        		0, true,
        		new DataValueColumnFilter(StringValue.class)));
        
        addDialogComponent(new DialogComponentStringSelection(
        		W3cCssValidatorNodeConfiguration.getProfileSettingsModel(),
        		W3cCssValidatorNodeConfiguration.FIELD_LABEL_PROFILE, 
        		W3cCssValidatorNodeConfiguration.FIELD_OPTIONS_PROFILE.keySet().toArray(new String[W3cCssValidatorNodeConfiguration.FIELD_OPTIONS_PROFILE.keySet().size()])));        

        addDialogComponent(new DialogComponentStringSelection(
        		W3cCssValidatorNodeConfiguration.getMediumSettingsModel(),
        		W3cCssValidatorNodeConfiguration.FIELD_LABEL_MEDIUM, 
        		W3cCssValidatorNodeConfiguration.FIELD_OPTIONS_MEDIUM.keySet().toArray(new String[W3cCssValidatorNodeConfiguration.FIELD_OPTIONS_MEDIUM.keySet().size()])));        
        
        addDialogComponent(new DialogComponentStringSelection(
        		W3cCssValidatorNodeConfiguration.getWarningsSettingsModel(),
        		W3cCssValidatorNodeConfiguration.FIELD_LABEL_WARNINGS, 
        		W3cCssValidatorNodeConfiguration.FIELD_OPTIONS_WARNINGS.keySet().toArray(new String[W3cCssValidatorNodeConfiguration.FIELD_OPTIONS_WARNINGS.keySet().size()])));        
        
        addDialogComponent(new DialogComponentStringSelection(
        		W3cCssValidatorNodeConfiguration.getVendorExtensionsSettingsModel(),
        		W3cCssValidatorNodeConfiguration.FIELD_LABEL_VENDOR_EXTENSIONS, 
        		W3cCssValidatorNodeConfiguration.FIELD_OPTIONS_VENDOR_EXTENSIONS.keySet().toArray(new String[W3cCssValidatorNodeConfiguration.FIELD_OPTIONS_VENDOR_EXTENSIONS.keySet().size()])));        
    }
    
}

