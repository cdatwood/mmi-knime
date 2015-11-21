package com.pg.facebook.api.connector.node;

import java.util.ArrayList;

import org.knime.core.node.ModelContentRO;
import org.knime.core.node.ModelContentWO;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.pg.knime.secure.DefaultVault;

public class FacebookConnectorConfiguration {

	private String accessToken;
	private String[] tokens;
    private String[] authTypes;
	
	private static final String CFG_APP_ID = "facebook app id";
	private static final String CFG_APP_SECRET = "facebook_app_secret";
	private static final String CFG_TOKEN = "facebook user token";
	
	public static final String DEFAULT_AUTH_TYPE = "User Specific";
    
	private DefaultVault vault;
	
	public FacebookConnectorConfiguration() {
	
		/* If FacebookVault implementation not present default to custom token */
    	try {
    		vault = (DefaultVault)Class.forName("com.pg.knime.vault.FacebookVault").newInstance();	
    	} catch ( ClassNotFoundException | IllegalAccessException | InstantiationException exc ) {
    		vault = new DefaultVault();
    	}
    	
    	tokens = vault.getKey("USER_TOKENS").split(",");
    	authTypes = vault.getKey("TOKEN_TYPES").split(",");
		
	}
	
	
	public String getAppId() {
		return vault.getKey("DEFAULT_APP_ID");
	}
	
	public String getAppSecret() {
		return vault.getKey("DEFAULT_APP_SECRET");
	}
	
	public String getAccessToken() {
		return this.accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public void save(NodeSettingsWO settings) {
		settings.addString(CFG_APP_ID, getAppId());
		settings.addString(CFG_APP_SECRET, getAppSecret());
		settings.addString(CFG_TOKEN, getAccessToken());
	}
	
	public void save(ModelContentWO settings) {
		settings.addString(CFG_APP_ID, getAppId());
		settings.addString(CFG_APP_SECRET, getAppSecret());
		settings.addString(CFG_TOKEN, getAccessToken());
	}
	
	public String[] getAuthTypes() {
		ArrayList<String> types = new ArrayList<String>();
		
		for ( String t : authTypes ) {
			types.add(t);
		}
		types.add(DEFAULT_AUTH_TYPE);
		
		return types.toArray(new String[] {} );
	}
	
	public String[] getTokens() {
		ArrayList<String> types = new ArrayList<String>();
		
		for ( String t : tokens ) {
			types.add(t);
		}
		types.add("");
		
		return types.toArray(new String[] {} );
	}
	
	public String getAuthTypeFromToken (String token) {
		
		for ( int i = 0; i < tokens.length; i++ ) {
			if ( token.equals(tokens[i]) ) return authTypes[i];
		}
		
		return DEFAULT_AUTH_TYPE;
	}
	
	public String getTokenFromAuthType( String authType ) {
		
		for ( int i=0; i < authTypes.length; i++ ) {
			if ( authType.equals(authTypes[i])) return tokens[i];
		}
		
		return "";
		
	}
	
	public void load(NodeSettingsRO settings) {
		this.accessToken = settings.getString(CFG_TOKEN, "");
	}

	public void load(ModelContentRO settings) {
		this.accessToken = settings.getString(CFG_TOKEN, "");
	}	
	
}
