package com.pg.facebook.api;

import java.util.Calendar;
import java.util.Date;

import com.restfb.util.DateUtils;

public class FacebookInsight {

	
	private String metricname;
	private String period;
	private String dimensionname;
	private Double value;
	private String date;

	public FacebookInsight() { }
	
	public FacebookInsight(String metricname, String period, String value ) {
		
	}

	public String getMetricname() {
		return metricname;
	}
	public void setMetricname(String metricname) {
		this.metricname = metricname;
	}

	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	
	public String getDimensionname() {
		return dimensionname;
	}
	public void setDimensionname(String dimensionname) {
		this.dimensionname = dimensionname;
	}
	
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	
	public String getDate() {
		return date;
	}
	public Date getDateAsAdjustedDate() {
		Date formattedDate = DateUtils.toDateFromLongFormat(getDate());
		Calendar cal = Calendar.getInstance();
		cal.setTime(formattedDate);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		return cal.getTime();
	}
	public void setDate(String date) {
		this.date = date;
	}

	public boolean hasDimensions() {
		return !(this.dimensionname == null || "".equals(this.dimensionname));
	}
	
	@Override
	public String toString() {
		return getMetricname() + " " + getDimensionname() + " " + getValue() + " " + getPeriod() + " " + getDate();
	}
	
	
	
}
