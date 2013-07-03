package org.nees.illinois.replay.restlet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Replay server version of a restlet {@link Application application}. The class
 * is designed to use injected resources from Google GUICE so that the app can
 * be configured for testing or production. The only thing this class does is
 * associate resources with URI's. The restlet code takes care of the rest.
 * @author Michael Bletzinger
 */
public class ReplayServerApplication extends Application {
	/**
	 * Root URL that all of the URI's are based on.
	 */
	private final String appRoot;
	/**
	 * Injected class type for a query resource.
	 */
	private final Class<? extends ServerResource> queryClass;
	/**
	 * Injected class type for a data update table resource.
	 */
	private final Class<? extends ServerResource> tableClass;
	/**
	 * Flag indicating whether tracing is enabled for the restlet.
	 */
	private final boolean tracing;

	/**
	 * Constructor.
	 * @param appRoot
	 *            Root URL that all of the URI's are based on.
	 * @param tableClass
	 *            Injected class type for a data update table resource.
	 * @param queryClass
	 *            Injected class type for a query resource.
	 * @param tracing
	 *            Flag indicating whether tracing is enabled for the restlet.
	 */
	// Needed for GUICE
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Inject
	public ReplayServerApplication(@Named("appRoot") final String appRoot,
			@Named("TableResource") final Class tableClass,
			@Named("QueryResource") final Class queryClass,
			@Named("tracing") final Boolean tracing) {
		super();
		this.appRoot = appRoot;
		this.tableClass = tableClass;
		this.queryClass = queryClass;
		this.tracing = tracing;
	}

	/*
	 * (non-Javadoc)
	 * @see org.restlet.Application#createInboundRoot()
	 */
	@Override
	public final Restlet createInboundRoot() {
		Router router = new Router(getContext());
		if (tracing) {
			getContext().getParameters().add("tracing", "true");
		}
		router.attach(appRoot + "/experiment/{experiment}/table/{table}",
				tableClass);
		router.attach(appRoot
				+ "/experiment/{experiment}/table/{table}",
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
