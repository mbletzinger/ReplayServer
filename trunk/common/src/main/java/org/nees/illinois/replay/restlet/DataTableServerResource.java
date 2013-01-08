package org.nees.illinois.replay.restlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.conversions.Representation2ChannelList;
import org.nees.illinois.replay.conversions.Representation2DoubleMatrix;
import org.nees.illinois.replay.data.DataUpdateI;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.restlet.AttributeExtraction.RequiredAttrType;
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
	private final List<RequiredAttrType> reqAttrs = new ArrayList<AttributeExtraction.RequiredAttrType>();
	private final List<RequiredAttrType> reqAttrsWithRate = new ArrayList<AttributeExtraction.RequiredAttrType>();
	private  AttributeExtraction extract;
	
	public DataTableServerResource() {
		super();
		reqAttrs.add(RequiredAttrType.Experiment);
		reqAttrs.add(RequiredAttrType.Table);
		reqAttrsWithRate.addAll(reqAttrs);
		reqAttrsWithRate.add(RequiredAttrType.Rate);
	}

	/* (non-Javadoc)
	 * @see org.restlet.resource.Resource#doInit()
	 */
	@Override
	protected void doInit() throws ResourceException {
		this.extract = new AttributeExtraction(getRequest().getAttributes());
		this.updates = (DataUpdateI) getContext().getAttributes().get("updatesI");
		super.doInit();
	}

	@Override
	@Put
	public void set(Representation channels) {
		Representation2ChannelList rep2cl = new Representation2ChannelList(
				channels);
		List<String> list = rep2cl.getIl2cl().getChannels();
		extract.extract(reqAttrs);
		Map<RequiredAttrType,Object> attrs = extract.getAttrs();
		TableType tbl = (TableType) attrs.get(RequiredAttrType.Table);
		String experiment = (String) attrs.get(RequiredAttrType.Experiment);
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
		extract.extract(reqAttrsWithRate);
		Map<RequiredAttrType,Object> attrs = extract.getAttrs();
		TableType tbl = (TableType) attrs.get(RequiredAttrType.Table);
		RateType rate = (RateType) attrs.get(RequiredAttrType.Rate);
		String experiment = (String) attrs.get(RequiredAttrType.Experiment);
		updates.setExperiment(experiment);
		updates.update(tbl, rate, dm.getData());
	}

}
