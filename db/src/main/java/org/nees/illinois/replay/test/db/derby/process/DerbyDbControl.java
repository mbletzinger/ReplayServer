package org.nees.illinois.replay.test.db.derby.process;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for starting and stopping a network derby database.
 * @author Michael Bletzinger
 */
public class DerbyDbControl {
	/**
	 * Hardcoded installation location for Derby.
	 */
	private final String derbyHome = new String("/Users/mbletzin/Install/derby");
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(DerbyDbControl.class);
	/**
	 * True if derby was started from this instance.
	 */
	private boolean localStart = false;
	/**
	 * Folder with the derby database files that can be deleted after testing is
	 * done.
	 */
	private FileWithContentDelete derbyData;

	/**
	 * Remove all data from the database run.
	 */
	private void cleanup() {
		if (derbyData.exists()) {
			log.debug("removing folder " + derbyData);
			boolean done = derbyData.delete();
			if (done) {
				return;
			}
			log.error("Could not delete " + derbyData.getAbsolutePath());
		}
	}

	/**
	 * Create a folder for the database files.
	 */
	private void createDirectory() {
		Properties p = System.getProperties();
		String userDir = p.getProperty("user.dir");
		derbyData = new FileWithContentDelete(userDir, "derbyDb");
		if (derbyData.exists() == false) {
			log.debug("Creating folder " + derbyData);
			derbyData.mkdir();
		} else {
			log.debug("Derby data folder " + derbyData + " already exists.");

		}

		p.setProperty("derby.system.home", derbyData.getAbsolutePath());
	}

	/**
	 * @return true if the database is running.
	 */
	public final boolean isRunning() {
		ProcessManagement pm = new ProcessManagement(derbyHome
				+ "/bin/NetworkServerControl", 4000);
		pm.addArg("ping");
		pm.execute();
		if (pm.getOutput().contains("Connection refused")) {
			return false;
		}
		return true;
	}

	/**
	 * Start the network derby if it has not already started.
	 */
	public final void startDerby() {
		if (isRunning()) {
			log.info("Derby server already started");
			localStart = false;
			return;
		}
		createDirectory();
		ProcessManagement pm = new ProcessManagement(derbyHome
				+ "/bin/startNetworkServer", 4000);
		pm.addEnv("DERBY_OPTS",
				"-Dderby.system.home=" + derbyData.getAbsolutePath());
		pm.execute();
		localStart = true;
	}

	/**
	 * Stop the network derby.
	 */
	public final void stopDerby() {
		if (localStart) {
			ProcessManagement pm = new ProcessManagement(derbyHome
					+ "/bin/stopNetworkServer", 4000);
			pm.execute();
			log.info("Derby server has stopped");
			cleanup();
		}
	}
}
