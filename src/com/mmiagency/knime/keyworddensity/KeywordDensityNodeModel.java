package com.mmiagency.knime.keyworddensity;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.mmiagency.knime.keyworddensity.util.KeywordDensityHelper;
import com.mmiagency.knime.keyworddensity.util.KeywordDensityRowEntry;
import com.mmiagency.knime.keyworddensity.util.KeywordDensityRowFactory;

/**
 * This is the model implementation of KeywordDensity.
 * 
 *
 * @author Ed Ng
 */
public class KeywordDensityNodeModel extends NodeModel {
    
    private static final NodeLogger logger = NodeLogger.getLogger(KeywordDensityNodeModel.class);
    
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
    	String exclude = m_config.getExclude().getStringValue();
    	int urlColumnIndex = inSpec.findColumnIndex(urlColumnName);

    	for (Iterator<DataRow> it = inData[0].iterator(); it.hasNext();) {
    		DataRow row = it.next();
    		DataCell cell = row.getCell(urlColumnIndex);
    		if (cell.isMissing()) {
    			container.addRowToTable(factory.createRow("" + index++, "", "FAILED: Missing URL"));
    			continue;
    		}
    		String url = ((StringValue)cell).getStringValue();
    		// using helper class to process content
    		KeywordDensityHelper helper = new KeywordDensityHelper(url, exclude);
    		
    		try {
    			helper.execute();
    		} catch (Exception e) {    			
    			container.addRowToTable(factory.createRow("" + index++, url, "FAILED: " + e.getMessage()));
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
		
		if (inSpecs[0].findColumnIndex(m_config.getUrl().getStringValue()) < 0) {
			throw new InvalidSettingsException("A URL column in the data input table must exist and must be specified.");
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

