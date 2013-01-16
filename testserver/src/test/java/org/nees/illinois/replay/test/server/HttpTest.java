package org.nees.illinois.replay.test.server;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.nees.illinois.replay.conversions.InputStream2DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.restlet.ReplayServerComponent;
import org.nees.illinois.replay.test.server.guice.LocalHttpTestModule;
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

	@BeforeClass
	public void setup() {
		// Instantiate our Restlet component
		injector = Guice.createInjector(new LocalHttpTestModule());
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
			execute(httpput, uriString);
		}

		ChannelListType typ = ChannelListType.OM;
		List<String> channels = cl.getChannels(typ);

		ChannelList2HttpEntity cl2ent = new ChannelList2HttpEntity(channels);
		HttpPut httpput = new HttpPut(hostname);
		httpput.setEntity(cl2ent.getEnt());
		String uriString = baseString + typ;
		uriString = uriString.replace("HybridMasonry1", "HybridMasonryERR");
		executeFail(httpput, uriString);
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
			execute(httppost, uriString);

			dataD = DataGenerator.initData(RateType.STEP, 20, columns, 0.02);
			httppost = new HttpPost(hostname);
			dm2ent = new DoubleMatrix2HttpEntity(dataD);
			httppost.setEntity(dm2ent.getEnt());
			uriString = baseString + typ + "/rate/STEP";
			execute(httppost, uriString);
		}
		ChannelListType typ = ChannelListType.OM;
		List<String> channels = cl.getChannels(typ);
		int columns = channels.size();
		double[][] dataD = DataGenerator.initData(RateType.CONT, 20, columns,
				0.02);
		HttpPost httppost = new HttpPost(hostname);
		DoubleMatrix2HttpEntity dm2ent = new DoubleMatrix2HttpEntity(dataD);
		httppost.setEntity(dm2ent.getEnt());
		String uriString = baseString + typ + "/rate/CONT";
		uriString = uriString.replace("HybridMasonry1", "HybridMasonryERR");
		executeFail(httppost, uriString);
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
			execute(httpput, uriString);
		}
		ChannelListType typ = ChannelListType.Query1;
		List<String> channels = cl.getChannels(typ);
		ChannelList2HttpEntity cl2ent = new ChannelList2HttpEntity(channels);
		HttpPut httpput = new HttpPut(hostname);
		httpput.setEntity(cl2ent.getEnt());
		String uriString = baseString + typ;
		uriString = uriString.replace("HybridMasonry1", "HybridMasonryERR");
		executeFail(httpput, uriString);
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
			HttpEntity ent = execute(httpget, uriString);
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

		ChannelListType typ = ChannelListType.Query1;
		HttpGet httpget = new HttpGet(hostname);
		String uriString = baseString + typ + "/rate/CONT/start/222.34";
		HttpEntity ent = execute(httpget, uriString);
		InputStream2DoubleMatrix in2dm = null;
		try {
			in2dm = new InputStream2DoubleMatrix(ent.getContent());
		} catch (Exception e) {
			log.error("Extracting entity from query \"" + uriString
					+ "\" failed because ", e);
			Assert.fail();
		}
		log.info("\"" + uriString + "\" retrieved " + in2dm.getMatrix());
		uriString = uriString.replace("HybridMasonry1", "HybridMasonryERR");
		executeFail(httpget, uriString);
	}

	private HttpEntity executeFail(HttpRequestBase request, String uriString) {
		log.debug("executing " + uriString);
		HttpResponse response = sendRequest(request, uriString);
		log.debug("done with " + uriString);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 500);
		HttpEntity entity = response.getEntity();
		return entity;
	}

	private HttpEntity execute(HttpRequestBase request, String uriString) {
		log.debug("executing " + uriString);
		HttpResponse response = sendRequest(request, uriString);
		log.debug("done with  " + uriString);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		HttpEntity entity = response.getEntity();

		Assert.assertNotNull(entity);
		return entity;

	}

	private HttpResponse sendRequest(HttpRequestBase request, String uriString) {
		HttpResponse response = null;
		HttpClient httpclient = new DefaultHttpClient();
		try {
			request.setURI(new URI(uriString));
		} catch (URISyntaxException e1) {
			log.error("URI : " + uriString + " could not be parsed", e1);
			Assert.fail();
		}

		try {
			response = httpclient.execute(request);
		} catch (ClientProtocolException e) {
			log.error("Protocol failure for " + uriString, e);
			Assert.fail();
		} catch (IOException e) {
			log.error("IO failure for " + uriString, e);
			Assert.fail();
		}
		log.debug("Request " + request + " cause Response " + response);
		return response;
	}
}