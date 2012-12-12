package org.nees.mustsim.replay.restlet.client;

import java.util.List;

import org.nees.mustsim.replay.conversions.ChannelList2Representation;
import org.nees.mustsim.replay.conversions.Representation2DoubleMatrix;
import org.nees.mustsim.replay.data.DoubleMatrix;
import org.nees.mustsim.replay.data.StepNumber;
import org.nees.mustsim.replay.restlet.DataQueryResource;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class DataQueryClient {

	private final String experiment;
	private final String hostname;

	public DataQueryClient(String hostname, String experiment) {
		super();
		this.experiment = experiment;
		this.hostname = hostname;
	}
	// /experiment/{experiment}/query/{query}/rate/{rate}/start/{start}/stop/{stop}
	private String buildUri(String name) {
		String result =  hostname + "/experiment/" + experiment + "/query/" + name;
		return result;
	}
	
	private String buildUri(String name, String rate) {
		String result =  buildUri(name);
		result += "/rate/" + rate;
		return result;
	}
	private String buildUri(String name, Double start, Double stop) {
		String result =  buildUri(name, "CONT");
		if(start == null) {
			return result;
		}
		result += "/start/" + start;
		if(stop == null) {
			return result;
		}
		result += "/stop/" + stop;
		return result;
	}
	private String buildUri(String name, StepNumber start, StepNumber stop) {
		String result =  buildUri(name, "STEP");
		if(start == null) {
			return result;
		}
		result += "/start/" + start;
		if(stop == null) {
			return result;
		}
		result += "/stop/" + stop;
		return result;
	}
	public DoubleMatrix getData(String name, Double start) {
		return getData(name,start,null);
	}
	public DoubleMatrix getData(String name, Double start, Double stop) {
		DataQueryResource cr = ClientResource.create(buildUri(name, start, stop), DataQueryResource.class);
		Representation rep = cr.getBin();
		Representation2DoubleMatrix rep2dm = new Representation2DoubleMatrix(rep);
		return rep2dm.getIn2dm().getMatrix();
	}
	public DoubleMatrix getData(String name, StepNumber start) {
		return getData(name,start,null);
	}

	public DoubleMatrix getData(String name, StepNumber start, StepNumber stop) {
		DataQueryResource cr = ClientResource.create(buildUri(name, start, stop), DataQueryResource.class);
		Representation rep = cr.getBin();
		Representation2DoubleMatrix rep2dm = new Representation2DoubleMatrix(rep);
		return rep2dm.getIn2dm().getMatrix();		
	}

	public void setQuery(String name, List<String> channels) {
		ChannelList2Representation cl2rep = new ChannelList2Representation(channels);
		DataQueryResource cr = ClientResource.create(buildUri(name), DataQueryResource.class);
		cr.set(cl2rep.getRep());
	}
}
