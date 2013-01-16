package org.nees.illinois.replay.test.server;

import static org.testng.AssertJUnit.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.conversions.ChannelList2Representation;
import org.nees.illinois.replay.conversions.DoubleMatrix2Representation;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.registries.ChannelNameRegistry;
import org.nees.illinois.replay.registries.ExperimentModule;
import org.nees.illinois.replay.registries.ExperimentRegistries;
import org.nees.illinois.replay.restlet.DataQueryServerResource;
import org.nees.illinois.replay.restlet.DataTableServerResource;
import org.nees.illinois.replay.test.data.TestDataQuery;
import org.nees.illinois.replay.test.data.TestDataUpdates;
import org.nees.illinois.replay.test.server.guice.ResourcesTestModule;
import org.nees.illinois.replay.test.utils.ChannelLists;
import org.nees.illinois.replay.test.utils.ChannelLists.ChannelListType;
import org.nees.illinois.replay.test.utils.DataGenerator;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
@Test(groups = { "resources-test" })
public class ResourcesTest {
//	private final Logger log = LoggerFactory
//			.getLogger(ResourcesTest.class);
	private Provider<TestDataUpdates> tdu;
	private Provider<TestDataQuery> tdq;
	private DataTableServerResource dtsR;
	private DataQueryServerResource dqsR;
	private ExperimentModule guiceMod;

	@BeforeTest
	public void setup() {
		Injector injector = Guice.createInjector(new ResourcesTestModule());
		tdu = injector.getProvider(TestDataUpdates.class);
		tdq = injector.getProvider(TestDataQuery.class);
		dtsR = injector.getInstance(DataTableServerResource.class);
		dqsR = injector.getInstance(DataQueryServerResource.class);
		guiceMod = injector.getInstance(ExperimentModule.class);
		
	}
	@Test
	public void testResourcesOm() {
		ChannelNameRegistry expectedCnr = new ChannelNameRegistry();
		Context cxt = new Context();
		cxt.getAttributes().put("updatesI", tdu);
		cxt.getAttributes().put("guiceMod", guiceMod);

		ChannelLists cl = new ChannelLists();
		List<String> channels = cl.getChannels(ChannelListType.OM);
		ChannelList2Representation cl2rep = new ChannelList2Representation(
				channels);
		for (String c : channels) {
			expectedCnr.addChannel(TableType.OM, c);
		}
		Request req = new Request(Method.PUT,
				"/test/data/experiment/HybridMasonry1/table/OM/",
				cl2rep.getRep());
		Map<String, Object> attr = new HashMap<String, Object>();
		attr.put("table", new String("OM"));
		attr.put("experiment", new String("HybridMasonry1"));
		req.setAttributes(attr);
		Response resp = new Response(req);
		dtsR.init(cxt, req, resp);
		dtsR.handle();
		assertTrue(resp.getStatus().isSuccess());
		cxt = dtsR.getContext();
		ExperimentRegistries er = (ExperimentRegistries) cxt.getAttributes().get("HybridMasonry1.registries");
		Assert.assertEquals(er.getLookups().getCnr().toString(), expectedCnr.toString());

		int columns = channels.size();
		double[][] dataD = DataGenerator.initData(RateType.CONT, 20, columns,
				0.02);
		DoubleMatrix2Representation dm2rep = new DoubleMatrix2Representation(
				dataD);
		req = new Request(Method.POST,
				"/test/data/experiment/HybridMasonry1/table/OM/rate/CONT",
				dm2rep.getRep());
		attr = new HashMap<String, Object>();
		attr.put("table", new String("OM"));
		attr.put("rate", new String("CONT"));
		attr.put("experiment", new String("HybridMasonry1"));
		req.setAttributes(attr);
		resp = new Response(req);
		dtsR.init(cxt, req, resp);
		dtsR.handle();
		assertTrue(resp.getStatus().isSuccess());
		DataGenerator.compareData(dataD, ((TestDataUpdates)dtsR.getUpdates()).getData());

		dataD = DataGenerator.initData(RateType.STEP, 15, columns, 0.02);
		dm2rep = new DoubleMatrix2Representation(dataD);
		req = new Request(Method.POST,
				"/test/data/experiment/HybridMasonry1/table/OM/rate/STEP",
				dm2rep.getRep());
		attr = new HashMap<String, Object>();
		attr.put("table", new String("OM"));
		attr.put("rate", new String("STEP"));
		attr.put("experiment", new String("HybridMasonry1"));
		req.setAttributes(attr);
		resp = new Response(req);
		dtsR.init(cxt, req, resp);
		dtsR.handle();
		assertTrue(resp.getStatus().isSuccess());
		DataGenerator.compareData(dataD, ((TestDataUpdates)dtsR.getUpdates()).getData());
	}

	@Test
	public void testResourcesDaq() {
		ChannelNameRegistry expectedCnr = new ChannelNameRegistry();
		Context cxt = new Context();
		cxt.getAttributes().put("updatesI", tdu);
		cxt.getAttributes().put("guiceMod", guiceMod);

		ChannelLists cl = new ChannelLists();
		List<String> channels = cl.getChannels(ChannelListType.DAQ);
		ChannelList2Representation cl2rep = new ChannelList2Representation(
				channels);
		for (String c : channels) {
			expectedCnr.addChannel(TableType.DAQ, c);
		}
		Request req = new Request(Method.PUT,
				"/test/data/experiment/HybridMasonry1/table/DAQ/",
				cl2rep.getRep());
		Map<String, Object> attr = new HashMap<String, Object>();
		attr.put("table", new String("DAQ"));
		attr.put("experiment", new String("HybridMasonry1"));
		req.setAttributes(attr);
		Response resp = new Response(req);
		dtsR.init(cxt, req, resp);
		dtsR.handle();
		assertTrue(resp.getStatus().isSuccess());
		cxt = dtsR.getContext();
		ExperimentRegistries er = (ExperimentRegistries) cxt.getAttributes().get("HybridMasonry1.registries");
		Assert.assertEquals(er.getLookups().getCnr().toString(), expectedCnr.toString());

		int columns = channels.size();
		double[][] dataD = DataGenerator.initData(RateType.CONT, 20, columns,
				0.02);
		DoubleMatrix2Representation dm2rep = new DoubleMatrix2Representation(
				dataD);
		req = new Request(Method.POST,
				"/test/data/experiment/HybridMasonry1/table/DAQ/rate/CONT",
				dm2rep.getRep());
		attr = new HashMap<String, Object>();
		attr.put("table", new String("DAQ"));
		attr.put("rate", new String("CONT"));
		attr.put("experiment", new String("HybridMasonry1"));
		req.setAttributes(attr);
		resp = new Response(req);
		dtsR.init(cxt, req, resp);
		dtsR.handle();
		assertTrue(resp.getStatus().isSuccess());
		DataGenerator.compareData(dataD, ((TestDataUpdates)dtsR.getUpdates()).getData());

		dataD = DataGenerator.initData(RateType.STEP, 15, columns, 0.02);
		dm2rep = new DoubleMatrix2Representation(dataD);
		req = new Request(Method.POST,
				"/test/data/experiment/HybridMasonry1/table/DAQ/rate/STEP",
				dm2rep.getRep());
		attr = new HashMap<String, Object>();
		attr.put("table", new String("DAQ"));
		attr.put("rate", new String("STEP"));
		attr.put("experiment", new String("HybridMasonry1"));
		req.setAttributes(attr);
		resp = new Response(req);
		dtsR.init(cxt, req, resp);
		dtsR.handle();
		assertTrue(resp.getStatus().isSuccess());
		DataGenerator.compareData(dataD, ((TestDataUpdates)dtsR.getUpdates()).getData());
	}

	@Test
	public void testQueryResource() {
		ChannelNameRegistry expectedCnr = new ChannelNameRegistry();
		Context cxt = new Context();
		cxt.getAttributes().put("updatesI", tdu);
		cxt.getAttributes().put("queryI", tdq);
		cxt.getAttributes().put("guiceMod", guiceMod);

		ChannelLists cl = new ChannelLists();
		List<String> channels = cl.getChannels(ChannelListType.OM);
		ChannelList2Representation cl2rep = new ChannelList2Representation(
				channels);
		for (String c : channels) {
			expectedCnr.addChannel(TableType.OM, c);
		}
		Request req = new Request(Method.PUT,
				"/test/data/experiment/HybridMasonry1/table/OM/",
				cl2rep.getRep());
		Map<String, Object> attr = new HashMap<String, Object>();
		attr.put("table", new String("OM"));
		attr.put("experiment", new String("HybridMasonry1"));
		req.setAttributes(attr);
		Response resp = new Response(req);
		dtsR.init(cxt, req, resp);
		dtsR.handle();
		cxt = dtsR.getContext();
		ExperimentRegistries er = (ExperimentRegistries) cxt.getAttributes().get("HybridMasonry1.registries");
		Assert.assertEquals(er.getLookups().getCnr().toString(), expectedCnr.toString());

		channels = cl.getChannels(ChannelListType.DAQ);
		cl2rep = new ChannelList2Representation(channels);
		for (String c : channels) {
			expectedCnr.addChannel(TableType.DAQ, c);
		}
		req = new Request(Method.PUT,
				"/test/data/experiment/HybridMasonry1/table/DAQ/",
				cl2rep.getRep());
		attr = new HashMap<String, Object>();
		attr.put("table", new String("DAQ"));
		attr.put("experiment", new String("HybridMasonry1"));
		req.setAttributes(attr);
		resp = new Response(req);
		dtsR.init(cxt, req, resp);
		dtsR.handle();
		cxt = dtsR.getContext();
		er = (ExperimentRegistries) cxt.getAttributes().get("HybridMasonry1.registries");
		Assert.assertEquals( er.getLookups().getCnr().toString(), expectedCnr.toString());

		channels = cl.getChannels(ChannelListType.Query1);
		cl2rep = new ChannelList2Representation(channels);
		req = new Request(Method.PUT,
				"/test/data/experiment/HybridMasonry1/query/query1",
				cl2rep.getRep());
		attr = new HashMap<String, Object>();
		attr.put("query", new String("Test Query Number 1"));
		attr.put("experiment", new String("HybridMasonry1"));
		req.setAttributes(attr);
		resp = new Response(req);
		dqsR.init(cxt, req, resp);
		dqsR.handle();
		assertTrue(resp.getStatus().isSuccess());
		cxt = dtsR.getContext();
		 er = (ExperimentRegistries) cxt.getAttributes().get("HybridMasonry1.registries");
		Assert.assertEquals( er.getLookups().getCnr().toString(), expectedCnr.toString());
		Assert.assertNotNull(er.getQueries().getQuery("Test Query Number 1", RateType.STEP));
		channels = cl.getChannels(ChannelListType.Query2);
		cl2rep = new ChannelList2Representation(channels);
		req = new Request(Method.PUT,
				"/test/data/experiment/HybridMasonry1/query/query2",
				cl2rep.getRep());
		attr = new HashMap<String, Object>();
		attr.put("query", new String("Test Query Number 2"));
		attr.put("experiment", new String("HybridMasonry1"));
		req.setAttributes(attr);
		resp = new Response(req);
		dqsR.init(cxt, req, resp);
		dqsR.handle();
		assertTrue(resp.getStatus().isSuccess());
		Assert.assertNotNull(er.getQueries().getQuery("Test Query Number 2", RateType.CONT));

	}
}
