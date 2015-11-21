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
package com.mmiagency.knime.nodes.moz.api.service;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import com.mmiagency.knime.nodes.moz.api.authentication.Authenticator;
import com.mmiagency.knime.nodes.moz.api.util.ConnectionUtil;

/**
 * 
 * Service class to call the various methods to
 * URL Metrics
 * 
 * URL Metrics is a paid API that returns the metrics about a URL or set of URLs.  
 * 
 * @author Radeep Solutions
 *
 */
public class URLMetricsService 
{
	private Authenticator authenticator;
	
	public URLMetricsService()
	{
		
	}
	
	/**
	 * 
	 * @param authenticator
	 */
	public URLMetricsService(Authenticator authenticator)
	{
		this.setAuthenticator(authenticator);
	}
	
	/**
	 * 
	 * This method returns the metrics about a URL or set of URLs.  
	 * 
	 * @param objectURL
	 * @param col This field filters the data to get only specific columns
	 * 			  col = 0 fetches all the data
	 * @return
	 */
	public String getUrlMetrics(String objectURL, BigInteger col) throws Exception
	{
		String urlToFetch = null;
		try {			
			urlToFetch = "http://lsapi.seomoz.com/linkscape/url-metrics/" + URLEncoder.encode(objectURL, "UTF-8") + "?" + authenticator.getAuthenticationStr();
		} catch (UnsupportedEncodingException e) {
			// do nothing as this should never happen
		}		
		//System.out.println(urlToFetch);
		if(col.signum() == 1)
		{
			urlToFetch = urlToFetch + "&Cols=" + col;
		}
		String response = ConnectionUtil.makeRequest(urlToFetch);
		
		return response;
	}
	public String getUrlMetrics(String objectURL, long col) throws Exception { return getUrlMetrics(objectURL, BigInteger.valueOf(col)); }
	
	/**
	 * 
	 * Fetch all the Url Metrics for the objectURL
	 * 
	 * @param objectURL
	 * @return
	 * 
	 * @see URLMetricsService#getUrlMetrics(String, int)
	 */
	public String getUrlMetrics(String objectURL) throws Exception
	{
		return getUrlMetrics(objectURL, 0);		
	}

	/**
	 * @param authenticator the authenticator to set
	 */
	public void setAuthenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
	}

	/**
	 * @return the authenticator
	 */
	public Authenticator getAuthenticator() {
		return authenticator;
	}	
}
