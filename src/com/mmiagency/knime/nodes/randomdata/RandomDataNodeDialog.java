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
package com.mmiagency.knime.nodes.randomdata;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;


/**
 *
 * @author Phuc Truong
 */
public class RandomDataNodeDialog extends DefaultNodeSettingsPane {

    /**Constructor for class RandomDataNodeDialog.
     *
     */
    public RandomDataNodeDialog() {
        String[] types = new String[] {"String", "Integer", "Date"};
        
        addDialogComponent(new DialogComponentNumber(RandomDataNodeModel.createNoOfRowsModel(), "Total no of rows: ", Integer.valueOf(100)));
        for (int i = 0; i < RandomDataNodeModel.MAX_COLUMNS; i++) {
        	int index = i + 1;
        	setHorizontalPlacement(true);
        	addDialogComponent(new DialogComponentString(RandomDataNodeModel.createColumnNameModel(i), "Column " + index + " Name", false, 20));
        	addDialogComponent(new DialogComponentStringSelection(RandomDataNodeModel.createColumnTypeModel(i), "Type", types));
        	addDialogComponent(new DialogComponentString(RandomDataNodeModel.createColumnMinModel(i), "Min", false, 8));
        	addDialogComponent(new DialogComponentString(RandomDataNodeModel.createColumnMaxModel(i), "Max", false, 8));
        	addDialogComponent(new DialogComponentBoolean(RandomDataNodeModel.createColumnActiveModel(i), "Is Active"));
        	addDialogComponent(new DialogComponentLabel(""));
        	setHorizontalPlacement(false);
        }
        
    }
}
