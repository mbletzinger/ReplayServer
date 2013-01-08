package org.nees.illinois.replay.restlet;

import java.util.List;

import org.nees.illinois.replay.conversions.Representation2ChannelList;
import org.nees.illinois.replay.conversions.Representation2DoubleMatrix;
import org.nees.illinois.replay.data.DataUpdateI;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.TableType;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class DataTableServerResource extends ServerResource implements
		DataTableResource {
//	private final Logger log = LoggerFactory
//			.getLogger(DataTableServerResource.class);

	private DataUpdateI updates;
	public DataTableServerResource() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.restlet.resource.Resource#doInit()
	 */
	@Override
	protected void doInit() throws ResourceException {
		this.updates = (DataUpdateI) getContext().getAttributes().get("updatesI");
		super.doInit();
	}

	@Override
	@Put
	public void set(Representation channels) {
		Representation2ChannelList rep2cl = new Representation2ChannelList(
				channels);
		List<String> list = rep2cl.getIl2cl().getChannels();
		TableType tbl = TableType.valueOf((String) getRequest().getAttributes()
				.get("table"));
		String experiment = (String) getRequest().getAttributes().get("experiment");
		updates.setExperiment(experiment);
		updates.createTable(tbl, list);

	}

	@Override
	@Post
	public void update(Representation data) {
		Representation2DoubleMatrix rep2dbl = new Representation2DoubleMatrix(
				data);
		List<List<Double>> doubles = rep2dbl.getIn2dm().getNumbers();
		DoubleMatrix dm = new DoubleMatrix(doubles, doubles.get(0).size());
		TableType tbl = TableType.valueOf((String) getRequest().getAttributes()
				.get("table"));
		RateType rt = RateType.valueOf((String) getRequest().getAttributes()
				.get("rate"));
		String experiment = (String) getRequest().getAttributes().get("experiment");
		updates.setExperiment(experiment);
		updates.update(tbl, rt, dm.getData());
	}

}
