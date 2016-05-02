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
package com.mmiagency.knime.nodes.moz.api.util;

import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

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
	static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	
	/**
	 * 
	 * Method to make a GET HTTP connecton to 
	 * the given url and return the output
	 * 
	 * @param urlToFetch url to be connected
	 * @return the http get response
	 */
	public static String makeRequest(String urlToFetch) throws Exception {
		
        HttpRequestFactory requestFactory =
        		HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    public void initialize(HttpRequest request) {
                    }
                });    	
        HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(urlToFetch.toString()));
        HttpResponse response = request.execute();
        StringWriter writer = new StringWriter();
        IOUtils.copy(response.getContent(), writer, response.getMediaType().getCharsetParameter().name());
        return writer.toString();
	}
}
