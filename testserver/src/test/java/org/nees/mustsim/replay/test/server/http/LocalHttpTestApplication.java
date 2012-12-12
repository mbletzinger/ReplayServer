package org.nees.mustsim.replay.test.server.http;

import org.nees.mustsim.replay.restlet.DataQueryServerResource;
import org.nees.mustsim.replay.restlet.DataTableServerResource;
import org.nees.mustsim.replay.restlet.ReplayServerApplication;

public class LocalHttpTestApplication extends ReplayServerApplication {

	public LocalHttpTestApplication() {
		super("/test/data", DataTableServerResource.class, DataQueryServerResource.class, true);
	}

}
