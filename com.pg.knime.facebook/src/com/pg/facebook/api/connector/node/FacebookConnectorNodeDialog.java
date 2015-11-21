package com.pg.facebook.api.connector.node;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextArea;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import com.pg.knime.node.StandardTrackedNodeDialogPane;

/**
 * <code>NodeDialog</code> for the "FacebookConnector" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author P&G, eBusiness Analytics
 */
public class FacebookConnectorNodeDialog extends StandardTrackedNodeDialogPane {

	private FacebookConnectorConfiguration configuration = new FacebookConnectorConfiguration();
	
    private JTextArea txtRefreshToken = new JTextArea();
    private DefaultComboBoxModel<String> cbmApiSelection = new DefaultComboBoxModel<String>();
	
    /**
     * New pane for configuring FacebookConnector node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected FacebookConnectorNodeDialog() {
    	
    	
    	// Setup user authentication select drop-down
    	JComboBox<String> cbxApiSelection = new JComboBox<String>(cbmApiSelection);
    	cbxApiSelection.addActionListener(new TokenChangeAction());
    	
    	for (String type: configuration.getAuthTypes()) {
    		cbmApiSelection.addElement(type);
    	}
    	
    	addTab(
    		"Settings",
    		buildStandardPanel(
    			new PanelBuilder()
    				.add("Credentials", cbxApiSelection )
    				.add("User Token", txtRefreshToken )
    				.build()
    		)
    	);
    	 
    }

    
	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		
		configuration.setAccessToken(txtRefreshToken.getText());
		configuration.save(settings);
		
	}
	
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			DataTableSpec[] specs) throws NotConfigurableException {
		
		configuration = new FacebookConnectorConfiguration();
		configuration.load(settings);
		
		// Initialize
		String selectedItem = (String)cbmApiSelection.getSelectedItem();
		if ( selectedItem != null && ( configuration.getAccessToken() == null || configuration.getAccessToken().isEmpty() ) ) {
			configuration.setAccessToken(configuration.getTokenFromAuthType(selectedItem) );
		}
		
		txtRefreshToken.setText(configuration.getAccessToken());
		
	}
    
    class TokenChangeAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			String selectedItem = (String)cbmApiSelection.getSelectedItem();
			if ( selectedItem == null ) return;
			
			String token = configuration.getTokenFromAuthType(selectedItem);
			configuration.setAccessToken(token);
			txtRefreshToken.setText(configuration.getAccessToken());
			
		}
    	
    }
	
}

