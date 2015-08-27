package com.mmiagency.knime.twitter.trends;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import org.knime.core.node.port.PortObjectSpec;
import org.knime.twitter.api.data.TwitterApiConnection;
import org.knime.twitter.api.data.TwitterApiConnectionPortObjectSpec;

import twitter4j.ResponseList;
import twitter4j.Twitter;
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

    private JComboBox<Integer> m_woeid;
    private JTextField m_exclude = new JTextField();
    private Map<Integer, String> woeidMap = new LinkedHashMap<Integer, String>();

    /**
     * New pane for configuring TwitterTrends node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected TwitterTrendsNodeDialog() {
    	
    	// set default woeid
    	try {
    		initWoeid(null);
    	} catch (TwitterException te) {
    		// this should not happen because 
    		// we are not even talking to twitter 
    		// at this point
    	}
    	
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
    
    private void initWoeid(TwitterApiConnection twitterApiConnection) throws TwitterException {

    	// don't run again if it's already initialized
    	if (m_woeid != null && m_woeid.getItemCount() > 1) {
    		return;
    	}
    	
    	// add worldwide as default
    	woeidMap.put(1, "Worldwide");
    	
    	if (twitterApiConnection == null) {
        	m_woeid = new JComboBox<Integer>();
        	m_woeid.addItem(1);
        	m_woeid.setRenderer(new WoeidCellRenderer());
    		return;
    	}
    	
    	Twitter twitter = twitterApiConnection.getTwitter();
    	    	
    	ResponseList<Location> response = twitter.getAvailableTrends();
    	
    	for (Location location : response) {
    		// skip worldwide as it's already added as default
    		if (location.getWoeid() == 1) continue;
    		woeidMap.put(location.getWoeid(), location.getCountryName() + ", " + location.getName());
    	}
    	
    	woeidMap = sortByValue(woeidMap);

    	for (Integer key : woeidMap.keySet()) {
    		if (key == 1) continue; // already added by default
    		m_woeid.addItem(key);
    	}
    }
    
    private Map sortByValue(Map unsortMap) {	 
    	List list = new LinkedList(unsortMap.entrySet());
     
    	Collections.sort(list, new Comparator() {
    		public int compare(Object o1, Object o2) {
    			return ((Comparable) ((Map.Entry) (o1)).getValue())
    						.compareTo(((Map.Entry) (o2)).getValue());
    		}
    	});
     
    	Map sortedMap = new LinkedHashMap();
    	for (Iterator it = list.iterator(); it.hasNext();) {
    		Map.Entry entry = (Map.Entry) it.next();
    		sortedMap.put(entry.getKey(), entry.getValue());
    	}
    	return sortedMap;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs) throws NotConfigurableException {

        if(specs[0] == null) {
            throw new NotConfigurableException("Missing Twitter API Connection");
        } else {
            TwitterApiConnectionPortObjectSpec connectionSpec = (TwitterApiConnectionPortObjectSpec)specs[0];
            if(connectionSpec.getTwitterApiConnection() == null) {
                throw new NotConfigurableException("Missing Twitter API Connection");
            } else {
            	
            	// load locations
            	try {
            		initWoeid(connectionSpec.getTwitterApiConnection());
            	} catch (TwitterException te) {
            		throw new NotConfigurableException("Unable to initialize places", te);
            	}
            	
            }
        }
        
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
	
	    @SuppressWarnings("unchecked")
		public Component getListCellRendererComponent(JList list,
	                                                  Object value,
	                                                  int index,
	                                                  boolean isSelected,
	                                                  boolean cellHasFocus) {	    	
	    	if (value != null) {
	    		setText(woeidMap.get((Integer)value));
	    	}

	        return this;
	    }
    }
}


