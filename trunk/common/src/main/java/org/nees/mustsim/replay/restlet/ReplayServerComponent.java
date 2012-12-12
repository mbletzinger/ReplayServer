package org.nees.mustsim.replay.restlet;

import org.nees.mustsim.replay.data.DataUpdatesI;
import org.nees.mustsim.replay.queries.DataQueryI;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.ext.slf4j.Slf4jLoggerFacade;

import com.google.inject.Inject;

public class ReplayServerComponent extends Component {

	private final HostInfo hostinfo;
	@Inject
	public ReplayServerComponent(HostInfo hostinfo,
			ReplayServerApplication app, DataUpdatesI tdu, DataQueryI tdq) {
		super();
		this.hostinfo = hostinfo;
		// Configure the log service
		Engine.getInstance().setLoggerFacade(new Slf4jLoggerFacade());
		// Set basic properties
		setName("MUST-SIM Replay Server component");
		setDescription("Replay server for collecting test data");
		setOwner("NEES@Illinois");
		setAuthor("Michael Bletzinger");
		Context cxt = new Context();
		cxt.getAttributes().put("updatesI", tdu);
		cxt.getAttributes().put("queryI", tdq);
		cxt.getParameters().set("tracing", "true");
		setContext(cxt);

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
