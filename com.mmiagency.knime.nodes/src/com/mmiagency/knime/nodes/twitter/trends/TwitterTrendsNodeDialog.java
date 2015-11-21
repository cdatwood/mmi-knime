/*
 * ------------------------------------------------------------------------
 * Copyright by MMI Agency, Houston, Texas, USA
 * Website: http://www.mmiagency.com; Contact: 713-929-6900
 *
 * The MMI KNIME Node is Copyright (C) 2015, MMI Agency The KNIME Nodes 
 * are free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your 
 * option) any later version. 
 * 
 * The KNIME Nodes are distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details. You should have received a 
 * copy of the GNU General Public License along with the KNIME Nodes. If 
 * not, see <http://www.gnu.org/licenses/>.
 * ------------------------------------------------------------------------
 */
package com.mmiagency.knime.nodes.twitter.trends;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

    private final JComboBox<String> m_country = new JComboBox<String>();
    private final JComboBox<String> m_city = new JComboBox<String>();
    private final Map<Integer, String> woeidMap = new HashMap<Integer, String>();
    private final Map<String, Integer> placeMap = new TreeMap<String, Integer>();
    private final Map<String, List<String>> countryCityMap = new HashMap<String, List<String>>();

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
        panel.add(new JLabel("Country:"), gbc);
        gbc.weightx = 1;
        gbc.gridx++;
        panel.add(m_country, gbc);
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.gridy++;
        panel.add(new JLabel("City:"), gbc);
        gbc.weightx = 1;
        gbc.gridx++;
        panel.add(m_city, gbc);
        addTab("Config", panel);
        
        // refresh m_city when m_country selection changes
        m_country.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
            	// refresh m_city
            	refreshCities();
            }
        });
        
    }
    
    protected void refreshCities() {
    	String country = (String)m_country.getSelectedItem();
    	List<String> cities = countryCityMap.get(country);
    	m_city.removeAllItems();
    	if (cities != null) {
    		for (String city : cities) {
    			m_city.addItem(city);
    		}
    	}
    }
    
    private void initWoeid(TwitterApiConnection twitterApiConnection) throws TwitterException {

    	// don't run again if it's already initialized
    	if (m_country.getItemCount() > 1) {
    		return;
    	}
    	
    	// add worldwide as default
    	woeidMap.put(1, "Worldwide");
    	placeMap.put("Worldwide", 1);
    	
    	// if Twitter API Connection is not available, use Worldwide as default
    	if (twitterApiConnection == null) {
        	m_country.addItem("Worldwide");
    		return;
    	}
    	
    	Twitter twitter = twitterApiConnection.getTwitter();
    	    	
    	ResponseList<Location> response = twitter.getAvailableTrends();
    	
    	for (Location location : response) {
    		// skip worldwide as it's already added as default
    		if (location.getWoeid() == 1) continue;
    		// check if it's city or country
    		if (location.getPlaceCode() == 12) {
    			woeidMap.put(location.getWoeid(), location.getCountryName());
    			placeMap.put(location.getCountryName(), location.getWoeid());
    		} else {
    			woeidMap.put(location.getWoeid(), location.getCountryName() + "|" + location.getName());
    			placeMap.put(location.getCountryName() + "|" + location.getName(), location.getWoeid());
    		}
    	}
    	
    	// split country and city
    	List<String> cities;
    	String[] tokens;
    	String lastCountry = "";
    	for (Map.Entry<String, Integer> entry : placeMap.entrySet()) {
    		// skip worldwide as it's already added as default
    		if (entry.getValue() == 1) continue; 
    		tokens = entry.getKey().split("\\|");
    		if (!lastCountry.equals(tokens[0])) {
    			m_country.addItem(tokens[0]);
    		}
    		lastCountry = tokens[0];
			if (countryCityMap.containsKey(tokens[0])) {
				cities = countryCityMap.get(tokens[0]);
			} else {
				cities = new ArrayList<String>();
				countryCityMap.put(tokens[0], cities);
			}
    		if (tokens.length > 1) {
  				cities.add(tokens[1]);
    		} else {
    			cities.add("-"+tokens[0]+"-");
    		}
    	}
    	
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
        String place = woeidMap.get(config.getWoeid());
        String[] tokens = place.split("\\|");
    	m_country.setSelectedItem(tokens[0]);
        if (tokens.length > 1) {
        	// refresh city
        	refreshCities();
        	m_city.setSelectedItem(tokens[1]);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
    	TwitterTrendsNodeConfiguration config = new TwitterTrendsNodeConfiguration();
    	if (m_city.getItemCount() == 0 || ((String)m_city.getSelectedItem()).startsWith("-")) {
    		config.setWoeid(placeMap.get((String)m_country.getSelectedItem()));
    	} else {
    		config.setWoeid(placeMap.get((String)m_country.getSelectedItem() + "|" + (String)m_city.getSelectedItem()));
    	}
        config.save(settings);
    }
}


