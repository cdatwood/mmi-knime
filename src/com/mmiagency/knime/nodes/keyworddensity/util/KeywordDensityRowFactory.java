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
package com.mmiagency.knime.nodes.keyworddensity.util;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

public class KeywordDensityRowFactory {

	public KeywordDensityRow createRow(final String id, final KeywordDensityRowEntry entry) {
		return new KeywordDensityRow(id, entry);
	}
	
	public KeywordDensityRow createRow(final String id, final String url, final String message) {
		KeywordDensityRowEntry entry = new KeywordDensityRowEntry(url, message, 0.0, 0);
		return new KeywordDensityRow(id, entry);
	}
	
    public DataTableSpec tableSpec() {
        List<DataColumnSpec> colSpecs = new ArrayList<DataColumnSpec>();
        colSpecs.add(new DataColumnSpecCreator("URL", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Keyword", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Percentage", DoubleCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Frequency", IntCell.TYPE).createSpec());
        return new DataTableSpec(colSpecs.toArray(new DataColumnSpec[colSpecs.size()]));
    }
}
