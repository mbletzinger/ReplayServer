package org.nees.mustsim.replay.test.server.http;

import org.nees.mustsim.replay.restlet.DataQueryServerResource;
import org.nees.mustsim.replay.restlet.DataTableServerResource;
import org.nees.mustsim.replay.restlet.ReplayServerApplication;

public class LocalRestletTestApplication extends ReplayServerApplication {

	public LocalRestletTestApplication() {
		super("", DataTableServerResource.class, DataQueryServerResource.class, true);
	}

}
