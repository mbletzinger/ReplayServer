package org.nees.illinois.replay.restlet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ReplayServerApplication extends Application {
	private final String appRoot;
	private final Class<? extends ServerResource> queryClass;
	private final Class<? extends ServerResource> tableClass;
	private final boolean tracing;

	// Needed for Guice
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Inject
	public ReplayServerApplication(@Named("appRoot") String appRoot,
			@Named("TableResource") Class tableClass,
			@Named("QueryResource") Class queryClass,
			@Named("tracing") Boolean tracing) {
		super();
		this.appRoot = appRoot;
		this.tableClass = tableClass;
		this.queryClass = queryClass;
		this.tracing = tracing;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.restlet.Application#createInboundRoot()
	 */
	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		if (tracing) {
			getContext().getParameters().add("tracing", "true");
		}
		router.attach(appRoot + "/experiment/{experiment}/table/{table}",
				tableClass);
		router.attach(appRoot
				+ "/experiment/{experiment}/table/{table}/rate/{rate}",
				tableClass);
		router.attach(appRoot + "/experiment/{experiment}/query/{query}",
				queryClass);
		router.attach(
				appRoot
						+ "/experiment/{experiment}/query/{query}/rate/{rate}/start/{start}",
				queryClass);
		router.attach(
				appRoot
						+ "/experiment/{experiment}/query/{query}/rate/{rate}/start/{start}/stop/{stop}",
				queryClass);

		return router;
	}

}
