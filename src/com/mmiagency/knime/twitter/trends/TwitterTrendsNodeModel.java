package com.mmiagency.knime.twitter.trends;

import java.io.File;
import java.io.IOException;

import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.twitter.api.data.TwitterApiConnectionPortObject;

import com.mmiagency.knime.twitter.util.TrendRowFactory;

import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.Location;


/**
 * This is the model implementation of TwitterTrends.
 * 
 *
 * @author Ed Ng
 */
public class TwitterTrendsNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(TwitterTrendsNodeModel.class);
        

	TwitterTrendsNodeConfiguration m_config = new TwitterTrendsNodeConfiguration();

	/**
     * Constructor for the node model.
     */
    protected TwitterTrendsNodeModel() {
    
        super(new PortType[]{TwitterApiConnectionPortObject.TYPE}, new PortType[]{BufferedDataTable.TYPE});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inObjects, final ExecutionContext exec) throws Exception {

    	logger.debug("Executing");
    	
    	TrendRowFactory factory = new TrendRowFactory();
        BufferedDataContainer container = exec.createDataContainer(factory.tableSpec());
        int index = 0;
    	
    	Twitter twitter = ((TwitterApiConnectionPortObject)inObjects[0]).getTwitterApiConnection().getTwitter();
    	
    	Trends trends = twitter.getPlaceTrends(m_config.getWoeid());
    	Location location = trends.getLocation();

    	for (Trend trend : trends.getTrends()) {
    		container.addRowToTable(factory.createRow("" + index++, location, trend));
    	}
    	
        container.close();
        return new PortObject[]{container.getTable()};           
        
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
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
            throws InvalidSettingsException {
        
        if (m_config.getWoeid() <= 0) {
            throw new InvalidSettingsException("Setting a place is mandatory");
        }

        return new PortObjectSpec[]{new TrendRowFactory().tableSpec()};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	m_config.save(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	TwitterTrendsNodeConfiguration config = new TwitterTrendsNodeConfiguration();
        config.loadInModel(settings);
        m_config = config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	m_config.loadInModel(settings);
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

