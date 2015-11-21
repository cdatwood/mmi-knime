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

import com.mmiagency.knime.nodes.moz.api.authentication.Authenticator;
import com.mmiagency.knime.nodes.moz.api.util.ConnectionUtil;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * Service class to call the various methods to
 * Top Pages Api
 * 
 * Top pages is a paid API that returns the metrics about many URLs on a given subdomain.
 * 
 * @author Radeep Solutions
 *
 */
public class TopPagesService 
{
private Authenticator authenticator;
	
	public TopPagesService()
	{
		
	}
	
	/**
	 * 
	 * @param authenticator
	 */
	public TopPagesService(Authenticator authenticator)
	{
		this.setAuthenticator(authenticator);
	}
	
	/**
	 * This method returns the metrics about many URLs on a given subdomain
	 * 
	 * @param objectURL
	 * @param col  A set of metrics can be requested by indicating them as bit flags in the Cols query parameter.
	 * @param offset The start record of the page can be specified using the Offset parameter
	 * @param limit The size of the page can by specified using the Limit parameter. 
	 * @return
	 */
	public String getTopPages(String objectURL, BigInteger col, int offset, int limit) throws Exception
	{
		String urlToFetch = null;
		try {
			urlToFetch = "http://lsapi.seomoz.com/linkscape/top-pages/" + URLEncoder.encode(objectURL, "UTF-8") + "?" + authenticator.getAuthenticationStr();
		} catch (UnsupportedEncodingException e) {
			// do nothing as this should never happen
		}		
		if(offset >= 0 )
		{
			urlToFetch = urlToFetch + "&Offset=" + offset;
		}
		if(limit >= 0)
		{
			urlToFetch = urlToFetch + "&Limit=" + limit;
		}
		if(col.signum() == 1)
		{
			urlToFetch = urlToFetch + "&Cols=" + col;
		}
		//System.out.println(urlToFetch);
		
		String response = ConnectionUtil.makeRequest(urlToFetch);
		
		return response;
	}
	public String getTopPages(String objectURL, long col, int offset, int limit) throws Exception { return getTopPages(objectURL, BigInteger.valueOf(col), offset, limit); }
	
	/**
	 * 
	 * @param objectURL
	 * @param col
	 * @return
	 * 
	 * @see #getTopPages(String, int, int, int)
	 */
	public String getTopPages(String objectURL, BigInteger col) throws Exception
	{
		return getTopPages(objectURL, col, -1, -1);
	}
	public String getTopPages(String objectURL, long col) throws Exception { return getTopPages(objectURL, BigInteger.valueOf(col)); }
	
	/**
	 * 
	 * @param objectURL
	 * @return
	 * 
	 * @see #getTopPages(String, int, int, int)
	 */
	public String getTopPages(String objectURL) throws Exception
	{
		return getTopPages(objectURL, 0);
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
