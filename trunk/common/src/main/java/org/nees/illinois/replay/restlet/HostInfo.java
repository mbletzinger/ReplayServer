package org.nees.illinois.replay.restlet;

/**
 * Class containing the Internet address of the replay server.
 * @author Michael Bletzinger
 */
public class HostInfo {
	/**
	 * Host for the replay server.
	 */
	private final String hostname;
	/**
	 * Port number for the replay server.
	 */
	private final int port;
	/**
	 * Flag to turn on restlet tracing.
	 */
	private final boolean tracing;

	/**
	 * Constructor.
	 * @param port
	 *            Port number for the replay server.
	 * @param hostname
	 *            Host for the replay server.
	 * @param tracing
	 *            Flag to turn on restlet tracing.
	 */
	public HostInfo(final int port, final String hostname, final boolean tracing) {
		super();
		this.port = port;
		this.hostname = hostname;
		this.tracing = tracing;
	}

	/**
	 * @return the url of the replay server.
	 */
	public final String getAddress() {
		return "http://" + hostname + ":" + port;
	}

	/**
	 * @return the hostname
	 */
	public final String getHostname() {
		return hostname;
	}

	/**
	 * @return the port
	 */
	public final int getPort() {
		return port;
	}

	/**
	 * @return the tracing
	 */
	public final boolean isTracing() {
		return tracing;
	}
}
