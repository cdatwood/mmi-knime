package com.pg.facebook.api.connector.node;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.pg.facebook.api.connector.data.FacebookApiConnectionPortObjectSpec;
import com.pg.facebook.api.connector.data.FacebookApiConnectorPortObject;
import com.pg.knime.node.ITracker;
import com.pg.knime.node.StandardTrackedNodeModel;


/**
 * This is the model implementation of FacebookConnector.
 * 
 *
 * @author P&G, eBusiness Analytics
 */
public class FacebookConnectorNodeModel extends StandardTrackedNodeModel {
    
	private FacebookConnectorConfiguration config;
        
    /**
     * Constructor for the node model.
     */
    protected FacebookConnectorNodeModel() {
    	super(new PortType[0], new PortType[]{FacebookApiConnectorPortObject.TYPE});
    }

    @Override
    protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec)
    		throws Exception {
    	
    	if ( config == null ) {
    		throw new InvalidSettingsException("Please configure node before execution");
    	}
    	
    	track(ITracker.EXECUTE);
    	return new PortObject[] { new FacebookApiConnectorPortObject(new FacebookApiConnectionPortObjectSpec(config)) };
    	
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        
    }

    @Override
    protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs)
    		throws InvalidSettingsException {
    	return new PortObjectSpec[] { new FacebookApiConnectionPortObjectSpec() };
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {


    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        config = new FacebookConnectorConfiguration();
        config.load(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
       config = new FacebookConnectorConfiguration();
       config.load(settings);
       if ( config.getAccessToken() == null || StringUtils.isEmpty(config.getAccessToken()) ) {
    	   throw new InvalidSettingsException("User token required.");
       }

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

