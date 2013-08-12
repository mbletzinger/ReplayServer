package org.nees.illinois.replay.db.events;

import java.util.List;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.events.EventCreator;
import org.nees.illinois.replay.events.EventI;
import org.nees.illinois.replay.events.EventListI;
import org.nees.illinois.replay.subresource.EventSubResourceI;

import com.google.inject.Inject;

/**
 * Subresource for dealing with events.
 * @author Michael Bletzinger
 */
public class DbEvents implements EventSubResourceI {
	/**
	 * Experiment scoped registries.
	 */
	private ExperimentRegistries er;
	/**
	 * Database operations.
	 */
	private final EventsTableOps eto;

	@Override
	public final void setExperiment(final ExperimentRegistries experiment) {
		er = experiment;
	}

	@Override
	public final ExperimentRegistries getExperiment() {
		return er;
	}

	@Override
	public final EventI createEvent(final String type, final double time,
			final String name, final String description, final String source) {
		EventCreator ec = new EventCreator();
		EventI event = ec.createEvent(type, time, name, description, source,
				null);
		eto.add(event);
		return event;
	}

	/**
	 * @param pools
	 *            Database connections.
	 *     @param er
	 *     Experiment registries.
	 */
	@Inject
	public DbEvents(final DbPools pools, final ExperimentRegistries er) {
		this.er = er;
		this.eto = new EventsTableOps(pools, er.getExperiment());
	}

	@Override
	public final EventListI getEvents(final List<String> names,
			final String source) {
		EventQueries queries = eto.getQueries();
		EventListI result = queries.getEvents(names, source);
		queries.close();
		return result;
	}

	@Override
	public final EventListI getEvents(final String start, final String stop,
			final String source) {
		EventQueries queries = eto.getQueries();
		EventListI result =  queries.getEvents(start, stop, source);
		queries.close();
		return result;
	}

}
