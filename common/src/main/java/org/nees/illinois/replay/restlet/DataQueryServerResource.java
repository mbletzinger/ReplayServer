package org.nees.illinois.replay.restlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.nees.illinois.replay.common.types.CompositeQueryI;
import org.nees.illinois.replay.conversions.DoubleMatrix2Representation;
import org.nees.illinois.replay.conversions.Representation2ChannelList;
import org.nees.illinois.replay.data.DataQuerySubResourceI;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.StepNumber;
import org.nees.illinois.replay.data.SubResourceI;
import org.nees.illinois.replay.restlet.AttributeExtraction.RequiredAttrType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Provider;

/**
 * Implements a query server resource for the replay server.
 * @author Michael Bletzinger
 */
public class DataQueryServerResource extends ServerResource implements
		DataQueryResource {
	/**
	 * Subresource that actually does all of the work. This is passed in as part
	 * of the restlet context so that it can be configured with Google GUICE.
	 */
	private DataQuerySubResourceI dquery;
	/**
	 * Used to extract attributes from the request URI.
	 */
	private AttributeExtraction extract;
	/**
	 * Registries for the experiment session.
	 */
	private ExperimentRegistries er;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory
			.getLogger(DataQueryServerResource.class);
	/**
	 * Map of URI attributes.
	 */
	private Map<RequiredAttrType, Object> attrs;
	/**
	 * Rate type specified in the request.
	 */
	private RateType rate;
	/**
	 * Query name specified in the request.
	 */
	private String query;

	/**
	 * Constructor.
	 */
	public DataQueryServerResource() {
		super();

	}

	// private final Logger log = LoggerFactory
	// .getLogger(DataQueryServerResource.class);

	/*
	 * (non-Javadoc)
	 * @see org.restlet.resource.Resource#doInit()
	 */
	@Override
	protected final void doInit() {
		@SuppressWarnings("unchecked")
		Provider<DataQuerySubResourceI> provider = (Provider<DataQuerySubResourceI>) getContext()
				.getAttributes().get("queryI");
		dquery = provider.get();
		extract = new AttributeExtraction(getRequest().getAttributes());
		ExperimentSessionManager esm = new ExperimentSessionManager(
				getContext().getAttributes(), getRequestAttributes());
		er = esm.getRegistries(false);
		dquery.setExperiment(er);
		super.doInit();
	}

	@Override
	@Get("bin")
	public final Representation getBin() {
		DoubleMatrix data = getDm();
		if (data == null) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
					"Query \"" + query + "\" returned an empty dataset");
		}
		DoubleMatrix2Representation dbl2rep = new DoubleMatrix2Representation(
				data.getData());
		return dbl2rep.getRep();
	}

	/**
	 * Function that does all of the work for a query request. The function
	 * determines the start, stop, and rate attributes and executes a query
	 * using the {@link DataQuerySubResourceI dquery} subresource.
	 * @return the data in double matrix form.
	 */
	private DoubleMatrix getDm() {
		final List<RequiredAttrType> reqAttrs = new ArrayList<AttributeExtraction.RequiredAttrType>();

		log.debug("Query Resource handling " + getRequest().getMethod()
				+ " with " + getRequest());
		reqAttrs.add(RequiredAttrType.Rate);
		reqAttrs.add(RequiredAttrType.Query);
		reqAttrs.add(RequiredAttrType.Start);
		reqAttrs.add(RequiredAttrType.Stop);
		extract.extract(reqAttrs);
		attrs = extract.getAttrs();
		rate = (RateType) attrs.get(RequiredAttrType.Rate);
		query = (String) attrs.get(RequiredAttrType.Query);

		CompositeQueryI spec = er.getQueries().getQuery(query);
		if (spec == null) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
					"Query \"" + query + "\" not recognized");
		}

		if (rate.equals(RateType.STEP)) {
			StepNumber strt = (StepNumber) attrs.get(RequiredAttrType.Start);
			StepNumber stp = (StepNumber) attrs.get(RequiredAttrType.Stop);
			DoubleMatrix data;
			if (strt == null) {
				data = dquery.doQuery(query);
			} else if (stp == null) {
				data = dquery.doQuery(query, strt, null, null);
			} else {
				data = dquery.doQuery(query, strt, stp, null);
			}
			return data;
		}
		Double strt = (Double) attrs.get(RequiredAttrType.Start);
		Double stp = (Double) attrs.get(RequiredAttrType.Stop);
		DoubleMatrix data;
		if (strt == null) {
			data = dquery.doQuery(query);
		} else if (stp == null) {
			data = dquery.doQuery(query, strt);
		} else {
			data = dquery.doQuery(query, strt, stp);
		}
		return data;

	}

	/**
	 * @return the dquery
	 */
	public final SubResourceI getDquery() {
		return dquery;
	}

	// @Override
	// @Get("txt")
	// public Representation getText() throws ResourceException {
	// DoubleMatrix data = getDm();
	// return new StringRepresentation(data.toString().toCharArray());
	// }

	@Override
	@Delete
	public final void removeList(final String query) {
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED, "Remove not implemented yet.");
	}

	@Override
	@Put
	public final void set(final Representation channels) {
		final List<RequiredAttrType> reqAttrs = new ArrayList<AttributeExtraction.RequiredAttrType>();

		reqAttrs.add(RequiredAttrType.Query);
		extract.extract(reqAttrs);
		attrs = extract.getAttrs();
		query = (String) attrs.get(RequiredAttrType.Query);

		Representation2ChannelList rep2cl = new Representation2ChannelList(
				channels);
		List<String> list = rep2cl.getIl2cl().getChannels();
		this.dquery.setQuery(query, list);
	}

	/**
	 * @param dquery
	 *            the dquery to set
	 */
	public final void setDquery(final DataQuerySubResourceI dquery) {
		this.dquery = dquery;
	}

}
