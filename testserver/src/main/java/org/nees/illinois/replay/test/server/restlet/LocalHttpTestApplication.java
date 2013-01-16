package org.nees.illinois.replay.test.server.restlet;

import org.nees.illinois.replay.restlet.DataQueryServerResource;
import org.nees.illinois.replay.restlet.DataTableServerResource;
import org.nees.illinois.replay.restlet.ReplayServerApplication;

public class LocalHttpTestApplication extends ReplayServerApplication {

	public LocalHttpTestApplication() {
		super("/test/data", DataTableServerResource.class, DataQueryServerResource.class, true);
	}

}
