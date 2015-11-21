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
package com.mmiagency.knime.nodes.sitemap;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentString;

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
        
        //FlowVariableModel flowVar = new FlowVariableModel(this, new String[]{XMLSitemapReaderNodeConfiguration.FIELD_KEY_URL}, FlowVariable.Type.STRING);
        
        addDialogComponent(new DialogComponentString(
        		XMLSitemapReaderNodeConfiguration.getUrlSettingsModel(),
        		XMLSitemapReaderNodeConfiguration.FIELD_LABEL_URL, true, 20));        
    }
}

