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

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.StringCell;

import twitter4j.Trend;
import twitter4j.Location;

public class TrendRowFactory {

    /**
     * @param id The row ID
     * @param trend The trend info return from Twitter Trends
     * @return Row containing trend
     */
    public TrendRow createRow(final String id, final Location location, final Trend trend) {
        return new TrendRow(id, location, trend);
    }

    /**
     * @return Spec for a table that can hold the rows created by this factory
     */
    public DataTableSpec tableSpec() {
        List<DataColumnSpec> colSpecs = new ArrayList<DataColumnSpec>();
        colSpecs.add(new DataColumnSpecCreator("Location", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Name", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Query", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("URL", StringCell.TYPE).createSpec());
        return new DataTableSpec(colSpecs.toArray(new DataColumnSpec[colSpecs.size()]));
    }
}
