package org.nees.mustsim.replay;

import org.nees.mustsim.replay.restlet.server.DataQueryServerResource;
import org.nees.mustsim.replay.restlet.server.DataTableServerResource;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class ReplayServerApplication extends Application {

	
	/* (non-Javadoc)
	 * @see org.restlet.Application#createInboundRoot()
	 */
	@Override
	public Restlet createInboundRoot() {
		// Create a router Restlet that routes each call to a new instance of HelloWorldResource.
        Router router = new Router(getContext());
        // /data/experiment/{experiment}/table/{table} - put, post
        // /data/experiment/{experiment}/query/{query}/start/{start} - put, get
        // Defines only one route
        router.attach("/data/experiment/{experiment}/table/{table}/rate/{rate}", DataTableServerResource.class);
        router.attach("/data/experiment/{experiment}/query/{query}/rate/{rate}/start/{start}", DataQueryServerResource.class);
        
        return router;	
	}

}
