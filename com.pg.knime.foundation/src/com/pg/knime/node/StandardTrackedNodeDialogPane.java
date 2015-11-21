package com.pg.knime.node;

import javax.swing.JPanel;

import org.knime.core.node.NodeLogger;

import com.pg.knime.node.tracker.AsyncHttpTracker;

public abstract class StandardTrackedNodeDialogPane extends StandardNodeDialogPane {

	protected ITracker tracker = new AsyncHttpTracker();
	
	private static final NodeLogger LOGGER = NodeLogger.getLogger(StandardTrackedNodeDialogPane.class);
	
	@Override
	protected JPanel buildStandardPanel(LabelComponentPair... pairs) {

		track(this.getClass().getSimpleName(), ITracker.BUILD_PANEL );
		return super.buildStandardPanel(pairs);
	}
	
	public void track ( String eventId ) {
		track ( this.getClass().getSimpleName(), eventId );
	}
	
	public void track ( String nodeName, String eventId ) {
		try {
			if ( tracker != null ) {
			tracker.track(nodeName, eventId );
			}
		} catch (Exception exc) {
			LOGGER.error(exc.getMessage());
		}
	}
	
}
