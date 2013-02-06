package org.nees.illinois.replay.test.server.restlet;

import org.nees.illinois.replay.restlet.ReplayServerComponent;
import org.nees.illinois.replay.test.server.guice.LocalRestletTestModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.AssertJUnit;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ReplayTestServer {

	/**
	 * @return the log
	 */
	public Logger getLog() {
		return log;
	}
	/**
	 * @return the component
	 */
	public ReplayServerComponent getComponent() {
		return component;
	}
	private final Logger log = LoggerFactory.getLogger(ReplayTestServer.class);
	
	private final ReplayServerComponent component;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ReplayTestServer rts = new ReplayTestServer();
		rts.log.info("Starting Test Server");
		try {
			rts.component.start();
		} catch (Exception e) {
			rts.log.error("Test Server Failed to start");
			return;
		}
	}
	
	public ReplayTestServer() {
		Injector injector = Guice.createInjector(new LocalRestletTestModule());
		System.setProperty("org.restlet.engine.loggerFacadeClass",
				"org.restlet.ext.slf4j.Slf4jLoggerFacade");
		component = injector.getInstance(ReplayServerComponent.class);
		try {
			component.start();
		} catch (Exception e) {
			log.error("Component failed to start because ", e);
			AssertJUnit.fail();
		}
	}

}
