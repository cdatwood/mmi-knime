package com.pg.knime.node;

public interface ITracker {

	public static final String BUILD_PANEL = "build.panel";
	public static final String EXECUTE = "execute";
	public static final String INSTANTIATE = "instantiate";
	
	public void track(String name, String classification);
	
}
