package com.pg.knime.node.configuration;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public class Configuration {

	private ConfigurationItemManager manager;
	
	public Configuration() {
		manager = new ConfigurationItemManager();
		
	}
	
	protected ConfigurationItemManager getManager() {
		if ( manager == null ) manager = new ConfigurationItemManager();
		return manager;
	}
	
	public void load ( NodeSettingsRO settings ) {
		getManager().load(settings);
	}
	
	public void save (NodeSettingsWO settings ) {
		getManager().save(settings);
	}
	
}
