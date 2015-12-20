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

import java.io.IOException;
import java.util.zip.ZipEntry;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContent;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.ModelContentWO;
import org.knime.core.node.port.AbstractSimplePortObjectSpec;
import org.knime.core.node.port.PortObjectSpecZipInputStream;
import org.knime.core.node.port.PortObjectSpecZipOutputStream;
import org.knime.core.node.util.ViewUtils;

/**
 * Specification for the MozApiConnectionPortObject.
 *
 * @author "Patrick Winter", University of Konstanz
 */
public final class MozApiConnectionPortObjectSpec extends AbstractSimplePortObjectSpec {

    private MozApiConnection m_mozApiConnection;

    /**
     * Constructor for a port object spec that holds no MozApiConnection.
     */
    public MozApiConnectionPortObjectSpec() {
        m_mozApiConnection = null;
    }

    /**
     * @param MozApiConnection The MozApiConnection that will be contained by this port object spec
     */
    public MozApiConnectionPortObjectSpec(final MozApiConnection mozApiConnection) {
        m_mozApiConnection = mozApiConnection;
    }

    /**
     * @return The contained MozApiConnection object
     */
    public MozApiConnection getMozApiConnection() {
        return m_mozApiConnection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void save(final ModelContentWO model) {
        m_mozApiConnection.save(model);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void load(final ModelContentRO model) throws InvalidSettingsException {
        m_mozApiConnection = new MozApiConnection(model);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object ospec) {
        if (this == ospec) {
            return true;
        }
        if (!(ospec instanceof MozApiConnectionPortObjectSpec)) {
            return false;
        }
        MozApiConnectionPortObjectSpec spec = (MozApiConnectionPortObjectSpec)ospec;
        return m_mozApiConnection.equals(spec.m_mozApiConnection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return m_mozApiConnection != null ? m_mozApiConnection.hashCode() : 0;
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
        return new JComponent[]{f};
    }
    
    static public class Serializer extends PortObjectSpecSerializer<MozApiConnectionPortObjectSpec> {

		@Override
		public void savePortObjectSpec(MozApiConnectionPortObjectSpec portObjectSpec, PortObjectSpecZipOutputStream out)
				throws IOException {
	        ModelContent model = new ModelContent("content.xml");
	        ModelContentWO wo = model.addModelContent("MozApiConnection");
	        portObjectSpec.getMozApiConnection().save(wo);
	        out.putNextEntry(new ZipEntry("content.xml"));
	        model.saveToXML(out);			
		}

		@Override
		public MozApiConnectionPortObjectSpec loadPortObjectSpec(PortObjectSpecZipInputStream in) throws IOException {
	        ZipEntry entry = in.getNextEntry();
	        if(!"content.xml".equals(entry.getName())) {
	            throw new IOException("Expected zip entry content.xml, got " + entry.getName());
	        } else {
	            ModelContentRO model = ModelContent.loadFromXML(in);
	            MozApiConnectionPortObjectSpec result = new MozApiConnectionPortObjectSpec();

	            try {
	            	if (!model.containsKey("MozApiConnection")) {
	            		return result;
	            	}
	                ModelContentRO ro = model.getModelContent("MozApiConnection");
	                result.load(ro);
	                return result;
	            } catch (InvalidSettingsException e) {
	                throw new IOException("Unable to load content into \"MozApiConnectionPortObjectSpec\": " + e.getMessage(), e);
	            }
	        }
		}
    	
    }

}
