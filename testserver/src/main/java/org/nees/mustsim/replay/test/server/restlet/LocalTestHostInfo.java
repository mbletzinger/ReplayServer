package org.nees.mustsim.replay.test.server.restlet;

import org.nees.mustsim.replay.restlet.HostInfo;

public class LocalTestHostInfo extends HostInfo {

	public LocalTestHostInfo() {
		super(8111, "localhost");
	}

}