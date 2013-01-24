package org.nees.illinois.replay.test.server;

import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.StepNumber;
import org.nees.illinois.replay.restlet.ReplayServerComponent;
import org.nees.illinois.replay.restlet.client.DataQueryClient;
import org.nees.illinois.replay.test.resources.utils.DatasetLoaderI;
import org.nees.illinois.replay.test.server.guice.LocalRestletTestModule;
import org.nees.illinois.replay.test.server.utils.InjectedErrorsType;
import org.nees.illinois.replay.test.server.utils.RestletLoader;
import org.nees.illinois.replay.test.server.utils.DataQueryBadClient;
import org.nees.illinois.replay.test.server.utils.DataTableBadClient;
import org.nees.illinois.replay.test.utils.ChannelLists;
import org.nees.illinois.replay.test.utils.ChannelLists.ChannelListType;
import org.nees.illinois.replay.test.utils.DataGenerator;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

@Test(groups = { "restlet-errors-client-test" })
public class RestletErrorsClientTest {
	private final Logger log = LoggerFactory
			.getLogger(RestletErrorsClientTest.class);
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
		DataTableBadClient dtc = new DataTableBadClient(root,
				"HybridMasonry1");
		ChannelListType typ = ChannelListType.OM;

		InjectedErrorsType[] btype = { InjectedErrorsType.NoTable,
				InjectedErrorsType.ErrExperiment,
				InjectedErrorsType.NoExperiment };

		List<String> channels = cl.getChannels(typ);
		for (InjectedErrorsType t : btype) {
			dtc.setInjectedError(t);
			try {
				dtc.createTable(cl.getTt(typ), channels);
				log.error("Error for " + t + " did not return correctly");
				Assert.fail();
			} catch (ResourceException e) {
				log.debug("Caught exception for " + t, e);
				Assert.assertEquals(e.getStatus().getCode(), t.ExpectedCode());
			}
		}
	}

	@Test(dependsOnMethods = { "testCreateTables" })
	public void testUpdateTables() {

		DatasetLoaderI dl = new RestletLoader(root, "HybridMasonry1", false);
		dl.createTables();

		ChannelLists cl = new ChannelLists();
		DataTableBadClient dtc = new DataTableBadClient(root,
				"HybridMasonry1");

		ChannelListType typ = ChannelListType.OM;

		List<String> channels = cl.getChannels(typ);

		int columns = channels.size();
		double[][] dataD = DataGenerator.initData(20, columns,
				0.02);

		InjectedErrorsType[] btype = { InjectedErrorsType.BadTable,
				InjectedErrorsType.BadRate, InjectedErrorsType.NoTable,
				InjectedErrorsType.NoRate, InjectedErrorsType.BadExperiment,
				InjectedErrorsType.ErrExperiment, InjectedErrorsType.NoExperiment };
		for (InjectedErrorsType t : btype) {
			dtc.setInjectedError(t);
			try {
				dtc.addData(cl.getTt(typ), RateType.CONT, new DoubleMatrix(
						dataD));
				log.error("Error for " + t + " did not return correctly");
				Assert.fail();
			} catch (ResourceException e) {
				log.debug("Caught exception for " + t, e);
				Assert.assertEquals(e.getStatus().getCode(), t.ExpectedCode());
			}
			dataD = DataGenerator.initData(20, columns, 0.02);
			try {
				dtc.addData(cl.getTt(typ), RateType.STEP, new DoubleMatrix(
						dataD));
				log.error("Error for " + t + " did not return correctly");
				Assert.fail();
			} catch (ResourceException e) {
				log.debug("Caught exception for " + t, e);
				Assert.assertEquals(e.getStatus().getCode(), t.ExpectedCode());
			}
		}
	}

	@Test
	public void testPutQueries() {

		ChannelLists cl = new ChannelLists();

		DataQueryBadClient dqc = new DataQueryBadClient(root,
				"HybridMasonry1");

		ChannelListType typ = ChannelListType.Query1;

		List<String> channels = cl.getChannels(typ);
		InjectedErrorsType[] btype = { InjectedErrorsType.NoQuery,
				InjectedErrorsType.BadExperiment, InjectedErrorsType.ErrExperiment,
				InjectedErrorsType.NoExperiment };
		for (InjectedErrorsType t : btype) {
			dqc.setInjectedError(t);
			try {
				dqc.setQuery(typ.name(), channels);
				log.error("Error for " + t + " did not return correctly");
				Assert.fail();
			} catch (ResourceException e) {
				log.debug("Caught exception for " + t, e);
				Assert.assertEquals(e.getStatus().getCode(),
						t.ExpectedCode());
			}
		}
	}

	@Test
	public void testGetQueries() {

		DatasetLoaderI dl = new RestletLoader(root, "HybridMasonry1", false);
		dl.createTables();
		dl.uploadData();
		dl.createQueries();
		
		ChannelLists cl = new ChannelLists();

		DataQueryClient clean_dqc = new DataQueryClient(root,
				"HybridMasonry1");

		for (ChannelListType typ : ChannelListType.values()) {
			if (typ.equals(ChannelListType.OM)
					|| typ.equals(ChannelListType.DAQ)) {
				continue;
			}
			List<String> channels = cl.getChannels(typ);
			clean_dqc.setQuery(typ.name(), channels);
		}

		DataQueryBadClient dqc = new DataQueryBadClient(root,
				"HybridMasonry1");

		ChannelListType typ = ChannelListType.Query1;
		InjectedErrorsType[] btype = { InjectedErrorsType.BadQuery,
				InjectedErrorsType.BadRate, InjectedErrorsType.NoQuery,
				InjectedErrorsType.NoRate, InjectedErrorsType.BadStart,
				InjectedErrorsType.NoStart, InjectedErrorsType.BadStop,
				InjectedErrorsType.NoStop, InjectedErrorsType.BadExperiment,
				InjectedErrorsType.ErrExperiment, InjectedErrorsType.NoExperiment };
		for (InjectedErrorsType t : btype) {
			dqc.setInjectedError(t);
			try {
				dqc.getData(typ.name(), 224.23, 322.45);
				Assert.fail();
			} catch (ResourceException e) {
				log.debug("Caught exception for " + t, e);
				Assert.assertEquals(e.getStatus().getCode(), t.ExpectedCode());
			}
			try {
				dqc.getData(typ.name(), new StepNumber(1.0, 22.0, 3.0),
						new StepNumber(3.0, 22.0, 1.0));
				Assert.fail();
			} catch (ResourceException e) {
				log.debug("Caught exception for " + t, e);
				Assert.assertEquals(e.getStatus().getCode(), t.ExpectedCode());
			}
		}
	}

}
