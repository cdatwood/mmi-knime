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
package com.mmiagency.knime.nodes.moz.linkmetrics;

import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.util.DataValueColumnFilter;

/**
 * 
 * @author Phuc Truong
 */
public class MozLinkMetricsNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring W3cHtmlValidatorNode node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    @SuppressWarnings("unchecked")
	protected MozLinkMetricsNodeDialog() {
        super();
        
        String[] scopes = new String[] {
        		"page_to_page", 
        		"page_to_subdomain",
        		"page_to_domain",
        		"subdomain_to_page",
        		"subdomain_to_subdomain",
        		"subdomain_to_domain",
        		"domain_to_page",
        		"domain_to_subdomain",
        		"domain_to_domain"
        		};
        
        String sort[] = new String[] {
        		"page_authority",
        		"domain_authority",
        		"domains_linking_domain",
        		"domains_linking_page"
        		};
        
        addDialogComponent(new DialogComponentColumnNameSelection(MozLinkMetricsNodeConfiguration.getUrlColumnSettingsModel(), "URL Column Name", 0, true, new DataValueColumnFilter(StringValue.class)));
        addDialogComponent(new DialogComponentBoolean(MozLinkMetricsNodeConfiguration.getLinkTargetColsSettingsModel(), "Include LinkCols & TargetCols"));
    	addDialogComponent(new DialogComponentStringSelection(MozLinkMetricsNodeConfiguration.getScopeSettingsModel(), "Scope", scopes));
    	addDialogComponent(new DialogComponentStringSelection(MozLinkMetricsNodeConfiguration.getSortSettingsModel(), "Sort", sort));
        addDialogComponent(new DialogComponentNumber(MozLinkMetricsNodeConfiguration.getMaxResultsSettingsModel(), "Max Results: ", Integer.valueOf(100)));
        addDialogComponent(new DialogComponentNumber(MozLinkMetricsNodeConfiguration.getDelayBetweenCallsSettingsModel(), "Delay Between Calls: ", Integer.valueOf(5)));
    }
}

