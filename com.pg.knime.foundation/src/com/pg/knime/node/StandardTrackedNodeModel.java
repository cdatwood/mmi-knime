package com.pg.knime.node;

import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.port.PortType;

import com.pg.knime.node.tracker.AsyncHttpTracker;

public abstract class StandardTrackedNodeModel extends NodeModel {

	protected ITracker tracker = new AsyncHttpTracker();
	private static final NodeLogger LOGGER = NodeLogger.getLogger(StandardTrackedNodeModel.class);
	
	protected StandardTrackedNodeModel(int nrInDataPorts, int nrOutDataPorts) {
		super(nrInDataPorts, nrOutDataPorts);
		track(ITracker.INSTANTIATE);
	}
	
	public StandardTrackedNodeModel( PortType[] inPortTypes, PortType[] outPortTypes ) {
		super(inPortTypes, outPortTypes);
		track(ITracker.INSTANTIATE);
	}
	
	protected void track ( String eventId ) {
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
