package org.nees.illinois.replay.restlet.client;

import java.util.List;

import org.nees.illinois.replay.conversions.ChannelList2Representation;
import org.nees.illinois.replay.conversions.DoubleMatrix2Representation;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.restlet.DataTableResource;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataTableClient {
	private final String experiment;

	private final String hostname;
	private final Logger log = LoggerFactory.getLogger(DataTableClient.class);

	public DataTableClient(String hostname, String experiment) {
		super();
		this.hostname = hostname;
		this.experiment = experiment;
	}

	public void addData(TableType table, RateType rate, DoubleMatrix data)
			throws ResourceException {
		DoubleMatrix2Representation dm2rep = new DoubleMatrix2Representation(
				data.getData());
		String uri = buildUri(table, rate);
		log.debug("URI = Update " + uri);
		DataTableResource cr = ClientResource.create(uri,
				DataTableResource.class);
		cr.update(dm2rep.getRep());
	}

	protected String buildUri(TableType table, RateType rate) {
		String result = hostname + "/experiment/" + experiment + "/table/"
				+ table;
		if (rate == null) {
			return result;
		}
		result += "/rate/" + rate;
		return result;
	}

	public void createTable(TableType table, List<String> channels)
			throws ResourceException {
		ChannelList2Representation cl2rep = new ChannelList2Representation(
				channels);
		String uri = buildUri(table, null);
		log.debug("URI = Set " + uri);
		DataTableResource cr = ClientResource.create(uri,
				DataTableResource.class);
		cr.set(cl2rep.getRep());
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
}
