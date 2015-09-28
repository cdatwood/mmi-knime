package com.mmiagency.knime.nodes.moz.data;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.ModelContentWO;
import org.knime.core.node.port.AbstractSimplePortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.util.ViewUtils;

/**
 * Port object containing a MozAuthenticator
 *
 * @author Phuc Truong
 */
public final class MozApiConnectionPortObject extends AbstractSimplePortObject {

    private MozApiConnectionPortObjectSpec m_spec;

    /**
     * The type of this port.
     */
    public static final PortType TYPE = new PortType(MozApiConnectionPortObject.class);

    /**
     * Database type for optional ports.
     */
    public static final PortType TYPE_OPTIONAL = new PortType(MozApiConnectionPortObject.class, true);
    
    /**
     * Constructor for framework.
     */
    public MozApiConnectionPortObject() {
        // used by framework
    }

    /**
     * @param spec The specification of this port object.
     */
    public MozApiConnectionPortObject(final MozApiConnectionPortObjectSpec spec) {
        m_spec = spec;
    }

    /**
     * @return The contained MozApiConnection object
     */
    public MozApiConnection getMozApiConnection() {
        return m_spec.getMozApiConnection();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSummary() {
        return m_spec.getMozApiConnection().toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PortObjectSpec getSpec() {
        return m_spec;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void save(final ModelContentWO model, final ExecutionMonitor exec) throws CanceledExecutionException {
        // nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void load(final ModelContentRO model, final PortObjectSpec spec, final ExecutionMonitor exec)
            throws InvalidSettingsException, CanceledExecutionException {
        m_spec = (MozApiConnectionPortObjectSpec)spec;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JComponent[] getViews() {
        String text;
        if (getMozApiConnection() != null) {
            text = "<html>" + getMozApiConnection().toString().replace("\n", "<br>") + "</html>";
        } else {
            text = "No connection available";
        }
        JPanel f = ViewUtils.getInFlowLayout(new JLabel(text));
        f.setName("Connection");
        return new JComponent[]{new JScrollPane(f)};
    }

}
