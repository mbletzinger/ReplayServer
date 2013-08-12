package org.nees.illinois.replay.common.registries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.common.types.CompositeQuery;
import org.nees.illinois.replay.common.types.CompositeQueryI;
import org.nees.illinois.replay.common.types.TableDef;
import org.nees.illinois.replay.common.types.TableDefinitionI;

/**
 * Create a query {@link CompositeQueryI definition} and adds it to the
 * {@link QueryRegistry query}.
 * @author Michael Bletzinger
 */
public class QueryDefiner {
	/**
	 * {@link ChannelNameRegistry Channel name registry} for the experiment.
	 */
	private final ChannelNameRegistry cnr;

	/**
	 * Resulting query {@link CompositeQueryI definition}.
	 */
	private CompositeQueryI def;

	/**
	 * Query Registry.
	 */
	private final QueryRegistry qr;

	/**
	 * {@link TableRegistry Table registry} for the experiment.
	 */
	private final TableRegistry tr;
	/**
	 * Class used to construct query {@link CompositeQueryI definitions}.
	 * @param cnr
	 *            {@link ChannelNameRegistry Channel name registry} for the
	 *            experiment
	 * @param tr
	 *            {@link TableRegistry Table registry} for the experiment.
	 * @param qr
	 *            Query Registry.
	 */
	public QueryDefiner(final ChannelNameRegistry cnr, final TableRegistry tr,
			final QueryRegistry qr) {
		this.cnr = cnr;
		this.tr = tr;
		this.qr = qr;
	}
	/**
	 * Create a new query {@link CompositeQueryI definition}.
	 * @param qname
	 *            Name of query.
	 * @param channels
	 *            List of channels.
	 * @return The new query {@link CompositeQueryI definition}.
	 */
	public final CompositeQueryI define(final String qname,
			final List<String> channels) {
		List<String> dbnames = cnr.names2Ids(channels);
		def = new CompositeQuery(qname, dbnames);
		for (String s : dbnames) {
			updateQueryTables(s);
		}
		qr.setQuery(null, def);
		return def;
	}
	/**
	 * @return the cnr
	 */
	public final ChannelNameRegistry getCnr() {
		return cnr;
	}

	/**
	 * @return the qr
	 */
	public final QueryRegistry getQr() {
		return qr;
	}

	/**
	 * @return the tr
	 */
	public final TableRegistry getTr() {
		return tr;
	}

	/**
	 * Lookup a channel name (the database friendly column header version) and
	 * add its table to the query definition.
	 * @param dbchannel
	 *            Database friendly channel name.
	 */
	private void updateQueryTables(final String dbchannel) {
		Map<String, TableDefinitionI> tmap = def.getTableQueries();
		String ti = tr.findTable(dbchannel);
		TableDefinitionI c = tmap.get(ti);
		if (c == null) {
			c = new TableDef(new ArrayList<String>(), ti);
			tmap.put(ti, c);
		}
		c.addDataColumn(dbchannel);
	}

}
