package org.nees.mustsim.replay;

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

        // Defines only one route
        router.attach("/postData/{dataTable}", DataServerResource.class);
        router.attach("/channelList", ChannelListServerResource.class);
        router.attach("/queryData/{list}&{start}&{stop}", DataServerResource.class);
        
        return router;	
	}

}
