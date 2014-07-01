package org.nees.illinois.replay.db.events;

import java.util.List;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.nees.illinois.replay.common.registries.TableRegistry;
import org.nees.illinois.replay.common.types.TableDefinitionI;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.events.Event;
import org.nees.illinois.replay.events.EventI;
import org.nees.illinois.replay.events.EventListI;
import org.nees.illinois.replay.subresource.EventSubResourceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

/**
 * Subresource for dealing with events.
 * @author Michael Bletzinger
 */
public class DbEvents implements EventSubResourceI {
	/**
	 * Database operations.
	 */
	private final EventsTableOps eto;

	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory.getLogger(DbEvents.class);

	/**
	 * @param pools
	 *            Database connections.
	 * @param er
	 *            Experiment registries.
	 */
	@Inject
	public DbEvents(final DbPools pools,final ExperimentRegistries er) {
		this.eto = new EventsTableOps(pools, er);
	}

	@Override
	public final EventI createEvent(final double time, final String name,
			final String description, final String source) {
		String tblid = getTableId(source, name);
		if(tblid == null) {
			return null;
		}
		EventI event = new Event(name, time, description,tblid);
		eto.add(event);
		EventQueries queries = eto.getQueries();
		EventListI result = queries.getEvents(name, source);
		queries.close();
		return result.getEvent(0);
	}

	@Override
	public final EventListI getEvents(final List<String> names,
			final String source) {
		String tblid = getTableId(source, names.get(0));
		if(tblid == null) {
			return null;
		}
		EventQueries queries = eto.getQueries();
		EventListI result = queries.getEvents(names, source);
		queries.close();
		return result;
	}

	@Override
	public final EventListI getEvents(final String start, final String stop,
			final String source) {
		EventQueries queries = eto.getQueries();
		EventListI result = queries.getEvents(start, stop, source);
		queries.close();
		return result;
	}

	@Override
	public final ExperimentRegistries getExperiment() {
		return eto.getEr();
	}

	/**
	 * Transform source name into a table id.
	 *@param source name.
	 *@param name of the event.
	 *@return table id.
	 */
	private String getTableId(final String source, final String name) {
		TableRegistry tr = eto.getEr().getTableDefs();
		TableDefinitionI tbl = tr.getTable(source);
		if(tbl == null) {
			log.error("Source " + source + " for event " + name + " does not exist");
			return null;
		}
		return tbl.getTableId();
	}
	@Override
	public final void setExperiment(final ExperimentRegistries experiment) {
		eto.setEr(experiment);
	}
}
