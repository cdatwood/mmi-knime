package com.mmiagency.knime.google.pagespeed;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.data.StringValue;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.util.DataValueColumnFilter;

/**
 * <code>NodeDialog</code> for the "PageSpeed Insight" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author MMI Agency
 */
public class GooglePageSpeedNodeDialog extends DefaultNodeSettingsPane {

    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(GooglePageSpeedNodeDialog.class);
    
    protected SettingsModelString localeSetting;
    
    private DialogComponentLabel m_warning = new DialogComponentLabel("");
	
    /**
     * New pane for configuring GooglePageSpeed node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    @SuppressWarnings("unchecked")
	protected GooglePageSpeedNodeDialog() {
        super();

        addDialogComponent(new DialogComponentString(
        		GooglePageSpeedNodeConfiguration.getApiKeySettingsModel(),
        		GooglePageSpeedNodeConfiguration.FIELD_LABEL_API_KEY, true, 20));        

        addDialogComponent(new DialogComponentColumnNameSelection(
        		GooglePageSpeedNodeConfiguration.getUrlColumnSettingsModel(),
        		GooglePageSpeedNodeConfiguration.FIELD_LABEL_URL_COLUMN, 
        		0, true,
        		new DataValueColumnFilter(StringValue.class)));
        
        addDialogComponent(new DialogComponentBoolean(
        		GooglePageSpeedNodeConfiguration.getFilterThirdPartyResourcesSettingsModel(),
        		GooglePageSpeedNodeConfiguration.FIELD_LABEL_FILTER_THIRD_PARTY_RESOURCES));        
        
        localeSetting = GooglePageSpeedNodeConfiguration.getLocaleSettingsModel();
        
        addDialogComponent(new DialogComponentString(
        		localeSetting,
        		GooglePageSpeedNodeConfiguration.FIELD_LABEL_LOCALE, 
        		false, 20));
        
        localeSetting.addChangeListener(new ChangeListener() {
        	Pattern pattern = Pattern.compile("[a-z]{2}_[A-Z]{2}");
        	
			public void stateChanged(final ChangeEvent e) {
				String locale = localeSetting.getStringValue();
				
				if (locale == null || locale.trim().isEmpty()) {
					logger.warn("Locale is empty");
					m_warning.setText("WARNING: Locale is empty");
					return;
				} else {
					m_warning.setText("");
				}
				
				Matcher matcher = pattern.matcher(locale);
				
				if (!matcher.matches()) {
					logger.warn("Locale is not in right format (e.g. en_US).");
					m_warning.setText("WARNING: Locale is not in right format (e.g. en_US).");
					return;
				} else {
					m_warning.setText("");
				}
			}
        });
                
        addDialogComponent(new DialogComponentStringSelection(
        		GooglePageSpeedNodeConfiguration.getStrategySettingsModel(),
        		GooglePageSpeedNodeConfiguration.FIELD_LABEL_STRATEGY, 
        		GooglePageSpeedNodeConfiguration.FIELD_OPTIONS_STRATEGY));        
                    
        addDialogComponent(m_warning);
        
    }
}

