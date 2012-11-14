package org.nees.mustsim.replay.restlet;

import java.util.List;

import org.nees.mustsim.replay.conversions.DoubleMatrix2Representation;
import org.nees.mustsim.replay.conversions.Str2CL;
import org.nees.mustsim.replay.data.DoubleMatrix;
import org.nees.mustsim.replay.data.RateType;
import org.nees.mustsim.replay.data.StepNumber;
import org.nees.mustsim.replay.queries.DataQueryI;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

public class DataQueryServerResource extends ServerResource implements
		DataQueryResource {
	private final DataQueryI dquery;

	public DataQueryServerResource(DataQueryI query) {
		super();
		this.dquery = query;
	}
	
	@Override
	@Get
	public Representation get(String query, String rate, String start, String stop) {
		RateType rt = RateType.valueOf(rate);
		if(rt.equals(RateType.STEP)) {
			StepNumber strt = (start.equals("") ? null : new StepNumber(start));
			StepNumber stp = (stop.equals("") ? null : new StepNumber(stop));
			DoubleMatrix data;
			if(strt == null) {
				data = dquery.doQuery(query);
			} else if(stp == null) {
				data = dquery.doQuery(query, strt);
			} else {
				data = dquery.doQuery(query, strt, stp);
			}
			DoubleMatrix2Representation dbl2rep = new DoubleMatrix2Representation(data.getData());
			return dbl2rep.getRep();
		}
		double strt = (start.equals("") ? 0.0 : Double.parseDouble(start));
		double stp = (stop.equals("") ? 0.0 : Double.parseDouble(stop));
		DoubleMatrix data;
		if(strt < .99) {
			data = dquery.doQuery(query);
		} else if(stp < .99) {
			data = dquery.doQuery(query, strt);
		} else {
			data = dquery.doQuery(query, strt, stp);
		}
		DoubleMatrix2Representation dbl2rep = new DoubleMatrix2Representation(data.getData());
		return dbl2rep.getRep();

	}

	@Override
	@Delete
	public void removeList(String query) {
		// TODO Auto-generated method stub

	}

	@Override
	@Put
	public void set(String query, String channels) {
		List<String> list = Str2CL.str2cl(channels);
		this.dquery.setQuery(query, list);
	}

}
