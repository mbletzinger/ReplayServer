package org.nees.mustsim.replay.test.server;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import java.util.Map;

import org.junit.Test;
import org.nees.mustsim.replay.channels.ChannelNameRegistry;
import org.nees.mustsim.replay.conversions.ChannelList2Representation;
import org.nees.mustsim.replay.data.TableType;
import org.nees.mustsim.replay.restlet.DataQueryServerResource;
import org.nees.mustsim.replay.restlet.DataTableServerResource;
import org.nees.mustsim.replay.test.data.TestDataQuery;
import org.nees.mustsim.replay.test.data.TestDataUpdates;
import org.nees.mustsim.replay.test.utils.ChannelLists;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;

public class ReplayTestServerTest {

	@Test
	public void testResourcesResource() {
		ChannelNameRegistry cnr = new ChannelNameRegistry();
		TestDataUpdates tdu = new TestDataUpdates(cnr);
		TestDataQuery tdq = new TestDataQuery(cnr);
		DataTableServerResource dtsR = new DataTableServerResource(tdu);
		DataQueryServerResource dqsR = new DataQueryServerResource(tdq);
		ChannelLists cl = new ChannelLists();
		ChannelList2Representation cl2rep = new ChannelList2Representation(
				cl.getChannels(TableType.OM));
		Request req = new Request(Method.PUT,
				"/test/data/experiment/HybridMasonry1/table/OM/rate/CONT",
				cl2rep.getRep());
		Map<String,Object> attr = new HashMap<String, Object>();
		attr.put("table", new String("OM"));
		attr.put("rate", new String("CONT"));
		req.setAttributes(attr);
		Response resp = new Response(req);
		dtsR.init(new Context(), req, resp);
		dtsR.handle();
		assertTrue(resp.getStatus().isSuccess());
	}

	@Test
	public void testComponent() {

//		// Instantiate our Restlet component
//		ReplayTestServerComponent component = new ReplayTestServerComponent();
//
//		// Prepare a mock HTTP call
//		Request request = new Request();
//		request.setMethod(Method.GET);
//		request.setResourceRef("http://cee-neesmrit3.cee.illinois.edu/test/data/experiment/Wall7/table/DAQ/rate/CONT");
//		request.setHostRef("http://cee-neesmrit3.cee.illinois.edu");
//		Response response = new Response(request);
//		response.getServerInfo().setAddress("127.0.0.1");
//		response.getServerInfo().setPort(8111);
//		component.handle(request, response);
//
//		// Test if response was successful
//		assertTrue(response.getStatus().isSuccess());
	}

}
