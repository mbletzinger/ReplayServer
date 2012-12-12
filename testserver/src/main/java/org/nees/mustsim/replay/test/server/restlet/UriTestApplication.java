package org.nees.mustsim.replay.test.server.restlet;

import org.nees.mustsim.replay.restlet.ReplayServerApplication;

public class UriTestApplication extends ReplayServerApplication {

	public UriTestApplication() {
		super("/test1/data", UriTestServerResource.class, UriTestServerResource.class, true);
	}

}
