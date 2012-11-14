package org.nees.mustsim.replay.restlet;

import java.util.List;

import org.nees.mustsim.replay.conversions.Representation2DoubleMatrix;
import org.nees.mustsim.replay.conversions.Str2CL;
import org.nees.mustsim.replay.data.DataUpdatesI;
import org.nees.mustsim.replay.data.DoubleMatrix;
import org.nees.mustsim.replay.data.RateType;
import org.nees.mustsim.replay.data.TableType;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class DataTableServerResource extends ServerResource implements
		DataTableResource {
	private final DataUpdatesI updates;
	private final Logger log = LoggerFactory
			.getLogger(DataTableServerResource.class);

	@Override
	@Put
	public void set(String channels) {
		List<String> list = Str2CL.str2cl(channels);
		TableType tbl = TableType.valueOf((String)getRequest().getAttributes().get("table"));
		updates.createTable(tbl, list);

	}
	@Inject
	public DataTableServerResource(DataUpdatesI updates) {
		super();
		this.updates = updates;
	}

	@Override
	@Post
	public void update(Representation data) {
		Representation2DoubleMatrix rep2dbl = new Representation2DoubleMatrix(data);
		List<List<Double>> doubles = rep2dbl.getNumbers();
		DoubleMatrix dm = new DoubleMatrix(doubles, doubles.get(0).size());
		TableType tbl = TableType.valueOf((String)getRequest().getAttributes().get("table"));
		RateType rt = RateType.valueOf((String)getRequest().getAttributes().get("rate"));
		updates.update(tbl, rt, dm.getData());
	}

}
