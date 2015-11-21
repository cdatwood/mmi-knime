package com.pg.facebook.api.selectaccount.node;

import org.knime.core.node.ModelContentRO;
import org.knime.core.node.ModelContentWO;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public class FacebookSelectAccountConfiguration {

	private String accountName;
	private String accountId;
	private String accessToken;
	
	private static final String CFG_ACCOUNT_NAME = "account.name";
	private static final String CFG_ACCOUNT_ID = "account.id";
	private static final String CFG_ACCESS_TOKEN = "access.token";

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void save ( NodeSettingsWO settings ) {
		settings.addString(CFG_ACCOUNT_ID, getAccountId() );
		settings.addString(CFG_ACCESS_TOKEN, getAccessToken() );
		settings.addString(CFG_ACCOUNT_NAME, getAccountName() );
	}
	
	public void save ( ModelContentWO model ) {
		model.addString(CFG_ACCOUNT_ID, getAccountId() );
		model.addString(CFG_ACCESS_TOKEN, getAccessToken() );
		model.addString(CFG_ACCOUNT_NAME, getAccountName() );
	}
	
	public void load ( NodeSettingsRO settings ) {
		setAccountId(settings.getString(CFG_ACCOUNT_ID, ""));
		setAccessToken(settings.getString(CFG_ACCESS_TOKEN, ""));
		setAccountName(settings.getString(CFG_ACCOUNT_NAME,""));
	}
	
	public void load ( ModelContentRO model ) {
		setAccountId(model.getString(CFG_ACCOUNT_ID, ""));
		setAccessToken(model.getString(CFG_ACCESS_TOKEN, ""));
		setAccountName(model.getString(CFG_ACCOUNT_NAME,""));
	}
	
}
