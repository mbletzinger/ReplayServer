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
        // /data/table/{table} - put, post
        // /data/query/{query}/start/{start} - put, get
        // Defines only one route
        router.attach("/data/table/{table}", DataTableServerResource.class);
        router.attach("/data/query/{query}/start/{start}", DataQueryServerResource.class);
        
        return router;	
	}

}
