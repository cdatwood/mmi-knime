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
 * Links API 
 * 
 * Links api returns a set of links to a page or domain.
 * 
 * @author Radeep Solutions
 *
 */
public class LinksService 
{
	private Authenticator authenticator;
	
	public LinksService()
	{
		
	}
	
	/**
	 * 
	 * @param authenticator
	 */
	public LinksService(Authenticator authenticator)
	{
		this.setAuthenticator(authenticator);
	}
	
	/**
	 * This method returns a set of links to a page or domain.
	 * 
	 * @param objectURL
	 * @param scope determines the scope of the Target link, as well as the Source results.
	 * @param filters  filters the links returned to only include links of the specified type.  You may include one or more of the following values separated by '+'
	 * @param sort determines the sorting of the links, in combination with limit and offset, this allows fast access to the top links by several orders:
	 * @param col specifies data about the source of the link is included
	 * @return
	 * 
	 * @see #getLinks(String, String, String, String, int, int, int)
	 */
	public String getLinks(String objectURL, String scope, String filters, String sort, BigInteger col) throws Exception
	{
		return getLinks(objectURL, scope, filters, sort, col, -1, -1);
	}
	public String getLinks(String objectURL, String scope, String filters, String sort, long col) throws Exception { return getLinks(objectURL, scope, filters, sort, BigInteger.valueOf(col)); }
	
	/**
	 * This method returns a set of links to a page or domain.
	 * 
	 * @param objectURL
	 * @param scope determines the scope of the Target link, as well as the Source results.
	 * @param filters  filters the links returned to only include links of the specified type.  You may include one or more of the following values separated by '+'
	 * @param sort determines the sorting of the links, in combination with limit and offset, this allows fast access to the top links by several orders:
	 * @param col specifies data about the source of the link is included
	 * @param offset The start record of the page can be specified using the Offset parameter
	 * @param limit The size of the page can by specified using the Limit parameter.
	 * @return
	 */
	public String getLinks(String objectURL, String scope, String filters, String sort, BigInteger col, int offset, int limit) throws Exception
	{
		String urlToFetch = null;
		try {
			urlToFetch = "http://lsapi.seomoz.com/linkscape/links/" + URLEncoder.encode(objectURL, "UTF-8") + "?" + authenticator.getAuthenticationStr();
		} catch (UnsupportedEncodingException e) {
			// do nothing as this should never happen
		}		
		if(scope != null)
		{
			urlToFetch = urlToFetch + "&Scope=" + scope;
		}
		if(filters != null)
		{
			urlToFetch = urlToFetch + "&Filter=" + filters;
		}
		if(sort != null)
		{
			urlToFetch = urlToFetch + "&Sort=" + sort;
		}
		if(col.signum() ==  1)
		{
			urlToFetch = urlToFetch + "&SourceCols=" + col;
		}
		if(offset >= 0)
		{
			urlToFetch = urlToFetch + "&Offset=" + offset;
		}
		if(limit >= 0)
		{
			urlToFetch = urlToFetch + "&Limit=" + limit;
		}
		
		String response = ConnectionUtil.makeRequest(urlToFetch);
		
		return response;
	}
	public String getLinks(String objectURL, String scope, String filters, String sort, long col, int offset, int limit) throws Exception { return getLinks(objectURL, scope, filters, sort, BigInteger.valueOf(col), offset, limit); }

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
