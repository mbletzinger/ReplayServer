package org.nees.illinois.replay.restlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.nees.illinois.replay.common.types.CompositeQueryI;
import org.nees.illinois.replay.common.types.TimeBounds;
import org.nees.illinois.replay.common.types.TimeBoundsI;
import org.nees.illinois.replay.conversions.DoubleMatrix2Representation;
import org.nees.illinois.replay.conversions.Representation2StringList;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.events.EventListI;
import org.nees.illinois.replay.restlet.AttributeExtraction.AttributeRules;
import org.nees.illinois.replay.restlet.AttributeExtraction.AttributeTypes;
import org.nees.illinois.replay.subresource.DataQuerySubResourceI;
import org.nees.illinois.replay.subresource.EventSubResourceI;
import org.nees.illinois.replay.subresource.SubResourceI;
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
DataQueryResourceI {
	/**
	 * Subresource that actually does all of the work. This is passed in as part
	 * of the restlet context so that it can be configured with Google GUICE.
	 */
	private DataQuerySubResourceI dquery;

	/**
	 * Subresource to query for events. This is passed in as part of the restlet
	 * context so that it can be configured with Google GUICE.
	 */
	private EventSubResourceI qevents;

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
	private Map<AttributeTypes, Object> attrs;
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
	public final Representation getBin(final Representation events) {
		List<String> discrete = null;
		if(events.isEmpty() == false) {
			Representation2StringList r2sl = new Representation2StringList(events);
			discrete = r2sl.getIl2cl().getStrings();
		}
		DoubleMatrixI data = getDm(discrete);
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
	 * @param discrete list of time events.
	 * @return the data in double matrix form.
	 */
	private DoubleMatrixI getDm(final List<String> discrete) {
		final List<AttributeRules> arules = new ArrayList<AttributeRules>();

		log.debug("Query Resource handling " + getRequest().getMethod()
				+ " with " + getRequest());
		arules.add(AttributeRules.ExperimentNameRequired);
		arules.add(AttributeRules.QueryNameRequired);
		if(discrete == null) {
			arules.add(AttributeRules.TimeBoundsRequired);
		}
		extract.extract(arules);
		attrs = extract.getAttrs();
		rate = (RateType) attrs.get(AttributeTypes.Rate);
		query = (String) attrs.get(AttributeTypes.Query);

		CompositeQueryI spec = er.getQueries().getQuery(query);
		if (spec == null) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
					"Query \"" + query + "\" not recognized");
		}
		TimeBoundsI bounds = null;
		if (attrs.get(AttributeTypes.StartTime) != null) {
			Double strt = (Double) attrs.get(AttributeTypes.Start);
			Double stp = (Double) attrs.get(AttributeTypes.Stop);
			bounds = new TimeBounds(strt, stp);
		} else if (attrs.get(AttributeTypes.Start) != null) {
			String strt = (String) attrs.get(AttributeTypes.Start);
			String stp = (String) attrs.get(AttributeTypes.Stop);
			if(rate.equals(RateType.CONTINUOUS)) {
				bounds = new TimeBounds(strt,stp);
			} else {
				EventListI events = qevents.getEvents(strt, stp, null);
				bounds = new TimeBounds(events.getEventNames());
			}
		}
		DoubleMatrixI data;
		data = dquery.doQuery(query, bounds);
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
		throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED,
				"Remove not implemented yet.");
	}

	@Override
	@Put
	public final void set(final Representation channels) {
		final List<AttributeRules> arules = new ArrayList<AttributeRules>();

		arules.add(AttributeRules.ExperimentNameRequired);
		arules.add(AttributeRules.QueryNameRequired);
		extract.extract(arules);
		attrs = extract.getAttrs();
		query = (String) attrs.get(AttributeTypes.Query);

		Representation2StringList rep2cl = new Representation2StringList(
				channels);
		List<String> list = rep2cl.getIl2cl().getStrings();
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
