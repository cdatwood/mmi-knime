package com.pg.knime.node.configuration;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public class ConfigurationItemManager {

	private List<ConfigurationItem<?>> items = new ArrayList<ConfigurationItem<?>>();

	public void addItem(ConfigurationItem<?> item) {
		items.add(item);
	}

	public void save(NodeSettingsWO settings) {

		for (ConfigurationItem<?> item : items) {
			String name = item.getSettingName();
			saveItem(name, item.getValue(), settings);
		}
	}
	
	public void load ( NodeSettingsRO settings ) {
		
		for (ConfigurationItem<?> item : items) {
			item.setValue(loadItem(settings, item));
		}
	}
	
	private Object loadItem(NodeSettingsRO settings, ConfigurationItem<?> item) {
		
		Object val = item.getValue();
		
		
		if ( val instanceof Boolean ) {
			return settings.getBoolean(item.getSettingName(), (Boolean)item.getDef());
		}
		
		if ( val instanceof String[]) {
			return settings.getStringArray(item.getSettingName(), (String[])item.getDef());
		}
		
		if ( val instanceof Double ) { 
			return settings.getDouble(item.getSettingName(), (Double)item.getDef());
		}
		
		// Default:
		return settings.getString(item.getSettingName(), (String)item.getDef());
		
	}
	
	private void saveItem(String name, Object value, NodeSettingsWO settings) {
		
		if ( value instanceof Boolean ) {
			settings.addBoolean(name, (Boolean)value);
			return;
		}

		if ( value instanceof String[]  ) {
			settings.addStringArray(name, (String[])value);
			return;
		}
		
		if ( value instanceof Double ) {
			settings.addDouble(name, (Double)value);
			return;
		}
		
		// Default:
		settings.addString(name, value.toString());
		
	}


	
}
