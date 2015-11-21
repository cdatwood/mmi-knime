package com.pg.facebook.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.knime.core.node.NodeLogger;

import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import com.restfb.types.Insight;
import com.restfb.util.DateUtils;

public class FacebookInsightFactory {

	private static final NodeLogger LOGGER = NodeLogger.getLogger(FacebookInsightFactory.class);
	
	public static List<FacebookInsight> getFacebookInsights(Insight insight) {
		List<FacebookInsight> facebookInsights = new ArrayList<FacebookInsight>();
		
		String metricName = insight.getName();
		String metricPeriod = insight.getPeriod();
		
		for ( JsonObject metricObject : insight.getValues() ) {
			Object valueObject = metricObject.get("value");
			String timeObject = new SimpleDateFormat(DateUtils.FACEBOOK_LONG_DATE_FORMAT).format(new Date());
			try {
				timeObject = metricObject.getString("end_time");
			} catch ( Exception ex ) {
				LOGGER.debug("Unable to get end_time value - defaulting to current date (assuming lifetime value)");
			}
						
			// Single (no) dimensions
			if ( valueObject instanceof Integer ) {
				FacebookInsight facebookInsight = new FacebookInsight();
				facebookInsight.setMetricname(metricName);
				facebookInsight.setPeriod(metricPeriod);
				facebookInsight.setDate(timeObject);
				Integer value = (Integer)valueObject;
				facebookInsight.setDimensionname("");
				facebookInsight.setValue(new Double(value));
				facebookInsights.add(facebookInsight);
			} 
			// Multidimensional
			else if (valueObject instanceof JsonObject ) {
				JsonObject valueArray = ((JsonObject)valueObject);
				String dimensionNames[] = JsonObject.getNames(valueArray);
				
				for ( String dimensionName : dimensionNames ) {
					FacebookInsight facebookInsight = new FacebookInsight();
					facebookInsight.setMetricname(metricName);
					facebookInsight.setPeriod(metricPeriod);
					facebookInsight.setDate(timeObject);
					
					facebookInsight.setDimensionname(dimensionName);
					String value = valueArray.getString(dimensionName);
					facebookInsight.setValue(Double.parseDouble(value));
					facebookInsights.add(facebookInsight);
				}
			} else if ( valueObject instanceof JsonArray ) {
				JsonArray valueArray = (JsonArray)valueObject;
				if ( valueArray.length() > 0 ) {
					LOGGER.warn("JsonArray: " + metricName + " : " + valueArray.toString());
					for ( int i = 0; i < valueArray.length(); i++ ) {
						FacebookInsight facebookInsight = new FacebookInsight();
						facebookInsight.setMetricname(metricName);
						facebookInsight.setPeriod(metricPeriod);
						facebookInsight.setDate(timeObject);
						
						facebookInsight.setDimensionname(String.valueOf(i));
						facebookInsight.setValue(valueArray.getDouble(i));
						facebookInsights.add(facebookInsight);
					}
					
					
				}
			}
		}
		
		return facebookInsights;
		
	}	
}
