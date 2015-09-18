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
package com.mmiagency.knime.nodes.twitter.util;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;

import twitter4j.Trend;
import twitter4j.Location;

public class TrendRow extends DefaultRow {

	public TrendRow (final String id, final Location location, final Trend trend) {
		super(id, createCells(location, trend));
	}
	
    private static DataCell[] createCells(final Location location, final Trend trend) {
        List<DataCell> cells = new ArrayList<DataCell>();
        if (location.getCountryName() == null) {
            cells.add(new StringCell(location.getName()));
        } else {
            cells.add(new StringCell(location.getCountryName() + " - " + location.getName()));
        }
        cells.add(new StringCell(trend.getName()));
        cells.add(new StringCell(trend.getQuery()));
        cells.add(new StringCell(trend.getURL()));
        return cells.toArray(new DataCell[cells.size()]);
    }	
	
}
