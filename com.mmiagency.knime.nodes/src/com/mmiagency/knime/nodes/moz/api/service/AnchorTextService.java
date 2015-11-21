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
 * Service class to call the various methods to 
 * Anchor Text API 
 * 
 * Anchor Text api returns a set of anchor text terms of phrases aggregated across links to a page or domain.
 * 
 * @author Radeep Solutions
 *
 */
public class AnchorTextService 
{
	private Authenticator authenticator;
	
	public AnchorTextService()
	{
		
	}
	
	/**
	 * 
	 * @param authenticator
	 */
	public AnchorTextService(Authenticator authenticator)
	{
		this.setAuthenticator(authenticator);
	}
	
	/**
	 * 
	 * @param objectURL
	 * @param scope
	 * @param sort
	 * @param col
	 * @return a set of anchor text terms of phrases aggregated across links to a page or domain. 
	 * 
	 * @see #getAnchorText(String, String, String, int, int, int)
	 */
	public String getAnchorText(String objectURL, String scope, String sort, BigInteger col) throws Exception
	{
		return getAnchorText(objectURL, scope, sort, col, -1, -1);
	}
	public String getAnchorText(String objectURL, String scope, String sort, long col) throws Exception { return getAnchorText(objectURL, scope, sort, BigInteger.valueOf(col)); }
	
	/**
	 * This method returns a set of anchor text terms of phrases aggregated across links to a page or domain.
	 * 
	 * @param objectURL
	 * @param scope determines the scope of the link, and takes one of the following values:
	 * 	phrase_to_page: returns a set of phrases found in links to the specified page
	 *	phrase_to_subdomain: returns a set of phrases found in links to the specified subdomain
	 *	phrase_to_domain: returns a set of phrases found in links to the specified root domain
	 *	term_to_page: returns a set of terms found in links to the specified page
	 *	term_to_subdomain: returns a a set of terms found in links to the specified subdomain
	 *	term_to_domain: returns a a set of terms found in links to the specified root domain
	 * @param sort etermines the sorting of the links, in combination with limit and offset, this allows fast access to the top links by several orders:
	 *	domains_linking_page: the phrases or terms found in links from the most number of root domains linking are returned first
	 * @param col determines what fields are returned 
	 * @param offset The start record of the page can be specified using the Offset parameter
	 * @param limit The size of the page can by specified using the Limit parameter.
	 * @return a set of anchor text terms of phrases aggregated across links to a page or domain.
	 */
	public String getAnchorText(String objectURL, String scope, String sort, BigInteger col, int offset, int limit) throws Exception {
		String urlToFetch = null;
		try {
			urlToFetch = "http://lsapi.seomoz.com/linkscape/anchor-text/" + URLEncoder.encode(objectURL, "UTF-8") + "?" + authenticator.getAuthenticationStr();
		} catch (UnsupportedEncodingException e) {
			// do nothing as this should never happen
		}
		if(scope != null)
		{
			urlToFetch = urlToFetch + "&Scope=" + scope;
		}
		if(sort != null)
		{
			urlToFetch = urlToFetch + "&Sort=" + sort;
		}
		if(col.signum() == 1)
		{
			urlToFetch = urlToFetch + "&Cols=" + col;
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
	public String getAnchorText(String objectURL, String scope, String sort, long col, int offset, int limit) throws Exception { return getAnchorText(objectURL, scope, sort, BigInteger.valueOf(col), offset, limit); }

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
