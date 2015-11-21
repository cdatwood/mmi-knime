package com.pg.facebook.api.listcomments.node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.date.DateAndTimeCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.LongCell;
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
import com.restfb.Connection;
import com.restfb.types.Comment;

/**
 * This is the model implementation of ListComments.
 * 
 *
 * @author P&G, eBusiness
 */
public class ListCommentsNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
    protected ListCommentsNodeModel() {
    
        super(
        	new PortType[]{FacebookApiConnectorPortObject.TYPE}, 
        	new PortType[]{ BufferedDataTable.TYPE }
        );
    }

    @Override
    protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec)
    		throws Exception {
    	
    	FacebookApiConnectorPortObject portObject = (FacebookApiConnectorPortObject)inObjects[0];
    	FacebookApiClient client = portObject.getFacebookApiClient();
    	Connection<Comment> connection = client.getComments();
    	
    	DataTableSpec outSpec = createSpec();
        BufferedDataContainer outContainer = exec.createDataContainer(outSpec);
        
        int i = 0;
        for ( List<Comment> comments : connection ) {
           	
        	exec.checkCanceled();
        	exec.setMessage("Retrieved " + i + " comments" );
        	
           	for ( Comment comment : comments ) {
           		
	        	List<DataCell> cells = new ArrayList<DataCell>(outSpec.getNumColumns());
	        	
	        	cells.add(new StringCell(client.getImpersonationAccountId()));
	        	cells.add(new StringCell(safe(comment.getId())));
	        	cells.add(new DateAndTimeCell(comment.getCreatedTime().getTime(), true, true, false));
	        	cells.add(new StringCell(safe(comment.getMessage())));
	        	if ( comment.getFrom() != null ) {
	        		cells.add(new StringCell(safe(comment.getFrom().getId())));
	            	cells.add(new StringCell(safe(comment.getFrom().getName())));	
	        	} else {
	        		cells.add(new StringCell("unknown"));
	            	cells.add(new StringCell("unknown"));
	        	}
	        	
	        	cells.add(new StringCell(safe(comment.getType())));
	        	
	        	if (comment.getLikeCount() != null )
	        		cells.add(new LongCell(comment.getLikeCount()));
	        	else cells.add(new LongCell(0));
	        	
	            outContainer.addRowToTable(new DefaultRow("Row" + i++, cells));
	           
        	}	
        }
    	
        outContainer.close();
        
        return new PortObject[] { outContainer.getTable() };
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // TODO: generated method stub
    }

    @Override
    protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs)
    		throws InvalidSettingsException {
    	
    	return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
         // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // TODO: generated method stub
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }
    
    private DataTableSpec createSpec() {
        List<DataColumnSpec> colSpecs = new ArrayList<DataColumnSpec>(2);
        
       	
        // Add additional columns to DataSpec
        colSpecs.add(new DataColumnSpecCreator("Post Id", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Comment Id", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Created Date", DateAndTimeCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Message", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("From Id", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("From Name", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Comment Type", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Like Count", LongCell.TYPE).createSpec());
        
        return new DataTableSpec(colSpecs.toArray(new DataColumnSpec[colSpecs.size()]));
    }

    private String safe ( String str ) {
    	if ( str == null ) return "";
    	return str;
    }
}

