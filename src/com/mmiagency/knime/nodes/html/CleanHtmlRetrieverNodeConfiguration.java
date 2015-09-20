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
package com.mmiagency.knime.nodes.html;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.xml.XMLCell;
import org.knime.core.data.xml.XMLCellFactory;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class CleanHtmlRetrieverNodeConfiguration {
	static final String FIELD_LABEL_URL_COLUMN = "URL Column Name (Required)";
	static final String FIELD_LABEL_CONTENT_COLUMN = "Content Column Name (Optional)";
	static final String FIELD_LABEL_OUTPUT_COLUMN = "Output Column Name (Default is \"XHTML\")";
	static final String FIELD_LABEL_XML = "Output result as XML (Will output as String if unchecked)";

	static final String FIELD_KEY_URL_COLUMN = "url";
	static final String FIELD_KEY_CONTENT_COLUMN = "content";
	static final String FIELD_KEY_OUTPUT_COLUMN = "output";
	static final String FIELD_KEY_XML = "xml";

	static final String FIELD_DEFAULT_URL_COLUMN = "url";
	static final String FIELD_DEFAULT_CONTENT_COLUMN = "";
	static final String FIELD_DEFAULT_OUTPUT_COLUMN = "XHTML";
	static final boolean FIELD_DEFAULT_XML = true;
	
	private final Map<String, String> m_encodingMap = new HashMap<String, String>();

	private final SettingsModelString m_url = getUrlColumnSettingsModel();	
	private final SettingsModelString m_content = getContentColumnSettingsModel();	
	private final SettingsModelString m_output = getOutputColumnSettingsModel();	
	private final SettingsModelBoolean m_xml = getXmlSettingsModel();
	
	public CleanHtmlRetrieverNodeConfiguration() {
	    
		// initialize list of available encoding
		Map<String, Charset> map = Charset.availableCharsets();
		for (Iterator<String> i = map.keySet().iterator(); i.hasNext();) {
			String key = i.next();
			m_encodingMap.put(key.toLowerCase(), key);
		}
	}

    public static SettingsModelString getUrlColumnSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_URL_COLUMN, "");   
    }
    
    public static SettingsModelString getContentColumnSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_CONTENT_COLUMN, FIELD_DEFAULT_CONTENT_COLUMN);   
    }
    
    public static SettingsModelString getOutputColumnSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_OUTPUT_COLUMN, FIELD_DEFAULT_OUTPUT_COLUMN);   
    }
    
    public static SettingsModelBoolean getXmlSettingsModel() {
    	return new SettingsModelBoolean(FIELD_KEY_XML, FIELD_DEFAULT_XML);
    }

    public SettingsModelString getUrl() {
    	return m_url;
    }
    
    public SettingsModelString getContent() {
    	return m_content;
    }
    
    public SettingsModelString getOutput() {
    	return m_output;
    }
    
    public SettingsModelBoolean getXml() {
    	return m_xml;
    }
    
    public Map<String, String> getEncodingMap() {
    	return m_encodingMap;
    }

    public void saveSettingsTo(final NodeSettingsWO settings) {
        m_url.saveSettingsTo(settings);
        m_content.saveSettingsTo(settings);
        m_output.saveSettingsTo(settings);
        m_xml.saveSettingsTo(settings);
    }
    
    public void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        m_url.loadSettingsFrom(settings);
        m_content.loadSettingsFrom(settings);
        m_output.loadSettingsFrom(settings);
        m_xml.loadSettingsFrom(settings);
    }
    
    public void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        m_url.validateSettings(settings);
        m_content.validateSettings(settings);
        m_output.validateSettings(settings);
        m_xml.validateSettings(settings);
    }
    
    public DataTableSpec tableSpec() {
        List<DataColumnSpec> colSpecs = new ArrayList<DataColumnSpec>();
        colSpecs.add(new DataColumnSpecCreator("URL", StringCell.TYPE).createSpec());
        if (m_xml.getBooleanValue()) {
        	colSpecs.add(new DataColumnSpecCreator(m_output.getStringValue(), XMLCell.TYPE).createSpec());
        } else {
        	colSpecs.add(new DataColumnSpecCreator(m_output.getStringValue(), StringCell.TYPE).createSpec());
        }
        return new DataTableSpec(colSpecs.toArray(new DataColumnSpec[colSpecs.size()]));
    }
    
    public DataRow createRow(String id, String url, String xhtml) {
        List<DataCell> cells = new ArrayList<DataCell>();
        cells.add(new StringCell(url));
        if (m_xml.getBooleanValue()) {
        	try {
				cells.add(XMLCellFactory.create(xhtml));
			} catch (Exception e) {
				try {
					cells.add(XMLCellFactory.create("<error>"+e.getMessage()+"</error>"));
				} catch (Exception e2) {
					// this should not throw an exception
				}
				e.printStackTrace();
			}
        } else {
        	cells.add(new StringCell(xhtml));
        }
        RowKey key = new RowKey(id);
        return new DefaultRow(key, cells.toArray(new DataCell[cells.size()]));
    }
}
