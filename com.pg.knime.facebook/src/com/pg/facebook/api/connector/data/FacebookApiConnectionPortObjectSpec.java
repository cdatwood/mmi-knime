package com.pg.facebook.api.connector.data;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.ModelContentWO;
import org.knime.core.node.port.AbstractSimplePortObjectSpec;

import com.pg.facebook.api.FacebookApiClient;
import com.pg.facebook.api.connector.node.FacebookConnectorConfiguration;
import com.pg.facebook.api.selectaccount.node.FacebookSelectAccountConfiguration;

public class FacebookApiConnectionPortObjectSpec extends AbstractSimplePortObjectSpec {

	private FacebookApiClient client = null;
	
	private FacebookConnectorConfiguration connectionConfig;
	private FacebookSelectAccountConfiguration accountConfig; 
	
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
		return client;
	}

}
