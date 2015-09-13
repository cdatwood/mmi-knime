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
package com.mmiagency.knime.keyworddensity.util;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

public class KeywordDensityRow extends DefaultRow {

	public KeywordDensityRow (final String id, final KeywordDensityRowEntry entry) {
		super(id, createCells(entry.getUrl(), entry.getKeyword(), entry.getPercentage(), entry.getFrequency()));
	}

    private static DataCell[] createCells(final String url, 
    		final String keyword, final double percentage, 
    		final int frequency) {
        List<DataCell> cells = new ArrayList<DataCell>();
        cells.add(new StringCell(url));
        cells.add(new StringCell(keyword));
        cells.add(new DoubleCell(percentage));
        cells.add(new IntCell(frequency));
        return cells.toArray(new DataCell[cells.size()]);
    }
	
}
