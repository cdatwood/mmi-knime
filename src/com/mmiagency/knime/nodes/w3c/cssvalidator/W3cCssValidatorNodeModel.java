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
package com.mmiagency.knime.nodes.w3c.cssvalidator;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * This is the model implementation of W3cCssValidatorNode.
 * 
 *
 * @author MMI Agency
 */
public class W3cCssValidatorNodeModel extends NodeModel {
    
    private W3cCssValidatorNodeConfiguration m_configuration = new W3cCssValidatorNodeConfiguration();

	/**
     * Constructor for the node model.
     */
    protected W3cCssValidatorNodeModel() {
    
        // one incoming port and two outgoing ports
        super(1, 2);
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(PortObject[] inObjects, final ExecutionContext exec) throws Exception {

		if (inObjects.length != 1 && inObjects[0] instanceof BufferedDataTable) {
			throw new InvalidSettingsException("You must link a table with URL column to this node.");
		}
		
		Pattern h3Pattern = Pattern.compile(".*\\(([0-9]*)\\).*");

		BufferedDataTable inData = (BufferedDataTable)inObjects[0];
		
    	DataTableSpec inSpec = inData.getSpec();
    	String urlColumnName = m_configuration.getUrl().getStringValue();
    	    	
    	int urlColumnIndex = inSpec.findColumnIndex(urlColumnName);

    	if (urlColumnIndex < 0) {
			throw new InvalidSettingsException("You must link a table with URL column to this node.");
    	}

    	// prepare output data container        
        DataColumnSpec[] summaryColSpecs = new DataColumnSpec[8];
        summaryColSpecs[0] = new DataColumnSpecCreator("url", StringCell.TYPE).createSpec();
        summaryColSpecs[1] = new DataColumnSpecCreator("profile", StringCell.TYPE).createSpec();
        summaryColSpecs[2] = new DataColumnSpecCreator("medium", StringCell.TYPE).createSpec();
        summaryColSpecs[3] = new DataColumnSpecCreator("warnings", StringCell.TYPE).createSpec();
        summaryColSpecs[4] = new DataColumnSpecCreator("vendor extensions", StringCell.TYPE).createSpec();
        summaryColSpecs[5] = new DataColumnSpecCreator("error count", IntCell.TYPE).createSpec();
        summaryColSpecs[6] = new DataColumnSpecCreator("warning count", IntCell.TYPE).createSpec();
        summaryColSpecs[7] = new DataColumnSpecCreator("status", StringCell.TYPE).createSpec();
        BufferedDataContainer containerSummary = exec.createDataContainer(
        		new DataTableSpec(summaryColSpecs));
        
        DataColumnSpec[] detailsColSpecs = new DataColumnSpec[7];
        detailsColSpecs[0] = new DataColumnSpecCreator("url", StringCell.TYPE).createSpec();
        detailsColSpecs[1] = new DataColumnSpecCreator("uri", StringCell.TYPE).createSpec();
        detailsColSpecs[2] = new DataColumnSpecCreator("type", StringCell.TYPE).createSpec();
        detailsColSpecs[3] = new DataColumnSpecCreator("warning level", IntCell.TYPE).createSpec();
        detailsColSpecs[4] = new DataColumnSpecCreator("line number", IntCell.TYPE).createSpec();
        detailsColSpecs[5] = new DataColumnSpecCreator("classes", StringCell.TYPE).createSpec();
        detailsColSpecs[6] = new DataColumnSpecCreator("message", StringCell.TYPE).createSpec();
        BufferedDataContainer containerDetails = exec.createDataContainer(
        		new DataTableSpec(detailsColSpecs));
        
        int summaryRowCount = 0;
		int detailsRowCount = 0;
		        
    	for (Iterator<DataRow> it = inData.iterator(); it.hasNext();) {
    		DataRow row = it.next();
    		
			DataCell cell = row.getCell(urlColumnIndex);
			
			String url = "";

    		// put results in outgoing table
    		RowKey key = new RowKey("Row " + summaryRowCount++);
    		
    		DataCell[] summaryCells = new DataCell[8];
    		
    		summaryCells[0] = new StringCell(url);
    		summaryCells[1] = new StringCell(m_configuration.getProfile().getStringValue());
    		summaryCells[2] = new StringCell(m_configuration.getMedium().getStringValue());
    		summaryCells[3] = new StringCell(m_configuration.getWarnings().getStringValue());
    		summaryCells[4] = new StringCell(m_configuration.getVendorExtensions().getStringValue());
    		summaryCells[5] = new IntCell(0);
    		summaryCells[6] = new IntCell(0);
    		summaryCells[7] = new StringCell("");
    		
    		boolean goodRespond = true;
    		String errorMessage = null;
    		
    	    Document doc = null;
    	    Connection.Response resp = null;
    	    
    	    try {    			
    			if (cell.isMissing()) {
    				throw new Exception("URL value is missing");
    			}
    			
    			if (!(cell.getType().isCompatible(StringValue.class))) {
    				throw new Exception("The specified URL column \"" + urlColumnName + "\" is not a string column.  Please specify a string column for URLs.");
    			}
    			
    			url = ((StringValue)cell).getStringValue();    		

        		// set URL in summary
        		summaryCells[0] = new StringCell(url);
    			
        		// retrieve validator results
        		String validatorUrl = m_configuration.getValidatorUrl().getStringValue() + 
        				"?uri=" + URLEncoder.encode(url, "UTF-8") +
        				"&profile=" + W3cCssValidatorNodeConfiguration.FIELD_OPTIONS_PROFILE.get(m_configuration.getProfile().getStringValue()) + 
        				"&usermedium=" + W3cCssValidatorNodeConfiguration.FIELD_OPTIONS_MEDIUM.get(m_configuration.getMedium().getStringValue()) +
        				"&warning=" + W3cCssValidatorNodeConfiguration.FIELD_OPTIONS_WARNINGS.get(m_configuration.getWarnings().getStringValue()) +
        				"&vextwarning=" + W3cCssValidatorNodeConfiguration.FIELD_OPTIONS_VENDOR_EXTENSIONS.get(m_configuration.getVendorExtensions().getStringValue());

        		Connection con = Jsoup.connect(validatorUrl).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").timeout(10000);    		

        	    resp = con.execute();
    	    	doc = con.get();
    	    	
    	    } catch (HttpStatusException hse) {
    	    	goodRespond = false;
    	    	errorMessage = hse.getMessage();
        		summaryCells[7] = new StringCell(errorMessage);
    	    } catch (Throwable t) {
    	    	goodRespond = false;
    	    	errorMessage = t.getMessage();
        		summaryCells[7] = new StringCell(errorMessage);
    	    }
    	    
    		if (goodRespond && resp.statusCode() == 200) {
        		summaryCells[7] = new StringCell("success");
        		
        		// errors
	    		Elements errors = doc.select("#errors");
	    		
	    		Elements h3 = errors.select("h3");
	    		
	    		if (h3.size() > 0) {
	    			Element e = h3.get(0);
	    			Matcher m = h3Pattern.matcher(e.text());
	    			if (m.matches()) {
	    				try {
	    					summaryCells[5] = new IntCell(new Integer(m.group(1)));	    				
	    				} catch (NumberFormatException nfe) {
	    					// leave it empty
	    				}
	    			}
	    		}
	    		
	    		Elements sections = errors.select(".error-section");
	    		
	    		for (Element section : sections) {
	    			String uri = "";
	    			
	    			Elements h4 = section.select("h4 a");
	    			if (h4.size() > 0) {
		    			uri = h4.get(0).text();
	    			}
	    			
	    			for (Element error : section.select(".error")) {
		    			
		        		RowKey detailsKey = new RowKey("Row " + detailsRowCount++);
		        		
		    			DataCell[] detailsCells = new DataCell[7];
		    			
		    			detailsCells[0] = new StringCell(url);
		    			detailsCells[1] = new StringCell(uri);
		    			detailsCells[2] = new StringCell("error");
		    			detailsCells[3] = new IntCell(0);
		    			
		    			Elements lineNumber = error.select(".linenumber");
		    			if (lineNumber.size() > 0) {
		    				try {
		    					detailsCells[4] = new IntCell(new Integer(lineNumber.get(0).text()));
		    				} catch (NumberFormatException nfe) {
				    			detailsCells[4] = new IntCell(0);
		    				}
		    			} else {
			    			detailsCells[4] = new IntCell(0);	    				
		    			}
		    			
		    			Elements codeContext = error.select(".codeContext");
		    			if (codeContext.size() > 0) {
			    			detailsCells[5] = new StringCell(codeContext.get(0).text());
		    			} else {
			    			detailsCells[5] = new StringCell("");
		    			}
		    			
		    			Elements parseError = error.select(".parse-error");
		    			if (parseError.size() > 0) {
			    			detailsCells[6] = new StringCell(parseError.get(0).text());
		    			} else {
			    			detailsCells[6] = new StringCell("");
		    			}
	
		    			DataRow detailsDataRow = new DefaultRow(detailsKey, detailsCells);
		        		containerDetails.addRowToTable(detailsDataRow);
		        		
	    			}
	    		}
	    		
	    		// warnings
	    		Elements warnings = doc.select("#warnings");
	    		
	    		h3 = warnings.select("h3");
	    		
	    		if (h3.size() > 0) {
	    			Element e = h3.get(0);
	    			Matcher m = h3Pattern.matcher(e.text());
	    			if (m.matches()) {
	    				try {
	    					summaryCells[6] = new IntCell(new Integer(m.group(1)));	    				
	    				} catch (NumberFormatException nfe) {
	    					// leave it empty
	    				}
	    			}
	    		}
	    		
	    		sections = warnings.select(".warning-section");
	    		
	    		for (Element section : sections) {
	    			String uri = "";
	    			
	    			Elements h4 = section.select("h4 a");
	    			if (h4.size() > 0) {
		    			uri = h4.get(0).text();
	    			}
	    			
	    			for (Element warning : section.select(".warning")) {
		    			
		        		RowKey detailsKey = new RowKey("Row " + detailsRowCount++);
		        		
		    			DataCell[] detailsCells = new DataCell[7];
		    			
		    			detailsCells[0] = new StringCell(url);
		    			detailsCells[1] = new StringCell(uri);
		    			detailsCells[2] = new StringCell("warning");
		    			detailsCells[3] = new IntCell(0); // default warning level to 0
		    			
		    			Elements lineNumber = warning.select(".linenumber");
		    			if (lineNumber.size() > 0) {
		    				try {
		    					detailsCells[4] = new IntCell(new Integer(lineNumber.get(0).text()));
		    				} catch (NumberFormatException nfe) {
				    			detailsCells[4] = new IntCell(0);
		    				}
		    			} else {
			    			detailsCells[4] = new IntCell(0);	    				
		    			}
		    			
		    			Elements codeContext = warning.select(".codeContext");
		    			if (codeContext.size() > 0) {
			    			detailsCells[5] = new StringCell(codeContext.get(0).text());
		    			} else {
			    			detailsCells[5] = new StringCell("");
		    			}

		    			// default message to empty
		    			detailsCells[6] = new StringCell("");
		    			Elements parseError = null;
		    			
		    			// try for 20 warning levels
		    			for (int i = 0; i < 20; i++) {
		    				parseError = warning.select(".level" + i);
			    			if (parseError.size() > 0) {
				    			detailsCells[3] = new IntCell(i);
				    			detailsCells[6] = new StringCell(parseError.get(0).text());
				    			break;
			    			}
		    			}
	
		    			DataRow detailsDataRow = new DefaultRow(detailsKey, detailsCells);
		        		containerDetails.addRowToTable(detailsDataRow);
		        		
	    			}
	    		}

    		} else {    	    	
        		RowKey detailsKey = new RowKey("Row " + detailsRowCount++);

        		DataCell[] detailsCells = new DataCell[7];
    			
    			detailsCells[0] = new StringCell(url);
    			detailsCells[1] = new StringCell("");
    			detailsCells[2] = new StringCell("");
    			detailsCells[3] = new IntCell(0);
    			detailsCells[4] = new IntCell(0);
    			detailsCells[5] = new StringCell("");
    			if (goodRespond) {
    				detailsCells[6] = new StringCell(doc.html().toString());
    			} else {
    				detailsCells[6] = new StringCell(errorMessage);
    			}

    			DataRow detailsDataRow = new DefaultRow(detailsKey, detailsCells);
        		containerDetails.addRowToTable(detailsDataRow);    			
    		}
    		
    		DataRow summaryDataRow = new DefaultRow(key, summaryCells);
    		containerSummary.addRowToTable(summaryDataRow);
    		
            // check if the execution monitor was canceled
            exec.checkCanceled();
            exec.setProgress(summaryRowCount / (double)inData.getRowCount(), 
                "Adding row " + summaryRowCount);
            
            // pause for 1 second to ensure we don't submit URL more frequent than 1 per second.
            try {
            	Thread.sleep(1000);
            } catch (InterruptedException e) {
            	throw new Exception("Processing interrupted", e);
            }
            
    	}
    	
        // once we are done, we close the container and return its table
    	containerSummary.close();
    	containerDetails.close();

    	PortObject[] portObject = new PortObject[2];
    	
    	portObject[0] = containerSummary.getTable();
    	portObject[1] = containerDetails.getTable();
    	
    	return portObject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // TODO Code executed on reset.
        // Models build during execute are cleared here.
        // Also data handled in load/saveInternals will be erased here.
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
		if (m_configuration.getUrl().getStringValue().isEmpty()) {
			int index = inSpecs[0].findColumnIndex(W3cCssValidatorNodeConfiguration.FIELD_DEFAULT_URL_COLUMN); 
			boolean found = false;
			if (index >= 0) {
				DataColumnSpec columnSpec = inSpecs[0].getColumnSpec(index);
				// check if column is of string type
				if (columnSpec.getType().equals(StringCell.TYPE)) {
					// found URL column
					m_configuration.getUrl().setStringValue(W3cCssValidatorNodeConfiguration.FIELD_DEFAULT_URL_COLUMN);
					setWarningMessage("Auto-guessing: Using column '"+W3cCssValidatorNodeConfiguration.FIELD_DEFAULT_URL_COLUMN+"' as URL column");
					found = true;
				}
			}
			
			// if URL column is still not found 
			if (!found) {
				// URL column doesn't exist, now check the first String column
				for (Iterator<DataColumnSpec> it = inSpecs[0].iterator(); it.hasNext();) {
					DataColumnSpec columnSpec = it.next();
					if (columnSpec.getType().equals(StringCell.TYPE)) {
						m_configuration.getUrl().setStringValue(columnSpec.getName());
						setWarningMessage("Auto-guessing: Using first string column '"+columnSpec.getName()+"' as URL column");
						break;
					}
				}
			}
		}		
		
		if (m_configuration.getUrl().getStringValue().isEmpty()) {
			setWarningMessage("A string column for URLs in the data input table must exist and must be specified.  Please create a URL column or pick the right column in this node's configuration.");
		}

		return new DataTableSpec[]{null, null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {

    	m_configuration.saveSettingsTo(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
    	m_configuration.loadValidatedSettingsFrom(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
    	m_configuration.validateSettings(settings);

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        
        // TODO load internal data. 
        // Everything handed to output ports is loaded automatically (data
        // returned by the execute method, models loaded in loadModelContent,
        // and user settings set through loadSettingsFrom - is all taken care 
        // of). Load here only the other internals that need to be restored
        // (e.g. data used by the views).

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
       
        // TODO save internal models. 
        // Everything written to output ports is saved automatically (data
        // returned by the execute method, models saved in the saveModelContent,
        // and user settings saved through saveSettingsTo - is all taken care 
        // of). Save here only the other internals that need to be preserved
        // (e.g. data used by the views).

    }

}

