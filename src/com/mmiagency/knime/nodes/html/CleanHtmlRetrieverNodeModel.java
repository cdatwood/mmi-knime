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
package com.mmiagency.knime.nodes.html;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * This is the model implementation of TidyHtmlRetriever.
 * 
 *
 * @author MMI Agency
 */
public class CleanHtmlRetrieverNodeModel extends NodeModel {
    
	private CleanHtmlRetrieverNodeConfiguration m_config = new CleanHtmlRetrieverNodeConfiguration();
	
    /**
     * Constructor for the node model.
     */
    protected CleanHtmlRetrieverNodeModel() {
    
        // TODO: Specify the amount of input and output ports needed.
        super(1, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

		BufferedDataContainer container = exec.createDataContainer(m_config.tableSpec());
        int index = 0;
        
    	DataTableSpec inSpec = inData[0].getSpec();
    	String urlColumnName = m_config.getUrl().getStringValue();
    	String contentColumnName = m_config.getContent().getStringValue();
    	
    	int urlColumnIndex = inSpec.findColumnIndex(urlColumnName);
    	int contentColumnIndex = inSpec.findColumnIndex(contentColumnName);
    	
		HtmlCleaner cleaner = new HtmlCleaner();
		
		CleanerProperties props = cleaner.getProperties();

		for (Iterator<DataRow> it = inData[0].iterator(); it.hasNext();) {
    		DataRow row = it.next();
    		DataCell cell = row.getCell(urlColumnIndex);
    		if (cell.isMissing()) {
    			container.addRowToTable(m_config.createRow("" + index++, "", "FAILED: Missing URL"));
    			continue;
    		}
			if (!(cell instanceof StringValue)) {
    			container.addRowToTable(m_config.createRow("" + index++, "", 
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
				} else if (contentCell instanceof StringValue) {
					content = ((StringValue)contentCell).getStringValue();
				} else {
					setWarningMessage("Content column is not a string for URL: " + url);
				}
			}

			String html = null;
			String result = null;
			
			if (content == null) {

				try {
			        Connection conn = Jsoup.connect(url);
			        
			        conn.validateTLSCertificates(false);
			        conn.followRedirects(true);
			        conn.userAgent(m_config.getUserAgent().getStringValue());
			        conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			        conn.header("Accept-Language", "en-US,en;q=0.5");
			        conn.header("Accept-Encoding", "gzip, deflate");
			        
			        conn.execute();
			        Document doc = conn.get();
			        html = doc.html();
				} catch (Throwable e) {
					setWarningMessage("Error found on " + url + ": " + e.getMessage());
				}

			} else {
				html = content;
			}
			
			if (html != null) {
				// clean html
				TagNode node = cleaner.clean(html);
				result = new PrettyXmlSerializer(props).getAsString(node);
			} else {
				result = "";
			}
			
		    container.addRowToTable(m_config.createRow("" + index, url, result));
			
            // check if the execution monitor was canceled
            exec.checkCanceled();
            exec.setProgress(index / (double)inData[0].getRowCount(), 
                "Adding row " + index++);
    	}
    	
    	container.close();
    	
        return new BufferedDataTable[]{container.getTable()};
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
			int index = inSpecs[0].findColumnIndex(CleanHtmlRetrieverNodeConfiguration.FIELD_DEFAULT_URL_COLUMN); 
			boolean found = false;
			if (index >= 0) {
				DataColumnSpec columnSpec = inSpecs[0].getColumnSpec(index);
				// check if column is of string type
				if (columnSpec.getType().equals(StringCell.TYPE)) {
					// found URL column
					m_config.getUrl().setStringValue(CleanHtmlRetrieverNodeConfiguration.FIELD_DEFAULT_URL_COLUMN);
					setWarningMessage("Auto-guessing: Using column '"+CleanHtmlRetrieverNodeConfiguration.FIELD_DEFAULT_URL_COLUMN+"' as URL column");
					found = true;
				}
			}
			
			// if URL column is still not found 
			if (!found) {
				// URL column doesn't exist, now check the first String column
				for (Iterator<DataColumnSpec> it = inSpecs[0].iterator(); it.hasNext();) {
					DataColumnSpec columnSpec = it.next();
					if (columnSpec.getType().equals(StringCell.TYPE)) {
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

		return new DataTableSpec[]{m_config.tableSpec()};
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
    	CleanHtmlRetrieverNodeConfiguration config = new CleanHtmlRetrieverNodeConfiguration();
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

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		
	}

	@Override
	protected void reset() {
		
	}
    
}

