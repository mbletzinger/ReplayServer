package org.nees.illinois.replay.test.server;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.nees.illinois.replay.conversions.InputStream2DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.restlet.ReplayServerComponent;
import org.nees.illinois.replay.test.server.guice.LocalRestletTestModule;
import org.nees.illinois.replay.test.server.utils.HttpClientFunctions;
import org.nees.illinois.replay.test.utils.ChannelLists;
import org.nees.illinois.replay.test.utils.ChannelLists.ChannelListType;
import org.nees.illinois.replay.test.utils.DataGenerator;
import org.nees.mustsim.replay.test.server.http.ChannelList2HttpEntity;
import org.nees.mustsim.replay.test.server.http.DoubleMatrix2HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

@Test(groups = { "http-test" })
public class HttpTest {
	private final Logger log = LoggerFactory.getLogger(HttpTest.class);
	private ReplayServerComponent component;
	private String hostname;
	private Injector injector;
	private HttpClientFunctions http = new HttpClientFunctions();

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

		// Prepare a mock HTTP call
		ChannelLists cl = new ChannelLists();
		String baseString = hostname
				+ "/test/data/experiment/HybridMasonry1/table/";

		for (ChannelListType typ : ChannelListType.values()) {
			if (typ.equals(ChannelListType.Query1)) {
				break;
			}
			List<String> channels = cl.getChannels(typ);

			ChannelList2HttpEntity cl2ent = new ChannelList2HttpEntity(channels);
			HttpPut httpput = new HttpPut(hostname);
			httpput.setEntity(cl2ent.getEnt());
			String uriString = baseString + typ;
			http.execute(httpput, uriString);
		}

	}

	@Test(dependsOnMethods = { "testCreateTables" })
	public void testUpdateTables() {

		ChannelLists cl = new ChannelLists();
		String baseString = hostname
				+ "/test/data/experiment/HybridMasonry1/table/";

		for (ChannelListType typ : ChannelListType.values()) {
			if (typ.equals(ChannelListType.Query1)) {
				break;
			}

			List<String> channels = cl.getChannels(typ);

			int columns = channels.size();
			double[][] dataD = DataGenerator.initData(RateType.CONT, 20,
					columns, 0.02);
			HttpPost httppost = new HttpPost(hostname);
			DoubleMatrix2HttpEntity dm2ent = new DoubleMatrix2HttpEntity(dataD);
			httppost.setEntity(dm2ent.getEnt());
			String uriString = baseString + typ + "/rate/CONT";
			http.execute(httppost, uriString);

			dataD = DataGenerator.initData(RateType.STEP, 20, columns, 0.02);
			httppost = new HttpPost(hostname);
			dm2ent = new DoubleMatrix2HttpEntity(dataD);
			httppost.setEntity(dm2ent.getEnt());
			uriString = baseString + typ + "/rate/STEP";
			http.execute(httppost, uriString);
		}
	}

	@Test(dependsOnMethods = { "testUpdateTables" })
	public void testPutQueries() {

		ChannelLists cl = new ChannelLists();
		String baseString = hostname
				+ "/test/data/experiment/HybridMasonry1/query/";

		for (ChannelListType typ : ChannelListType.values()) {
			if (typ.equals(ChannelListType.OM)
					|| typ.equals(ChannelListType.DAQ)) {
				continue;
			}

			List<String> channels = cl.getChannels(typ);
			ChannelList2HttpEntity cl2ent = new ChannelList2HttpEntity(channels);
			HttpPut httpput = new HttpPut(hostname);
			httpput.setEntity(cl2ent.getEnt());
			String uriString = baseString + typ;
			http.execute(httpput, uriString);
		}
	}

	@Test(dependsOnMethods = { "testPutQueries" })
	public void testGetQueries() {

		String baseString = hostname
				+ "/test/data/experiment/HybridMasonry1/query/";

		for (ChannelListType typ : ChannelListType.values()) {
			if (typ.equals(ChannelListType.OM)
					|| typ.equals(ChannelListType.DAQ)) {
				continue;
			}

			HttpGet httpget = new HttpGet(hostname);
			String uriString = baseString + typ + "/rate/CONT/start/222.34";
			HttpEntity ent = http.execute(httpget, uriString);
			InputStream2DoubleMatrix in2dm = null;
			try {
				in2dm = new InputStream2DoubleMatrix(ent.getContent());
			} catch (Exception e) {
				log.error("Extracting entity from query \"" + uriString
						+ "\" failed because ", e);
				Assert.fail();
			}
			log.info("\"" + uriString + "\" retrieved " + in2dm.getMatrix());
		}

	}

}
