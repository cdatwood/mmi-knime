package com.pg.facebook.api.selectpost.node;

import java.io.File;
import java.io.IOException;

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

import com.pg.facebook.api.connector.data.FacebookApiConnectorPortObject;

/**
 * This is the model implementation of SelectPost.
 * 
 * 
 * @author P&G, eBusiness
 */
public class SelectPostNodeModel extends NodeModel {

	private SelectPostConfiguration config;
	
	/**
	 * Constructor for the node model.
	 */
	protected SelectPostNodeModel() {

		super(new PortType[] { FacebookApiConnectorPortObject.TYPE },
				new PortType[] { FacebookApiConnectorPortObject.TYPE });
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec)
			throws Exception {
		
		FacebookApiConnectorPortObject portObject = (FacebookApiConnectorPortObject)inObjects[0];
		portObject.setObjectId(config.getPostId());
		
		return new PortObject[] { portObject };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		// TODO: generated method stub
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {
		return inSpecs;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		if ( config != null ) {
			config.save(settings);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		
		config = new SelectPostConfiguration();
		config.load(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		// TODO: generated method stub
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
