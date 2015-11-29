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

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DefaultRow;
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
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import com.google.gson.Gson;
import com.mmiagency.knime.nodes.moz.api.authentication.Authenticator;
import com.mmiagency.knime.nodes.moz.api.constants.LinksConstants;
import com.mmiagency.knime.nodes.moz.api.response.LinksResponse;
import com.mmiagency.knime.nodes.moz.api.service.LinksService;
import com.mmiagency.knime.nodes.moz.data.MozApiConnectionPortObject;
import com.mmiagency.knime.nodes.util.Util;



/**
 * This is the model implementation of MozLinkMetrics.
 * 
 *
 * @author Phuc Truong
 */
public class MozLinkMetricsNodeModel extends NodeModel {
        
    MozLinkMetricsNodeConfiguration m_config = new MozLinkMetricsNodeConfiguration();

	/**
     * Constructor for the node model.
     */
    protected MozLinkMetricsNodeModel() {
        super(new PortType[]{BufferedDataTable.TYPE, MozApiConnectionPortObject.TYPE}, new PortType[]{BufferedDataTable.TYPE});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inData, final ExecutionContext exec) throws Exception {
    
    	// Load the Moz Authentication information
        Authenticator authenticator = ((MozApiConnectionPortObject)inData[1]).getMozApiConnection().getMozAuthenticator();
        
        // Load the URL's to analyze from the input table
        BufferedDataTable inputTable = (BufferedDataTable)inData[0];
       	DataTableSpec inSpec = inputTable.getSpec();
       	
       	String linkColumnName = m_config.getUrl().getStringValue();    	
    	int linkColumnIndex = inSpec.findColumnIndex(linkColumnName);		
    	if (linkColumnIndex < 0) throw new InvalidSettingsException("You must link a table with URL column to this node.");

    	// Look through the table for values and process
        BufferedDataContainer dc = exec.createDataContainer(createTableSpec());
        long rowIndex = 0;
        long inDataIndex = -1;
    	for (Iterator<DataRow> it = inputTable.iterator(); it.hasNext();) {
    		inDataIndex++;
    		DataRow row = it.next();
			DataCell cell = row.getCell(linkColumnIndex);
			
			// Validate the cell information
			if (cell.isMissing()) {
				setWarningMessage("URL is missing on row " + inDataIndex);
				continue;
			}
			
			// Skip blank URL's
			String url = ((StringValue)cell).getStringValue();
			if (Util.isBlankOrNull(url)) {
				setWarningMessage("Skipping blank url: " + url);
				continue;
			}
			
			// Update the progress
			BigDecimal progressPercentage = new BigDecimal(rowIndex).divide(new BigDecimal(inputTable.size()), 2, BigDecimal.ROUND_HALF_UP);
			exec.setProgress(progressPercentage.doubleValue(), "Processing " + (rowIndex + 1) + " of " + inputTable.size() + ": "+ url);
			
			// Process the URL using MOZ
    		try {    			
    			rowIndex = processMozLinkMetrics(authenticator, dc, rowIndex, url, exec);
    		} catch (Exception e) {
    			setWarningMessage("Unable to retrieve Moz LinkMetrics results for url: " + url + ", error: " + e.getClass() + " -> " + e.getMessage());
    		}
    	}
        
        dc.close();
        
        return new BufferedDataTable[] {dc.getTable()};         
    }

    public static String MOZ_FIELD_TITLE = "ut";
    public static String MOZ_FIELD_CANONICAL_URL = "uu";
    public static String MOZ_FIELD_EXTERNAL_EQUITY_URLS = "ueid";
    public static String MOZ_FIELD_URLS = "uid";
    public static String MOZ_FIELD_MOZ_RANK_URL = "umrp";
    public static String MOZ_FIELD_MOZ_RANK_RAW_URL = "umrr";
    public static String MOZ_FIELD_MOZ_RANK_SUBDOMAIN = "fmrp";
    public static String MOZ_FIELD_MOZ_RANK_RAW_SUBDOMAIN = "fmrr";
    public static String MOZ_FIELD_HTTP_STATUS_CODE = "us";
    public static String MOZ_FIELD_PAGE_AUTHORITY = "upa";
    public static String MOZ_FIELD_DOMAIN_AUTHORITY = "pda";
    public static String MOZ_FIELD_TIME_LAST_CRAWLED = "ulc";
   
    private Long m_lastApiCallMillis = null;
    
    private long processMozLinkMetrics(Authenticator authenticator, BufferedDataContainer dc, long rowIndex, String url, final ExecutionContext exec) throws Exception {
    	
    	// Grab the parameters set by the user
    	String scope = m_config.getScope().getStringValue();
    	String sort = m_config.getSort().getStringValue();
    	Integer maxResults = m_config.getMaxResults().getIntValue();

    	
    	// Run the API calls in bac
        LinksService linksService = new LinksService();
        linksService.setAuthenticator(authenticator);
    	int index = 0;
    	int batchSize = 50;
    	if (maxResults < batchSize) batchSize = maxResults;

    	// Create sub process status
    	ExecutionMonitor subExec = exec.createSubProgress(1);


    	BigDecimal delayBetweenCalls = new BigDecimal(m_config.getDelayBetweenCalls().getDoubleValue());
    	long sleepBetweenCalls =  delayBetweenCalls.multiply(new BigDecimal(1000)).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
    	
    	while (index < maxResults) {
			BigDecimal progressPercentage = new BigDecimal(index).divide(new BigDecimal(maxResults), 2, BigDecimal.ROUND_HALF_UP);
    		
    		// Sleep 5 seconds between calls
    		if (m_lastApiCallMillis != null) {
        		long currentTimeMillis = System.currentTimeMillis();
        		long timeElapsedSinceLastCall = currentTimeMillis - m_lastApiCallMillis;
        		if (timeElapsedSinceLastCall < sleepBetweenCalls) {
            		subExec.setProgress(progressPercentage.doubleValue(), "Delaying " +  delayBetweenCalls + " seconds between calls for URL: " + url + " -> Next Index: " + (index + 1) + " - " + (index + batchSize));
        			Thread.sleep(sleepBetweenCalls - timeElapsedSinceLastCall);
        		}
    		}
    		
			// Update the progress
    		subExec.setProgress(progressPercentage.doubleValue(), "Calling API for URL: " + url + " -> Index: " + (index + 1) + " - " + (index + batchSize));

			// Make the API Call
            String response = linksService.getLinks(url, scope, null, sort, LinksConstants.LINKS_COL_ALL, index, batchSize);
            m_lastApiCallMillis = System.currentTimeMillis();
            
            Gson gson = new Gson();
            LinksResponse links[] = gson.fromJson(response, LinksResponse[].class);
            for (int i =0 ; i < links.length; i++) {
            	LinksResponse result = links[i];
            	
            	// Add the data to row
            	final LinkedList<DataCell> cells = new LinkedList<DataCell>();
            	cells.add(new StringCell(url));
            	cells.add(new StringCell(result.fmrp));
            	cells.add(new StringCell(result.fmrr));
            	cells.add(new StringCell(result.lrid));
            	cells.add(new StringCell(result.lsrc));
            	cells.add(new StringCell(result.ltgt));
            	cells.add(new StringCell(result.lufmrp));
            	cells.add(new StringCell(result.lufmrr));
            	cells.add(new StringCell(result.lupda));
            	cells.add(new StringCell(result.luueid));
            	cells.add(new StringCell(result.luuid));
            	cells.add(new StringCell(result.luulc));
            	cells.add(new StringCell(result.luumrp));
            	cells.add(new StringCell(result.luumrr));
            	cells.add(new StringCell(result.luupa));
            	cells.add(new StringCell(result.luus));
            	cells.add(new StringCell(result.luut));
            	cells.add(new StringCell(result.pda));
            	cells.add(new StringCell(result.ueid));
            	cells.add(new StringCell(result.uid));
            	cells.add(new StringCell(result.ulc));
            	cells.add(new StringCell(result.umrp));
            	cells.add(new StringCell(result.umrr));
            	cells.add(new StringCell(result.upa));
            	cells.add(new StringCell(result.us));
            	cells.add(new StringCell(result.ut));
            	final DefaultRow row = new DefaultRow(RowKey.createRowKey(rowIndex), cells);
            	dc.addRowToTable(row);
            	rowIndex++;
            }
            
            // Stop processing if there are no more results
            if (links.length < batchSize) break;
            index += batchSize;
            
    	}
    	
		return rowIndex;
    }

    private DataTableSpec createTableSpec() {
        final LinkedList<DataColumnSpec> specs = new LinkedList<DataColumnSpec>();
        specs.add(new DataColumnSpecCreator("url", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("fmrp", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("fmrr", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("lrid", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("lsrc", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("ltgt", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("lufmrp", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("lufmrr", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("lupda", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("luueid", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("luuid", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("luulc", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("luumrp", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("luumrr", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("luupa", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("luus", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("luut", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("pda", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("ueid", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("uid", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("ulc", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("umrp", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("umrr", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("upa", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("us", StringCell.TYPE).createSpec());
        specs.add(new DataColumnSpecCreator("ut", StringCell.TYPE).createSpec());
    	return new DataTableSpec(specs.toArray(new DataColumnSpec[0]));
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
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		if (inSpecs.length<2) {
			throw new InvalidSettingsException("You must link a table with URL column to this node.");
		}
		
		DataTableSpec tableSpec = (DataTableSpec) inSpecs[0];
		
		int index = tableSpec.findColumnIndex(m_config.getUrl().getStringValue()); 
		if (index < 0) {
			// URL column doesn't exist, now check the first String column
			for (Iterator<DataColumnSpec> it = tableSpec.iterator(); it.hasNext();) {
				DataColumnSpec columnSpec = it.next();
				if (columnSpec.getType().isCompatible(StringValue.class)) {
					m_config.getUrl().setStringValue(columnSpec.getName());
					setWarningMessage("Auto-guessing: Using first string column '"+columnSpec.getName()+"' as URL column");
					break;
				}
			}			
		}	
		
		if (m_config.getUrl().getStringValue().isEmpty()) {
			setWarningMessage("A string column for URLs in the data input table must exist and must be specified.  Please create a URL column or pick the right column in this node's configuration.");
			throw new InvalidSettingsException("Url is required");
		}    	

        return new PortObjectSpec[]{createTableSpec()};
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
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
    	MozLinkMetricsNodeConfiguration config = new MozLinkMetricsNodeConfiguration();
        config.loadValidatedSettingsFrom(settings);
        m_config = config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
    	m_config.validateSettings(settings);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {
        
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
    protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {
       
        // TODO save internal models. 
        // Everything written to output ports is saved automatically (data
        // returned by the execute method, models saved in the saveModelContent,
        // and user settings saved through saveSettingsTo - is all taken care 
        // of). Save here only the other internals that need to be preserved
        // (e.g. data used by the views).

    }

}

