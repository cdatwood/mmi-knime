package com.pg.knime.node.tracker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.knime.core.node.NodeLogger;

import com.pg.knime.node.ITracker;

public class AsyncHttpTracker implements ITracker {

	private static final NodeLogger LOGGER = NodeLogger.getLogger(AsyncHttpTracker.class);
	
	public void track(String name, String classification) {
		
		// Create non-blocking thread request
		Thread thread = new Thread(new AsyncHttpTrackerThread(trackerUrl(name, classification)));
		thread.start();
		
	}
	
	private String trackerUrl (String name, String classification ) {
		return "http://go-mrd-003.na.pg.com/knimetrack/tracker.php?node_name=" + name + "&event_id=" + classification;
	}
	
	private class AsyncHttpTrackerThread implements Runnable {

		String url;
		
		public AsyncHttpTrackerThread(String url) {
			this.url = url;
		}
		
		public void run() {
			try {
				URL url = new URL (this.url);
				BufferedReader br = new BufferedReader( new InputStreamReader( url.openStream() ));
				while ( br.readLine() != null );
				
			} catch ( Exception exc ) {
				LOGGER.debug("Unable to track node usage");
			}
		}
	}
	
}
