package org.nees.illinois.replay.registries;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;


public class ExperimentRegistries {
	private ChannelLookups lookups;

	private final String experiment;

	private final QueryRegistry queries = new QueryRegistry();

	@Inject
	public ExperimentRegistries(@Named("experiment") String experiment) {
		super();
		this.experiment = experiment;
	}

	public void setLookups(Provider<ChannelLookups> plookups) {
		lookups = plookups.get();
	}
	/**
	 * @return the lookups
	 */
	public  ChannelLookups getLookups() {
		return lookups;
	}
	/**
	 * @return the cnr clone
	 */
	public  ChannelNameRegistry getCnrClone() {
		ChannelNameRegistry clone = new ChannelNameRegistry();
		clone.init(lookups.getCnr().getClone(), lookups.getCnr().getAfterLastChannel());
		return clone;
	}
	/**
	 * @return the lookups clone
	 */
	public  ChannelLookups getLookupsClone() {
		return lookups.clone();
	}
	/**
	 * @return the experiment
	 */
	public  String getExperiment() {
		return experiment;
	}
	/**
	 * @return the queries
	 */
	public  QueryRegistry getQueries() {
		return queries;
	}
}
