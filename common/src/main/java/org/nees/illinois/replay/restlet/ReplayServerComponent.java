package org.nees.illinois.replay.restlet;

import org.nees.illinois.replay.data.DataQuerySubResourceI;
import org.nees.illinois.replay.data.DataUpdateSubResourceI;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.service.StatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Replay server version of a restlet {@link Component component}. The class has
 * the main function which boots up the server. The purpose of the class is to
 * load the context with sub-resources injected from Google GUICE.
 * @author Michael Bletzinger
 */
public class ReplayServerComponent extends Component {
/**
 * Class containing all of the server Internet parameters.
 */
	private final HostInfo hostinfo;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory
			.getLogger(ReplayServerComponent.class);
/**
 * Constructor.
 *@param hostinfo
 * Class containing all of the server Internet parameters.
 *@param app
 *Restlet app version to use.
 *@param tdu
 *A provider for data update subresource instances.
 *@param tdq
 *A provider for query subresource instances.
 */
	@Inject
	public ReplayServerComponent(final HostInfo hostinfo,
			final ReplayServerApplication app, final Provider<DataUpdateSubResourceI> tdu,
			final Provider<DataQuerySubResourceI> tdq) {
		super();
		this.hostinfo = hostinfo;
		// Configure the log service
		System.setProperty("org.restlet.engine.loggerFacadeClass",
				"org.restlet.ext.slf4j.Slf4jLoggerFacade");
		log.debug("Set system property for facade");
		StatusService status = new ReplayErrorService();
		setStatusService(status);
		app.setStatusService(status);

		// Set basic properties
		setName("MUST-SIM Replay Server component");
		setDescription("Replay server for collecting test data");
		setOwner("NEES@Illinois");
		setAuthor("Michael Bletzinger");
		Context cxt = getContext().createChildContext();
		cxt.getAttributes().put("updatesI", tdu);
		cxt.getAttributes().put("queryI", tdq);
		if (hostinfo.isTracing()) {
			cxt.getParameters().set("tracing", "true");
		}
		// Add connectors
		getClients().add(new Client(Protocol.CLAP));
		Server server = new Server(cxt, Protocol.HTTP, hostinfo.getPort());
		getServers().add(server);

		// Attach the application to the default virtual host
		app.setContext(cxt);
		getDefaultHost().attachDefault(app);

	}

	/**
	 * @return the host information class.
	 */
	public final HostInfo getHostinfo() {
		return hostinfo;
	}

}
