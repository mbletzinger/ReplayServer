package org.nees.illinois.replay.common.registries;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Registeries to manage the dataset of an experiment. The Replay server manages
 * a session that has experiment scope. The registries save data that needs to
 * be shared between requests but is not necessarily part of the database. Each
 * experiment has the following registry:
 * <dl>
 * <dt>Channel Name Registry
 * <dd>Maps channel names to database-friendly column names
 * <dt>Table Identity Registry
 * <dd>Maps dataset table names to their database table {@link TableIdentityI
 * identities}.
 * <dt>Table Registry
 * <dd>Maps table {@link TableIdentityI identities} to their
 * {@link TableColumnsI definitions}.
 * </dl>
 * @author Michael Bletzinger
 */
public class ExperimentRegistries {
	/**
	 * Channel name {@link ChannelNameRegistry registry} for the experiment.
	 */
	private final ChannelNameRegistry cnr = new ChannelNameRegistry();
	/**
	 * Name of the experiment.
	 */
	private final String experiment;
	/**
	 * Query {@link QueryRegistry registry} for the experiment.
	 */
	private final QueryRegistry queries = new QueryRegistry();
	/**
	 * Table identity {@link TableIdentityRegistry registry} for the experiment.
	 */
	private final TableIdentityRegistry tblIdr = new TableIdentityRegistry();
	/**
	 * Table {@link TableRegistry registry} for the experiment.
	 */
	private final TableRegistry tblDefr = new TableRegistry();

	/**
	 * Creates an experiment registry.
	 * @param experiment
	 *            Name of the experiment.
	 */
	@Inject
	public ExperimentRegistries(@Named("experiment") final String experiment) {
		super();
		this.experiment = experiment;
	}

	/**
	 * @return the channel name registry
	 */
	public final ChannelNameRegistry getCnr() {
		return cnr;
	}

	/**
	 * @return the experiment
	 */
	public final String getExperiment() {
		return experiment;
	}

	/**
	 * @return the queries
	 */
	public final QueryRegistry getQueries() {
		return queries;
	}

	/**
	 * @return the table identity registry
	 */
	public final TableIdentityRegistry getTblIdr() {
		return tblIdr;
	}

	/**
	 * @return the table registry
	 */
	public final TableRegistry getTblDefr() {
		return tblDefr;
	}
}
