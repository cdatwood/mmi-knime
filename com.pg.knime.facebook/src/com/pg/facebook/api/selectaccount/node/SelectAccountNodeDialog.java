package com.pg.facebook.api.selectaccount.node;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.port.PortObjectSpec;

import com.pg.facebook.api.FacebookApiClient;
import com.pg.facebook.api.connector.data.FacebookApiConnectionPortObjectSpec;
import com.pg.knime.node.SortedComboBoxModel;
import com.pg.knime.node.StandardNodeDialogPane;
import com.restfb.types.Account;
import com.restfb.types.User;

/**
 * <code>NodeDialog</code> for the "SelectAccount" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author P&G, eBusiness
 */
public class SelectAccountNodeDialog extends StandardNodeDialogPane {

	private SortedComboBoxModel<String> accounts = new SortedComboBoxModel<String>();
	private Map<String, String[]> accountMap = new HashMap<String, String[]>();
	
	private JTextField txtAccountId = new JTextField();
	private JTextField txtAccountAccessToken = new JTextField();
	private JButton btnListAccounts = new JButton("Get");
	
	private FacebookSelectAccountConfiguration config = new FacebookSelectAccountConfiguration();
	
	
    /**
     * New pane for configuring the SelectAccount node.
     */
    protected SelectAccountNodeDialog() {
    	
    	JComboBox<String> cbxAccounts = new JComboBox<String>(accounts);
    	
    	addTab(
    		"Settings", 
    		buildStandardPanel(
    			new PanelBuilder()
    				.add("Accounts", cbxAccounts, btnListAccounts )
    				.add("Account Id", txtAccountId )
    				.add("Account Token", txtAccountAccessToken )
    				.build()
    		)
    	);
    	
    	
    	// Action Handler
    	cbxAccounts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (accounts == null ) return;
				String accountName = (String)accounts.getSelectedItem();
				if ( accountName == null ) return;
				if ( accountMap==null) return;
				
				if ( accountMap != null && accountMap.size() > 0 ) {
					txtAccountId.setText(accountMap.get(accountName)[0]);
					txtAccountAccessToken.setText(accountMap.get(accountName)[1]);
				}
				
			}
		});
    	
    	   	
    }

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		
		config.setAccountId(txtAccountId.getText());
		config.setAccessToken(txtAccountAccessToken.getText());
		config.setAccountName((String)accounts.getSelectedItem());
		config.save(settings);
		
	}
	
	
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs) throws NotConfigurableException {
		
		if (! (specs[0] instanceof FacebookApiConnectionPortObjectSpec ) ) {
			throw new NotConfigurableException("Please connect to Facebook API Connector before configurating");
		}
		      
		FacebookApiClient client = ((FacebookApiConnectionPortObjectSpec)specs[0]).getFacebookClient();
		if ( client == null || !client.isConfigured() ) {
			throw new NotConfigurableException("Please execute Facebook API Connector before configurating");
		}
		
		config = new FacebookSelectAccountConfiguration();
		config.load(settings);
		
		txtAccountId.setText(config.getAccountId());
		txtAccountAccessToken.setText(config.getAccessToken());
		
		accounts.removeAllElements();
		if ( config.getAccountName() != null && !config.getAccountName().isEmpty() && accounts.getSize() == 0)
			accounts.addElement(config.getAccountName());
		else {
			accounts.addElement("                    "); // Force expansion
		}
		
		// Create Button Action Handler:
		if ( specs[0] instanceof FacebookApiConnectionPortObjectSpec && btnListAccounts.getActionListeners().length == 0 ) {
			btnListAccounts.addActionListener(new GetAccountList(((FacebookApiConnectionPortObjectSpec)specs[0]).getFacebookClient()));
		}
		
		if (txtAccountId.getText() == null || txtAccountId.getText().isEmpty()) {
			User me = client.getMe();
			txtAccountId.setText(me.getId());
			txtAccountAccessToken.setText(((FacebookApiConnectionPortObjectSpec)specs[0]).getAccessToken());
		}
	}
	
    
    class GetAccountList implements ActionListener {

    	private FacebookApiClient client;
    	
    	public GetAccountList(FacebookApiClient client) {
			this.client = client;
		}
    	
		public void actionPerformed(ActionEvent e) {
			accounts.removeAllElements();
			try {
				accountMap = new HashMap<String, String[]>();
				List<Account> accountList = client.getAccounts();
				
				for ( Account account : accountList ) {
					accountMap.put(account.getName(), new String[] { account.getId(), account.getAccessToken() });
					accounts.addElement(account.getName());
				}
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
    }    
}

