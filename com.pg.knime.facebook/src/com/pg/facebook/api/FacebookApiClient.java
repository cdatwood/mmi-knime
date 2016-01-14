package com.pg.facebook.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.knime.core.node.NodeLogger;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.exception.FacebookException;
import com.restfb.types.Account;
import com.restfb.types.Comment;
import com.restfb.types.Insight;
import com.restfb.types.Post;
import com.restfb.types.User;

public class FacebookApiClient {

	private FacebookClient client;
	private String impersonationAccountId;
	private String impersonationAccessToken;
	
	private Boolean tokenConfigured = false;
	
	private static int MAX_ATTEMPTS = 3;
	private static NodeLogger LOGGER = NodeLogger.getLogger(FacebookApiClient.class);
	
	private String accessToken;
	
	public FacebookApiClient() {
		LOGGER.info(" Instantiating blank FacebookClient");
		client = new DefaultFacebookClient();
	}
	
	public FacebookApiClient(String accessToken, String appSecret) {
		LOGGER.info(" Instantiating FacebookClient("+accessToken+","+appSecret+")");
		client = new DefaultFacebookClient(accessToken, appSecret, Version.VERSION_2_1);
		tokenConfigured = true;
		this.accessToken = accessToken;
	}
	
	public boolean isConfigured() {
		return tokenConfigured;
	}
	
	public String getImpersonationAccountId() {
		return impersonationAccountId;
	}

	public void setImpersonationAccountId(String accountId) {
		this.impersonationAccountId = accountId;
	}

	public String getImpersonationAccessToken() {
		return impersonationAccessToken;
	}

	public void setImpersonationAccessToken(String impersonationAccessToken) {
		this.impersonationAccessToken = impersonationAccessToken;
	}

	public List<Account> getAccounts() {
		Connection<Account> accounts = getConnection( client, "me/accounts", Account.class );
		return accounts.getData();
	}
	
	public Connection<Post> getPosts() {
		
		DefaultFacebookClient impersonationClient = new DefaultFacebookClient(getImpersonationAccessToken());
		
		List<Parameter> params = new ArrayList<Parameter>();
		params.add(Parameter.with("fields", "id,created_time,message,from,link,type,application,shares,likes.limit(1).summary(true),comments.limit(1).summary(true)"));
		Parameter[] parameters = params.toArray(new Parameter[0]);
		
		
		Connection<Post> posts = getConnection( impersonationClient, "me/feed", Post.class, parameters );
		
		return posts;
	}
	
	public Connection<Comment> getComments() {
		
		DefaultFacebookClient impersonationClient = new DefaultFacebookClient(getImpersonationAccessToken());
		
		Connection<Comment> comments = getConnection( impersonationClient, getImpersonationAccountId() + "/comments", Comment.class );
		
		return comments;
	}
	
	public Connection<Insight> getInsights(String period, String[] metrics, String startDate, String endDate ) {
		
		DefaultFacebookClient impersonationClient = new DefaultFacebookClient(getImpersonationAccessToken());
		
		List<Parameter> params = new ArrayList<Parameter>();
		params.add(Parameter.with("metric", StringUtils.join(metrics,",")));
		if ( period != null ) params.add( Parameter.with("period", period));
		if ( startDate != null && !startDate.isEmpty()) params.add(Parameter.with("since", startDate));
		if ( endDate != null && !endDate.isEmpty()) params.add(Parameter.with("until", endDate));
		
		Parameter[] parameters = params.toArray(new Parameter[0]);
		
		return getConnection(
									impersonationClient,
									getImpersonationAccountId() +"/insights", 
									Insight.class, 
									parameters );
		
		
		
		
	}
	
	public User getMe() {
		return client.fetchObject("me", User.class);
	}
	
	private <T> Connection<T> getConnection(FacebookClient client, String connection, Class<T> clz, Parameter... parameters ) {
		/*
		LOGGER.info(" Access token: " + accessToken);
		LOGGER.info(" Impersonation account ID: " + this.getImpersonationAccountId());
		LOGGER.info(" Impersonation token: " + this.getImpersonationAccessToken());
		*/
		
		// Guard Statement:
		if ( client == null ) return null;
		
		int attempts = 1;
		Connection<T> cnxn = null;
		
		do {
			LOGGER.debug( "Get connection: " + connection + " (attempt: " + attempts + ")" );
			try { cnxn = client.fetchConnection( connection, clz, parameters );}
			catch ( FacebookException fexc ) { 
				LOGGER.error( "Facebook Error: " + fexc.getMessage() );
				try { Thread.sleep(attempts++ * 1000); } 
				catch (InterruptedException iexc ) {} 
			}
		} while (cnxn == null && attempts++ <= MAX_ATTEMPTS );
		
		return cnxn;
	}
}
