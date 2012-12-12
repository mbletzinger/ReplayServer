package org.nees.mustsim.replay.restlet;

public class HostInfo {

	private final String hostname;
	private final int port;
	public HostInfo(int port, String hostname) {
		super();
		this.port = port;
		this.hostname = hostname;
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
}
