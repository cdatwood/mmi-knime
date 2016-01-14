package com.pg.facebook.api.connector.data;

import java.io.IOException;
import java.util.zip.ZipEntry;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContent;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.ModelContentWO;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.port.AbstractSimplePortObjectSpec;
import org.knime.core.node.port.PortObjectSpecZipInputStream;
import org.knime.core.node.port.PortObjectSpecZipOutputStream;
import com.pg.facebook.api.FacebookApiClient;
import com.pg.facebook.api.connector.node.FacebookConnectorConfiguration;
import com.pg.facebook.api.selectaccount.node.FacebookSelectAccountConfiguration;

public class FacebookApiConnectionPortObjectSpec extends AbstractSimplePortObjectSpec {

	private FacebookApiClient client = null;
	
	private FacebookConnectorConfiguration connectionConfig;
	private FacebookSelectAccountConfiguration accountConfig; 
	
	private static NodeLogger LOGGER = NodeLogger.getLogger(FacebookApiConnectionPortObjectSpec.class);
	
	public FacebookApiConnectionPortObjectSpec() {
		

	}
	
	public FacebookApiConnectionPortObjectSpec(final FacebookConnectorConfiguration config) {
		connectionConfig = config;
		if ( config == null ) client = new FacebookApiClient();
		else client = new FacebookApiClient(config.getAccessToken(), config.getAppSecret());
	}
	
	public void setObjectId( String accountId) {
		if ( accountConfig == null ) accountConfig = new FacebookSelectAccountConfiguration();
		
		accountConfig.setAccountId(accountId);
		client.setImpersonationAccountId(accountId);
	}
	
	public void setImpersonationAccessToken(String accessToken) {
		if ( accountConfig == null ) accountConfig = new FacebookSelectAccountConfiguration();
		
		accountConfig.setAccessToken(accessToken);
		client.setImpersonationAccessToken(accessToken);
	}
	
	@Override
	protected void save(ModelContentWO model) {
		if ( connectionConfig != null)
			connectionConfig.save(model);
		
		if ( accountConfig != null ) 
			accountConfig.save(model);
	}

	@Override
	protected void load(ModelContentRO model) throws InvalidSettingsException {
		
		// Load Connection settings
		connectionConfig = new FacebookConnectorConfiguration();
		connectionConfig.load(model);
		client = new FacebookApiClient(connectionConfig.getAccessToken(), connectionConfig.getAppSecret());
		
		accountConfig = new FacebookSelectAccountConfiguration();
		accountConfig.load(model);
		client.setImpersonationAccountId(accountConfig.getAccountId());
		client.setImpersonationAccessToken(accountConfig.getAccessToken());
		
	}
	
	public FacebookApiClient getFacebookClient() {
		/*
		if (connectionConfig == null) 
			LOGGER.info("ACCESS TOKEN: []");
		else
			LOGGER.info("ACCESS TOKEN: " + connectionConfig.getAccessToken());
			*/
		return client;
	}
	
	public String getAccessToken() {
		return connectionConfig.getAccessToken();
	}


    static public class Serializer extends PortObjectSpecSerializer<FacebookApiConnectionPortObjectSpec> {

		@Override
		public void savePortObjectSpec(FacebookApiConnectionPortObjectSpec portObjectSpec, PortObjectSpecZipOutputStream out)
				throws IOException {
	        ModelContent model = new ModelContent("content.xml");
	        ModelContentWO wo = model.addModelContent("FacebookApiConnection");
	        portObjectSpec.save(wo);
	        out.putNextEntry(new ZipEntry("content.xml"));
	        model.saveToXML(out);			
		}

		@Override
		public FacebookApiConnectionPortObjectSpec loadPortObjectSpec(PortObjectSpecZipInputStream in) throws IOException {
	        ZipEntry entry = in.getNextEntry();
	        if(!"content.xml".equals(entry.getName())) {
	            throw new IOException("Expected zip entry content.xml, got " + entry.getName());
	        } else {
	            ModelContentRO model = ModelContent.loadFromXML(in);
	            FacebookApiConnectionPortObjectSpec result = new FacebookApiConnectionPortObjectSpec();

	            try {
	            	if (!model.containsKey("FacebookApiConnection")) {
	            		return result;
	            	}
	                ModelContentRO ro = model.getModelContent("FacebookApiConnection");
	                result.load(ro);
	                return result;
	            } catch (InvalidSettingsException e) {
	                throw new IOException("Unable to load content into \"FacebookApiConnectionPortObjectSpec\": " + e.getMessage(), e);
	            }
	        }
		}
    	
    }
}
