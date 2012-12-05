package org.nees.mustsim.replay.test.server;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Preference;
import org.restlet.data.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReplayTestServerTest {
	private final Logger log = LoggerFactory
			.getLogger(ReplayTestServerTest.class);

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

		String hostname = "http://localhost:8111";
		cxt.getParameters().add("hostname", hostname);

		// Prepare a mock HTTP call
		ChannelNameRegistry expectedCnr = new ChannelNameRegistry();

		ChannelLists cl = new ChannelLists();
		List<String> channels = cl.getChannels(ChannelListType.OM);
		ChannelList2Representation cl2rep = new ChannelList2Representation(
				channels);
		for (String c : channels) {
			expectedCnr.addChannel(TableType.OM, c);
		}
		Request req = new Request(Method.PUT, hostname + "/test/data/experiment/HybridMasonry1/table/OM", cl2rep.getRep());
		log.info("Request \"" + req + "\"");
		
		Response resp = new Response(req);

		app.handle(req, resp);
		log.info(resp.toString());
		// Test if response was successful
		assertTrue(resp.getStatus().isSuccess());

	
		channels = cl.getChannels(ChannelListType.DAQ);
		cl2rep = new ChannelList2Representation(channels);
		for (String c : channels) {
			expectedCnr.addChannel(TableType.DAQ, c);
		}
		req = new Request(Method.PUT, hostname + 
				"/test/data/experiment/HybridMasonry1/table/DAQ",
				cl2rep.getRep());
		resp = new Response(req);
		log.info("Request \"" + req + "\"");
		
		resp = new Response(req);

		app.handle(req, resp);
		log.info(resp.toString());
		// Test if response was successful
		assertTrue(resp.getStatus().isSuccess());

		channels = cl.getChannels(ChannelListType.Query1);
		cl2rep = new ChannelList2Representation(channels);
		req = new Request(Method.PUT, hostname + 
				"/test1/data/experiment/HybridMasonry1/query/StrainGage/rate/CONT/start/222.34",
				cl2rep.getRep());
		Preference<MediaType> pref = new Preference<MediaType>();
		pref.setMetadata(new MediaType("txt"));
		List<Preference<MediaType>> prefs = new ArrayList<Preference<MediaType>>();
		prefs.add(pref);
		req.getClientInfo().setAcceptedMediaTypes(prefs);
		resp = new Response(req);
		log.info("Request \"" + req.getEntityAsText() + "\"");
		
		resp = new Response(req);

		app.handle(req, resp);
		log.info(resp.toString());
		// Test if response was successful
		assertTrue(resp.getStatus().isSuccess());

		channels = cl.getChannels(ChannelListType.Query2);
		cl2rep = new ChannelList2Representation(channels);
		req = new Request(Method.PUT, hostname + 
				"/test/data/experiment/HybridMasonry1/query/query2",
				cl2rep.getRep());
		resp = new Response(req);
		log.info("Request \"" + req + "\"");
		
		resp = new Response(req);

		app.handle(req, resp);
		log.info(resp.toString());
		// Test if response was successful
		assertTrue(resp.getStatus().isSuccess());

	}

	@Test
	public void testComponent() {

		// Instantiate our Restlet component
		ReplayTestServerComponent component = new ReplayTestServerComponent(8111);

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
