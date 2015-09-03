package com.mmiagency.knime.randomdata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.mmiagency.knime.util.Util;


/**
 *
 * @author Phuc Truong
 */
public class RandomDataNodeModel extends NodeModel {

    /**Constructor for class RandomDataNodeModel.
     */
    protected RandomDataNodeModel() {
        super(0, 1);
     
        // Initialize the field variables
    	m_noOfRows = createNoOfRowsModel();
    	m_columnNames = new ArrayList();
    	m_columnTypes = new ArrayList();
    	m_columnMin = new ArrayList();
    	m_columnMax = new ArrayList();
    	m_columnActive = new ArrayList();
    	
    	for (int i = 0;i < MAX_COLUMNS; i++) {
    		m_columnNames.add(createColumnNameModel(i));
    		m_columnTypes.add(createColumnTypeModel(i));
    		m_columnMin.add(createColumnMinModel(i));
    		m_columnMax.add(createColumnMaxModel(i));
    		m_columnActive.add(createColumnActiveModel(i));
    	}
    	System.out.println("NEW: " + m_noOfRows);
        
    }

    public static int MAX_COLUMNS = 100;
    public static String COLUMN_TYPE_STRING = "String";
    public static String COLUMN_TYPE_INTEGER = "Integer";
    public static String COLUMN_TYPE_DATE = "Date";
    public static String DATE_TYPE_FORMAT= "M/d/yyyy";
    
    
    private SettingsModelInteger m_noOfRows;
    private List<SettingsModelString> m_columnNames;
    private List<SettingsModelString> m_columnTypes;
    private List<SettingsModelString> m_columnMin;
    private List<SettingsModelString> m_columnMax;
    private List<SettingsModelBoolean> m_columnActive;

    public static SettingsModelInteger createNoOfRowsModel() {
        return new SettingsModelIntegerBounded("noOfRows", 100, 1, Integer.MAX_VALUE);
    }
    
    public static SettingsModelString createColumnNameModel(int index) {
        return new SettingsModelString("columnName_" + index, null);
    }
    
    public static SettingsModelString createColumnTypeModel(int index) {
        return new SettingsModelString("columnType_" + index, "String");
    }
     
    public static SettingsModelString createColumnMinModel(int index) {
        return new SettingsModelString("columnMin_" + index, null);
    }
    
    public static SettingsModelString createColumnMaxModel(int index) {
        return new SettingsModelString("columnMax_" + index, null);
    }
    public static SettingsModelBoolean createColumnActiveModel(int index) {
        return new SettingsModelBoolean("columnActive_" + index, true);
    }
    
    

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(@SuppressWarnings("unused") final DataTableSpec[] inSpecs) throws InvalidSettingsException {
    	System.out.println("CONFIGURE: " + m_noOfRows);
        return new DataTableSpec[] {createSpec()};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(@SuppressWarnings("unused") final BufferedDataTable[] inData, final ExecutionContext exec) throws Exception {
    	System.out.println("EXECUTE: " + m_noOfRows);
        final DataTableSpec newSpec = createSpec();
        final BufferedDataContainer dc = exec.createDataContainer(newSpec);
        final int totalNoOfRows = m_noOfRows.getIntValue();
        
        for (int rowIdx = 0; rowIdx < totalNoOfRows; rowIdx++) {
            exec.checkCanceled();
            final LinkedList<DataCell> cells = new LinkedList<DataCell>();
            
        	for (int i = 0;i < MAX_COLUMNS; i++) {
        		if (!shouldProcessColumn(m_columnNames.get(i).getStringValue(), m_columnActive.get(i).getBooleanValue())) continue;
        		
        		Integer columnNumber = i + 1;
        		String columnType = m_columnTypes.get(i).getStringValue();
        		String columnMin = m_columnMin.get(i).getStringValue();
        		String columnMax = m_columnMax.get(i).getStringValue();
        		
        		if (columnType.equals(COLUMN_TYPE_STRING)) {        			
                    cells.add(new StringCell(columnType));
        		} else if (columnType.equals(COLUMN_TYPE_INTEGER)) {
        			Integer minInteger = 0;
        			Integer maxInteger = 100;
        			Integer columnMinInteger = Util.toInteger(columnMin, null);
        			if (columnMinInteger != null) {
        				minInteger = columnMinInteger;
        				maxInteger = minInteger + 100;
        			}
        			Integer columnMaxInteger = Util.toInteger(columnMin, null);
        			if (columnMaxInteger != null) {
        				maxInteger = columnMaxInteger;
        			}
                    cells.add(new IntCell(getRandomInteger(minInteger, maxInteger)));
        		} else if (columnType.equals(COLUMN_TYPE_DATE)) {
                    cells.add(new StringCell(columnType));
        		} 
        	}        
            

            final DefaultRow row = new DefaultRow(RowKey.createRowKey(rowIdx), cells);
            dc.addRowToTable(row);
        }
        dc.close();
        return new BufferedDataTable[] {dc.getTable()};
    }

    private static Integer getRandomInteger(int min, int max) {
    	int size = max - min;
        Random random = new Random();
        int randomIncrement = random.nextInt(size);
        return min + randomIncrement;
    }

    
    private DataTableSpec createSpec() {
		System.out.println("CREATE SPEC: " + m_noOfRows);
        final LinkedList<DataColumnSpec> specs = new LinkedList<DataColumnSpec>();
        
    	for (int i = 0;i < MAX_COLUMNS; i++) {
    		if (!shouldProcessColumn(m_columnNames.get(i).getStringValue(), m_columnActive.get(i).getBooleanValue())) continue;
    		specs.add(new DataColumnSpecCreator(m_columnNames.get(i).getStringValue(), StringCell.TYPE).createSpec());
    	}        

        return new DataTableSpec(specs.toArray(new DataColumnSpec[0]));
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unused")
    @Override
    protected void loadInternals(final File nodeInternDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {
    	System.out.println("LOAD INTERNALS: " + m_noOfRows);
    	// nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unused")
    @Override
    protected void saveInternals(final File nodeInternDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {
    	System.out.println("SAVE INTERNALS: " + m_noOfRows);
        // nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
    	System.out.println("LOAD SETTINGS: " + m_noOfRows);
        m_noOfRows.loadSettingsFrom(settings);
    	for (int i = 0;i < MAX_COLUMNS; i++) {
    		m_columnNames.get(i).loadSettingsFrom(settings);
    		m_columnTypes.get(i).loadSettingsFrom(settings);
    		m_columnMin.get(i).loadSettingsFrom(settings);
    		m_columnMax.get(i).loadSettingsFrom(settings);
    		m_columnActive.get(i).loadSettingsFrom(settings);
    	}
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	System.out.println("SAVE SETTINGS: " + m_noOfRows);
        m_noOfRows.saveSettingsTo(settings);
    	for (int i = 0;i < MAX_COLUMNS; i++) {
    		m_columnNames.get(i).saveSettingsTo(settings);
    		m_columnTypes.get(i).saveSettingsTo(settings);
    		m_columnMin.get(i).saveSettingsTo(settings);
    		m_columnMax.get(i).saveSettingsTo(settings);
    		m_columnActive.get(i).saveSettingsTo(settings);
    	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
    	System.out.println("VALIDATE SETTINGS: " + m_noOfRows);
        m_noOfRows.validateSettings(settings);
        
        final int totalNoOfRows = ((SettingsModelInteger) m_noOfRows.createCloneWithValidatedValue(settings)).getIntValue();
        
        List<SettingsModelString> columnNames = new ArrayList();
        List<SettingsModelString> columnTypes = new ArrayList();
        List<SettingsModelString> columnMin = new ArrayList();
        List<SettingsModelString> columnMax = new ArrayList();
        List<SettingsModelBoolean> columnActive = new ArrayList();    	
        for (int i = 0;i < MAX_COLUMNS; i++) {
        	columnNames.add((SettingsModelString) m_columnNames.get(i).createCloneWithValidatedValue(settings));
        	columnTypes.add((SettingsModelString) m_columnTypes.get(i).createCloneWithValidatedValue(settings));
        	columnMin.add((SettingsModelString) m_columnMin.get(i).createCloneWithValidatedValue(settings));
        	columnMax.add((SettingsModelString) m_columnMax.get(i).createCloneWithValidatedValue(settings));
        	columnActive.add((SettingsModelBoolean) m_columnActive.get(i).createCloneWithValidatedValue(settings));
    	}

        
        
        List<String> errors = new ArrayList();
    	for (int i = 0;i < MAX_COLUMNS; i++) {
    		if (!shouldProcessColumn(m_columnNames.get(i).getStringValue(), m_columnActive.get(i).getBooleanValue())) continue;
    		
    		Integer columnNumber = i + 1;
    		String name = columnNames.get(i).getStringValue();
    		String type = columnTypes.get(i).getStringValue();
    		String min = columnMin.get(i).getStringValue();
    		String max = columnMax.get(i).getStringValue();
    		Boolean active = columnActive.get(i).getBooleanValue();
    		System.out.println("Validating: " + columnNumber + " -> " + type + " -> " + min + " -> " + max);
    		
    		// For "String", "Integer" types, validate max/min as numbers
    		if (type.equals(COLUMN_TYPE_STRING) || type.equals(COLUMN_TYPE_INTEGER)) {
    			Integer minInteger = Util.toInteger(min, null);
    			Integer maxInteger = Util.toInteger(max, null);
    			if (!Util.isBlankOrNull(min) && minInteger == null) {
    				errors.add("Column " + columnNumber + " has an invalid min value: " + min);
    			}
    			if (!Util.isBlankOrNull(max) && maxInteger == null) {
    				errors.add("Column " + columnNumber + " has an invalid max value: " + max);
    			}
    			if (minInteger != null && maxInteger != null && maxInteger < minInteger) {
    				errors.add("Column " + columnNumber + " has max value that is less than min value: " + max + " max < " + min + " min");
    			}
    			
    		}
    		// For "Date" types, validate max/min as dates
    		if (type.equals(COLUMN_TYPE_DATE)) {
    			Date minDate = Util.toDate(DATE_TYPE_FORMAT, min, null);
    			if (!Util.isBlankOrNull(min) && minDate == null) {
    				errors.add("Column " + columnNumber + " has an invalid min date value: " + min);
    			}
    			Date maxDate = Util.toDate(DATE_TYPE_FORMAT, max, null);
    			if (!Util.isBlankOrNull(max) && maxDate == null) {
    				errors.add("Column " + columnNumber + " has an invalid max date value: " + max);
    			}
    			if (minDate != null && maxDate != null && maxDate.before(minDate)) {
    				errors.add("Column " + columnNumber + " has max value that is less than min value: " + max + " max < " + min + " min");
    			}
    		}
    	}
    	if (!errors.isEmpty()) {
    		throw new InvalidSettingsException(Util.join("\n", errors));
    	}
    	
    }

    protected boolean shouldProcessColumn(String columnName, Boolean active) {
		if (active == false) return false;
		if (!Util.isBlankOrNull(columnName)) return true;
		return false;
    }
    
    
}
