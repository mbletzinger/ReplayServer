package org.nees.illinois.replay.restlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.conversions.DoubleMatrix2Representation;
import org.nees.illinois.replay.conversions.Representation2ChannelList;
import org.nees.illinois.replay.data.DataQueryI;
import org.nees.illinois.replay.data.DataUpdateI;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.StepNumber;
import org.nees.illinois.replay.restlet.AttributeExtraction.RequiredAttrType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.google.inject.Provider;

public class DataQueryServerResource extends ServerResource implements
		DataQueryResource {
	private  DataQueryI dquery;

	private AttributeExtraction extract;

	public DataQueryServerResource() {
		super();
		
	}
	
//	private final Logger log = LoggerFactory
//			.getLogger(DataQueryServerResource.class);
	
	/* (non-Javadoc)
	 * @see org.restlet.resource.Resource#doInit()
	 */
	@Override
	protected void doInit() throws ResourceException {
		@SuppressWarnings("unchecked")
		Provider<DataQueryI> provider = (Provider<DataQueryI>) getContext().getAttributes().get("queryI");
		dquery = provider.get();
		extract = new AttributeExtraction(getRequest().getAttributes());
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
		final List<RequiredAttrType> reqAttrs= new ArrayList<AttributeExtraction.RequiredAttrType>();

		reqAttrs.add(RequiredAttrType.Experiment);
		reqAttrs.add(RequiredAttrType.Rate);
		reqAttrs.add(RequiredAttrType.Query);
		extract.extract(reqAttrs);
		Map<RequiredAttrType,Object> attrs = extract.getAttrs();
		RateType rate = (RateType) attrs.get(RequiredAttrType.Rate);
		String experiment = (String) attrs.get(RequiredAttrType.Experiment);
		String query = (String) attrs.get(RequiredAttrType.Query);

		dquery.setExperiment(experiment);

		reqAttrs.clear();
		reqAttrs.add(RequiredAttrType.Start);
		reqAttrs.add(RequiredAttrType.Stop);
		reqAttrs.add(rate.equals(RateType.STEP) ? RequiredAttrType.StepNumber : RequiredAttrType.Double);
		extract.extract(reqAttrs);
		attrs = extract.getAttrs();
		
		if(rate.equals(RateType.STEP)) {
			StepNumber strt = (StepNumber) attrs.get(RequiredAttrType.Start);
			StepNumber stp = (StepNumber) attrs.get(RequiredAttrType.Stop);
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
		Double strt = (Double) attrs.get(RequiredAttrType.Start);
		Double stp = (Double) attrs.get(RequiredAttrType.Stop);
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
	
	/**
	 * @return the dquery
	 */
	public DataQueryI getDquery() {
		return dquery;
	}
	
	@Override
	@Get("txt")
	public Representation getText() {
		DoubleMatrix data = getDm();
		return new StringRepresentation(data.toString().toCharArray());
	}
	
	@Override
	@Delete
	public void removeList(String query) {
		// TODO Auto-generated method stub

	}

	@Override
	@Put
	public void set(Representation channels) {
		final List<RequiredAttrType> reqAttrs= new ArrayList<AttributeExtraction.RequiredAttrType>();

		reqAttrs.add(RequiredAttrType.Experiment);
		reqAttrs.add(RequiredAttrType.Query);
		extract.extract(reqAttrs);
		Map<RequiredAttrType,Object> attrs = extract.getAttrs();
		String experiment = (String) attrs.get(RequiredAttrType.Experiment);
		String query = (String) attrs.get(RequiredAttrType.Query);

		Representation2ChannelList rep2cl = new Representation2ChannelList(
				channels);
		dquery.setExperiment(experiment);
		List<String> list = rep2cl.getIl2cl().getChannels();
		this.dquery.setQuery(query, list);
	}

	/**
	 * @param dquery the dquery to set
	 */
	public void setDquery(DataQueryI dquery) {
		this.dquery = dquery;
	}

}
