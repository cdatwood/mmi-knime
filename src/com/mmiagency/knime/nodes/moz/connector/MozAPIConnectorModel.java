package com.mmiagency.knime.nodes.moz.connector;

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
import com.mmiagency.knime.nodes.moz.data.MozApiConnectionPortObject;
import com.mmiagency.knime.nodes.moz.data.MozApiConnectionPortObjectSpec;

/**
 * @author Phuc Truong
 */
public class MozAPIConnectorModel extends NodeModel {

    private MozAPIConnectorConfiguration m_config = new MozAPIConnectorConfiguration();

    /**
     * 
     */
    public MozAPIConnectorModel() {
        super(new PortType[0], new PortType[]{MozApiConnectionPortObject.TYPE});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // Not used
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // Not used
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(NodeSettingsWO settings) {
        m_config.save(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
        MozAPIConnectorConfiguration config = new MozAPIConnectorConfiguration();
        config.loadInModel(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
        MozAPIConnectorConfiguration config = new MozAPIConnectorConfiguration();
        config.loadInModel(settings);
        m_config = config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // Not used
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
        return new PortObject[]{new MozApiConnectionPortObject(createSpec())};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
        return new PortObjectSpec[]{createSpec()};
    }

    private MozApiConnectionPortObjectSpec createSpec() throws InvalidSettingsException {
        return new MozApiConnectionPortObjectSpec(m_config.createMozApiConnection());
    }

}
