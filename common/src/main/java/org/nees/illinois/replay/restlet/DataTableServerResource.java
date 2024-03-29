package org.nees.illinois.replay.restlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.nees.illinois.replay.common.registries.TableType;
import org.nees.illinois.replay.conversions.Representation2DoubleMatrix;
import org.nees.illinois.replay.conversions.Representation2StringList;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.restlet.AttributeExtraction.AttributeRules;
import org.nees.illinois.replay.restlet.AttributeExtraction.AttributeTypes;
import org.nees.illinois.replay.subresource.DataUpdateSubResourceI;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.google.inject.Provider;

/**
 * Implements the data update resource for the replay server.
 * @author Michael Bletzinger
 */
public class DataTableServerResource extends ServerResource implements
DataTableResourceI {
	// private final Logger log = LoggerFactory
	// .getLogger(DataTableServerResource.class);

	/**
	 * Used to extract attributes from the request URI.
	 */
	private AttributeExtraction extract;
	/**
	 * List of required attributes for the Put request.
	 */
	private final List<AttributeRules> reqAttrs = new ArrayList<AttributeRules>();
	/**
	 * List of required attributes for the Post request.
	 */
	private final List<AttributeRules> reqAttrsWithRate = new ArrayList<AttributeRules>();
	/**
	 * Subresource that actually does all of the work. This is passed in as part
	 * of the restlet context so that it can be configured with Google GUICE.
	 */
	private DataUpdateSubResourceI updates;

	/**
	 * Constructor.
	 */
	public DataTableServerResource() {
		super();
		reqAttrs.add(AttributeRules.SourceRequired);
		reqAttrsWithRate.addAll(reqAttrs);
		reqAttrsWithRate.add(AttributeRules.RateRequired);
	}

	/*
	 * (non-Javadoc)
	 * @see org.restlet.resource.Resource#doInit()
	 */
	@Override
	protected final void doInit() {
		this.extract = new AttributeExtraction(getRequest().getAttributes());
		@SuppressWarnings("unchecked")
		Provider<DataUpdateSubResourceI> provider = (Provider<DataUpdateSubResourceI>) getContext()
		.getAttributes().get("updatesI");
		this.updates = provider.get();

		ExperimentSessionManager esm = new ExperimentSessionManager(
				getContext().getAttributes(), getRequestAttributes());
		boolean allowCreated = getRequest().getMethod().equals(Method.PUT);
		ExperimentRegistries er = esm.getRegistries(allowCreated);
		updates.setExperiment(er);
		super.doInit();
	}

	/**
	 * @return the updates
	 */
	public final DataUpdateSubResourceI getUpdates() {
		return updates;
	}

	@Override
	@Put
	public final void set(final Representation channels) {
		Representation2StringList rep2cl = new Representation2StringList(
				channels);
		List<String> list = rep2cl.getIl2cl().getStrings();
		extract.extract(reqAttrs);
		Map<AttributeTypes, Object> attrs = extract.getAttrs();
		TableType tbl = (TableType) attrs.get(AttributeTypes.Source);
		updates.createTable(null, tbl, list);

	}

	/**
	 * @param updates
	 *            the updates to set
	 */
	public final void setUpdates(final DataUpdateSubResourceI updates) {
		this.updates = updates;
	}

	@Override
	@Post
	public final void update(final Representation data) {
		Representation2DoubleMatrix rep2dbl = new Representation2DoubleMatrix(
				data);
		List<List<Double>> doubles = rep2dbl.getIn2dm().getNumbers();
		DoubleMatrixI dm = new DoubleMatrix(doubles);
		extract.extract(reqAttrsWithRate);
		Map<AttributeTypes, Object> attrs = extract.getAttrs();
		String tbl = (String) attrs.get(AttributeTypes.Source);
		updates.update(tbl, dm.getData());
	}

}
