package com.pg.facebook.api.selectaccount.node;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
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

import com.pg.facebook.api.connector.data.FacebookApiConnectionPortObjectSpec;
import com.pg.facebook.api.connector.data.FacebookApiConnectorPortObject;

/**
 * This is the model implementation of SelectAccount.
 * 
 *
 * @author P&G, eBusiness
 */
public class SelectAccountNodeModel extends NodeModel {
    
	private FacebookSelectAccountConfiguration config;
	
    /**
     * Constructor for the node model.
     */
    protected SelectAccountNodeModel() {
    
        // TODO: Specify the amount of input and output ports needed.
        super(
        		new PortType[] { FacebookApiConnectorPortObject.TYPE }, 
        		new PortType[] { FacebookApiConnectorPortObject.TYPE }
        );
    }

    
    @Override
    protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec)
    		throws Exception {
    	
    	if (!( inObjects[0] instanceof FacebookApiConnectorPortObject ) ) {
    		throw new Exception("Incorrect input port class type");
    	}
    	
    	if ( config == null ) {
    		throw new InvalidSettingsException("Please configure node before execution");
    	}
    	
    	FacebookApiConnectorPortObject portObject = (FacebookApiConnectorPortObject)inObjects[0];
    	portObject.setObjectId(config.getAccountId());
    	portObject.setImpersonationAccessToken(config.getAccessToken());
    	
    	
    	return new PortObject[] {portObject};

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
		
	   // Validate that API node is executed
	   if ( inSpecs == null || inSpecs.length <= 0) {
		   throw new InvalidSettingsException("Please connect to Facebook API Connector before configurating");
	   }
	   
	   if (! (inSpecs[0] instanceof FacebookApiConnectionPortObjectSpec ) ) {
		   throw new InvalidSettingsException("Please connect to Facebook API Connector before configurating");
	   }
	   
	   return inSpecs;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
         if ( config != null ) 
        	 config.save(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        
    	config = new FacebookSelectAccountConfiguration();
    	config.load(settings);
    	
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    
    	
    	config = new FacebookSelectAccountConfiguration();
    	config.load(settings);
    	
    	if ( StringUtils.isEmpty(config.getAccessToken()) ) {
    		throw new InvalidSettingsException("Access Token is required");
    	}
    	
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

