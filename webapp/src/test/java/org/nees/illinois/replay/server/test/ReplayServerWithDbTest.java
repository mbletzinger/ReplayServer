package org.nees.illinois.replay.server.test;

import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.StepNumber;
import org.nees.illinois.replay.restlet.ReplayServerComponent;
import org.nees.illinois.replay.restlet.client.DataQueryClient;
import org.nees.illinois.replay.restlet.client.DataTableClient;
import org.nees.illinois.replay.restlet.guice.DbConfigType;
import org.nees.illinois.replay.restlet.guice.LocalRestletTestDbModule;
import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.nees.illinois.replay.test.utils.ChannelDataTestingLists;
import org.nees.illinois.replay.test.utils.ChannelDataTestingLists.ChannelListType;
import org.nees.illinois.replay.test.utils.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ReplayServerWithDbTest {
	private ReplayServerComponent component;
	private final DerbyDbControl derby = new DerbyDbControl();
	private String hostname;
	private Injector injector;
	private final Logger log = LoggerFactory
			.getLogger(ReplayServerWithDbTest.class);
	private boolean ismysql;

	@AfterClass
	public void afterClass() {
		if (component.isStarted()) {
			try {
				component.stop();
				Thread.sleep(4000);
			} catch (Exception e) {
				log.error("Component Stop Failed ", e);
			}
		}
		if (ismysql == false) {
			derby.stopDerby();
		}
	}

	@Parameters("db")
	@BeforeClass
	public void beforeClass(@Optional("derby") String db) {
		ismysql = db.equals("mysql");
		if (ismysql == false) {
			derby.startDerby();
		}
		// Instantiate our Restlet component
		injector = Guice.createInjector(new LocalRestletTestDbModule(ismysql ? DbConfigType.LocalTestMySql : DbConfigType.LocalTestDerby));
		component = injector.getInstance(ReplayServerComponent.class);
		try {
			component.start();
		} catch (Exception e2) {
			log.error("Component failed to start because ", e2);
			Assert.fail();
		}
		hostname = component.getHostinfo().getAddress();
	}

	@Test
	public void testCreateTables() {
		ChannelDataTestingLists cl = new ChannelDataTestingLists();
		DataTableClient dtc = new DataTableClient(hostname, "HybridMasonry1");

		for (ChannelListType typ : ChannelListType.values()) {
			if (typ.equals(ChannelListType.Query1)) {
				break;
			}
			List<String> channels = cl.getChannels(typ);
			dtc.createTable(cl.getTt(typ), channels);
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
			result = dqc.getData(typ.name(), new StepNumber(1.0, 22.0, 3.0),
					new StepNumber(3.0, 22.0, 1.0));
			log.info("Received: " + result);
		}
	}

	@Test(dependsOnMethods = { "testUpdateTables" })
	public void testPutQueries() {

		ChannelDataTestingLists cl = new ChannelDataTestingLists();
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

	@Test(dependsOnMethods = { "testCreateTables" })
	public void testUpdateTables() {

		ChannelDataTestingLists cl = new ChannelDataTestingLists();
		DataTableClient dtc = new DataTableClient(hostname, "HybridMasonry1");

		for (ChannelListType typ : ChannelListType.values()) {
			if (typ.equals(ChannelListType.Query1)) {
				break;
			}

			List<String> channels = cl.getChannels(typ);

			int columns = channels.size();
			double[][] dataD = DataGenerator.initData(20, columns, 0.02);
			dtc.addData(cl.getTt(typ), RateType.CONT, new DoubleMatrix(dataD));

			dataD = DataGenerator.initData(20, columns, 0.02);
			dtc.addData(cl.getTt(typ), RateType.STEP, new DoubleMatrix(dataD));
		}
	}

}
