package org.nees.mustsim.replay.test;

import org.nees.mustsim.replay.restlet.DataQueryServerResource;
import org.nees.mustsim.replay.restlet.DataTableServerResource;
import org.nees.mustsim.replay.test.server.utils.Tracer;
import org.nees.mustsim.replay.test.server.utils.UriTestServerResource;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class ReplayTestServerApplication extends Application {

	
	/* (non-Javadoc)
	 * @see org.restlet.Application#createInboundRoot()
	 */
	@Override
	public Restlet createInboundRoot() {
		Tracer tracer = new Tracer(getContext());
		String hostname = getContext().getParameters().getValues("hostname");
		// Create a router Restlet that routes each call to a new instance of HelloWorldResource.
        Router router = new Router(getContext());
        getContext().getParameters().add("tracing", "true");

        router.attach(hostname + "/test3/", tracer);

        router.attach(hostname + "/test1/data/", UriTestServerResource.class);
        router.attach(hostname + "/test1/data/experiment/", UriTestServerResource.class);
        router.attach(hostname + "/test1/data/experiment/{experiment}", UriTestServerResource.class);
        router.attach(hostname + "/test1/data/experiment/{experiment}/table/", UriTestServerResource.class);
        router.attach(hostname + "/test1/data/experiment/{experiment}/table/{table}", UriTestServerResource.class);
        router.attach(hostname + "/test1/data/experiment/{experiment}/table/{table}/rate/{rate}", UriTestServerResource.class);
        router.attach(hostname + "/test1/data/experiment/{experiment}/query/{query}/rate/{rate}/start/{start}", UriTestServerResource.class);
        router.attach(hostname + "/test1/data/experiment/{experiment}/query/{query}/rate/{rate}/start/{start}/stop/{stop}", UriTestServerResource.class);
        router.attach(hostname + "/test/data/experiment/{experiment}/table/{table}", DataTableServerResource.class);
        router.attach(hostname + "/test/data/experiment/{experiment}/table/{table}/rate/{rate}", DataTableServerResource.class);
        router.attach(hostname + "/test/data/experiment/{experiment}/query/{query}/rate/{rate}/start/{start}/stop/{stop}", DataQueryServerResource.class);
        
        return router;	
	}

}
