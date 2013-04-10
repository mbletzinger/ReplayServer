package org.nees.illinois.replay.restlet.client;

import java.util.List;

import org.nees.illinois.replay.conversions.ChannelList2Representation;
import org.nees.illinois.replay.conversions.Representation2DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.StepNumber;
import org.nees.illinois.replay.restlet.DataQueryResource;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataQueryClient {

	private final String experiment;
	private final String hostname;
	private final Logger log = LoggerFactory.getLogger(DataQueryClient.class);

	public DataQueryClient(String hostname, String experiment) {
		super();
		this.experiment = experiment;
		this.hostname = hostname;
	}

	// /experiment/{experiment}/query/{query}/rate/{rate}/start/{start}/stop/{stop}
	protected String buildUri(String name) {
		String result = hostname + "/experiment/" + experiment + "/query/"
				+ name;
		return result;
	}

	protected String buildUri(String name, Double start, Double stop) {
		String result = buildUri(name, "CONT");
		if (start == null) {
			return result;
		}
		result += "/start/" + start;
		if (stop == null) {
			return result;
		}
		result += "/stop/" + stop;
		return result;
	}

	protected String buildUri(String name, StepNumber start, StepNumber stop) {
		String result = buildUri(name, "STEP");
		if (start == null) {
			return result;
		}
		result += "/start/" + start;
		if (stop == null) {
			return result;
		}
		result += "/stop/" + stop;
		return result;
	}

	protected String buildUri(String name, String rate) {
		String result = buildUri(name);
		result += "/rate/" + rate;
		return result;
	}

	public DoubleMatrix getData(String name, Double start)
			throws ResourceException {
		return getData(name, start, null);
	}

	public DoubleMatrix getData(String name, Double start, Double stop)
			throws ResourceException {
		String uri = buildUri(name, start, stop);
		log.debug("URI = getBin " + uri);
		DataQueryResource cr = ClientResource.create(uri,
				DataQueryResource.class);
		Representation rep = cr.getBin();
		if (rep == null) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "\""
					+ name + "\" returned no data");
		}
		Representation2DoubleMatrix rep2dm = new Representation2DoubleMatrix(
				rep);
		return rep2dm.getIn2dm().getMatrix();
	}

	public DoubleMatrix getData(String name, StepNumber start)
			throws ResourceException {
		return getData(name, start, null);
	}

	public DoubleMatrix getData(String name, StepNumber start, StepNumber stop)
			throws ResourceException {
		String uri = buildUri(name, start, stop);
		log.debug("URI = getBin " + uri);
		DataQueryResource cr = ClientResource.create(uri,
				DataQueryResource.class);
		Representation rep = cr.getBin();
		if (rep == null) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "\""
					+ name + "\" returned no data");
		}
		Representation2DoubleMatrix rep2dm = new Representation2DoubleMatrix(
				rep);
		return rep2dm.getIn2dm().getMatrix();
	}

	/**
	 * @return the experiment
	 */
	public String getExperiment() {
		return experiment;
	}

	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	public void setQuery(String name, List<String> channels)
			throws ResourceException {
		ChannelList2Representation cl2rep = new ChannelList2Representation(
				channels);
		String uri = buildUri(name);
		log.debug("URI = set " + uri);
		DataQueryResource cr = ClientResource.create(uri,
				DataQueryResource.class);
		cr.set(cl2rep.getRep());
	}
}