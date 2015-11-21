package com.pg.facebook.api.connector.data;

import java.io.IOException;

import javax.swing.JComponent;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.port.AbstractPortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;
import org.knime.core.node.port.PortType;

import com.pg.facebook.api.FacebookApiClient;

public class FacebookApiConnectorPortObject extends AbstractPortObject {

	private FacebookApiConnectionPortObjectSpec spec;
	
	public static final PortType TYPE = new PortType(FacebookApiConnectorPortObject.class);

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

	@Override
	protected void save(PortObjectZipOutputStream out, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		
		
	}

	
	
	@Override
	protected void load(PortObjectZipInputStream in, PortObjectSpec spec,
			ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
		
		this.spec = (FacebookApiConnectionPortObjectSpec)spec;
		
	}

}
