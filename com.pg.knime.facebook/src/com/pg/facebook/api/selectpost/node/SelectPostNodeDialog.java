package com.pg.facebook.api.selectpost.node;

import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.port.PortObjectSpec;

import com.pg.knime.node.StandardNodeDialogPane;

/**
 * <code>NodeDialog</code> for the "SelectPost" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author P&G, eBusiness
 */
public class SelectPostNodeDialog extends StandardNodeDialogPane {

	private JTextField txtPostId;
	
	private SelectPostConfiguration config;
	
    /**
     * New pane for configuring the SelectPost node.
     */
    protected SelectPostNodeDialog() {
    	
    	int ypos = 0;
    	JPanel pnlSettings = new JPanel(new GridBagLayout());
    	
    	pnlSettings.add( new JLabel("Post Id:"), getGBC(0,ypos++,0,0));
    	txtPostId = new JTextField();
        pnlSettings.add( txtPostId, getGBC(0, ypos++, 1, 0));
    	
		pnlSettings.add(new JPanel(), getGBC(0, ypos++, 100, 100));
    	addTab("Settings", pnlSettings);
    }

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		
		if ( config == null ) return;
		
		config.setPostId(txtPostId.getText());
		config.save(settings);
		
	}
	
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			PortObjectSpec[] specs) throws NotConfigurableException {
		
		config = new SelectPostConfiguration();
		config.load(settings);
		
		txtPostId.setText(config.getPostId());
		
	}

}

