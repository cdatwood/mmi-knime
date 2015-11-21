package com.pg.facebook.api.listposts.node;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public class ListPostConfiguration {

	private int maxPosts = 1000;
	
	private static final String CFG_MAX_POSTS = "max.posts";

	public int getMaxPosts() {
		return maxPosts;
	}

	public void setMaxPosts(int maxPosts) {
		this.maxPosts = maxPosts;
	}
	
	public void load ( NodeSettingsRO settings ) {
		setMaxPosts(settings.getInt(CFG_MAX_POSTS, 100));
	}
	
	public void save (NodeSettingsWO settings ) {
		settings.addInt(CFG_MAX_POSTS, getMaxPosts());
	}
	
}
