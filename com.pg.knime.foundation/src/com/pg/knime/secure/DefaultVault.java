package com.pg.knime.secure;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class DefaultVault {

	private Map<String, String> vault = new HashMap<String, String>();
	
	// TODO: Research at using char[] instead
	protected void addKey(String key, String value) {
		vault.put(key, value);
	}
	
	public String getKey(String key) {
		String value = vault.get(key);
		if ( value == null ) return "";
		
		return value;
	}
	
	protected String arrayToString( String... values ) {
		return StringUtils.join(values, ",");
	}
	
	public String[] getArrayKey(String key) {
		return getKey(key).split(",");
	}
	
	public static DefaultVault getVault ( String vaultClass ) {
		DefaultVault vault = new DefaultVault();
		
		try {
			return (DefaultVault)Class.forName(vaultClass).newInstance();
		}
		catch ( Exception exc ) {
			
		}
		
		return vault;
		
	}
	
	
	public void initialize() throws Exception {
		
	}
	
}
