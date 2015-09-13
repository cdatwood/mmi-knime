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
package com.mmiagency.knime.w3c.htmlvalidator;

import java.util.Arrays;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
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

