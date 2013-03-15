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
import org.nees.illinois.replay.registries.ExperimentModule;
import org.nees.illinois.replay.registries.ExperimentRegistries;
import org.nees.illinois.replay.registries.ExperimentSessionManager;
import org.nees.illinois.replay.restlet.AttributeExtraction.RequiredAttrType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.google.inject.Provider;

public class DataTableServerResource extends ServerResource implements
		DataTableResource {
	// private final Logger log = LoggerFactory
	// .getLogger(DataTableServerResource.class);

	private AttributeExtraction extract;

	private final List<RequiredAttrType> reqAttrs = new ArrayList<AttributeExtraction.RequiredAttrType>();

	private final List<RequiredAttrType> reqAttrsWithRate = new ArrayList<AttributeExtraction.RequiredAttrType>();
	private DataUpdateI updates;
	private  ExperimentModule guiceMod;


	public DataTableServerResource() {
		super();
		reqAttrs.add(RequiredAttrType.Table);
		reqAttrsWithRate.addAll(reqAttrs);
		reqAttrsWithRate.add(RequiredAttrType.Rate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.restlet.resource.Resource#doInit()
	 */
	@Override
	protected void doInit() throws ResourceException {
		this.extract = new AttributeExtraction(getRequest().getAttributes());
		@SuppressWarnings("unchecked")
		Provider<DataUpdateI> provider = (Provider<DataUpdateI>) getContext()
				.getAttributes().get("updatesI");
		this.updates = provider.get();
		guiceMod = (ExperimentModule) getContext().getAttributes().get("guiceMod");
		ExperimentSessionManager esm = new ExperimentSessionManager(
				getContext().getAttributes(), getRequestAttributes(), guiceMod);
		boolean allowCreated = getRequest().getMethod().equals(Method.PUT);
		ExperimentRegistries er = esm.getRegistries(allowCreated);
		updates.setExperiment(er);
		super.doInit();
	}

	/**
	 * @return the updates
	 */
	public DataUpdateI getUpdates() {
		return updates;
	}

	@Override
	@Put
	public void set(Representation channels) throws ResourceException  {
		Representation2ChannelList rep2cl = new Representation2ChannelList(
				channels);
		List<String> list = rep2cl.getIl2cl().getChannels();
		extract.extract(reqAttrs);
		Map<RequiredAttrType, Object> attrs = extract.getAttrs();
		TableType tbl = (TableType) attrs.get(RequiredAttrType.Table);
		updates.createTable(tbl, list);

	}

	/**
	 * @param updates
	 *            the updates to set
	 */
	public void setUpdates(DataUpdateI updates) {
		this.updates = updates;
	}

	@Override
	@Post
	public void update(Representation data) throws ResourceException  {
		Representation2DoubleMatrix rep2dbl = new Representation2DoubleMatrix(
				data);
		List<List<Double>> doubles = rep2dbl.getIn2dm().getNumbers();
		DoubleMatrix dm = new DoubleMatrix(doubles);
		extract.extract(reqAttrsWithRate);
		Map<RequiredAttrType, Object> attrs = extract.getAttrs();
		TableType tbl = (TableType) attrs.get(RequiredAttrType.Table);
		RateType rate = (RateType) attrs.get(RequiredAttrType.Rate);
		updates.update(tbl, rate, dm.getData());
	}

}
