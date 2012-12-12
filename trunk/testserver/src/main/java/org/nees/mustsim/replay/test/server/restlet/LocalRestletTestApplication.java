package org.nees.mustsim.replay.test.server.restlet;

import org.nees.illinois.replay.restlet.DataQueryServerResource;
import org.nees.illinois.replay.restlet.DataTableServerResource;
import org.nees.illinois.replay.restlet.ReplayServerApplication;

public class LocalRestletTestApplication extends ReplayServerApplication {

	public LocalRestletTestApplication() {
		super("", DataTableServerResource.class, DataQueryServerResource.class, true);
	}

}
