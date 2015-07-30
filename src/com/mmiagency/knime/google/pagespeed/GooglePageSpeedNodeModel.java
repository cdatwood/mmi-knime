package com.mmiagency.knime.google.pagespeed;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.LongCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.Key;


/**
 * This is the model implementation of GooglePageSpeed.
 * 
 *
 * @author Ed Ng
 */
public class GooglePageSpeedNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(GooglePageSpeedNodeModel.class);
        
    static final String REST_URL = "https://www.googleapis.com/pagespeedonline/v2/runPagespeed?url=";
    
	static final String FIELD_LABEL_URL_COLUMN = "URL Column Name";
	static final String FIELD_LABEL_API_KEY = "Google API Key";
	static final String FIELD_LABEL_FILTER_THIRD_PARTY_RESOURCES = "Filter Third Party Resources";
	static final String FIELD_LABEL_LOCALE = "Locale";
	static final String FIELD_LABEL_STRATEGY = "Strategy";
	
	static final String FIELD_KEY_URL_COLUMN = "urlColumn";
	static final String FIELD_KEY_API_KEY = "apiKey";
	static final String FIELD_KEY_FILTER_THIRD_PARTY_RESOURCES = "filterThirdPartyResources";
	static final String FIELD_KEY_LOCALE = "locale";
	static final String FIELD_KEY_STRATEGY = "strategy";
	
	static final String FIELD_DEFAULT_URL_COLUMN = "url";
	static final String FIELD_DEFAULT_LOCALE = "en_US";
	static final String FIELD_DEFAULT_STRATEGY = "desktop";
	static final String[] FIELD_OPTIONS_STRATEGY = (String[])Arrays.asList("desktop", "mobile").toArray();

	private final SettingsModelString m_url = 
			GooglePageSpeedNodeModel.getUrlColumnSettingsModel();	
	private final SettingsModelString m_apikey =
			GooglePageSpeedNodeModel.getApiKeySettingsModel();
	private final SettingsModelBoolean m_filterThirdPartyResources =
			GooglePageSpeedNodeModel.getFilterThirdPartyResourcesSettingsModel();
	private final SettingsModelString m_locale =
			GooglePageSpeedNodeModel.getLocaleSettingsModel();
	private final SettingsModelString m_strategy =
			GooglePageSpeedNodeModel.getStrategySettingsModel();
	
	static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	private final List<String> hasRules = new ArrayList();

    /**
     * Constructor for the node model.
     */
    protected GooglePageSpeedNodeModel() {
    
        // one incoming port and one outgoing port
        super(1, 1);
    }
    
    public static SettingsModelString getUrlColumnSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_URL_COLUMN, FIELD_DEFAULT_URL_COLUMN);   
    }
    
    public static SettingsModelString getApiKeySettingsModel() {
    	return new SettingsModelString(FIELD_KEY_API_KEY,"");   
    }
    
    public static SettingsModelBoolean getFilterThirdPartyResourcesSettingsModel() {
    	return new SettingsModelBoolean(FIELD_KEY_FILTER_THIRD_PARTY_RESOURCES, false);
    }
    
    public static SettingsModelString getLocaleSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_LOCALE, FIELD_DEFAULT_LOCALE);
    }
    
    public static SettingsModelString getStrategySettingsModel() {
    	return new SettingsModelString(FIELD_KEY_STRATEGY, FIELD_DEFAULT_STRATEGY);
    }
    
    protected PageSpeedResult retrievePageSpeedResult(String url) throws IOException {
    	// pad URL with API key
    	StringBuilder theUrl = new StringBuilder();
    	theUrl.append(REST_URL);
    	theUrl.append(URLEncoder.encode(url.trim(), "UTF-8"));
    	theUrl.append("&filter_third_party_resources="+(m_filterThirdPartyResources.getBooleanValue()?"true":"false"));
    	if (m_locale.getStringValue() != null && !m_locale.getStringValue().trim().isEmpty()) {
    		theUrl.append("&locale="+m_locale.getStringValue().trim());
    	}
    	theUrl.append("&strategy="+m_strategy.getStringValue());
    	theUrl.append("&key=" + m_apikey.getStringValue().trim());
    	
        HttpRequestFactory requestFactory =
        		HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    public void initialize(HttpRequest request) {
                    	request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });    	
        HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(theUrl.toString()));
        PageSpeedResult result = request.execute().parseAs(PageSpeedResult.class);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

        logger.info("Loading URLs from incoming tables");

    	DataTableSpec inSpec = inData[0].getSpec();
    	String urlColumnName = m_url.getStringValue();
    	
    	DataColumnSpec urlColumnSpec = inSpec.getColumnSpec(urlColumnName);
    	
    	int urlColumnIndex = inSpec.findColumnIndex(urlColumnName);
    	
    	// prepare output data container
        BufferedDataContainer container = null;
        
        int i = 0;
        
    	for (Iterator<DataRow> it = inData[0].iterator(); it.hasNext();) {
    		DataRow row = it.next();
    		String url = ((StringValue)row.getCell(urlColumnIndex)).getStringValue();
    	
    		// call pagespeed function
    		PageSpeedResult pageSpeedResult = retrievePageSpeedResult(url);
    		
    		if (pageSpeedResult == null 
    				|| pageSpeedResult.pageStats == null
    				|| pageSpeedResult.ruleGroups == null
    				|| (pageSpeedResult.ruleGroups.SPEED == null && pageSpeedResult.ruleGroups.USABILITY == null)
    				) {
    			logger.error("Unable to retrieve PageSpeed results for url: " + url);
    			continue;
    		}
    		
    		// initialize container after first page speed result 
    		if (container == null) {
    	        DataColumnSpec[] allColSpecs = getDataColumnSpec(pageSpeedResult);
    	    	
    	        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
    	        container = exec.createDataContainer(outputSpec);
    		}
    		
    		// put results in outgoing table
    		RowKey key = new RowKey("Row " + i);

    		DataCell[] cells = mapDataCells(pageSpeedResult);

    		DataRow dataRow = new DefaultRow(key, cells);
    		container.addRowToTable(dataRow);
    		
            // check if the execution monitor was canceled
            exec.checkCanceled();
            exec.setProgress(i / (double)inData[0].getRowCount(), 
                "Adding row " + i);
            
            i++;
    	}

        // once we are done, we close the container and return its table
        container.close();
        BufferedDataTable out = container.getTable();
        return new BufferedDataTable[]{out};
    }
    
    private DataColumnSpec[] getDataColumnSpec(PageSpeedResult pageSpeedResult) {
    	List<DataColumnSpec> allColSpecs = new ArrayList();
    	
    	allColSpecs.add(new DataColumnSpecCreator("url", StringCell.TYPE).createSpec());
    	allColSpecs.add(new DataColumnSpecCreator("strategy", StringCell.TYPE).createSpec());
    	allColSpecs.add(new DataColumnSpecCreator("responseCode", IntCell.TYPE).createSpec());
    	allColSpecs.add(new DataColumnSpecCreator("title", StringCell.TYPE).createSpec());
    	if (pageSpeedResult.ruleGroups.SPEED != null) {
    		allColSpecs.add(new DataColumnSpecCreator("speed score", IntCell.TYPE).createSpec());
    	}
    	if (pageSpeedResult.ruleGroups.USABILITY != null) {
    		allColSpecs.add(new DataColumnSpecCreator("usability score", IntCell.TYPE).createSpec());
    	}
		allColSpecs.add(new DataColumnSpecCreator("pageStats - numberResources", IntCell.TYPE).createSpec());
		allColSpecs.add(new DataColumnSpecCreator("pageStats - numberHosts", IntCell.TYPE).createSpec());
		allColSpecs.add(new DataColumnSpecCreator("pageStats - totalRequestBytes", StringCell.TYPE).createSpec());
		allColSpecs.add(new DataColumnSpecCreator("pageStats - numberStaticResources", IntCell.TYPE).createSpec());
		allColSpecs.add(new DataColumnSpecCreator("pageStats - htmlResponseBytes", StringCell.TYPE).createSpec());
		allColSpecs.add(new DataColumnSpecCreator("pageStats - cssResponseBytes", StringCell.TYPE).createSpec());
		allColSpecs.add(new DataColumnSpecCreator("pageStats - imageResponseBytes", StringCell.TYPE).createSpec());
		allColSpecs.add(new DataColumnSpecCreator("pageStats - javascriptResponseBytes", StringCell.TYPE).createSpec());
		allColSpecs.add(new DataColumnSpecCreator("pageStats - otherResponseBytes", StringCell.TYPE).createSpec());
		allColSpecs.add(new DataColumnSpecCreator("pageStats - numberJsResources", IntCell.TYPE).createSpec());
		allColSpecs.add(new DataColumnSpecCreator("pageStats - numberCssResources", IntCell.TYPE).createSpec());
		allColSpecs.add(new DataColumnSpecCreator("locale", StringCell.TYPE).createSpec());
		
		if (pageSpeedResult.formattedResults.ruleResults.AvoidLandingPageRedirects != null) {
			hasRules.add("AvoidLandingPageRedirects");
			allColSpecs.add(new DataColumnSpecCreator("AvoidLandingPageRedirects - ruleImpact", DoubleCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("AvoidLandingPageRedirects - summary", StringCell.TYPE).createSpec());
		}
		if (pageSpeedResult.formattedResults.ruleResults.AvoidPlugins != null) {
			hasRules.add("AvoidPlugins");
			allColSpecs.add(new DataColumnSpecCreator("AvoidPlugins - ruleImpact", DoubleCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("AvoidPlugins - summary", StringCell.TYPE).createSpec());
		}
		if (pageSpeedResult.formattedResults.ruleResults.ConfigureViewport != null) {
			hasRules.add("ConfigureViewport");
			allColSpecs.add(new DataColumnSpecCreator("ConfigureViewport - ruleImpact", DoubleCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("ConfigureViewport - summary", StringCell.TYPE).createSpec());
		}
		if (pageSpeedResult.formattedResults.ruleResults.EnableGzipCompression != null) {
			hasRules.add("EnableGzipCompression");
			allColSpecs.add(new DataColumnSpecCreator("EnableGzipCompression - ruleImpact", DoubleCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("EnableGzipCompression - summary", StringCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("EnableGzipCompression - urlBlocks", StringCell.TYPE).createSpec());
		}
		if (pageSpeedResult.formattedResults.ruleResults.LeverageBrowserCaching != null) {
			hasRules.add("LeverageBrowserCaching");
			allColSpecs.add(new DataColumnSpecCreator("LeverageBrowserCaching - ruleImpact", DoubleCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("LeverageBrowserCaching - summary", StringCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("LeverageBrowserCaching - urlBlocks", StringCell.TYPE).createSpec());
		}
		if (pageSpeedResult.formattedResults.ruleResults.MainResourceServerResponseTime != null) {
			hasRules.add("MainResourceServerResponseTime");
			allColSpecs.add(new DataColumnSpecCreator("MainResourceServerResponseTime - ruleImpact", DoubleCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("MainResourceServerResponseTime - summary", StringCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("MainResourceServerResponseTime - urlBlocks", StringCell.TYPE).createSpec());
		}
		if (pageSpeedResult.formattedResults.ruleResults.MinifyCss != null) {
			hasRules.add("MinifyCss");
			allColSpecs.add(new DataColumnSpecCreator("MinifyCss - ruleImpact", DoubleCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("MinifyCss - summary", StringCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("MinifyCss - urlBlocks", StringCell.TYPE).createSpec());
		}
		if (pageSpeedResult.formattedResults.ruleResults.MinifyHTML != null) {
			hasRules.add("MinifyHTML");
			allColSpecs.add(new DataColumnSpecCreator("MinifyHTML - ruleImpact", DoubleCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("MinifyHTML - summary", StringCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("MinifyHTML - urlBlocks", StringCell.TYPE).createSpec());
		}
		if (pageSpeedResult.formattedResults.ruleResults.MinifyJavaScript != null) {
			hasRules.add("MinifyJavaScript");
			allColSpecs.add(new DataColumnSpecCreator("MinifyJavaScript - ruleImpact", DoubleCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("MinifyJavaScript - summary", StringCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("MinifyJavaScript - urlBlocks", StringCell.TYPE).createSpec());
		}
		if (pageSpeedResult.formattedResults.ruleResults.MinimizeRenderBlockingResources != null) {
			hasRules.add("MinimizeRenderBlockingResources");
			allColSpecs.add(new DataColumnSpecCreator("MinimizeRenderBlockingResources - ruleImpact", DoubleCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("MinimizeRenderBlockingResources - summary", StringCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("MinimizeRenderBlockingResources - urlBlocks", StringCell.TYPE).createSpec());
		}
		if (pageSpeedResult.formattedResults.ruleResults.OptimizeImages != null) {
			hasRules.add("OptimizeImages");
			allColSpecs.add(new DataColumnSpecCreator("OptimizeImages - ruleImpact", DoubleCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("OptimizeImages - summary", StringCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("OptimizeImages - urlBlocks", StringCell.TYPE).createSpec());
		}
		if (pageSpeedResult.formattedResults.ruleResults.PrioritizeVisibleContent != null) {
			hasRules.add("PrioritizeVisibleContent");
			allColSpecs.add(new DataColumnSpecCreator("PrioritizeVisibleContent - ruleImpact", DoubleCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("PrioritizeVisibleContent - summary", StringCell.TYPE).createSpec());
		}
		if (pageSpeedResult.formattedResults.ruleResults.SizeContentToViewport != null) {
			hasRules.add("SizeContentToViewport");
			allColSpecs.add(new DataColumnSpecCreator("SizeContentToViewport - ruleImpact", DoubleCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("SizeContentToViewport - summary", StringCell.TYPE).createSpec());
		}
		if (pageSpeedResult.formattedResults.ruleResults.SizeTapTargetAppropriately != null) {
			hasRules.add("SizeTapTargetAppropriately");
			allColSpecs.add(new DataColumnSpecCreator("SizeTapTargetAppropriately - ruleImpact", DoubleCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("SizeTapTargetAppropriately - summary", StringCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("SizeTapTargetAppropriately - urlBlocks", StringCell.TYPE).createSpec());
		}
		if (pageSpeedResult.formattedResults.ruleResults.UseLegibleFontSizes != null) {
			hasRules.add("UseLegibleFontSizes");
			allColSpecs.add(new DataColumnSpecCreator("UseLegibleFontSizes - ruleImpact", DoubleCell.TYPE).createSpec());
			allColSpecs.add(new DataColumnSpecCreator("UseLegibleFontSizes - summary", StringCell.TYPE).createSpec());
		}
		
		return allColSpecs.toArray(new DataColumnSpec[allColSpecs.size()]);
    }
    
    private DataCell[] mapDataCells(PageSpeedResult pageSpeedResult) {
    	List<DataCell> cells = new ArrayList();
    	
		cells.add(new StringCell(pageSpeedResult.id));
		cells.add(new StringCell(m_strategy.getStringValue()));
		cells.add(new IntCell(pageSpeedResult.responseCode));
		cells.add(new StringCell(pageSpeedResult.title));
    	if (pageSpeedResult.ruleGroups.SPEED != null) {
    		cells.add(new IntCell(pageSpeedResult.ruleGroups.SPEED.score));
    	}
    	if (pageSpeedResult.ruleGroups.USABILITY != null) {
    		cells.add(new IntCell(pageSpeedResult.ruleGroups.USABILITY.score));
    	}
    	cells.add(new IntCell(pageSpeedResult.pageStats.numberResources));
    	cells.add(new IntCell(pageSpeedResult.pageStats.numberHosts));
    	cells.add(new StringCell(pageSpeedResult.pageStats.totalRequestBytes));
    	cells.add(new IntCell(pageSpeedResult.pageStats.numberStaticResources));
    	cells.add(new StringCell(pageSpeedResult.pageStats.htmlResponseBytes));
    	cells.add(new StringCell(pageSpeedResult.pageStats.cssResponseBytes));
    	cells.add(new StringCell(pageSpeedResult.pageStats.imageResponseBytes));
    	cells.add(new StringCell(pageSpeedResult.pageStats.javascriptResponseBytes));
    	cells.add(new StringCell(pageSpeedResult.pageStats.otherResponseBytes));
    	cells.add(new IntCell(pageSpeedResult.pageStats.numberJsResources));
    	cells.add(new IntCell(pageSpeedResult.pageStats.numberCssResources));
		
    	cells.add(new StringCell(pageSpeedResult.formattedResults.locale));

    	if (hasRules.contains("AvoidLandingPageRedirects")) {
			if (pageSpeedResult.formattedResults.ruleResults.AvoidLandingPageRedirects != null) {
		    	cells.add(new DoubleCell(pageSpeedResult.formattedResults.ruleResults.AvoidLandingPageRedirects.ruleImpact));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.AvoidLandingPageRedirects.getSummary()));
			} else {
				cells.add(new DoubleCell(-1));
				cells.add(new StringCell(""));
			}
    	}
    	if (hasRules.contains("AvoidPlugins")) {
			if (pageSpeedResult.formattedResults.ruleResults.AvoidPlugins != null) {
		    	cells.add(new DoubleCell(pageSpeedResult.formattedResults.ruleResults.AvoidPlugins.ruleImpact));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.AvoidPlugins.getSummary()));
			} else {
				cells.add(new DoubleCell(-1));
				cells.add(new StringCell(""));
			}
		}
    	if (hasRules.contains("ConfigureViewport")) {
			if (pageSpeedResult.formattedResults.ruleResults.ConfigureViewport != null) {
		    	cells.add(new DoubleCell(pageSpeedResult.formattedResults.ruleResults.ConfigureViewport.ruleImpact));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.ConfigureViewport.getSummary()));
			} else {
				cells.add(new DoubleCell(-1));
				cells.add(new StringCell(""));
			}
		}
    	if (hasRules.contains("EnableGzipCompression")) {
			if (pageSpeedResult.formattedResults.ruleResults.EnableGzipCompression != null) {
		    	cells.add(new DoubleCell(pageSpeedResult.formattedResults.ruleResults.EnableGzipCompression.ruleImpact));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.EnableGzipCompression.getSummary()));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.EnableGzipCompression.getUrlBlocksSummary()));
			} else {
				cells.add(new DoubleCell(-1));
				cells.add(new StringCell(""));
				cells.add(new StringCell(""));
			}
		}
    	if (hasRules.contains("LeverageBrowserCaching")) {
			if (pageSpeedResult.formattedResults.ruleResults.LeverageBrowserCaching != null) {
		    	cells.add(new DoubleCell(pageSpeedResult.formattedResults.ruleResults.LeverageBrowserCaching.ruleImpact));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.LeverageBrowserCaching.getSummary()));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.LeverageBrowserCaching.getUrlBlocksSummary()));
			} else {
				cells.add(new DoubleCell(-1));
				cells.add(new StringCell(""));
				cells.add(new StringCell(""));
			}
		}
    	if (hasRules.contains("MainResourceServerResponseTime")) {
			if (pageSpeedResult.formattedResults.ruleResults.MainResourceServerResponseTime != null) {
		    	cells.add(new DoubleCell(pageSpeedResult.formattedResults.ruleResults.MainResourceServerResponseTime.ruleImpact));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.MainResourceServerResponseTime.getSummary()));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.MainResourceServerResponseTime.getUrlBlocksSummary()));
			} else {
				cells.add(new DoubleCell(-1));
				cells.add(new StringCell(""));
				cells.add(new StringCell(""));
			}
		}
    	if (hasRules.contains("MinifyCss")) {
			if (pageSpeedResult.formattedResults.ruleResults.MinifyCss != null) {
		    	cells.add(new DoubleCell(pageSpeedResult.formattedResults.ruleResults.MinifyCss.ruleImpact));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.MinifyCss.getSummary()));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.MinifyCss.getUrlBlocksSummary()));
			} else {
				cells.add(new DoubleCell(-1));
				cells.add(new StringCell(""));
				cells.add(new StringCell(""));
			}
		}
    	if (hasRules.contains("MinifyHTML")) {
			if (pageSpeedResult.formattedResults.ruleResults.MinifyHTML != null) {
		    	cells.add(new DoubleCell(pageSpeedResult.formattedResults.ruleResults.MinifyHTML.ruleImpact));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.MinifyHTML.getSummary()));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.MinifyHTML.getUrlBlocksSummary()));
			} else {
				cells.add(new DoubleCell(-1));
				cells.add(new StringCell(""));
				cells.add(new StringCell(""));
			}
		}
    	if (hasRules.contains("MinifyJavaScript")) {
			if (pageSpeedResult.formattedResults.ruleResults.MinifyJavaScript != null) {
		    	cells.add(new DoubleCell(pageSpeedResult.formattedResults.ruleResults.MinifyJavaScript.ruleImpact));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.MinifyJavaScript.getSummary()));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.MinifyJavaScript.getUrlBlocksSummary()));
			} else {
				cells.add(new DoubleCell(-1));
				cells.add(new StringCell(""));
				cells.add(new StringCell(""));
			}
		}
    	if (hasRules.contains("MinimizeRenderBlockingResources")) {
			if (pageSpeedResult.formattedResults.ruleResults.MinimizeRenderBlockingResources != null) {
		    	cells.add(new DoubleCell(pageSpeedResult.formattedResults.ruleResults.MinimizeRenderBlockingResources.ruleImpact));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.MinimizeRenderBlockingResources.getSummary()));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.MinimizeRenderBlockingResources.getUrlBlocksSummary()));
			} else {
				cells.add(new DoubleCell(-1));
				cells.add(new StringCell(""));
				cells.add(new StringCell(""));
			}
		}
    	if (hasRules.contains("OptimizeImages")) {
			if (pageSpeedResult.formattedResults.ruleResults.OptimizeImages != null) {
		    	cells.add(new DoubleCell(pageSpeedResult.formattedResults.ruleResults.OptimizeImages.ruleImpact));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.OptimizeImages.getSummary()));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.OptimizeImages.getUrlBlocksSummary()));
			} else {
				cells.add(new DoubleCell(-1));
				cells.add(new StringCell(""));
				cells.add(new StringCell(""));
			}
		}
    	if (hasRules.contains("PrioritizeVisibleContent")) {
			if (pageSpeedResult.formattedResults.ruleResults.PrioritizeVisibleContent != null) {
		    	cells.add(new DoubleCell(pageSpeedResult.formattedResults.ruleResults.PrioritizeVisibleContent.ruleImpact));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.PrioritizeVisibleContent.getSummary()));
			} else {
				cells.add(new DoubleCell(-1));
				cells.add(new StringCell(""));
			}
		}
    	if (hasRules.contains("SizeContentToViewport")) {
			if (pageSpeedResult.formattedResults.ruleResults.SizeContentToViewport != null) {
		    	cells.add(new DoubleCell(pageSpeedResult.formattedResults.ruleResults.SizeContentToViewport.ruleImpact));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.SizeContentToViewport.getSummary()));
			} else {
				cells.add(new DoubleCell(-1));
				cells.add(new StringCell(""));
			}
		}
    	if (hasRules.contains("SizeTapTargetAppropriately")) {
			if (pageSpeedResult.formattedResults.ruleResults.SizeTapTargetAppropriately != null) {
		    	cells.add(new DoubleCell(pageSpeedResult.formattedResults.ruleResults.SizeTapTargetAppropriately.ruleImpact));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.SizeTapTargetAppropriately.getSummary()));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.SizeTapTargetAppropriately.getUrlBlocksSummary()));
			} else {
				cells.add(new DoubleCell(-1));
				cells.add(new StringCell(""));
				cells.add(new StringCell(""));
			}
		}
    	if (hasRules.contains("UseLegibleFontSizes")) {
			if (pageSpeedResult.formattedResults.ruleResults.UseLegibleFontSizes != null) {
		    	cells.add(new DoubleCell(pageSpeedResult.formattedResults.ruleResults.UseLegibleFontSizes.ruleImpact));
		    	cells.add(new StringCell(pageSpeedResult.formattedResults.ruleResults.UseLegibleFontSizes.getSummary()));
			} else {
				cells.add(new DoubleCell(-1));
				cells.add(new StringCell(""));
			}
		}    	

		return cells.toArray(new DataCell[cells.size()]);
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
        
        // TODO: check if user settings are available, fit to the incoming
        // table structure, and the incoming types are feasible for the node
        // to execute. If the node can execute in its current state return
        // the spec of its output data table(s) (if you can, otherwise an array
        // with null elements), or throw an exception with a useful user message

        return new DataTableSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {

        // save user settings to the config object.
        m_url.saveSettingsTo(settings);
        m_apikey.saveSettingsTo(settings);
        m_filterThirdPartyResources.saveSettingsTo(settings);
        m_locale.saveSettingsTo(settings);
        m_strategy.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        // load (valid) settings from the config object.
        // It can be safely assumed that the settings are valided by the 
        // method below.        
        m_url.loadSettingsFrom(settings);
        m_apikey.loadSettingsFrom(settings);
        m_filterThirdPartyResources.loadSettingsFrom(settings);
        m_locale.loadSettingsFrom(settings);
        m_strategy.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        // check if the settings could be applied to our model
        // e.g. if the count is in a certain range (which is ensured by the
        // SettingsModel).
        // Do not actually set any values of any member variables.

        m_url.validateSettings(settings);
        m_apikey.validateSettings(settings);
        m_filterThirdPartyResources.validateSettings(settings);
        m_locale.validateSettings(settings);
        m_strategy.validateSettings(settings);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        
        // TODO load internal data. 
        // Everything handed to output ports is loaded automatically (data
        // returned by the execute method, models loaded in loadModelContent,
        // and user settings set through loadSettingsFrom - is all taken care 
        // of). Load here only the other internals that need to be restored
        // (e.g. data used by the views).

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
       
        // TODO save internal models. 
        // Everything written to output ports is saved automatically (data
        // returned by the execute method, models saved in the saveModelContent,
        // and user settings saved through saveSettingsTo - is all taken care 
        // of). Save here only the other internals that need to be preserved
        // (e.g. data used by the views).

    }
    
    public static class PageSpeedResult {
    	@Key
    	String kind;
    	@Key
    	String id;
    	@Key
    	int responseCode;
    	@Key
    	String title;
    	@Key
    	PageSpeedRuleGroup ruleGroups;
    	@Key
    	PageSpeedPageStats pageStats;
    	@Key
    	PageSpeedFormattedResults formattedResults;
    }
    public static class PageSpeedRuleGroup {
    	@Key
    	PageSpeedRuleGroupGroup SPEED;
    	@Key
    	PageSpeedRuleGroupGroup USABILITY;
    }
    public static class PageSpeedRuleGroupGroup {
    	@Key
    	int score;
    }
    public static class PageSpeedPageStats {
    	@Key
    	int numberResources;
    	@Key
    	int numberHosts;
    	@Key
    	String totalRequestBytes;
    	@Key
    	int numberStaticResources;
    	@Key
    	String htmlResponseBytes;
    	@Key
    	String cssResponseBytes;
    	@Key
    	String imageResponseBytes;
    	@Key
    	String javascriptResponseBytes;
    	@Key
    	String otherResponseBytes;
    	@Key
    	int numberJsResources;
    	@Key
    	int numberCssResources;
    }
    public static class PageSpeedFormattedResults {
    	@Key
    	String locale;
    	@Key
    	PageSpeedRuleResults ruleResults;
    }
    public static class PageSpeedRuleResults {
    	@Key
    	PageSpeedRuleResult AvoidLandingPageRedirects;
    	@Key
    	PageSpeedRuleResult AvoidPlugins;
    	@Key
    	PageSpeedRuleResult ConfigureViewport;
    	@Key
    	PageSpeedRuleResult EnableGzipCompression;
    	@Key
    	PageSpeedRuleResult LeverageBrowserCaching;
    	@Key
    	PageSpeedRuleResult MainResourceServerResponseTime;
    	@Key
    	PageSpeedRuleResult MinifyCss;
    	@Key
    	PageSpeedRuleResult MinifyHTML;
    	@Key
    	PageSpeedRuleResult MinifyJavaScript;
    	@Key
    	PageSpeedRuleResult MinimizeRenderBlockingResources;
    	@Key
    	PageSpeedRuleResult OptimizeImages;
    	@Key
    	PageSpeedRuleResult PrioritizeVisibleContent;
    	@Key
    	PageSpeedRuleResult SizeContentToViewport;
    	@Key
    	PageSpeedRuleResult SizeTapTargetAppropriately;
    	@Key
    	PageSpeedRuleResult UseLegibleFontSizes;
    }
    public static class PageSpeedRuleResult {
    	@Key
    	String localizedRuleName;
    	@Key
    	double ruleImpact;
    	@Key
    	List<String> groups;
    	@Key
    	PageSpeedRuleResultSummary summary;
    	@Key
    	List<PageSpeedRuleResultUrlBlock> urlBlocks;
    	
    	public String getSummary() {
    		if (summary != null) {
    			return summary.getSummary();
    		}
    		return "";
    	}
    	
    	public String getUrlBlocksSummary() {
    		if (urlBlocks != null && urlBlocks.size() > 0) {
    			StringBuilder sb = new StringBuilder();
    			for (PageSpeedRuleResultUrlBlock block : urlBlocks) {
    				sb.append(block.getSummary());
    			}
    			return sb.toString();
    		}
    		return "";
    	}
    	
    }
    public static class PageSpeedRuleResultSummary {
    	@Key
    	String format;
    	@Key
    	List<PageSpeedRuleResultArgs> args;
    	
    	public String getSummary() {
    		if (format == null || format.isEmpty()) {
    			return "";
    		}
    		String summary = format;
    		if (args == null || args.isEmpty()) {
    			return format;
    		}
    		for (PageSpeedRuleResultArgs arg : args) {
    			if ("HYPERLINK".equals(arg.type)) {
    				summary = summary.replace("{{BEGIN_LINK}}", "<a href='" + arg.value + "'>");
    				summary = summary.replace("{{END_LINK}}", "</a>");
    			} else {
    				summary = summary.replace("{{"+arg.key+"}}", arg.value);
    			}
    		}
    		return summary;
    	}
    }
    public static class PageSpeedRuleResultUrlBlock {
    	@Key
    	PageSpeedRuleResultSummary header;
    	@Key
    	List<PageSpeedRuleResultUrl> urls;
    	
    	public String getSummary() {
    		StringBuilder sb = new StringBuilder();
    		
    		if (header != null) {
    			sb.append(header.getSummary());
    			sb.append("\r");
    		}
    		
    		if (urls != null && urls.size() > 0) {
	    		for (PageSpeedRuleResultUrl url : urls) {
	    			sb.append(url.result.getSummary());
	    			sb.append("\r");
	    		}
    		}
    		return sb.toString();
    	}
    }
    public static class PageSpeedRuleResultUrl {
    	@Key
    	PageSpeedRuleResultSummary result;
    }
    public static class PageSpeedRuleResultArgs {
    	@Key
    	String type;
    	@Key
    	String key;
    	@Key
    	String value;
    }
}

