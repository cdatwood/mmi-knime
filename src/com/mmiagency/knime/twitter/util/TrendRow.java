package com.mmiagency.knime.twitter.util;

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
