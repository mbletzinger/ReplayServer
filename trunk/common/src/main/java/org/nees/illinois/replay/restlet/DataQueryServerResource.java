package org.nees.illinois.replay.restlet;

import java.util.List;

import org.nees.illinois.replay.conversions.DoubleMatrix2Representation;
import org.nees.illinois.replay.conversions.Representation2ChannelList;
import org.nees.illinois.replay.data.DataQueryI;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.StepNumber;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataQueryServerResource extends ServerResource implements
		DataQueryResource {
	private  DataQueryI dquery;
	
	private final Logger log = LoggerFactory
			.getLogger(DataQueryServerResource.class);

	public DataQueryServerResource() {
		super();
		
	}

	/* (non-Javadoc)
	 * @see org.restlet.resource.Resource#doInit()
	 */
	@Override
	protected void doInit() throws ResourceException {
		this.dquery = (DataQueryI) getContext().getAttributes().get("queryI");
		super.doInit();
	}
	
	@Override
	@Get("bin")
	public Representation getBin() {
		DoubleMatrix data = getDm();
		DoubleMatrix2Representation dbl2rep = new DoubleMatrix2Representation(data.getData());
		return dbl2rep.getRep();
	}
	
	private DoubleMatrix getDm() {
		RateType rt = RateType.valueOf((String) getRequest().getAttributes()
				.get("rate"));
		String start = (String) getRequest().getAttributes()
				.get("start");
		String stop = (String) getRequest().getAttributes()
				.get("stop");
		String query = (String) getRequest().getAttributes()
				.get("query");
		String experiment = (String) getRequest().getAttributes().get("experiment");
		dquery.setExperiment(experiment);



		if(rt.equals(RateType.STEP)) {
			StepNumber strt = parseStepNumber(start);
			StepNumber stp = parseStepNumber(stop);
			DoubleMatrix data;
			if(strt == null) {
				data = dquery.doQuery(query);
			} else if(stp == null) {
				data = dquery.doQuery(query, strt);
			} else {
				data = dquery.doQuery(query, strt, stp);
			}
			return data;
		}
		Double strt = parseDouble(start);
		Double stp = parseDouble(stop);
		DoubleMatrix data;
		if(strt == null) {
			data = dquery.doQuery(query);
		} else if(stp == null) {
			data = dquery.doQuery(query, strt);
		} else {
			data = dquery.doQuery(query, strt, stp);
		}
		return data;

	}

	private StepNumber parseStepNumber(String str) {
		if(str == null) {
			return null;
		}
		if(str.equals("")) {
			return null;
		}
		StepNumber result = null;
		try {
			result = new StepNumber(str);
		}
		catch (Exception e) {
			log.error("Step \"" + str + "\" could not be parsed");
			return null;
		}
		return result;
	}

	private Double parseDouble(String str) {
		if(str == null) {
			return null;
		}
		if(str.equals("")) {
			return null;
		}
		Double result = null;
		try {
			result = new Double(str);
		}
		catch (Exception e) {
			log.error("Double \"" + str + "\" could not be parsed");
			return null;
		}
		return result;
	}

	@Override
	@Delete
	public void removeList(String query) {
		// TODO Auto-generated method stub

	}

	@Override
	@Put
	public void set(Representation channels) {
		String query = (String) getRequest().getAttributes()
				.get("query");
		Representation2ChannelList rep2cl = new Representation2ChannelList(
				channels);
		String experiment = (String) getRequest().getAttributes().get("experiment");
		dquery.setExperiment(experiment);
		List<String> list = rep2cl.getIl2cl().getChannels();
		this.dquery.setQuery(query, list);
	}

	@Override
	@Get("txt")
	public Representation getText() {
		DoubleMatrix data = getDm();
		return new StringRepresentation(data.toString().toCharArray());
	}

}
