package com.pg.facebook.api.listaccounts.node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import com.pg.facebook.api.FacebookApiClient;
import com.pg.facebook.api.connector.data.FacebookApiConnectorPortObject;
import com.restfb.types.Account;

/**
 * This is the model implementation of ListAccounts.
 * 
 *
 * @author 
 */
public class ListAccountsNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
    protected ListAccountsNodeModel() {
    
        super(
        		new PortType[] { FacebookApiConnectorPortObject.TYPE }, 
        		new PortType[] { BufferedDataTable.TYPE }
        );
    }
    
    @Override
    protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec)
    		throws Exception {
    	
    	if ( !(inObjects[0] instanceof FacebookApiConnectorPortObject) ) {
    		throw new Exception("Invalid input port type");
    	}
    	
    	FacebookApiClient client = ((FacebookApiConnectorPortObject)inObjects[0]).getFacebookApiClient();
    	List<Account> accounts = client.getAccounts();
    	
    	DataTableSpec outSpec = createSpec();
        BufferedDataContainer outContainer = exec.createDataContainer(outSpec);
        
        int i = 0;
        for ( Account account : accounts ) {
        	List<DataCell> cells = new ArrayList<DataCell>(outSpec.getNumColumns());
        	cells.add(new StringCell(account.getId()));
            cells.add(new StringCell(account.getName()));
            cells.add(new StringCell(account.getCategory()));
            cells.add(new StringCell(account.getAccessToken()));
            if ( account.getType() != null)
            	cells.add(new StringCell(account.getType()));
            else 
            	cells.add(new StringCell(""));
            cells.add(new StringCell(StringUtils.join(account.getPerms(), ",")));
            
            outContainer.addRowToTable(new DefaultRow("Row" + i++, cells));
        }
        
        outContainer.close();
    	return new PortObject[]{outContainer.getTable()}; 
    }
    
    private DataTableSpec createSpec() {
        List<DataColumnSpec> colSpecs = new ArrayList<DataColumnSpec>(2);
        
        // Add additional columns to DataSpec
        colSpecs.add(new DataColumnSpecCreator("Account Id", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Account Name", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Category", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Access Token", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Type", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Permissions", StringCell.TYPE).createSpec());
        
        return new DataTableSpec(colSpecs.toArray(new DataColumnSpec[colSpecs.size()]));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        
    }

    @Override
    protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs)
    		throws InvalidSettingsException {
    	
    	return new PortObjectSpec[] { createSpec() };
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
       
    }

}

