package org.nees.mustsim.replay.test.server;

import java.util.List;

import org.nees.mustsim.replay.data.DoubleMatrix;
import org.nees.mustsim.replay.data.RateType;
import org.nees.mustsim.replay.data.StepNumber;
import org.nees.mustsim.replay.restlet.ReplayServerComponent;
import org.nees.mustsim.replay.restlet.client.DataQueryClient;
import org.nees.mustsim.replay.restlet.client.DataTableClient;
import org.nees.mustsim.replay.test.server.guice.LocalHttpTestModule;
import org.nees.mustsim.replay.test.server.guice.LocalRestletTestModule;
import org.nees.mustsim.replay.test.utils.ChannelLists;
import org.nees.mustsim.replay.test.utils.ChannelLists.ChannelListType;
import org.nees.mustsim.replay.test.utils.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class RestletClientTest {
	private final Logger log = LoggerFactory.getLogger(RestletClientTest.class);
	private ReplayServerComponent component;
	private String hostname;
	private Injector injector;

	@BeforeClass
	public void setup() {
		// Instantiate our Restlet component
		injector = Guice.createInjector(new LocalRestletTestModule());
		component = injector.getInstance(ReplayServerComponent.class);
		try {
			component.start();
		} catch (Exception e2) {
			log.error("Component failed to start because ", e2);
			Assert.fail();
		}
		hostname = component.getHostinfo().getAddress();
	}

	@AfterClass
	public void teardown() {
		if (component.isStarted()) {
			try {
				component.stop();
				Thread.sleep(4000);
			} catch (Exception e) {
				log.error("Component Stop Failed ", e);
				Assert.fail();
			}
		}
	}

	@Test
	public void testCreateTables() {

		ChannelLists cl = new ChannelLists();
		DataTableClient dtc = new DataTableClient(hostname, "HybridMasonry1");
		
		for (ChannelListType typ : ChannelListType.values()) {
			if (typ.equals(ChannelListType.Query1)) {
				break;
			}
			List<String> channels = cl.getChannels(typ);
			dtc.createTable(cl.getTt(typ), channels);
		}
	}

	@Test(dependsOnMethods = { "testCreateTables" })
	public void testUpdateTables() {

		ChannelLists cl = new ChannelLists();
		DataTableClient dtc = new DataTableClient(hostname, "HybridMasonry1");
		
		for (ChannelListType typ : ChannelListType.values()) {
			if (typ.equals(ChannelListType.Query1)) {
				break;
			}

			List<String> channels = cl.getChannels(typ);

			int columns = channels.size();
			double[][] dataD = DataGenerator.initData(RateType.CONT, 20,
					columns, 0.02);
			dtc.addData(cl.getTt(typ), RateType.CONT, new DoubleMatrix(dataD, columns));

			dataD = DataGenerator.initData(RateType.STEP, 20, columns, 0.02);
			dtc.addData(cl.getTt(typ), RateType.STEP, new DoubleMatrix(dataD, columns));
		}
	}

	@Test(dependsOnMethods = { "testUpdateTables" })
	public void testPutQueries() {

		ChannelLists cl = new ChannelLists();
		DataQueryClient dqc = new DataQueryClient(hostname, "HybridMasonry1");

		for (ChannelListType typ : ChannelListType.values()) {
			if (typ.equals(ChannelListType.OM)
					|| typ.equals(ChannelListType.DAQ)) {
				continue;
			}
			List<String> channels = cl.getChannels(typ);
			dqc.setQuery(typ.name(), channels);
		}
	}

	@Test(dependsOnMethods = { "testPutQueries" })
	public void testGetQueries() {

		DataQueryClient dqc = new DataQueryClient(hostname, "HybridMasonry1");

		for (ChannelListType typ : ChannelListType.values()) {
			if (typ.equals(ChannelListType.OM)
					|| typ.equals(ChannelListType.DAQ)) {
				continue;
			}
			DoubleMatrix result = dqc.getData(typ.name(), 224.23);
			log.info("Received: " + result);
			result = dqc.getData(typ.name(), new StepNumber(1.0, 22.0, 3.0), new StepNumber(3.0, 22.0, 1.0));
			log.info("Received: " + result);
		}
	}

}
