package com.pg.facebook.api.connector.data;

import java.io.IOException;
import java.util.zip.ZipEntry;

import javax.swing.JComponent;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContent;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.ModelContentWO;
import org.knime.core.node.port.AbstractSimplePortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;
import com.pg.facebook.api.FacebookApiClient;

public class FacebookApiConnectorPortObject extends AbstractSimplePortObject {

	private FacebookApiConnectionPortObjectSpec spec;
	
    public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(FacebookApiConnectorPortObject.class);

	public FacebookApiConnectorPortObject() {
		
	}
	
	public FacebookApiClient getFacebookApiClient() {
		return spec.getFacebookClient();
	}
	
	public void setObjectId( String accountId) {
		spec.setObjectId(accountId);
	}
	
	public void setImpersonationAccessToken( String token ) {
		spec.setImpersonationAccessToken(token);
	}
	
	public FacebookApiConnectorPortObject(FacebookApiConnectionPortObjectSpec spec) {
		this.spec = spec;
	}
	
	@Override
	public String getSummary() {
		return spec.getFacebookClient().toString();
	}

	@Override
	public PortObjectSpec getSpec() {
		return spec;
	}

	@Override
	public JComponent[] getViews() {
		// TODO Auto-generated method stub
		return null;
	}

    protected void save(final ModelContentWO model, final ExecutionMonitor exec) throws CanceledExecutionException {
		this.spec.save(model);
    }
	
	protected void load(final ModelContentRO model, final PortObjectSpec spec, final ExecutionMonitor exec)
            throws InvalidSettingsException, CanceledExecutionException {
		this.spec = (FacebookApiConnectionPortObjectSpec)spec;
		this.spec.load(model);
    }

	static public class Serializer extends PortObjectSerializer<FacebookApiConnectorPortObject> {

		@Override
		public void savePortObject(FacebookApiConnectorPortObject portObject, PortObjectZipOutputStream out,
				ExecutionMonitor exec) throws IOException, CanceledExecutionException {
	        ModelContent model = new ModelContent("content.xml");
	        ModelContentWO wo = model.addModelContent("FacebookApiConnector");
	        portObject.save(wo, exec);
	        out.putNextEntry(new ZipEntry("content.xml"));
	        model.saveToXML(out);
		}

		@Override
		public FacebookApiConnectorPortObject loadPortObject(PortObjectZipInputStream in, PortObjectSpec spec,
				ExecutionMonitor exec) throws IOException, CanceledExecutionException {
	        ZipEntry entry = in.getNextEntry();
	        if(!"content.xml".equals(entry.getName())) {
	            throw new IOException("Expected zip entry content.xml, got " + entry.getName());
	        } else {
	            ModelContentRO model = ModelContent.loadFromXML(in);
	            FacebookApiConnectorPortObject result = new FacebookApiConnectorPortObject();

	            try {
	            	if (!model.containsKey("FacebookApiConnector")) {
	            		return result;
	            	}
	                ModelContentRO ro = model.getModelContent("FacebookApiConnector");
	                result.load(ro, spec, exec);
	                return result;
	            } catch (InvalidSettingsException e) {
	                throw new IOException("Unable to load content into \"FacebookApiConnectionPortObject\": " + e.getMessage(), e);
	            }
	        }
		}
    }
}
