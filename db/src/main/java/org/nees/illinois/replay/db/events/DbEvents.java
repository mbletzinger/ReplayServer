package org.nees.illinois.replay.db.events;

import java.util.List;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.nees.illinois.replay.db.DbPools;
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
	 * Database connection pools.
	 */
	private final DbPools pools;

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
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param pools
	 *            Database connections.
	 */
	@Inject
	public DbEvents(final DbPools pools) {
		this.pools = pools;
	}

	@Override
	public final EventListI getEvents(final List<String> names, final String source) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final EventListI getEvents(final String start, final String stop, final String source) {
		// TODO Auto-generated method stub
		return null;
	}

}
