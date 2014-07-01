/**
 *
 */
package org.nees.illinois.replay.common.registries;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.common.types.TableDef;
import org.nees.illinois.replay.common.types.TableDefinitionI;

/**
 * Create a table {@link TableDefinitionI definition} and adds it to the
 * {@link TableIdentityRegistry id} and {@link TableRegistry table} registries.
 * @author Michael Bletzinger
 */
public class TableDefiner {
	/**
	 * {@link ChannelNameRegistry Channel name registry} for the experiment.
	 */
	private final ChannelNameRegistry cnr;
	/**
	 * {@link TableRegistry Table registry} for the experiment.
	 */
	private final TableRegistry tr;

	/**
	 * @param cnr
	 *            {@link ChannelNameRegistry Channel name registry} for the
	 *            experiment.
	 * @param tr
	 *            {@link TableRegistry Table registry} for the experiment.
	 */
	public TableDefiner(final ChannelNameRegistry cnr, final TableRegistry tr) {
		this.cnr = cnr;
		this.tr = tr;
	}

	/**
	 * Establish a table.
	 * @param source
	 *            name of the source where the data came from.
	 * @param type
	 *            Table {@link TableType type}.
	 * @param channels
	 *            List of channels.
	 * @return New table {@link TableDefinitionI definition}.
	 */
	public final TableDefinitionI define(final String source,
			final TableType type, final List<String> channels) {
		String tableid = lookupTableId(source, type);
		List<String> dataColumns = lookupChannels(type, channels);
		TableDefinitionI result = new TableDef(dataColumns, tableid, source);
		tr.setTable(source, result);
		return result;
	}

	/**
	 * Lookup database friendly names for all of the channels.
	 * @param type
	 *            Database friendly table name.
	 * @param channels
	 *            List of channels.
	 * @return Database friendly list.
	 */
	private List<String> lookupChannels(final TableType type,
			final List<String> channels) {
		List<String> result = new ArrayList<String>();
		for (String c : channels) {
			String id = cnr.addChannel(type, c);
			result.add(id);
		}
		return result;
	}

	/**
	 * Establish the identity for a table.
	 * @param name
	 *            Table name.
	 * @param type
	 *            Table {@link TableType type}.
	 * @return Table identity
	 */
	private String lookupTableId(final String name, final TableType type) {
		return type.name() + "_" + name;
	}
}
