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

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class XMLSitemapReaderNodeConfiguration {
	static final String FIELD_LABEL_URL = "URL (Required)";

	static final String FIELD_KEY_URL = "url";
	
	private final SettingsModelString m_url = getUrlSettingsModel();

	public XMLSitemapReaderNodeConfiguration() {
		
	}
	
    public static SettingsModelString getUrlSettingsModel() {
    	return new SettingsModelString(FIELD_KEY_URL, "");   
    }
	
    public SettingsModelString getUrl() {
    	return m_url;
    }

    public void saveSettingsTo(final NodeSettingsWO settings) {
        m_url.saveSettingsTo(settings);
    }
    
    public void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        m_url.loadSettingsFrom(settings);
    }
    
    public void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        m_url.validateSettings(settings);
    }

    public DataTableSpec tableSpec() {
        List<DataColumnSpec> colSpecs = new ArrayList<DataColumnSpec>();
        colSpecs.add(new DataColumnSpecCreator("Sitemap URL", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("URL", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Last modification date", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Change frequency", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Priority", DoubleCell.TYPE).createSpec());
        return new DataTableSpec(colSpecs.toArray(new DataColumnSpec[colSpecs.size()]));
    }
    
    public DataRow createRow(String id, String sitemapUrl, String url, String lastmod, String changefreq, double priority) {
        List<DataCell> cells = new ArrayList<DataCell>();
        cells.add(new StringCell(sitemapUrl));
        cells.add(new StringCell(url));
        cells.add(new StringCell(lastmod));
        cells.add(new StringCell(changefreq));
        cells.add(new DoubleCell(priority));
        RowKey key = new RowKey(id);
        return new DefaultRow(key, cells.toArray(new DataCell[cells.size()]));
    }
}
