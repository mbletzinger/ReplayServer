package org.nees.illinois.replay.test.db.derby.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

/**
 * Thread to monitor input streams from a running process.
 * @author Michael Bletzinger
 */
public class ProcessResponse implements Runnable {
	/**
	 * Flag signaling a thread exit.
	 */
	private boolean done;
	/**
	 * Determines how the received text is logged.
	 */
	private final Level level;
	/**
	 * Logger for the thread.
	 */
	private final Logger log = LoggerFactory.getLogger(ProcessResponse.class);
	/**
	 * Time to wait before reading again.
	 */
	private final int millSecWait;
	/**
	 * Content from the stream.
	 */
	private String output = "";
	/**
	 * Name of the process being monitored. Used as part of the logging output.
	 */
	private final String processName;
	/**
	 * Input stream to monitor.
	 */
	private final InputStream strm;

	/**
	 * Constructor.
	 * @param level
	 *            Logger for the thread.
	 * @param strm
	 *            Input stream to monitor.
	 * @param millSecWait
	 *            Time to wait before reading again.
	 * @param processName
	 *            Name of the process being monitored. Used as part of the
	 *            logging output.
	 */
	public ProcessResponse(final Level level,final InputStream strm,
			final int millSecWait,final String processName) {
		super();
		this.level = level;
		this.strm = strm;
		this.millSecWait = millSecWait;
		this.processName = processName;
	}

	/**
	 * @return the level
	 */
	public final Level getLevel() {
		return level;
	}

	/**
	 * @return the output
	 */
	public final String getOutput() {
		return output;
	}

	/**
	 * @return the done
	 */
	public final synchronized boolean isDone() {
		return done;
	}

	@Override
	public final void run() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(strm));
		while (isDone() == false) {
			try {
				String cbuf;
				if (reader.ready()) {
					cbuf = reader.readLine();
					// log.debug("read \"" + cbuf + "\"");
					writeLog(cbuf);
					output += cbuf + "\n";
				}
			} catch (IOException e) {
				log.debug("Stream for \"" + processName + "\" has closed");
				setDone(true);
			}
			try {
				Thread.sleep(millSecWait);
			} catch (InterruptedException e) {
				log.debug("Help I've been interrupted.");
			}
		}
	}

	/**
	 * @param done
	 *            the done to set
	 */
	public final synchronized void setDone(final boolean done) {
		this.done = done;
	}

	/**
	 * Log the text received from the stream.
	 * @param line
	 *            of text form the input stream.
	 */
	private void writeLog(final String line) {
		if (level.equals(Level.ERROR)) {
			log.error("[" + processName + "] " + line);
		}
		if (level.equals(Level.INFO)) {
			log.info("[" + processName + "] " + line);
		}
		if (level.equals(Level.DEBUG)) {
			log.debug("[" + processName + "] " + line);
		}
	}

}
