package com.pg.facebook.api.insights.node;

import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import au.com.bytecode.opencsv.CSVReader;

public class FacebookInsightsConfiguration {

	private String period;
	private String[] metrics;

	private String startDate;
	private String endDate;

	private Boolean useStartDate, useEndDate;

	private static final String CFG_PERIOD = "cfg.period";
	private static final String CFG_METRICS = "cfg.metrics";
	private static final String CFG_START_DATE = "cfg.start.date";
	private static final String CFG_END_DATE = "cfg.end.date";
	private static final String CFG_USE_START_DATE = "cfg.use.start";
	private static final String CFG_USE_END_DATE = "cfg.use.end";

	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	
	private static HashMap<String, String> PERIODS;
	private static HashMap<String, List<String[]> > METRICS;
	
	private static NodeLogger LOGGER = NodeLogger.getLogger(FacebookInsightsConfiguration.class);
	
	
	public FacebookInsightsConfiguration() { }

	public String getPeriod() {
		return period;
	}
	
	public String getFacebookPeriodIdentifier() {
		return PERIOD_MAP().get(getPeriod());
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String[] getMetrics() {
		return metrics;
	}

	public void setMetrics(String[] metrics) {
		this.metrics = metrics;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getAdjustedStartDate() {
		return DATE_FORWARD(getStartDate());
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getAdjustedEndDate() {
		return DATE_FORWARD(getEndDate());
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Boolean getUseStartDate() {
		return useStartDate;
	}

	public void setUseStartDate(Boolean useStartDate) {
		this.useStartDate = useStartDate;
	}

	public Boolean getUseEndDate() {
		return useEndDate;
	}

	public void setUseEndDate(Boolean useEndDate) {
		this.useEndDate = useEndDate;
	}

	public void save(NodeSettingsWO settings) {
		settings.addString(CFG_PERIOD, getPeriod());
		settings.addStringArray(CFG_METRICS, getMetrics());
		settings.addString(CFG_START_DATE, getStartDate());
		settings.addString(CFG_END_DATE, getEndDate());
		settings.addBoolean(CFG_USE_START_DATE, getUseStartDate());
		settings.addBoolean(CFG_USE_END_DATE, getUseEndDate());
	}

	public void load(NodeSettingsRO settings) {
		setPeriod(settings.getString(CFG_PERIOD, "Daily"));
		setMetrics(settings.getStringArray(CFG_METRICS, new String[] {}));
		setStartDate(settings.getString(CFG_START_DATE, ""));
		setEndDate(settings.getString(CFG_END_DATE, ""));
		setUseStartDate(settings.getBoolean(CFG_USE_START_DATE, true));
		setUseEndDate(settings.getBoolean(CFG_USE_END_DATE, true));
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, List<String[]> > METRIC_HELP_LIST() {
		
		// Guard statement:
		if ( METRICS != null && METRICS.size() > 0 ) return METRICS;
		
		METRICS = new HashMap<String, List<String[]>>();
		
		try { 
			// Cols: Group Metric Description Periods Values
			CSVReader reader = new CSVReader(new InputStreamReader(FacebookInsightsConfiguration.class.getResource("facebook_metric_list.tsv").openStream()), '\t');
			List<String[]> lines = reader.readAll();
			
			int rowCount = 0;
			for ( String[] row : lines ) {
				if ( rowCount++ == 0 ) continue;
				
				String groupName = row[0];
				List<String[]> metrics = METRICS.get(groupName);
				if ( metrics == null ) metrics = new ArrayList<String[]>();
				metrics.add(row);
				METRICS.put(groupName, metrics);
			}
			
		} catch ( Exception exc )  {
			LOGGER.error(exc.getMessage());
		}
		
		return METRICS;
	}

	public static HashMap<String, String> PERIOD_MAP() {
		if (PERIODS != null)
			return PERIODS;

		HashMap<String, String> PERIODS = new HashMap<String, String>();
		PERIODS.put("All", null);
		PERIODS.put("Daily", "day");
		PERIODS.put("Weekly", "week");
		PERIODS.put("28 Days", "days_28");
		PERIODS.put("Lifetime", "lifetime");

		return PERIODS;
	}

	public static String DATE_FORWARD(String date) {
		try {
			Date val = SDF.parse(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(val);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			return SDF.format(cal.getTime());
		} catch (ParseException exc) {
			return date;
		}

	}

}
