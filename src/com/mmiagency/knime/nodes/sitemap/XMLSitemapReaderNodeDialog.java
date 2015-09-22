package com.mmiagency.knime.nodes.sitemap;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;

import com.mmiagency.knime.nodes.html.CleanHtmlRetrieverNodeConfiguration;

/**
 * <code>NodeDialog</code> for the "XMLSitemapReader" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Ed Ng
 */
public class XMLSitemapReaderNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring XMLSitemapReader node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected XMLSitemapReaderNodeDialog() {
        super();
        
        addDialogComponent(new DialogComponentString(
        		XMLSitemapReaderNodeConfiguration.getUrlSettingsModel(),
        		XMLSitemapReaderNodeConfiguration.FIELD_LABEL_URL, true, 20));        
    }
}

