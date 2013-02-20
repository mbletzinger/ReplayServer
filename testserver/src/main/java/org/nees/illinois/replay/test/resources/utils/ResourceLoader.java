package org.nees.illinois.replay.test.resources.utils;

import static org.testng.AssertJUnit.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.conversions.ChannelList2Representation;
import org.nees.illinois.replay.conversions.DoubleMatrix2Representation;
import org.nees.illinois.replay.conversions.Representation2DoubleMatrix;
import org.nees.illinois.replay.data.DataQueryI;
import org.nees.illinois.replay.data.DataUpdateI;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.registries.ExperimentModule;
import org.nees.illinois.replay.registries.ExperimentRegistries;
import org.nees.illinois.replay.restlet.DataQueryServerResource;
import org.nees.illinois.replay.restlet.DataTableServerResource;
import org.nees.illinois.replay.test.data.TestDataUpdates;
import org.nees.illinois.replay.test.resources.utils.DatasetDirector.ExperimentNames;
import org.nees.illinois.replay.test.resources.utils.DatasetDirector.QueryTypes;
import org.nees.illinois.replay.test.resources.utils.DatasetDirector.TimeSpec;
import org.nees.illinois.replay.test.utils.ChannelLists;
import org.nees.illinois.replay.test.utils.ChannelLists.ChannelListType;
import org.nees.illinois.replay.test.utils.DataGenerator;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class ResourceLoader implements DatasetLoaderI {
	/**
	 * @return the cl
	 */
	public ChannelLists getCl() {
		return cl;
	}

	/**
	 * @return the uploadRows
	 */
	public int getUploadRows() {
		return uploadRows;
	}

	/**
	 * @return the dqsR
	 */
	public DataQueryServerResource getDqsR() {
		return dqsR;
	}

	/**
	 * @return the dtsR
	 */
	public DataTableServerResource getDtsR() {
		return dtsR;
	}

	private final String appRoot;
	
	private final ChannelLists cl = new ChannelLists();

	private final Context cxt;

	private final DatasetDirector dd = new DatasetDirector();
	private final DataQueryServerResource dqsR;
	private final DataTableServerResource dtsR;
	private ExperimentNames experiment;
	private boolean secondExperiment;
	private final int uploadRows = 20;
	private final Logger log = LoggerFactory.getLogger(ResourceLoader.class);

	public ResourceLoader(String appRoot, ExperimentNames experiment,
			AbstractModule module, boolean secondExperiment) {
		Injector injector = Guice.createInjector(module);
		Provider<DataUpdateI> tdu = injector.getProvider(DataUpdateI.class);
		Provider<DataQueryI> tdq = injector.getProvider(DataQueryI.class);
		dtsR = injector.getInstance(DataTableServerResource.class);
		dqsR = injector.getInstance(DataQueryServerResource.class);
		ExperimentModule guiceMod = injector
				.getInstance(ExperimentModule.class);
		cxt = new Context();
		cxt.getAttributes().put("updatesI", tdu);
		cxt.getAttributes().put("queryI", tdq);
		cxt.getAttributes().put("guiceMod", guiceMod);
		this.experiment = experiment;
		this.secondExperiment = secondExperiment;
		this.appRoot = appRoot;
	}

	public void checkChannels() {
		Context context = dqsR.getContext();
		ExperimentRegistries er = (ExperimentRegistries) context
				.getAttributes().get(experiment + ".registries");
		dd.checkExpectedCnr(experiment, er.getChnlNamesMgmt().getCnr());
	}

	public void checkDataset(ChannelListType typ) {
		List<String> channels = cl.getChannels(typ);
		int columns = channels.size();
		for (RateType rate : RateType.values()) {
			double[][] dataD = DataGenerator.initData(uploadRows,
					columns, 0.02);
			log.debug("For " + typ + " and " + rate);
		DataGenerator.compareData(dataD,
					((TestDataUpdates) dtsR.getUpdates()).getData());
		}
	}

	public void checkQuery(ChannelListType quy) {
		Context context = dqsR.getContext();
		ExperimentRegistries er = (ExperimentRegistries) context
				.getAttributes().get(experiment + ".registries");
		Assert.assertNotNull(er.getQueries()
				.getQuery(quy.name(), RateType.STEP));
	}

	public void checkTable(ChannelListType typ) {
		Context context = dtsR.getContext();
		ExperimentRegistries er = (ExperimentRegistries) context
				.getAttributes().get(experiment + ".registries");
		dd.checkExpectedCnr(experiment, er.getChnlNamesMgmt().getCnr());
	}

	@Override
	public void createQueries() {
		if (secondExperiment) {
			createQuery(ChannelListType.Query3);
			createQuery(ChannelListType.Query4);
		} else {
			createQuery(ChannelListType.Query1);
			createQuery(ChannelListType.Query2);
		}
	}

	public void createQuery(ChannelListType quy) {

		List<String> channels = cl.getChannels(quy);

		log.debug("Creating query for " + quy);
		ChannelList2Representation cl2rep = new ChannelList2Representation(
				channels);
		Request req = new Request(Method.PUT, appRoot + "/experiment/"
				+ experiment + "/query/" + quy, cl2rep.getRep());
		req.setAttributes(genAttributes(experiment.name(), quy.name(), null,
				null, null));
		Response resp = new Response(req);
		dqsR.init(cxt, req, resp);
		dqsR.handle();
		assertTrue(resp.getStatus().isSuccess());
	}

	public void createTable(ChannelListType typ) {
		List<String> channels = cl.getChannels(typ);
		log.debug("Creating table for " + typ);
		ChannelList2Representation cl2rep = new ChannelList2Representation(
				channels);
		dd.addExpectedCnr(experiment, cl.getTt(typ), channels);
		Request req = new Request(Method.PUT, appRoot + "/experiment/"
				+ experiment + "/table/" + cl.getTt(typ), cl2rep.getRep());
		req.setAttributes(genAttributes(experiment.name(),
				cl.getTt(typ).name(), null));
		Response resp = new Response(req);
		dtsR.init(cxt, req, resp);
		dtsR.handle();
		assertTrue(resp.getStatus().isSuccess());
	}

	@Override
	public void createTables() {

		if (secondExperiment) {
			createTable(ChannelListType.OM2);
			createTable(ChannelListType.DAQ2);
		} else {
			createTable(ChannelListType.OM);
			createTable(ChannelListType.DAQ);
		}
	}

	public DoubleMatrix doQuery(QueryTypes qt, ChannelListType quy) {
		RateType rate = dd.getRate(qt);
		log.debug("Executing query for " + qt + " and " + quy);
		Request req = new Request(Method.GET, appRoot + "/experiment/"
				+ experiment + "/rate/" + rate);
		TimeSpec tm = dd.getTimes(qt);
		req.setAttributes(genAttributes(experiment.name(),
				quy.name(), rate.name(), tm.getStart(), tm.getStop()));
		Response resp = new Response(req);
		dqsR.init(cxt, req, resp);
		dqsR.handle();
		assertTrue(resp.getStatus().isSuccess());
		Representation rep = resp.getEntity();
		Representation2DoubleMatrix rep2Dm = new Representation2DoubleMatrix(
				rep);
		return rep2Dm.getIn2dm().getMatrix();
	}

	private Map<String, Object> genAttributes(String experiment, String table,
			String rate) {
		Map<String, Object> result = new HashMap<String, Object>();

		if (experiment != null) {
			result.put("experiment", experiment);
		}
		if (table != null) {
			result.put("table", table);
		}
		if (rate != null) {
			result.put("rate", rate);
		}
		return result;
	}

	private Map<String, Object> genAttributes(String experiment, String query,
			String rate, Object start, Object stop) {
		Map<String, Object> result = new HashMap<String, Object>();

		if (experiment != null) {
			result.put("experiment", experiment);
		}
		if (query != null) {
			result.put("query", query);
		}
		if (rate != null) {
			result.put("rate", rate);
		}
		if (start != null) {
			result.put("start", start.toString());
		}
		if (stop != null) {
			result.put("stop", stop.toString());
		}
		return result;
	}

	/**
	 * @return the cxt
	 */
	public Context getCxt() {
		return cxt;
	}

	/**
	 * @return the dd
	 */
	public DatasetDirector getDd() {
		return dd;
	}

	@Override
	public String getExperiment() {
		return experiment.name();
	}

	@Override
	public String getHostname() {
		return null;
	}

	@Override
	public boolean isSecondExperiment() {
		return secondExperiment;
	}

	/**
	 * @param experiment
	 *            the experiment to set
	 */
	public void setExperiment(ExperimentNames experiment) {
		this.experiment = experiment;
	}

	@Override
	public void setSecondExperiment(boolean secondExperiment) {
		this.secondExperiment = secondExperiment;
	}

	@Override
	public void uploadData() {
		if (secondExperiment) {
			uploadDataset(ChannelListType.OM2);
			uploadDataset(ChannelListType.DAQ2);
		} else {
			uploadDataset(ChannelListType.OM);
			uploadDataset(ChannelListType.DAQ);
		}

	}

	public void uploadDataset(ChannelListType typ) {
		List<String> channels = cl.getChannels(typ);
		int columns = channels.size();

		for (RateType rate : RateType.values()) {
			log.debug("Uploading data for " + typ + " and " + rate);
			double[][] dataD = DataGenerator.initData(uploadRows,
					columns, 0.02);
			DoubleMatrix2Representation dm2rep = new DoubleMatrix2Representation(
					dataD);
			Request req = new Request(Method.POST, appRoot + "/experiment/"
					+ experiment + "/table/" + cl.getTt(typ) + "/rate/" + rate,
					dm2rep.getRep());
			req.setAttributes(genAttributes(experiment.name(), cl.getTt(typ)
					.name(), rate.name()));
			Response resp = new Response(req);
			dtsR.init(cxt, req, resp);
			dtsR.handle();
			assertTrue(resp.getStatus().isSuccess());
		}
	}
}
