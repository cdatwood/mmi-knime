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
package com.mmiagency.knime.nodes.sitemap;

import java.io.File;
import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;


/**
 * This is the model implementation of XMLSitemapReader.
 * 
 *
 * @author Ed Ng
 */
public class XMLSitemapReaderNodeModel extends NodeModel {

	private XMLSitemapReaderNodeConfiguration m_config = new XMLSitemapReaderNodeConfiguration();

    /**
     * Constructor for the node model.
     */
    protected XMLSitemapReaderNodeModel() {
    
        // TODO one incoming port and one outgoing port is assumed
        super(0, 1);
    }

    private int processSitemap(final BufferedDataContainer container, final ExecutionContext exec, final int id, final String url) throws IOException, CanceledExecutionException {
    	int index = id;
    	Connection conn = null;
    	
    	try {
	        conn = Jsoup.connect(url);
	        
	        conn.validateTLSCertificates(false);
	        conn.followRedirects(true);
	        conn.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:40.0) Gecko/20100101 Firefox/40.0");
	        conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	        conn.header("Accept-Language", "en-US,en;q=0.5");
	        conn.header("Accept-Encoding", "gzip, deflate");
	        
	        conn.execute();
    	} catch (Throwable e) {
			setWarningMessage("FAILED on URL \"" + url + "\": " + e.getMessage());
			container.addRowToTable(m_config.createRow(""+index++, url, "", "", "", 0));
			return index;
    	}
        Document doc = conn.get();
        
        // check if sitemap is an index page
        Elements sitemapindex = doc.select("sitemapindex");
        if (sitemapindex != null && sitemapindex.size() > 0) {
        	// iterate through each individual sitemap
        	Elements locs = sitemapindex.select("loc");
        	for (Element loc : locs) {        		
        		try {
        			index = processSitemap(container, exec, index, loc.text()); 
        		} catch (Exception e) {
         			container.addRowToTable(m_config.createRow(""+index++, url, loc.text(), "FAILED: "+e.getMessage(), "", 0));        			
        		}
        		
    	        // check if the execution monitor was canceled
    	        exec.checkCanceled();
    	        exec.setProgress(index / (double)locs.size(), 
    	            "Adding row " + index++);
        	}
        	// finish processing index, return now
        	return index;
        }
        
    	Elements urlTags = doc.select("url");
    	
    	if (urlTags.size() <= 0) {
			setWarningMessage("FAILED on URL \"" + url + "\": Content is not in sitemap XML format");
			container.addRowToTable(m_config.createRow(""+index++, url, "", "", "", 0));
		} else {    	
	    	for (Element urlTag : urlTags) {
	    		String loc = "";
	    		String lastmod = "";
	    		String changefreq = "";
	    		double priority = 0;
	    		Elements elements = urlTag.select("loc");
	    		if (elements.size() > 0) {
	    			loc = elements.get(0).text();
	    		}
	    		elements = urlTag.select("lastmod");
	    		if (elements.size() > 0) {
	    			lastmod = elements.get(0).text();
	    		}
	    		elements = urlTag.select("changefreq");
	    		if (elements.size() > 0) {
	    			changefreq = elements.get(0).text();
	    		}
	    		elements = urlTag.select("priority");
	    		if (elements.size() > 0) {
	    			priority = new Double(elements.get(0).text());
	    		}
	 			container.addRowToTable(m_config.createRow(""+index++, url, loc, lastmod, changefreq, priority));
	    	}
    	}
    	return index;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

		BufferedDataContainer container = exec.createDataContainer(m_config.tableSpec());
        int index = 0;
    	String sitemapUrl = m_config.getUrl().getStringValue();
    	
    	processSitemap(container, exec, index, sitemapUrl); 
    	
        container.close();
    	
        return new BufferedDataTable[]{container.getTable()};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // TODO Code executed on reset.
        // Models build during execute are cleared here.
        // Also data handled in load/saveInternals will be erased here.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
		return new DataTableSpec[]{m_config.tableSpec()};
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	m_config.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	XMLSitemapReaderNodeConfiguration config = new XMLSitemapReaderNodeConfiguration();
    	config.loadValidatedSettingsFrom(settings);
    	m_config = config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	m_config.validateSettings(settings);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }

}

