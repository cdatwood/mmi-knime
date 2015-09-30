package com.mmiagency.knime.nodes.moz.api.util;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 
 * Utility Class to make a GET HTTP connection
 * to the given url and pass the output
 * 
 * @author Radeep Solutions
 *
 */
public class ConnectionUtil 
{
	
	/**
	 * 
	 * Method to make a GET HTTP connecton to 
	 * the given url and return the output
	 * 
	 * @param urlToFetch url to be connected
	 * @return the http get response
	 */
	public static String makeRequest(String urlToFetch) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();

        HttpGet httpget = new HttpGet(urlToFetch); 

        // Create a response handler
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = "";
		try 
		{
			responseBody = httpclient.execute(httpget, responseHandler);        
		} 
		catch (ClientProtocolException e) 
		{
			e.printStackTrace();
			throw e;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			throw e;
		}
        httpclient.getConnectionManager().shutdown();        
        return responseBody;
	}
}
