package org.nees.illinois.replay.restlet.client;

import java.util.List;

import org.nees.illinois.replay.common.registries.TableType;
import org.nees.illinois.replay.conversions.StringList2Representation;
import org.nees.illinois.replay.conversions.DoubleMatrix2Representation;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.restlet.DataTableResourceI;
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

	public void addData(TableType table, RateType rate, DoubleMatrixI data)
			throws ResourceException {
		DoubleMatrix2Representation dm2rep = new DoubleMatrix2Representation(
				data.getData());
		String uri = buildUri(table, rate);
		log.debug("URI = Update " + uri);
		DataTableResourceI cr = ClientResource.create(uri,
				DataTableResourceI.class);
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
		StringList2Representation cl2rep = new StringList2Representation(
				channels);
		String uri = buildUri(table, null);
		log.debug("URI = Set " + uri);
		DataTableResourceI cr = ClientResource.create(uri,
				DataTableResourceI.class);
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
