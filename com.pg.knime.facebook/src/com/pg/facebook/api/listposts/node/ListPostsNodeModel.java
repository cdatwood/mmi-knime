package com.pg.facebook.api.listposts.node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import com.pg.facebook.api.FacebookApiClient;
import com.pg.facebook.api.connector.data.FacebookApiConnectorPortObject;
import com.restfb.Connection;
import com.restfb.types.Post;

/**
 * This is the model implementation of ListPosts.
 * 
 *
 * @author P&G, eBusiness
 */
public class ListPostsNodeModel extends NodeModel {
    
	private ListPostConfiguration config = new ListPostConfiguration();
	
	private static final NodeLogger LOGGER = NodeLogger.getLogger(ListPostsNodeModel.class);
	
    /**
     * Constructor for the node model.
     */
    protected ListPostsNodeModel() {
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
    	Connection<Post> postConnection = client.getPosts();
    	
    	DataTableSpec outSpec = createSpec();
        BufferedDataContainer outContainer = exec.createDataContainer(outSpec);
        
        Iterator<List<Post>> iterator = postConnection.iterator();
        int i = 0;
        do {
        	
        	List<Post> posts = iterator.next();
        	
        	exec.checkCanceled();
        	exec.setProgress("Retrieved " + i + " posts");
        	
        	for ( Post post : posts ) {
        		
	        	List<DataCell> cells = new ArrayList<DataCell>(outSpec.getNumColumns());
	        	
	        	cells.add(new StringCell(client.getImpersonationAccountId()));
	        	cells.add(new StringCell(safe(post.getId())));
	        	cells.add(new DateAndTimeCell(post.getCreatedTime().getTime(), true, true, false));
	        	cells.add(new StringCell(safe(post.getMessage())));
	        	if ( post.getFrom() != null ) {
	        		cells.add(new StringCell(safe(post.getFrom().getId())));
	            	cells.add(new StringCell(safe(post.getFrom().getName())));	
	        	} else {
	        		cells.add(new StringCell("unknown"));
	            	cells.add(new StringCell("unknown"));
	        	}
	        	cells.add(new StringCell(safe(post.getLink())));
	        	cells.add(new StringCell(safe(post.getType())));
	        	if ( post.getApplication() != null ) 
	        		cells.add(new StringCell(post.getApplication().getName()));
	        	else 
	        		cells.add(new StringCell("unknown"));
	        	
	        	cells.add(new LongCell(post.getSharesCount()));
	        	if (post.getLikesCount() != null )
	        		cells.add(new LongCell(post.getLikesCount()));
	        	else cells.add(new LongCell(0));
	        	if ( post.getComments() != null )
	        		cells.add(new LongCell(post.getComments().getTotalCount()));
	        	else cells.add(new LongCell(0));
	        	
	            outContainer.addRowToTable(new DefaultRow("Row" + i++, cells));
	            LOGGER.debug("Added " + i + " rows");
        	}
        	
        } while ( iterator.hasNext() && i <= config.getMaxPosts() );
        
        outContainer.close();
    	return new PortObject[]{outContainer.getTable()}; 
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
         if ( config != null )
        	 config.save(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        
    	config = new ListPostConfiguration();
    	config.load(settings);
    	
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
        colSpecs.add(new DataColumnSpecCreator("Account Id", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Post Id", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Created Date", DateAndTimeCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Message", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("From Id", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("From Name", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Link", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Post Type", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Application", StringCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Share Count", LongCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Like Count", LongCell.TYPE).createSpec());
        colSpecs.add(new DataColumnSpecCreator("Comment Count", LongCell.TYPE).createSpec());
        
        return new DataTableSpec(colSpecs.toArray(new DataColumnSpec[colSpecs.size()]));
    }
    
    private String safe ( String str ) {
    	if ( str == null ) return "";
    	return str;
    }
    
}

