package com.mmiagency.knime.keyworddensity.util;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

public class KeywordDensityRow extends DefaultRow {

	public KeywordDensityRow (final String id, final KeywordDensityRowEntry entry) {
		super(id, createCells(entry.getUrl(), entry.getKeyword(), entry.getPercentage(), entry.getFrequency()));
	}

    private static DataCell[] createCells(final String url, 
    		final String keyword, final String percentage, 
    		final int frequency) {
        List<DataCell> cells = new ArrayList<DataCell>();
        cells.add(new StringCell(url));
        cells.add(new StringCell(keyword));
        cells.add(new StringCell(percentage));
        cells.add(new IntCell(frequency));
        return cells.toArray(new DataCell[cells.size()]);
    }
	
}
