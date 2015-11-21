package com.pg.facebook.api.listposts.node;

import javax.swing.JTextField;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.port.PortObjectSpec;

import com.pg.knime.node.StandardNodeDialogPane;

/**
 * <code>NodeDialog</code> for the "ListPosts" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author P&G, eBusiness
 */
public class ListPostsNodeDialog extends StandardNodeDialogPane {

	private ListPostConfiguration config = new ListPostConfiguration();
	JTextField txtMaxPosts = new JTextField();
	
    /**
     * New pane for configuring the ListPosts node.
     */
    protected ListPostsNodeDialog() {

    	addTab ( 
    		"Settings",
    		buildStandardPanel(
    			new PanelBuilder()
    				.add ("Max Results", txtMaxPosts )
    				.build()
    		)	
    	);
    }

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		
		try {
			config.setMaxPosts(Integer.parseInt(txtMaxPosts.getText()));
		} catch ( Exception ex ) {
			throw new InvalidSettingsException("Max posts should be integer value");
		}
		
		config.save(settings);
		
	}
	
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			PortObjectSpec[] specs) throws NotConfigurableException {
		
		config = new ListPostConfiguration();
		config.load(settings);
		
		txtMaxPosts.setText(Integer.toString(config.getMaxPosts()));
		
	}
}

