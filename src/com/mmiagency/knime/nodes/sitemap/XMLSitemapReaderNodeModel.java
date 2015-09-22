package com.mmiagency.knime.nodes.sitemap;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.mmiagency.knime.nodes.html.CleanHtmlRetrieverNodeConfiguration;


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

    private int processSitemap(BufferedDataContainer container, int id, String url) throws IOException {
    	int index = id;
        Connection conn = Jsoup.connect(url);
        conn.validateTLSCertificates(false);
        conn.followRedirects(true);
        conn.userAgent("Mozilla");
        conn.execute();
        Document doc = conn.get();
    	Elements urlTags = doc.select("url");
    	for (Element urlTag : urlTags) {
    		String loc = "";
    		String lastmod = "";
    		String changefreq = "";
    		double priority = -1;
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
        Connection conn = Jsoup.connect(sitemapUrl);
        conn.validateTLSCertificates(false);
        conn.followRedirects(true);
        conn.userAgent("Mozilla");
        conn.execute();
        Document doc = conn.get();
        
        Elements sitemapindex = doc.select("sitemapindex");
        
        if (sitemapindex != null && sitemapindex.size() > 0) {
        	Elements locs = sitemapindex.select("loc");
        	for (Element loc : locs) {        		
        		try {
        			index = processSitemap(container, index, loc.text()); 
        		} catch (Exception e) {
         			container.addRowToTable(m_config.createRow(""+index++, sitemapUrl, loc.text(), "FAILED: "+e.getMessage(), "", -1));        			
        		}
        		
    	        // check if the execution monitor was canceled
    	        exec.checkCanceled();
    	        exec.setProgress(index / (double)locs.size(), 
    	            "Adding row " + index++);
        	}
        } else {
			index = processSitemap(container, index, doc.select("loc").text()); 
        }

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

