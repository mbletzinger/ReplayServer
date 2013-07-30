package org.nees.illinois.replay.common.registries;

/**
 * Registeries to manage the dataset of an experiment. The Replay server manages
 * a session that has experiment scope. The registries save data that needs to
 * be shared between requests but is not necessarily part of the database. Some
 * registries Each experiment has the following registry:
 * <dl>
 * <dt>Channel Name Registry
 * <dd>Maps channel names to database-friendly column names
 * <dt>Table Identity Registry
 * <dd>Maps dataset table names to their database table {@link TableIdentityI
 * identities}.
 * <dt>Table Registry
 * <dd>Maps table {@link TableIdentityI identities} to their
 * {@link TableDefinitionI definitions}.
 * <dt>Query Registry
 * <dd>Maps query names to their definitions.
 * <dt>Event ID generator.
 * <dd>Generate unique ids for events.
 * </dl>
 * @author Michael Bletzinger
 */
public class ExperimentRegistries {
	/**
	 * Channel name {@link ChannelNameRegistry registry} for the experiment.
	 */
	private final ChannelNameRegistry cnr;

	/**
	 * Generate unique ids for events.
	 */
	private final EventIdGenerator eventIds;
	/**
	 * Name of the experiment.
	 */
	private final String experiment;
	/**
	 * Query {@link QueryRegistry registry} for the experiment.
	 */
	private final QueryRegistry queries;
	/**
	 * Table {@link TableRegistry registry} for the experiment.
	 */
	private final TableRegistry tableDefs;
	/**
	 * Table identity {@link TableIdentityRegistry registry} for the experiment.
	 */
	private final TableIdentityRegistry tableIds;

	/**
	 * Creates an experiment registry.
	 * @param experiment
	 *            Name of the experiment.
	 */
	public ExperimentRegistries(final String experiment) {
		this.experiment = experiment;
		this.cnr = new ChannelNameRegistry();
		this.queries = new QueryRegistry();
		this.tableDefs = new TableRegistry();
		this.tableIds = new TableIdentityRegistry();
		this.eventIds = new EventIdGenerator();
	}

	/**
	 * @param experiment
	 *            Name of the experiment.
	 * @param cnr
	 *            Channel name {@link ChannelNameRegistry registry} for the
	 *            experiment.
	 * @param tableDefs
	 *            Table {@link TableRegistry registry} for the experiment.
	 * @param tableIds
	 *            Table identity {@link TableIdentityRegistry registry} for the
	 *            experiment.
	 * @param eventIds
	 *            Generate unique ids for events.
	 */
	public ExperimentRegistries(final String experiment,
			final ChannelNameRegistry cnr, final TableRegistry tableDefs,
			final TableIdentityRegistry tableIds,
			final EventIdGenerator eventIds) {
		this.experiment = experiment;
		this.cnr = cnr;
		this.tableDefs = tableDefs;
		this.tableIds = tableIds;
		this.eventIds = eventIds;
		this.queries = new QueryRegistry();
	}

	/**
	 * @return the channel name registry
	 */
	public final ChannelNameRegistry getCnr() {
		return cnr;
	}

	/**
	 * @return the eventIds
	 */
	public final EventIdGenerator getEventIds() {
		return eventIds;
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
	 * @return the table registry
	 */
	public final TableRegistry getTableDefs() {
		return tableDefs;
	}

	/**
	 * @return the table identity registry
	 */
	public final TableIdentityRegistry getTableIds() {
		return tableIds;
	}
	/**
	 * 
	 *@return A definer based on the registry references.
	 */
	public final TableDefiner createTableDefiner() {
		return new TableDefiner(experiment, cnr, tableIds, tableDefs);
	}
}
