package com.pg.facebook.api.insights.node;


import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerDateModel;

import org.apache.commons.lang.StringUtils;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.port.PortObjectSpec;

import com.pg.knime.node.HelpPanel;
import com.pg.knime.node.SortedComboBoxModel;
import com.pg.knime.node.StandardNodeDialogPane;

/**
 * <code>NodeDialog</code> for the "Insights" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author P&G, eBusiness
 */
public class InsightsNodeDialog extends StandardNodeDialogPane {

	private SortedComboBoxModel<String> cbmMetricGroups = new SortedComboBoxModel<String>();
	private SortedComboBoxModel<String> cbmMetrics = new SortedComboBoxModel<String>();
	private JButton btnAdd	= new JButton("Add");
	private HelpPanel helpPanel = new HelpPanel("Metric description displayed here if available", "No Metric Selected" );
	
	
	private JTextArea txtMetrics = new JTextArea();
	private DefaultComboBoxModel<String> cbmPeriod = new DefaultComboBoxModel<String>();

	private JCheckBox useStartDate = new JCheckBox(), useEndDate = new JCheckBox();
	private JSpinner startDate = new JSpinner(new SpinnerDateModel()), endDate = new JSpinner(new SpinnerDateModel());
    
	private FacebookInsightsConfiguration config = new FacebookInsightsConfiguration();
	
    /**
     * New pane for configuring the Insights node.
     */
	
	// https://developers.facebook.com/docs/graph-api/reference/v2.2/insights/
    protected InsightsNodeDialog() {

    	btnAdd.addActionListener(new AddButtonListener());
    	
    	txtMetrics.setRows(5);
    	txtMetrics.setLineWrap(true);
    	txtMetrics.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        
    	startDate.setEditor(new JSpinner.DateEditor(startDate, "yyyy-MM-dd"));
        useStartDate.setSelected(true);
        useStartDate.addActionListener(new ToggleEnabledActionListener(startDate));
    	
        endDate.setEditor(new JSpinner.DateEditor(endDate, "yyyy-MM-dd"));
        useEndDate.setSelected(true);
        useEndDate.addActionListener(new ToggleEnabledActionListener(endDate));
        
        PanelBuilder panelBuilder = new PanelBuilder();

        // Build help panel (optional)
        HashMap<String, List<String[]>> helpMetrics = FacebookInsightsConfiguration.METRIC_HELP_LIST();
        if ( helpMetrics != null && helpMetrics.size() > 0 ) {
        
        	for ( String group : helpMetrics.keySet() ) cbmMetricGroups.addElement(group);
            
        	JComboBox<String> cbxMetricGroups = new JComboBox<String>(cbmMetricGroups);
        	cbxMetricGroups.addActionListener(new MetricGroupActionListener());
        	
        	JComboBox<String> cbxMetrics = new JComboBox<String>(cbmMetrics);
        	cbxMetrics.addActionListener(new MetricActionListener());
        	
        	panelBuilder
        		.add("Metric Selector", cbxMetricGroups)
        		.add(null, cbxMetrics, btnAdd )
        		.add(null,	helpPanel );
        }
        
        
    	/*
    	 * Settings Tab
    	 */
    	
    	addTab(
    		"Settings",
    		buildStandardPanel(
    			panelBuilder
    				.add("Metric(s)", txtMetrics )
    				.add("Period", new JComboBox<String>(cbmPeriod) )
    				.add("Since" , startDate, useStartDate)
    				.add("Until (Inclusive)" , endDate, useEndDate )
    				.build()
    		)
    	);
    	
    }

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {

		config.setMetrics(txtMetrics.getText().replaceAll("\\s", "").split(","));
		config.setPeriod((String)cbmPeriod.getSelectedItem());
		config.setUseStartDate(useStartDate.isSelected());
		config.setUseEndDate(useEndDate.isSelected());
		config.setStartDate(((JSpinner.DefaultEditor)startDate.getEditor()).getTextField().getText());
		config.setEndDate(((JSpinner.DefaultEditor)endDate.getEditor()).getTextField().getText());
		config.save(settings);
	}
	

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			PortObjectSpec[] specs) throws NotConfigurableException {

		config = new FacebookInsightsConfiguration();
		config.load(settings);
		
		// Initialize drop-down boxes
		if ( cbmPeriod == null || cbmPeriod.getSize() == 0 ) {
			for ( String period : FacebookInsightsConfiguration.PERIOD_MAP().keySet() ) {
				cbmPeriod.addElement(period);
			}
		}
			
		txtMetrics.setText(StringUtils.join(config.getMetrics(),",\n"));
		cbmPeriod.setSelectedItem(config.getPeriod());
		useStartDate.setSelected(config.getUseStartDate());
		useEndDate.setSelected(config.getUseEndDate());
		
		if ( config.getStartDate() != null && !config.getStartDate().isEmpty())
			((JSpinner.DefaultEditor)startDate.getEditor()).getTextField().setText(config.getStartDate());
        
		if ( config.getEndDate() != null && !config.getEndDate().isEmpty())
			((JSpinner.DefaultEditor)endDate.getEditor()).getTextField().setText(config.getEndDate());
		
		new MetricGroupActionListener().actionPerformed(null);
		new MetricActionListener().actionPerformed(null);
		
		if ( useStartDate != null ) startDate.setEnabled(useStartDate.isSelected());
		if ( useEndDate != null ) endDate.setEnabled(useEndDate.isSelected());
		
	}
	
	private class MetricGroupActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			String group = (String)cbmMetricGroups.getSelectedItem();
			List<String[]> metrics = FacebookInsightsConfiguration.METRIC_HELP_LIST().get(group);
			if ( metrics == null || metrics.size() == 0 ) return;
			
			cbmMetrics.removeAllElements();
			for ( String[] row : metrics ) {
				String metricName = row[1];
				cbmMetrics.addElement(metricName);
			}
		}
	}
	
	private class MetricActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			String group = (String)cbmMetricGroups.getSelectedItem();
			String metric = (String)cbmMetrics.getSelectedItem();
			List<String[]> metrics = FacebookInsightsConfiguration.METRIC_HELP_LIST().get(group);
			if ( metrics == null || metrics.size() == 0 || metric == null || StringUtils.isEmpty(metric)) return;
			
			for ( String[] row : metrics ) {
				String metricName = row[1];
				if ( metric.equals(metricName) ) {
					helpPanel.getTextArea().setText( row[2] + " (" + row[3] + ")" );
				}
			}
		}
	}	
	
	private class AddButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			
			String metric = (String)cbmMetrics.getSelectedItem();
			String queryMetrics = txtMetrics.getText();
			
			String toAdd = "";
			if (queryMetrics != null && !StringUtils.isEmpty(queryMetrics.trim()) ) toAdd += ", ";
			toAdd += metric;
			
			txtMetrics.setText(queryMetrics + toAdd );
			
		}
		
	}
	
	private class ToggleEnabledActionListener implements ActionListener {
		
		private Component component;
		
		public ToggleEnabledActionListener(Component component) {
			this.component = component;
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			if ( component == null ) return;
			
			component.setEnabled(!component.isEnabled());
		}
		
	}
	
	
}

