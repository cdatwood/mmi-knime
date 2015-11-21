package com.pg.facebook.api.selectpost.node;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public class SelectPostConfiguration {

	private String postId;
	
	private static final String CFG_POST_ID = "post.id";

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}
	
	public void load ( NodeSettingsRO settings ) {
		setPostId(settings.getString(CFG_POST_ID, ""));
	}
	
	public void save ( NodeSettingsWO settings ) {
		settings.addString(CFG_POST_ID, getPostId());
	}
	
}
