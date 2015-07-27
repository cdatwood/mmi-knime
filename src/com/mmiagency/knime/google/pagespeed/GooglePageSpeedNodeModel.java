package com.mmiagency.knime.google.pagespeed;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
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
    
	static final String URL_COLUMN = "URL Column Name";

	private final SettingsModelString m_url = 
			GooglePageSpeedNodeModel.getUrlColumnSettingsModel();
	
	static final String API_KEY = "Google API Key";
	public final SettingsModelString m_apikey =
			GooglePageSpeedNodeModel.getApiKeySettingsModel();
	
	static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /**
     * Constructor for the node model.
     */
    protected GooglePageSpeedNodeModel() {
    
        // one incoming port and one outgoing port
        super(1, 1);
    }
    
    public static SettingsModelString getUrlColumnSettingsModel() {
    	return new SettingsModelString(URL_COLUMN,"url");   
    }
    
    public static SettingsModelString getApiKeySettingsModel() {
    	return new SettingsModelString(API_KEY,"");   
    }
    
    protected PageSpeedResult retrievePageSpeedResult(String url) throws IOException {
    	// pad URL with API key
    	String theUrl = REST_URL + URLEncoder.encode(url, "UTF-8") + "&key=" + m_apikey.getStringValue();
    	
        HttpRequestFactory requestFactory =
        		HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    public void initialize(HttpRequest request) {
                    	request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });    	
        HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(theUrl));
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
    	
    	// check if column exists
    	if (urlColumnSpec == null) {
    		// TODO do something
    	}
    	
    	// check if column data type is compatible to string
    	if (!urlColumnSpec.getType().isCompatible(StringValue.class)) {
    		// TODO do something
    	}
    	
    	int urlColumnIndex = inSpec.findColumnIndex(urlColumnName);
    	
    	// prepare output data container
        DataColumnSpec[] allColSpecs = getDataColumnSpec();
    	
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
        
        int i = 0;
        
    	for (Iterator<DataRow> it = inData[0].iterator(); it.hasNext();) {
    		DataRow row = it.next();
    		String url = ((StringValue)row.getCell(urlColumnIndex)).getStringValue();
    	
    		// call pagespeed function
    		PageSpeedResult pageSpeedResult = retrievePageSpeedResult(url);
    		
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
    
    private DataColumnSpec[] getDataColumnSpec() {
        DataColumnSpec[] allColSpecs = new DataColumnSpec[43];
        allColSpecs[0] = 
        		new DataColumnSpecCreator("url", StringCell.TYPE).createSpec();
        allColSpecs[1] = 
        		new DataColumnSpecCreator("responseCode", IntCell.TYPE).createSpec();
        allColSpecs[2] = 
        		new DataColumnSpecCreator("title", StringCell.TYPE).createSpec();
        allColSpecs[3] = 
                new DataColumnSpecCreator("score", IntCell.TYPE).createSpec();
        allColSpecs[4] = 
                new DataColumnSpecCreator("pageStats - numberResources", IntCell.TYPE).createSpec();
        allColSpecs[5] = 
                new DataColumnSpecCreator("pageStats - numberHosts", IntCell.TYPE).createSpec();
        allColSpecs[6] = 
                new DataColumnSpecCreator("pageStats - totalRequestBytes", StringCell.TYPE).createSpec();
        allColSpecs[7] = 
                new DataColumnSpecCreator("pageStats - numberStaticResources", IntCell.TYPE).createSpec();
        allColSpecs[8] = 
                new DataColumnSpecCreator("pageStats - htmlResponseBytes", StringCell.TYPE).createSpec();
        allColSpecs[9] = 
                new DataColumnSpecCreator("pageStats - cssResponseBytes", StringCell.TYPE).createSpec();
        allColSpecs[10] = 
                new DataColumnSpecCreator("pageStats - imageResponseBytes", StringCell.TYPE).createSpec();
        allColSpecs[11] = 
                new DataColumnSpecCreator("pageStats - javascriptResponseBytes", StringCell.TYPE).createSpec();
        allColSpecs[12] = 
                new DataColumnSpecCreator("pageStats - otherResponseBytes", StringCell.TYPE).createSpec();
        allColSpecs[13] = 
                new DataColumnSpecCreator("pageStats - numberJsResources", IntCell.TYPE).createSpec();
        allColSpecs[14] = 
                new DataColumnSpecCreator("pageStats - numberCssResources", IntCell.TYPE).createSpec();
        
        allColSpecs[15] = 
                new DataColumnSpecCreator("locale", StringCell.TYPE).createSpec();
    	
        allColSpecs[16] = 
                new DataColumnSpecCreator("AvoidLandingPageRedirects - ruleImpact", DoubleCell.TYPE).createSpec();
        allColSpecs[17] = 
                new DataColumnSpecCreator("AvoidLandingPageRedirects - summary", StringCell.TYPE).createSpec();

        allColSpecs[18] = 
                new DataColumnSpecCreator("EnableGzipCompression - ruleImpact", DoubleCell.TYPE).createSpec();
        allColSpecs[19] = 
                new DataColumnSpecCreator("EnableGzipCompression - summary", StringCell.TYPE).createSpec();
        allColSpecs[20] = 
                new DataColumnSpecCreator("EnableGzipCompression - urlBlocks", StringCell.TYPE).createSpec();

        allColSpecs[21] = 
                new DataColumnSpecCreator("LeverageBrowserCaching - ruleImpact", DoubleCell.TYPE).createSpec();
        allColSpecs[22] = 
                new DataColumnSpecCreator("LeverageBrowserCaching - summary", StringCell.TYPE).createSpec();
        allColSpecs[23] = 
                new DataColumnSpecCreator("LeverageBrowserCaching - urlBlocks", StringCell.TYPE).createSpec();

        allColSpecs[24] = 
                new DataColumnSpecCreator("MainResourceServerResponseTime - ruleImpact", DoubleCell.TYPE).createSpec();
        allColSpecs[25] = 
                new DataColumnSpecCreator("MainResourceServerResponseTime - urlBlocks", StringCell.TYPE).createSpec();

        allColSpecs[26] = 
                new DataColumnSpecCreator("MinifyCss - ruleImpact", DoubleCell.TYPE).createSpec();
        allColSpecs[27] = 
                new DataColumnSpecCreator("MinifyCss - summary", StringCell.TYPE).createSpec();
        allColSpecs[28] = 
                new DataColumnSpecCreator("MinifyCss - urlBlocks", StringCell.TYPE).createSpec();

        allColSpecs[29] = 
                new DataColumnSpecCreator("MinifyHTML - ruleImpact", DoubleCell.TYPE).createSpec();
        allColSpecs[30] = 
                new DataColumnSpecCreator("MinifyHTML - summary", StringCell.TYPE).createSpec();
        allColSpecs[31] = 
                new DataColumnSpecCreator("MinifyHTML - urlBlocks", StringCell.TYPE).createSpec();

        allColSpecs[32] = 
                new DataColumnSpecCreator("MinifyJavaScript - ruleImpact", DoubleCell.TYPE).createSpec();
        allColSpecs[33] = 
                new DataColumnSpecCreator("MinifyJavaScript - summary", StringCell.TYPE).createSpec();
        allColSpecs[34] = 
                new DataColumnSpecCreator("MinifyJavaScript - urlBlocks", StringCell.TYPE).createSpec();

        allColSpecs[35] = 
                new DataColumnSpecCreator("MinimizeRenderBlockingResources - ruleImpact", DoubleCell.TYPE).createSpec();
        allColSpecs[36] = 
                new DataColumnSpecCreator("MinimizeRenderBlockingResources - summary", StringCell.TYPE).createSpec();
        allColSpecs[37] = 
                new DataColumnSpecCreator("MinimizeRenderBlockingResources - urlBlocks", StringCell.TYPE).createSpec();

        allColSpecs[38] = 
                new DataColumnSpecCreator("OptimizeImages - ruleImpact", DoubleCell.TYPE).createSpec();
        allColSpecs[39] = 
                new DataColumnSpecCreator("OptimizeImages - summary", StringCell.TYPE).createSpec();
        allColSpecs[40] = 
                new DataColumnSpecCreator("OptimizeImages - urlBlocks", StringCell.TYPE).createSpec();

        allColSpecs[41] = 
                new DataColumnSpecCreator("PrioritizeVisibleContent - ruleImpact", DoubleCell.TYPE).createSpec();
        allColSpecs[42] = 
                new DataColumnSpecCreator("PrioritizeVisibleContent - summary", StringCell.TYPE).createSpec();

        return allColSpecs;
    }
    
    private DataCell[] mapDataCells(PageSpeedResult pageSpeedResult) {
		DataCell[] cells = new DataCell[43];
		
		cells[0] = new StringCell(pageSpeedResult.id);
		cells[1] = new IntCell(pageSpeedResult.responseCode);
		cells[2] = new StringCell(pageSpeedResult.title);
		cells[3] = new IntCell(pageSpeedResult.ruleGroups.SPEED.score);
		
		cells[4] = new IntCell(pageSpeedResult.pageStats.numberResources);
		cells[5] = new IntCell(pageSpeedResult.pageStats.numberHosts);
		cells[6] = new StringCell(pageSpeedResult.pageStats.totalRequestBytes);
		cells[7] = new IntCell(pageSpeedResult.pageStats.numberStaticResources);
		cells[8] = new StringCell(pageSpeedResult.pageStats.htmlResponseBytes);
		cells[9] = new StringCell(pageSpeedResult.pageStats.cssResponseBytes);
		cells[10] = new StringCell(pageSpeedResult.pageStats.imageResponseBytes);
		cells[11] = new StringCell(pageSpeedResult.pageStats.javascriptResponseBytes);
		cells[12] = new StringCell(pageSpeedResult.pageStats.otherResponseBytes);
		cells[13] = new IntCell(pageSpeedResult.pageStats.numberJsResources);
		cells[14] = new IntCell(pageSpeedResult.pageStats.numberCssResources);
		
		cells[15] = new StringCell(pageSpeedResult.formattedResults.locale);
		
		cells[16] = new DoubleCell(pageSpeedResult.formattedResults.ruleResults.AvoidLandingPageRedirects.ruleImpact);
		cells[17] = new StringCell(pageSpeedResult.formattedResults.ruleResults.AvoidLandingPageRedirects.getSummary());
		
		cells[18] = new DoubleCell(pageSpeedResult.formattedResults.ruleResults.EnableGzipCompression.ruleImpact);
		cells[19] = new StringCell(pageSpeedResult.formattedResults.ruleResults.EnableGzipCompression.getSummary());
		cells[20] = new StringCell(pageSpeedResult.formattedResults.ruleResults.EnableGzipCompression.getUrlBlocksSummary());
		
		cells[21] = new DoubleCell(pageSpeedResult.formattedResults.ruleResults.LeverageBrowserCaching.ruleImpact);
		cells[22] = new StringCell(pageSpeedResult.formattedResults.ruleResults.LeverageBrowserCaching.getSummary());
		cells[23] = new StringCell(pageSpeedResult.formattedResults.ruleResults.LeverageBrowserCaching.getUrlBlocksSummary());
		
		cells[24] = new DoubleCell(pageSpeedResult.formattedResults.ruleResults.MainResourceServerResponseTime.ruleImpact);
		cells[25] = new StringCell(pageSpeedResult.formattedResults.ruleResults.MainResourceServerResponseTime.getUrlBlocksSummary());
		
		cells[26] = new DoubleCell(pageSpeedResult.formattedResults.ruleResults.MinifyCss.ruleImpact);
		cells[27] = new StringCell(pageSpeedResult.formattedResults.ruleResults.MinifyCss.getSummary());
		cells[28] = new StringCell(pageSpeedResult.formattedResults.ruleResults.MinifyCss.getUrlBlocksSummary());
		
		cells[29] = new DoubleCell(pageSpeedResult.formattedResults.ruleResults.MinifyHTML.ruleImpact);
		cells[30] = new StringCell(pageSpeedResult.formattedResults.ruleResults.MinifyHTML.getSummary());
		cells[31] = new StringCell(pageSpeedResult.formattedResults.ruleResults.MinifyHTML.getUrlBlocksSummary());
		
		cells[32] = new DoubleCell(pageSpeedResult.formattedResults.ruleResults.MinifyJavaScript.ruleImpact);
		cells[33] = new StringCell(pageSpeedResult.formattedResults.ruleResults.MinifyJavaScript.getSummary());
		cells[34] = new StringCell(pageSpeedResult.formattedResults.ruleResults.MinifyJavaScript.getUrlBlocksSummary());
		
		cells[35] = new DoubleCell(pageSpeedResult.formattedResults.ruleResults.MinimizeRenderBlockingResources.ruleImpact);
		cells[36] = new StringCell(pageSpeedResult.formattedResults.ruleResults.MinimizeRenderBlockingResources.getSummary());
		cells[37] = new StringCell(pageSpeedResult.formattedResults.ruleResults.MinimizeRenderBlockingResources.getUrlBlocksSummary());
		
		cells[38] = new DoubleCell(pageSpeedResult.formattedResults.ruleResults.OptimizeImages.ruleImpact);
		cells[39] = new StringCell(pageSpeedResult.formattedResults.ruleResults.OptimizeImages.getSummary());
		cells[40] = new StringCell(pageSpeedResult.formattedResults.ruleResults.OptimizeImages.getUrlBlocksSummary());
		
		cells[41] = new DoubleCell(pageSpeedResult.formattedResults.ruleResults.PrioritizeVisibleContent.ruleImpact);
		cells[42] = new StringCell(pageSpeedResult.formattedResults.ruleResults.PrioritizeVisibleContent.getSummary());
		
		return cells;
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
    	PageSpeedRuleGroupSpeed SPEED;
    }
    public static class PageSpeedRuleGroupSpeed {
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

