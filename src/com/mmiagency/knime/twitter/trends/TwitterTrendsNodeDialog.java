package com.mmiagency.knime.twitter.trends;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.twitter.api.data.TwitterApiConnectionPortObject;
import org.knime.twitter.api.nodes.search.TwitterSearchConfiguration;

import twitter4j.ResponseList;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.Query.ResultType;
import twitter4j.Location;
import twitter4j.TwitterException;

/**
 * <code>NodeDialog</code> for the "TwitterTrends" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Ed Ng
 */
public class TwitterTrendsNodeDialog extends NodeDialogPane {

    private JComboBox<List> m_woeid = new JComboBox<List>();

    private JTextField m_exclude = new JTextField();

    /**
     * New pane for configuring TwitterTrends node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected TwitterTrendsNodeDialog() {
    	
    	// set default woeid
    	//TODO call initWoeid to initialize Woeid
    	//TODO add listener to refresh Woeid list
    	
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Place:"), gbc);
        gbc.weightx = 1;
        gbc.gridx++;
        panel.add(m_woeid, gbc);
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Exclude:"), gbc);
        gbc.gridx++;
        gbc.weightx = 1;
        panel.add(m_exclude, gbc);
        addTab("Config", panel);
    }
    
    private void initWoeid(TwitterApiConnectionPortObject twitterPortObject) throws TwitterException {

    	Vector woeidList = new Vector();
    	
    	List woeidWorldwide = new ArrayList();
    	
    	woeidWorldwide.add(new Integer(1));
    	woeidWorldwide.add("Worldwide");
    	woeidList.add(woeidWorldwide);
    	
    	Twitter twitter = twitterPortObject.getTwitterApiConnection().getTwitter();
    	    	
    	ResponseList<Location> response = twitter.getAvailableTrends();
    	
    	for (Location location : response) {
    		// skip worldwide as it's already added as default
    		if (location.getWoeid() == 1) continue;
    		woeidWorldwide = new ArrayList();
    		woeidWorldwide.add(location.getWoeid());
    		woeidWorldwide.add(location.getPlaceName() + ", " + location.getCountryName());    		
        	woeidList.add(woeidWorldwide);
    	}
    	
    	m_woeid = new JComboBox(woeidList);
    	m_woeid.setRenderer(new WoeidCellRenderer());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs) throws NotConfigurableException {
    	TwitterTrendsNodeConfiguration config = new TwitterTrendsNodeConfiguration();
        config.loadInDialog(settings);
        m_woeid.setSelectedItem(config.getWoeid());
        m_exclude.setText(config.getExclude());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
    	TwitterTrendsNodeConfiguration config = new TwitterTrendsNodeConfiguration();
    	config.setWoeid((Integer)m_woeid.getSelectedItem());
    	config.setExclude(m_exclude.getText());
        config.save(settings);
    }

    class WoeidCellRenderer extends JLabel implements ListCellRenderer {
	    public WoeidCellRenderer() {
	        setOpaque(true);
	    }
	
	    public Component getListCellRendererComponent(JList list,
	                                                  Object value,
	                                                  int index,
	                                                  boolean isSelected,
	                                                  boolean cellHasFocus) {
	
	    	if (value != null) {
	    		setText((String)((List)value).get(1));
	    	}

	        return this;
	    }
    }
}


