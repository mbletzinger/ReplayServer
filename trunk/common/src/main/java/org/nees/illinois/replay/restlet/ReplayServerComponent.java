package org.nees.illinois.replay.restlet;

import org.nees.illinois.replay.common.registries.ExperimentModuleDeleteMe;
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

public class ReplayServerComponent extends Component {

	private final HostInfo hostinfo;
	private final Logger log = LoggerFactory
			.getLogger(ReplayServerComponent.class);

	@Inject
	public ReplayServerComponent(HostInfo hostinfo,
			ReplayServerApplication app, Provider<DataUpdateSubResourceI> tdu,
			Provider<DataQuerySubResourceI> tdq, ExperimentModuleDeleteMe guiceMod) {
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
		cxt.getAttributes().put("guiceMod", guiceMod);
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
	 * @return the hostinfo
	 */
	public HostInfo getHostinfo() {
		return hostinfo;
	}

	// /**
	// * Launches the mail server component.
	// *
	// * @param args
	// * The arguments.
	// * @throws Exception
	// */
	// public static void main(String[] args) throws Exception {
	// new ReplayTestServerComponent(8111).start();
	// }

}
