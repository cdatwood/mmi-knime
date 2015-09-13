package com.mmiagency.knime.keyworddensity.util;

public class KeywordDensityRowEntry {
	private String url;
	private String keyword;
	private double percentage;
	private int frequency;
	
	public KeywordDensityRowEntry(final String url, final String keyword,
			final Double percentage, final int frequency) {
		this.url = url;
		this.keyword = keyword;
		this.percentage = percentage;
		this.frequency = frequency;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public double getPercentage() {
		return percentage;
	}
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
}
