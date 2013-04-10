package org.nees.illinois.replay.common.registries;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;


public class ExperimentRegistries {
	private ChannelNameManagement chnlNamesMgmt;

	private final String experiment;

	private final QueryRegistry queries = new QueryRegistry();

	@Inject
	public ExperimentRegistries(@Named("experiment") String experiment) {
		super();
		this.experiment = experiment;
	}

	public void setLookups(Provider<ChannelNameManagement> plookups) {
		chnlNamesMgmt = plookups.get();
	}
	/**
	 * @return the lookups
	 */
	public  ChannelNameManagement getChnlNamesMgmt() {
		return chnlNamesMgmt;
	}
	/**
	 * @return the cnr clone
	 */
	public  ChannelNameRegistry getCnrClone() {
		ChannelNameRegistry clone = new ChannelNameRegistry();
		clone.init(chnlNamesMgmt.getCnr().getClone(), chnlNamesMgmt.getCnr().getAfterLastChannel());
		return clone;
	}
	/**
	 * @return the lookups clone
	 */
	public  ChannelNameManagement getLookupsClone() {
		return chnlNamesMgmt.clone();
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
