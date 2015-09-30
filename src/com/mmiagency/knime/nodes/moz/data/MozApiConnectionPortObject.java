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
    public String getSummary() {
        return m_spec.getMozApiConnection().toString();
    }

    /**
     * {@inheritDoc}
     */
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
