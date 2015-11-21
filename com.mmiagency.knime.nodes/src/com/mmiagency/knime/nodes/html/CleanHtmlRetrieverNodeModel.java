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
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.MissingCell;
import org.knime.core.data.StringValue;
import org.knime.core.data.container.CellFactory;
import org.knime.core.data.container.ColumnRearranger;
import org.knime.core.data.container.SingleCellFactory;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.xml.XMLCell;
import org.knime.core.data.xml.XMLCellFactory;
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

        ColumnRearranger c = createColumnRearranger(inData[0].getDataTableSpec());
        BufferedDataTable out = exec.createColumnRearrangeTable(inData[0], c, exec);
        return new BufferedDataTable[]{out};    	

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
		
		ColumnRearranger c = createColumnRearranger(inSpecs[0]);
	    DataTableSpec result = c.createSpec();
	    return new DataTableSpec[]{result};
    }
    
    private ColumnRearranger createColumnRearranger(DataTableSpec in) {
        ColumnRearranger c = new ColumnRearranger(in);
        // column spec of the appended column
        DataColumnSpec newColSpec = null;
        if (m_config.getXml().getBooleanValue()) {
        	newColSpec = new DataColumnSpecCreator(m_config.getOutput().getStringValue(), XMLCell.TYPE).createSpec();
        } else {
        	newColSpec = new DataColumnSpecCreator(m_config.getOutput().getStringValue(), StringCell.TYPE).createSpec();
        }

    	String urlColumnName = m_config.getUrl().getStringValue();
    	String contentColumnName = m_config.getContent().getStringValue();
    	
    	final int urlColumnIndex = in.findColumnIndex(urlColumnName);
    	final int contentColumnIndex = in.findColumnIndex(contentColumnName);
        
        // utility object that performs the calculation
        CellFactory factory = new SingleCellFactory(newColSpec) {
            public DataCell getCell(DataRow row) {
            	
        		HtmlCleaner cleaner = new HtmlCleaner();
        		
        		CleanerProperties props = cleaner.getProperties();
        		
        		DataCell urlCell = row.getCell(urlColumnIndex);
        		
        		if (urlCell.isMissing()) {
					setWarningMessage("FAILED, missing URL");
					return new MissingCell("FAILED, missing URL");
        		}
        		
        		String url = ((StringValue)urlCell).getStringValue();

        		String content = null;
    			
    			// content
    			if (contentColumnIndex >= 0) {
    				DataCell contentCell = row.getCell(contentColumnIndex);
    				if (contentCell.isMissing()) {
    					// do nothing, we will pull content from URL
    				}
    			}

    			String html = content;
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
    					setWarningMessage("FAILED on URL \"" + url + "\": " + e.getMessage());
    				}

    			}
    			
    			if (html != null) {
    				// clean html
    				TagNode node = cleaner.clean(html);
    				result = new PrettyXmlSerializer(props).getAsString(node);
    			} else {
    				result = "";
    			}
            	
                if (m_config.getXml().getBooleanValue()) {
                	try {
        				return XMLCellFactory.create(result);
        			} catch (Exception e) {
        				setWarningMessage("FAILED on URL \"" + url + "\": " + e.getMessage());
        				return new MissingCell(e.getMessage());
        			}
                } else {
                	return new StringCell(result);
                }

            }
        };
        c.append(factory);
        return c;
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

