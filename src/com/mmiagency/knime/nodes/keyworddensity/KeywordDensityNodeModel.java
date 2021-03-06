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
package com.mmiagency.knime.nodes.keyworddensity;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.mmiagency.knime.nodes.keyworddensity.util.KeywordDensityHelper;
import com.mmiagency.knime.nodes.keyworddensity.util.KeywordDensityRowEntry;
import com.mmiagency.knime.nodes.keyworddensity.util.KeywordDensityRowFactory;

/**
 * This is the model implementation of KeywordDensity.
 * 
 *
 * @author Ed Ng
 */
public class KeywordDensityNodeModel extends NodeModel {
    
    private KeywordDensityNodeConfiguration m_config = new KeywordDensityNodeConfiguration();
    
    /**
     * Constructor for the node model.
     */
    protected KeywordDensityNodeModel() {    
        super(1, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	
    	KeywordDensityRowFactory factory = new KeywordDensityRowFactory();
    	
        BufferedDataContainer container = exec.createDataContainer(factory.tableSpec());
        int index = 0;
        
    	DataTableSpec inSpec = inData[0].getSpec();
    	String urlColumnName = m_config.getUrl().getStringValue();
    	String contentColumnName = m_config.getContent().getStringValue();
    	String excludeColumnName = m_config.getExcludeColumn().getStringValue();
    	String exclude = m_config.getExclude().getStringValue();
    	boolean includeMetaKeywords = m_config.getIncludeMetaKeywords().getBooleanValue();
    	boolean includeMetaDescription = m_config.getIncludeMetaDescription().getBooleanValue();
    	boolean includePageTitle = m_config.getIncludePageTitle().getBooleanValue();
    	
    	int urlColumnIndex = inSpec.findColumnIndex(urlColumnName);
    	int contentColumnIndex = inSpec.findColumnIndex(contentColumnName);
    	int excludeColumnIndex = inSpec.findColumnIndex(excludeColumnName);

    	for (Iterator<DataRow> it = inData[0].iterator(); it.hasNext();) {
    		DataRow row = it.next();
    		DataCell cell = row.getCell(urlColumnIndex);
    		if (cell.isMissing()) {
    			container.addRowToTable(factory.createRow("" + index++, "", "FAILED: Missing URL"));
    			continue;
    		}
    		if (!(cell.getType().isCompatible(StringValue.class))) {				
    			container.addRowToTable(factory.createRow("" + index++, "", 
    					"The specified URL column \"" + urlColumnName + "\" is not a string column.  Please specify a string column for URLs."));
    			continue;
			}
			
    		String url = ((StringValue)cell).getStringValue();

    		String content = null;
			
			// content
			if (contentColumnIndex >= 0) {
				DataCell contentCell = row.getCell(contentColumnIndex);
				if (contentCell.isMissing()) {
					// do nothing, we will pull content from URL
				} else if (contentCell.getType().isCompatible(StringValue.class)) {
					content = ((StringValue)contentCell).getStringValue();
				} else {
					setWarningMessage("Content column is not a string for URL: " + url);
				}
			}
			
    		String urlExclude = "";
			
			// url specific exclude
			if (excludeColumnIndex >= 0) {
				DataCell urlExcludeCell = row.getCell(excludeColumnIndex);
				if (urlExcludeCell.isMissing()) {
					// do nothing, because some URLs may not have specific exclude terms
				} else if (urlExcludeCell.getType().isCompatible(StringValue.class)) {
					urlExclude = ((StringValue)urlExcludeCell).getStringValue();
				} else {
					setWarningMessage("URL Exclude Terms is not a string for URL: " + url);
				}
			}
			
    		// using helper class to process content
    		KeywordDensityHelper helper = new KeywordDensityHelper(url, content,
    				exclude + " " + urlExclude, includeMetaKeywords, 
    				includeMetaDescription, includePageTitle);
    		
    		try {
    			helper.execute();
    		} catch (Exception e) {    			
    			setWarningMessage("FAILED on URL \"" + url + "\": " + e.getMessage());
    			container.addRowToTable(factory.createRow("" + index++, url, ""));
    			continue;
    		}
    		
    		for (Iterator<KeywordDensityRowEntry> it2 = helper.iterator(); it2.hasNext();) {
    			KeywordDensityRowEntry entry = it2.next();
    			container.addRowToTable(factory.createRow("" + index++, entry));
    		}
    	}
    	
    	container.close();
    	
        return new BufferedDataTable[]{container.getTable()};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {

		if (inSpecs.length<1) {
			throw new InvalidSettingsException("You must link a table with URL column to this node.");
		}
		
		// user has not set up URL column yet, auto-guessing URL column
		if (m_config.getUrl().getStringValue().isEmpty()) {
			int index = inSpecs[0].findColumnIndex(KeywordDensityNodeConfiguration.FIELD_DEFAULT_URL_COLUMN); 
			boolean found = false;
			if (index >= 0) {
				DataColumnSpec columnSpec = inSpecs[0].getColumnSpec(index);
				// check if column is of string type
				if (columnSpec.getType().isCompatible(StringValue.class)) {
					// found URL column
					m_config.getUrl().setStringValue(KeywordDensityNodeConfiguration.FIELD_DEFAULT_URL_COLUMN);
					setWarningMessage("Auto-guessing: Using column '"+KeywordDensityNodeConfiguration.FIELD_DEFAULT_URL_COLUMN+"' as URL column");
					found = true;
				}
			}
			
			// if URL column is still not found 
			if (!found) {
				// URL column doesn't exist, now check the first String column
				for (Iterator<DataColumnSpec> it = inSpecs[0].iterator(); it.hasNext();) {
					DataColumnSpec columnSpec = it.next();
					if (columnSpec.getType().isCompatible(StringValue.class)) {
						m_config.getUrl().setStringValue(columnSpec.getName());
						setWarningMessage("Auto-guessing: Using first string column '"+columnSpec.getName()+"' as URL column");
						break;
					}
				}
			}
		}		
		
		if (m_config.getUrl().getStringValue().isEmpty()) {
			setWarningMessage("A string column for URLs in the data input table must exist and must be specified.  Please create a URL column or pick the right column in this node's configuration.");
		}

		return new DataTableSpec[]{new KeywordDensityRowFactory().tableSpec()};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {

    	m_config.saveSettingsTo(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	KeywordDensityNodeConfiguration config = new KeywordDensityNodeConfiguration();
    	config.loadValidatedSettingsFrom(settings);
    	m_config = config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
    	m_config.validateSettings(settings);

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }

}

