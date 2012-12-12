package org.nees.illinois.replay.restlet.client;

import java.util.List;

import org.nees.illinois.replay.conversions.ChannelList2Representation;
import org.nees.illinois.replay.conversions.DoubleMatrix2Representation;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.restlet.DataTableResource;
import org.restlet.resource.ClientResource;

public class DataTableClient {
	private final String experiment;
	private final String hostname;
	public DataTableClient(String hostname, String experiment) {
		super();
		this.hostname = hostname;
		this.experiment = experiment;
	}
	public void addData(TableType table, RateType rate, DoubleMatrix data) {
		DoubleMatrix2Representation dm2rep = new DoubleMatrix2Representation(data.getData());
		DataTableResource cr = ClientResource.create(buildUri(table, rate), DataTableResource.class);
		cr.update(dm2rep.getRep());
	}
	private String buildUri(TableType table, RateType rate) {
		String result =  hostname + "/experiment/" + experiment + "/table/" + table;
		if(rate == null) {
			return result;
		}
		result += "/rate/" + rate;
		return result;
	}
	public void createTable(TableType table, List<String> channels) {
		ChannelList2Representation cl2rep = new ChannelList2Representation(channels);
		DataTableResource cr = ClientResource.create(buildUri(table,null), DataTableResource.class);
		cr.set(cl2rep.getRep());
	}
}
