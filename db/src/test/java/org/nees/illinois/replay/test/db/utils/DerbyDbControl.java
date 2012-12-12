package org.nees.illinois.replay.test.db.utils;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

public class DerbyDbControl {
	private String derbyHome = new String("/Users/mbletzin/Install/derby");
	private boolean started = false;
	private final Logger log = Logger.getLogger(DerbyDbControl.class);
	public void startDerby() {
		if (started) {
			log.info("Derby server already started");
			return;
		}
		ProcessBuilder pb = new ProcessBuilder(derbyHome + "/bin/startNetworkServer");
		 started = execute(pb, "start");
		log.info("Derby server has started");
	}
	public void stopDerby() {
		if (started == false) {
			log.info("Derby server is not running");
			return;
		}
		ProcessBuilder pb = new ProcessBuilder(derbyHome + "/bin/stopNetworkServer");
		 started = execute(pb, "stop");
		log.info("Derby server has stopped");
	}
	private boolean execute(ProcessBuilder pb, String cmdDisp) {
		 Map<String, String> env = pb.environment();
		 env.put("DERBY_HOME", derbyHome);
		 env.put("JAVA_HOME", System.getProperty("java.home"));
		Process p = null;
		 try {
			p = pb.start();
		} catch (IOException e) {
			log.fatal("derby server failed to " + cmdDisp + " because ", e);
			return false;
		}
		 try {
			 log.debug("Derby server is " + cmdDisp +"ing");
			p.waitFor();
		} catch (InterruptedException e) {
			log.error("Derby server  " + cmdDisp + " interrupted");
			return false;
		}
		 return true;
	}
}
