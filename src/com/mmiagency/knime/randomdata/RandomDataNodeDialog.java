package com.mmiagency.knime.randomdata;

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
