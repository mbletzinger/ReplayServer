package org.nees.illinois.replay.test.db.derby.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

/**
 * Class to manage an outside process.
 * @author Michael Bletzinger
 */
public class ProcessManagement {
	/**
	 * Command to execute.
	 */
	private final String cmd;
	/**
	 * Time to wait after starting the process.
	 */
	private final int waitInMillSecs;
	/**
	 * Arguments to the process.
	 */
	private final List<String> args = new ArrayList<String>();
	/**
	 * Environmental variables to be set before the process starts.
	 */
	private final Map<String, String> env = new HashMap<String, String>();
	/**
	 * Pipe to error out.
	 */
	private ProcessResponse errPr;
	/**
	 * Pipe to standard out.
	 */
	private ProcessResponse stoutPr;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(ProcessManagement.class);

	/**
	 * @param cmd
	 *            Command to execute.
	 * @param waitInMilliSec
	 *            Time to wait after starting the process.
	 */
	public ProcessManagement(final String cmd,final int waitInMilliSec) {
		super();
		this.cmd = cmd;
		this.waitInMillSecs = waitInMilliSec;
	}

	/**
	 * Add an argument.
	 * @param arg
	 *            content.
	 */
	public final void addArg(final String arg) {
		args.add(arg);
	}

	/**
	 * Add a variable to the process environment.
	 * @param name
	 *            of variable.
	 * @param value
	 *            to set.
	 */
	public final void addEnv(final String name, final String value) {
		env.put(name, value);
	}

	/**
	 * @return the command with its arguments and environmental variables.
	 */
	private String[] assemble() {
		String[] result = new String[args.size() + 1];
		result[0] = cmd;
		int i = 1;
		for (String a : args) {
			result[i] = a;
			i++;
		}
		return result;
	}

	/**
	 * Start the outside process.
	 */
	public final void execute() {
		String[] executeLine = assemble();
		ProcessBuilder pb = new ProcessBuilder(executeLine);
		pb.environment().putAll(env);

		Process p = null;
		log.debug("Starting process");
		try {
			p = pb.start();
		} catch (IOException e) {
			log.error(cmd + " failed to start because", e);
		}
		log.debug("Creating threads");
		errPr = new ProcessResponse(Level.ERROR, p.getErrorStream(), 100, cmd);
		stoutPr = new ProcessResponse(Level.DEBUG, p.getInputStream(), 100, cmd);
		Thread errThrd = new Thread(errPr);
		Thread stoutThrd = new Thread(stoutPr);
		log.debug("Starting threads");
		errThrd.start();
		stoutThrd.start();
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			log.debug("I was Interrupted");
		}
		log.debug("Waiting for threads");
		try {
			Thread.sleep(waitInMillSecs);
		} catch (InterruptedException e) {
			log.debug("Help I was interrupted.");
		}
		log.debug("Ending threads");
		errPr.setDone(true);
		stoutPr.setDone(true);
	}

	/**
	 * @return the error text.
	 */
	public final String getError() {
		return errPr.getOutput();
	}

	/**
	 * @return the standard out text.
	 */
	public final String getOutput() {
		return stoutPr.getOutput();
	}
}
