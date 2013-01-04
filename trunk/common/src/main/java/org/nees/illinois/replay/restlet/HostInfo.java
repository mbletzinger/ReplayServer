package org.nees.illinois.replay.restlet;

public class HostInfo {

	private final String hostname;
	private final int port;
	private final boolean tracing;
	public HostInfo(int port, String hostname, boolean tracing) {
		super();
		this.port = port;
		this.hostname = hostname;
		this.tracing = tracing;
	}
	public String getAddress() {
		return "http://" + hostname + ":" + port;
	}
	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @return the tracing
	 */
	public boolean isTracing() {
		return tracing;
	}
}
