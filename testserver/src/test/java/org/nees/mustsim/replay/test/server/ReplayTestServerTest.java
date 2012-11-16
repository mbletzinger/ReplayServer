package org.nees.mustsim.replay.test.server;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.nees.mustsim.replay.channels.ChannelNameRegistry;
import org.nees.mustsim.replay.conversions.ChannelList2Representation;
import org.nees.mustsim.replay.conversions.DoubleMatrix2Representation;
import org.nees.mustsim.replay.data.RateType;
import org.nees.mustsim.replay.data.TableType;
import org.nees.mustsim.replay.restlet.DataQueryServerResource;
import org.nees.mustsim.replay.restlet.DataTableServerResource;
import org.nees.mustsim.replay.test.ReplayTestServerApplication;
import org.nees.mustsim.replay.test.data.TestDataQuery;
import org.nees.mustsim.replay.test.data.TestDataUpdates;
import org.nees.mustsim.replay.test.server.utils.ReplayTestServerComponent;
import org.nees.mustsim.replay.test.utils.ChannelLists;
import org.nees.mustsim.replay.test.utils.ChannelLists.ChannelListType;
import org.nees.mustsim.replay.test.utils.DataGenerator;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReplayTestServerTest {
	private final Logger log = LoggerFactory
			.getLogger(ReplayTestServerTest.class);

	@Test
	public void testResourcesOm() {
		ChannelNameRegistry actualCnr = new ChannelNameRegistry();
		ChannelNameRegistry expectedCnr = new ChannelNameRegistry();
		TestDataUpdates tdu = new TestDataUpdates(actualCnr);
		DataTableServerResource dtsR = new DataTableServerResource();
		Context cxt = new Context();
		cxt.getAttributes().put("updatesI", tdu);

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
		req.setAttributes(attr);
		Response resp = new Response(req);
		dtsR.init(cxt, req, resp);
		dtsR.handle();
		assertTrue(resp.getStatus().isSuccess());
		Assert.assertEquals(expectedCnr.toString(), actualCnr.toString());

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
		req.setAttributes(attr);
		resp = new Response(req);
		dtsR.init(cxt, req, resp);
		dtsR.handle();
		assertTrue(resp.getStatus().isSuccess());
		DataGenerator.compareData(dataD, tdu.getData());

		dataD = DataGenerator.initData(RateType.STEP, 15, columns, 0.02);
		dm2rep = new DoubleMatrix2Representation(dataD);
		req = new Request(Method.POST,
				"/test/data/experiment/HybridMasonry1/table/OM/rate/STEP",
				dm2rep.getRep());
		attr = new HashMap<String, Object>();
		attr.put("table", new String("OM"));
		attr.put("rate", new String("STEP"));
		req.setAttributes(attr);
		resp = new Response(req);
		dtsR.init(cxt, req, resp);
		dtsR.handle();
		assertTrue(resp.getStatus().isSuccess());
		DataGenerator.compareData(dataD, tdu.getData());
	}

	@Test
	public void testResourcesDaq() {
		ChannelNameRegistry actualCnr = new ChannelNameRegistry();
		ChannelNameRegistry expectedCnr = new ChannelNameRegistry();
		TestDataUpdates tdu = new TestDataUpdates(actualCnr);
		DataTableServerResource dtsR = new DataTableServerResource();
		Context cxt = new Context();
		cxt.getAttributes().put("updatesI", tdu);

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
		req.setAttributes(attr);
		Response resp = new Response(req);
		dtsR.init(cxt, req, resp);
		dtsR.handle();
		assertTrue(resp.getStatus().isSuccess());
		Assert.assertEquals(expectedCnr.toString(), actualCnr.toString());

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
		req.setAttributes(attr);
		resp = new Response(req);
		dtsR.init(cxt, req, resp);
		dtsR.handle();
		assertTrue(resp.getStatus().isSuccess());
		DataGenerator.compareData(dataD, tdu.getData());

		dataD = DataGenerator.initData(RateType.STEP, 15, columns, 0.02);
		dm2rep = new DoubleMatrix2Representation(dataD);
		req = new Request(Method.POST,
				"/test/data/experiment/HybridMasonry1/table/DAQ/rate/STEP",
				dm2rep.getRep());
		attr = new HashMap<String, Object>();
		attr.put("table", new String("DAQ"));
		attr.put("rate", new String("STEP"));
		req.setAttributes(attr);
		resp = new Response(req);
		dtsR.init(cxt, req, resp);
		dtsR.handle();
		assertTrue(resp.getStatus().isSuccess());
		DataGenerator.compareData(dataD, tdu.getData());
	}

	@Test
	public void testQueryResource() {
		ChannelNameRegistry actualCnr = new ChannelNameRegistry();
		ChannelNameRegistry expectedCnr = new ChannelNameRegistry();
		TestDataUpdates tdu = new TestDataUpdates(actualCnr);
		TestDataQuery tdq = new TestDataQuery(actualCnr);
		DataTableServerResource dtsR = new DataTableServerResource();
		DataQueryServerResource dqsR = new DataQueryServerResource();
		Context cxt = new Context();
		cxt.getAttributes().put("updatesI", tdu);
		cxt.getAttributes().put("queryI", tdq);

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
		req.setAttributes(attr);
		Response resp = new Response(req);
		dtsR.init(cxt, req, resp);
		dtsR.handle();
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
		req.setAttributes(attr);
		resp = new Response(req);
		dtsR.init(cxt, req, resp);
		dtsR.handle();

		channels = cl.getChannels(ChannelListType.Query1);
		cl2rep = new ChannelList2Representation(channels);
		req = new Request(Method.PUT,
				"/test/data/experiment/HybridMasonry1/query/query1",
				cl2rep.getRep());
		attr = new HashMap<String, Object>();
		attr.put("query", new String("Test Query Number 1"));
		req.setAttributes(attr);
		resp = new Response(req);
		dqsR.init(cxt, req, resp);
		dqsR.handle();
		assertTrue(resp.getStatus().isSuccess());
		Assert.assertNotNull(tdq.getContQr().getQuery("Test Query Number 1"));

		channels = cl.getChannels(ChannelListType.Query2);
		cl2rep = new ChannelList2Representation(channels);
		req = new Request(Method.PUT,
				"/test/data/experiment/HybridMasonry1/query/query2",
				cl2rep.getRep());
		attr = new HashMap<String, Object>();
		attr.put("query", new String("Test Query Number 2"));
		req.setAttributes(attr);
		resp = new Response(req);
		dqsR.init(cxt, req, resp);
		dqsR.handle();
		assertTrue(resp.getStatus().isSuccess());
		Assert.assertNotNull(tdq.getContQr().getQuery("Test Query Number 2"));

	}

	@Test
	public void testApplication() {

		ChannelNameRegistry cnr = new ChannelNameRegistry();
		TestDataUpdates tdu = new TestDataUpdates(cnr);
		TestDataQuery tdq = new TestDataQuery(cnr);
		Context cxt = new Context();
		cxt.getAttributes().put("updatesI", tdu);
		cxt.getAttributes().put("queryI", tdq);
		cxt.getParameters().set("tracing", "true");

		// Instantiate our Restlet component
		ReplayTestServerApplication app = new ReplayTestServerApplication();
		app.setContext(cxt);

		// Prepare a mock HTTP call
		ChannelNameRegistry expectedCnr = new ChannelNameRegistry();

		ChannelLists cl = new ChannelLists();
		List<String> channels = cl.getChannels(ChannelListType.OM);
		ChannelList2Representation cl2rep = new ChannelList2Representation(
				channels);
		for (String c : channels) {
			expectedCnr.addChannel(TableType.OM, c);
		}
		Reference ref = new Reference();
		ref.setPath("/test/data/experiment/HybridMasonry1/table/OM");
		ref.setBaseRef("http://localhost");
		Request req = new Request(Method.PUT, ref, cl2rep.getRep());
		Map<String, Object> attr = new HashMap<String, Object>();
		attr.put("table", new String("OM"));
		req.setAttributes(attr);
		req.setHostRef("http://localhost");
		Response response = new Response(req);
		response.getServerInfo().setAddress("127.0.0.1");
		response.getServerInfo().setPort(8111);

		app.handle(req, response);
		log.info(response.toString());
		// Test if response was successful
		assertTrue(response.getStatus().isSuccess());
	}

	@Test
	public void testComponent() {

		// Instantiate our Restlet component
		ReplayTestServerComponent component = new ReplayTestServerComponent();

		// Prepare a mock HTTP call
		ChannelNameRegistry expectedCnr = new ChannelNameRegistry();

		ChannelLists cl = new ChannelLists();
		List<String> channels = cl.getChannels(ChannelListType.OM);
		ChannelList2Representation cl2rep = new ChannelList2Representation(
				channels);
		for (String c : channels) {
			expectedCnr.addChannel(TableType.OM, c);
		}
		Reference ref = new Reference();
		ref.setPath("/test/data/experiment/HybridMasonry1/table/OM/");
		ref.setBaseRef("http://localhost");
		Request req = new Request(Method.PUT, ref, cl2rep.getRep());
		Map<String, Object> attr = new HashMap<String, Object>();
		attr.put("table", new String("OM"));
		req.setAttributes(attr);
		req.setHostRef("http://localhost");
		Response response = new Response(req);
		response.getServerInfo().setAddress("127.0.0.1");
		response.getServerInfo().setPort(8111);
		component.handle(req, response);
		log.info(response.toString());

		// Test if response was successful
		assertTrue(response.getStatus().isSuccess());
	}

}
