package com.mmiagency.knime.keyworddensity.util;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

public class KeywordDensityRowFactory {

	public KeywordDensityRow createRow(final String id, final KeywordDensityRowEntry entry) {
		return new KeywordDensityRow(id, entry);
	}
	
	public KeywordDensityRow createRow(final String id, final String url, final String message) {
		KeywordDensityRowEntry entry = new KeywordDensityRowEntry(url, message, "", 0);
		return new KeywordDensityRow(id, entry);
	}
	
    public DataTableSpec tableSpec() {
        List<DataColumnSpec> colSpecs = new ArrayList<DataColumnSpec>();
        colSpecs.add(new DataColumnSpecCreator("URL", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Keyword", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Percentage", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Frequency", IntCell.TYPE).createSpec());
        return new DataTableSpec(colSpecs.toArray(new DataColumnSpec[colSpecs.size()]));
    }
}
