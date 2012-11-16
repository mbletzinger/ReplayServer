package org.nees.mustsim.replay.test.server.utils;

import org.nees.mustsim.replay.channels.ChannelNameRegistry;
import org.nees.mustsim.replay.test.ReplayTestServerApplication;
import org.nees.mustsim.replay.test.data.TestDataQuery;
import org.nees.mustsim.replay.test.data.TestDataUpdates;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.ext.slf4j.Slf4jLoggerFacade;

public class ReplayTestServerComponent extends Component {

	public ReplayTestServerComponent() {
		super();
		// Configure the log service
		Engine.getInstance().setLoggerFacade(new Slf4jLoggerFacade());
		// Set basic properties
		setName("RESTful Mail Server component");
		setDescription("Example for 'Restlet in Action' book");
		setOwner("Noelios Technologies");
		setAuthor("The Restlet Team");
		ChannelNameRegistry cnr = new ChannelNameRegistry();
		TestDataUpdates tdu = new TestDataUpdates(cnr);
		TestDataQuery tdq = new TestDataQuery(cnr);
		Context cxt = new Context();
		cxt.getAttributes().put("updatesI", tdu);
		cxt.getAttributes().put("queryI", tdq);
		cxt.getParameters().set("tracing", "true");
		setContext(cxt);

		// Add connectors
		getClients().add(new Client(Protocol.CLAP));
		Server server = new Server(cxt, Protocol.HTTP, 8111);
		getServers().add(server);

		// Attach the application to the default virtual host
		getDefaultHost().attachDefault(new ReplayTestServerApplication());

	}

	/**
	 * Launches the mail server component.
	 * 
	 * @param args
	 *            The arguments.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new ReplayTestServerComponent().start();
	}

}
