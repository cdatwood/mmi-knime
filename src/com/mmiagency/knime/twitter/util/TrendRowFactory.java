package com.mmiagency.knime.twitter.util;

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
