package com.mmiagency.knime.w3c.cssvalidator;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
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
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * This is the model implementation of W3cCssValidatorNode.
 * 
 *
 * @author MMI Agency
 */
public class W3cCssValidatorNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(W3cCssValidatorNodeModel.class);
        
	static final String FIELD_LABEL_VALIDATOR_URL = "CSS Validator URL";
	static final String FIELD_LABEL_URL_COLUMN = "URL Column Name";
	static final String FIELD_LABEL_PROFILE = "Profile";
	static final String FIELD_LABEL_MEDIUM = "Medium";
	static final String FIELD_LABEL_WARNINGS = "Warnings";
	static final String FIELD_LABEL_VENDOR_EXTENSIONS = "Vendor Extensions";

	static final String FIELD_KEY_VALIDATOR_URL = "validatorUrl";
	static final String FIELD_KEY_URL_COLUMN = "urlColumn";
	static final String FIELD_KEY_PROFILE = "profile";
	static final String FIELD_KEY_MEDIUM = "usermedium";
	static final String FIELD_KEY_WARNINGS = "warning";
	static final String FIELD_KEY_VENDOR_EXTENSIONS = "vextwarning";

	static final String FIELD_DEFAULT_VALIDATOR_URL = "http://jigsaw.w3.org/css-validator/validator";
	static final String FIELD_DEFAULT_URL_COLUMN = "url";
	static final String FIELD_DEFAULT_PROFILE = "CSS level 3";
	static final String FIELD_DEFAULT_MEDIUM = "All";
	static final String FIELD_DEFAULT_WARNINGS = "Normal report";
	static final String FIELD_DEFAULT_VENDOR_EXTENSIONS = "Default";
	
	static final Map<String, String> FIELD_OPTIONS_PROFILE = new HashMap<String, String>();
	static final Map<String, String> FIELD_OPTIONS_MEDIUM = new HashMap<String, String>();
	static final Map<String, String> FIELD_OPTIONS_WARNINGS = new HashMap<String, String>();
	static final Map<String, String> FIELD_OPTIONS_VENDOR_EXTENSIONS = new HashMap<String, String>();
	
	private final SettingsModelString m_validatorUrl = 
			W3cCssValidatorNodeModel.getValidatorUrlSettingsModel();	
	private final SettingsModelString m_url = 
			W3cCssValidatorNodeModel.getUrlColumnSettingsModel();	
	private final SettingsModelString m_profile = 
			W3cCssValidatorNodeModel.getProfileSettingsModel();	
	private final SettingsModelString m_medium = 
			W3cCssValidatorNodeModel.getMediumSettingsModel();	
	private final SettingsModelString m_warnings = 
			W3cCssValidatorNodeModel.getWarningsSettingsModel();	
	private final SettingsModelString m_vendorExtensions = 
			W3cCssValidatorNodeModel.getVendorExtensionsSettingsModel();	

	/**
     * Constructor for the node model.
     */
    protected W3cCssValidatorNodeModel() {
    
        // one incoming port and two outgoing ports
        super(1, 2);
        
        initOptions();
    }
    
    private void initOptions() {
    	FIELD_OPTIONS_PROFILE.put("No special profile", "none");
    	FIELD_OPTIONS_PROFILE.put("CSS level 1", "css1");
    	FIELD_OPTIONS_PROFILE.put("CSS level 2", "css2");
    	FIELD_OPTIONS_PROFILE.put("CSS level 2.1", "css21");
    	FIELD_OPTIONS_PROFILE.put("CSS level 3", "css3");
    	FIELD_OPTIONS_PROFILE.put("SVG", "svg");
    	FIELD_OPTIONS_PROFILE.put("SVG Basic", "svgbasic");
    	FIELD_OPTIONS_PROFILE.put("SVG tiny", "svgtiny");
    	FIELD_OPTIONS_PROFILE.put("Mobile", "mobile");
    	FIELD_OPTIONS_PROFILE.put("ATSC TV profile", "atsc-tv");
    	FIELD_OPTIONS_PROFILE.put("TV profile", "tv");
    	
    	FIELD_OPTIONS_MEDIUM.put("All", "all");
    	FIELD_OPTIONS_MEDIUM.put("aural", "aural");
    	FIELD_OPTIONS_MEDIUM.put("braille", "braille");
    	FIELD_OPTIONS_MEDIUM.put("embossed", "embossed");
    	FIELD_OPTIONS_MEDIUM.put("handheld", "handheld");
    	FIELD_OPTIONS_MEDIUM.put("print", "print");
    	FIELD_OPTIONS_MEDIUM.put("projection", "projection");
    	FIELD_OPTIONS_MEDIUM.put("screen", "screen");
    	FIELD_OPTIONS_MEDIUM.put("TTY", "tty");
    	FIELD_OPTIONS_MEDIUM.put("TV", "tv");
    	FIELD_OPTIONS_MEDIUM.put("presentation", "presentation");
    	
    	FIELD_OPTIONS_WARNINGS.put("All", "2");
    	FIELD_OPTIONS_WARNINGS.put("Normal report", "1");
    	FIELD_OPTIONS_WARNINGS.put("Most important", "0");
    	FIELD_OPTIONS_WARNINGS.put("No warnings", "no");

    	FIELD_OPTIONS_VENDOR_EXTENSIONS.put("Default", "");
    	FIELD_OPTIONS_VENDOR_EXTENSIONS.put("Warnings", "true");
    	FIELD_OPTIONS_VENDOR_EXTENSIONS.put("Errors", "true");
    }

    public static SettingsModelString getValidatorUrlSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_VALIDATOR_URL, FIELD_DEFAULT_VALIDATOR_URL);   
    }
    public static SettingsModelString getUrlColumnSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_URL_COLUMN, FIELD_DEFAULT_URL_COLUMN);   
    }
    public static SettingsModelString getProfileSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_PROFILE, FIELD_DEFAULT_PROFILE);   
    }
    public static SettingsModelString getMediumSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_MEDIUM, FIELD_DEFAULT_MEDIUM);   
    }
    public static SettingsModelString getWarningsSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_WARNINGS, FIELD_DEFAULT_WARNINGS);   
    }
    public static SettingsModelString getVendorExtensionsSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_VENDOR_EXTENSIONS, FIELD_DEFAULT_VENDOR_EXTENSIONS);   
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(PortObject[] inObjects, final ExecutionContext exec) throws Exception {

		if (inObjects.length != 1 && inObjects[0] instanceof BufferedDataTable) {
			throw new InvalidSettingsException("You must link a table with URL column to this node.");
		}
		
		Pattern h3Pattern = Pattern.compile(".*\\(([0-9]*)\\).*");

		BufferedDataTable inData = (BufferedDataTable)inObjects[0];
		
    	DataTableSpec inSpec = inData.getSpec();
    	String urlColumnName = m_url.getStringValue();
    	    	
    	int urlColumnIndex = inSpec.findColumnIndex(urlColumnName);

    	// prepare output data container        
        DataColumnSpec[] summaryColSpecs = new DataColumnSpec[7];
        summaryColSpecs[0] = new DataColumnSpecCreator("url", StringCell.TYPE).createSpec();
        summaryColSpecs[1] = new DataColumnSpecCreator("profile", StringCell.TYPE).createSpec();
        summaryColSpecs[2] = new DataColumnSpecCreator("medium", StringCell.TYPE).createSpec();
        summaryColSpecs[3] = new DataColumnSpecCreator("warnings", StringCell.TYPE).createSpec();
        summaryColSpecs[4] = new DataColumnSpecCreator("vendor extensions", StringCell.TYPE).createSpec();
        summaryColSpecs[5] = new DataColumnSpecCreator("error count", IntCell.TYPE).createSpec();
        summaryColSpecs[6] = new DataColumnSpecCreator("warning count", IntCell.TYPE).createSpec();
        BufferedDataContainer containerSummary = exec.createDataContainer(
        		new DataTableSpec(summaryColSpecs));
        
        DataColumnSpec[] detailsColSpecs = new DataColumnSpec[7];
        detailsColSpecs[0] = new DataColumnSpecCreator("url", StringCell.TYPE).createSpec();
        detailsColSpecs[1] = new DataColumnSpecCreator("uri", StringCell.TYPE).createSpec();
        detailsColSpecs[2] = new DataColumnSpecCreator("type", StringCell.TYPE).createSpec();
        detailsColSpecs[3] = new DataColumnSpecCreator("warning level", IntCell.TYPE).createSpec();
        detailsColSpecs[4] = new DataColumnSpecCreator("line number", IntCell.TYPE).createSpec();
        detailsColSpecs[5] = new DataColumnSpecCreator("classes", StringCell.TYPE).createSpec();
        detailsColSpecs[6] = new DataColumnSpecCreator("message", StringCell.TYPE).createSpec();
        BufferedDataContainer containerDetails = exec.createDataContainer(
        		new DataTableSpec(detailsColSpecs));
        
        int summaryRowCount = 0;
		int detailsRowCount = 0;
		        
    	for (Iterator<DataRow> it = inData.iterator(); it.hasNext();) {
    		DataRow row = it.next();
    		String url = ((StringValue)row.getCell(urlColumnIndex)).getStringValue();
    		// put results in outgoing table
    		RowKey key = new RowKey("Row " + summaryRowCount++);

    		// retrieve validator results
    		String validatorUrl = m_validatorUrl.getStringValue() + 
    				"?uri=" + URLEncoder.encode(url, "UTF-8") +
    				"&profile=" + FIELD_OPTIONS_PROFILE.get(m_profile.getStringValue()) + 
    				"&usermedium=" + FIELD_OPTIONS_MEDIUM.get(m_medium.getStringValue()) +
    				"&warning=" + FIELD_OPTIONS_WARNINGS.get(m_warnings.getStringValue()) +
    				"&vextwarning=" + FIELD_OPTIONS_VENDOR_EXTENSIONS.get(m_vendorExtensions.getStringValue());
    		
    		logger.info("Validation URL: " + validatorUrl);
    		
    		DataCell[] summaryCells = new DataCell[7];
    		
    		summaryCells[0] = new StringCell(url);
    		summaryCells[1] = new StringCell(m_profile.getStringValue());
    		summaryCells[2] = new StringCell(m_medium.getStringValue());
    		summaryCells[3] = new StringCell(m_warnings.getStringValue());
    		summaryCells[4] = new StringCell(m_vendorExtensions.getStringValue());
    		summaryCells[5] = new IntCell(0);
    		summaryCells[6] = new IntCell(0);
    		
    		boolean goodRespond = true;
    		String errorMessage = null;
    		
    		Connection con = Jsoup.connect(validatorUrl).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").timeout(10000);    		
    	    Connection.Response resp = null;
    	    Document doc = null;
    	    try {
    	    	resp = con.execute();
    	    	doc = con.get();
    	    } catch (HttpStatusException hse) {
    	    	goodRespond = false;
    	    	errorMessage = hse.getMessage();
    	    }
    	    
    		if (goodRespond && resp.statusCode() == 200) {
    			// errors
	    		Elements errors = doc.select("#errors");
	    		
	    		Elements h3 = errors.select("h3");
	    		
	    		if (h3.size() > 0) {
	    			Element e = h3.get(0);
	    			Matcher m = h3Pattern.matcher(e.text());
	    			if (m.matches()) {
	    				try {
	    					summaryCells[5] = new IntCell(new Integer(m.group(1)));	    				
	    				} catch (NumberFormatException nfe) {
	    					// leave it empty
	    				}
	    			}
	    		}
	    		
	    		Elements sections = errors.select(".error-section");
	    		
	    		for (Element section : sections) {
	    			String uri = "";
	    			
	    			Elements h4 = section.select("h4 a");
	    			if (h4.size() > 0) {
		    			uri = h4.get(0).text();
	    			}
	    			
	    			for (Element error : section.select(".error")) {
		    			
		        		RowKey detailsKey = new RowKey("Row " + detailsRowCount++);
		        		
		    			DataCell[] detailsCells = new DataCell[7];
		    			
		    			detailsCells[0] = new StringCell(url);
		    			detailsCells[1] = new StringCell(uri);
		    			detailsCells[2] = new StringCell("error");
		    			detailsCells[3] = new IntCell(0);
		    			
		    			Elements lineNumber = error.select(".linenumber");
		    			if (lineNumber.size() > 0) {
		    				try {
		    					detailsCells[4] = new IntCell(new Integer(lineNumber.get(0).text()));
		    				} catch (NumberFormatException nfe) {
				    			detailsCells[4] = new IntCell(0);
		    				}
		    			} else {
			    			detailsCells[4] = new IntCell(0);	    				
		    			}
		    			
		    			Elements codeContext = error.select(".codeContext");
		    			if (codeContext.size() > 0) {
			    			detailsCells[5] = new StringCell(codeContext.get(0).text());
		    			} else {
			    			detailsCells[5] = new StringCell("");
		    			}
		    			
		    			Elements parseError = error.select(".parse-error");
		    			if (parseError.size() > 0) {
			    			detailsCells[6] = new StringCell(parseError.get(0).text());
		    			} else {
			    			detailsCells[6] = new StringCell("");
		    			}
	
		    			DataRow detailsDataRow = new DefaultRow(detailsKey, detailsCells);
		        		containerDetails.addRowToTable(detailsDataRow);
		        		
	    			}
	    		}
	    		
	    		// warnings
	    		Elements warnings = doc.select("#warnings");
	    		
	    		h3 = warnings.select("h3");
	    		
	    		if (h3.size() > 0) {
	    			Element e = h3.get(0);
	    			Matcher m = h3Pattern.matcher(e.text());
	    			if (m.matches()) {
	    				try {
	    					summaryCells[6] = new IntCell(new Integer(m.group(1)));	    				
	    				} catch (NumberFormatException nfe) {
	    					// leave it empty
	    				}
	    			}
	    		}
	    		
	    		sections = warnings.select(".warning-section");
	    		
	    		for (Element section : sections) {
	    			String uri = "";
	    			
	    			Elements h4 = section.select("h4 a");
	    			if (h4.size() > 0) {
		    			uri = h4.get(0).text();
	    			}
	    			
	    			for (Element warning : section.select(".warning")) {
		    			
		        		RowKey detailsKey = new RowKey("Row " + detailsRowCount++);
		        		
		    			DataCell[] detailsCells = new DataCell[7];
		    			
		    			detailsCells[0] = new StringCell(url);
		    			detailsCells[1] = new StringCell(uri);
		    			detailsCells[2] = new StringCell("warning");
		    			detailsCells[3] = new IntCell(0); // default warning level to 0
		    			
		    			Elements lineNumber = warning.select(".linenumber");
		    			if (lineNumber.size() > 0) {
		    				try {
		    					detailsCells[4] = new IntCell(new Integer(lineNumber.get(0).text()));
		    				} catch (NumberFormatException nfe) {
				    			detailsCells[4] = new IntCell(0);
		    				}
		    			} else {
			    			detailsCells[4] = new IntCell(0);	    				
		    			}
		    			
		    			Elements codeContext = warning.select(".codeContext");
		    			if (codeContext.size() > 0) {
			    			detailsCells[5] = new StringCell(codeContext.get(0).text());
		    			} else {
			    			detailsCells[5] = new StringCell("");
		    			}

		    			// default message to empty
		    			detailsCells[6] = new StringCell("");
		    			Elements parseError = null;
		    			
		    			// try for 20 warning levels
		    			for (int i = 0; i < 20; i++) {
		    				parseError = warning.select(".level" + i);
			    			if (parseError.size() > 0) {
				    			detailsCells[3] = new IntCell(i);
				    			detailsCells[6] = new StringCell(parseError.get(0).text());
				    			break;
			    			}
		    			}
	
		    			DataRow detailsDataRow = new DefaultRow(detailsKey, detailsCells);
		        		containerDetails.addRowToTable(detailsDataRow);
		        		
	    			}
	    		}

    		} else {    	    	
        		RowKey detailsKey = new RowKey("Row " + detailsRowCount++);

        		DataCell[] detailsCells = new DataCell[7];
    			
    			detailsCells[0] = new StringCell(url);
    			detailsCells[1] = new StringCell("");
    			detailsCells[2] = new StringCell("");
    			detailsCells[3] = new IntCell(0);
    			detailsCells[4] = new IntCell(0);
    			detailsCells[5] = new StringCell("");
    			if (goodRespond) {
    				detailsCells[6] = new StringCell(doc.html().toString());
    			} else {
    				detailsCells[6] = new StringCell(errorMessage);
    			}

    			DataRow detailsDataRow = new DefaultRow(detailsKey, detailsCells);
        		containerDetails.addRowToTable(detailsDataRow);    			
    		}
    		
    		DataRow summaryDataRow = new DefaultRow(key, summaryCells);
    		containerSummary.addRowToTable(summaryDataRow);
    		
            // check if the execution monitor was canceled
            exec.checkCanceled();
            exec.setProgress(summaryRowCount / (double)inData.getRowCount(), 
                "Adding row " + summaryRowCount);
            
            // pause for 1 second to ensure we don't submit URL more frequent than 1 per second.
            try {
            	Thread.sleep(1000);
            } catch (InterruptedException e) {
            	// do nothing
            }
            
    	}
    	
        // once we are done, we close the container and return its table
    	containerSummary.close();
    	containerDetails.close();

    	PortObject[] portObject = new PortObject[2];
    	
    	portObject[0] = containerSummary.getTable();
    	portObject[1] = containerDetails.getTable();
    	
    	return portObject;
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

		if (inSpecs.length<1) {
			throw new InvalidSettingsException("You must link a table with URL column to this node.");
		}
		
		if (inSpecs[0].findColumnIndex(m_url.getStringValue()) < 0) {
			throw new InvalidSettingsException("A URL column in the data input table must exist and must be specified.");
		}

		return new DataTableSpec[]{null, null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {

        m_validatorUrl.saveSettingsTo(settings);
        m_url.saveSettingsTo(settings);
        m_profile.saveSettingsTo(settings);
        m_medium.saveSettingsTo(settings);
        m_warnings.saveSettingsTo(settings);
        m_vendorExtensions.saveSettingsTo(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        m_validatorUrl.loadSettingsFrom(settings);
        m_url.loadSettingsFrom(settings);
        m_profile.loadSettingsFrom(settings);
        m_medium.loadSettingsFrom(settings);
        m_warnings.loadSettingsFrom(settings);
        m_vendorExtensions.loadSettingsFrom(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        m_validatorUrl.validateSettings(settings);
        m_url.validateSettings(settings);
        m_profile.validateSettings(settings);
        m_medium.validateSettings(settings);
        m_warnings.validateSettings(settings);
        m_vendorExtensions.validateSettings(settings);

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

}

