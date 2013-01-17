package org.nees.illinois.replay.test.server;

import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.StepNumber;
import org.nees.illinois.replay.restlet.ReplayServerComponent;
import org.nees.illinois.replay.restlet.client.DataQueryClient;
import org.nees.illinois.replay.restlet.client.DataTableClient;
import org.nees.illinois.replay.test.server.guice.LocalRestletTestModule;
import org.nees.illinois.replay.test.utils.ChannelLists;
import org.nees.illinois.replay.test.utils.ChannelLists.ChannelListType;
import org.nees.illinois.replay.test.utils.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
@Test(groups = { "restlet-client-test" })
public class RestletClientTest {
	private final Logger log = LoggerFactory.getLogger(RestletClientTest.class);
	private ReplayServerComponent component;
	private String root;
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
			AssertJUnit.fail();
		}
		root = component.getHostinfo().getAddress();
		root += "/test/data";
	}

	@AfterClass
	public void teardown() {
		if (component.isStarted()) {
			try {
				component.stop();
				Thread.sleep(4000);
			} catch (Exception e) {
				log.error("Component Stop Failed ", e);
				AssertJUnit.fail();
			}
		}
	}

	@Test
	public void testCreateTables() {

		ChannelLists cl = new ChannelLists();
		DataTableClient dtc = new DataTableClient(root, "HybridMasonry1");
		
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
		DataTableClient dtc = new DataTableClient(root, "HybridMasonry1");
		
		for (ChannelListType typ : ChannelListType.values()) {
			if (typ.equals(ChannelListType.Query1)) {
				break;
			}

			List<String> channels = cl.getChannels(typ);

			int columns = channels.size();
			double[][] dataD = DataGenerator.initData(RateType.CONT, 20,
					columns, 0.02);
			dtc.addData(cl.getTt(typ), RateType.CONT, new DoubleMatrix(dataD));

			dataD = DataGenerator.initData(RateType.STEP, 20, columns, 0.02);
			dtc.addData(cl.getTt(typ), RateType.STEP, new DoubleMatrix(dataD));
		}
	}

	@Test(dependsOnMethods = { "testUpdateTables" })
	public void testPutQueries() {

		ChannelLists cl = new ChannelLists();
		DataQueryClient dqc = new DataQueryClient(root, "HybridMasonry1");

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

		DataQueryClient dqc = new DataQueryClient(root, "HybridMasonry1");

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
