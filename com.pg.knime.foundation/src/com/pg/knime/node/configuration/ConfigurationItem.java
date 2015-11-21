package com.pg.knime.node.configuration;

public class ConfigurationItem<T> {

	private String settingName;
	private T value;
	private T def;

	public ConfigurationItem(String name, T def ) {
		this.settingName = name;
		this.setDef(def);
		this.setValue(def);
	}
	
	public String getSettingName() {
		return settingName;
	}

	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}

	public T getValue() {
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public void setValue ( Object value ) {
		this.value = (T)value;
	}

	public T getDef() {
		return def;
	}

	@SuppressWarnings("unchecked")
	public void setDef(Object def) {
		this.def = (T)def;
	}
	
}
