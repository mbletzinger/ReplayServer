package org.nees.illinois.replay.restlet.client;

import java.util.List;

import org.nees.illinois.replay.conversions.StringList2Representation;
import org.nees.illinois.replay.conversions.Representation2DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.events.EventI;
import org.nees.illinois.replay.restlet.DataQueryResourceI;
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

	public DataQueryClient(final String hostname, final String experiment) {
		super();
		this.experiment = experiment;
		this.hostname = hostname;
	}

	// /experiment/{experiment}/query/{query}/rate/{rate}/start/{start}/stop/{stop}
	protected String buildUri(final String name) {
		String result = hostname + "/experiment/" + experiment + "/query/"
				+ name;
		return result;
	}

	protected String buildUri(final String name, final Double start, final Double stop) {
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

	protected String buildUri(final String name, final EventI start, final EventI stop) {
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

	protected String buildUri(final String name, final String rate) {
		String result = buildUri(name);
		result += "/rate/" + rate;
		return result;
	}

	public DoubleMatrixI getData(final String name, final Double start)
			throws ResourceException {
		return getData(name, start, null);
	}

	public DoubleMatrixI getData(final String name, final Double start, final Double stop)
			throws ResourceException {
		String uri = buildUri(name, start, stop);
		log.debug("URI = getBin " + uri);
		DataQueryResourceI cr = ClientResource.create(uri,
				DataQueryResourceI.class);
		Representation rep = cr.getBin(null);
		if (rep == null) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "\""
					+ name + "\" returned no data");
		}
		Representation2DoubleMatrix rep2dm = new Representation2DoubleMatrix(
				rep);
		return rep2dm.getIn2dm().getMatrix();
	}

	public DoubleMatrixI getData(final String name, final StepNumber start)
			throws ResourceException {
		return getData(name, start, null);
	}

	public DoubleMatrixI getData(final String name, final StepNumber start, final StepNumber stop)
			throws ResourceException {
		String uri = buildUri(name, start, stop);
		log.debug("URI = getBin " + uri);
		DataQueryResourceI cr = ClientResource.create(uri,
				DataQueryResourceI.class);
		Representation rep = cr.getBin(null);
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

	public void setQuery(final String name, final List<String> channels)
			throws ResourceException {
		StringList2Representation cl2rep = new StringList2Representation(
				channels);
		String uri = buildUri(name);
		log.debug("URI = set " + uri);
		DataQueryResourceI cr = ClientResource.create(uri,
				DataQueryResourceI.class);
		cr.set(cl2rep.getRep());
	}
}
