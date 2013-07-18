/**
 *
 */
package org.nees.illinois.replay.common.registries;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.common.types.TableColumns;
import org.nees.illinois.replay.common.types.TableColumnsI;
import org.nees.illinois.replay.common.types.TableIdentityI;

/**
 * Create a table {@link TableColumnsI definition} and adds it to the
 * {@link TableIdentityRegistry id} and {@link TableRegistry table} registries.
 * @author Michael Bletzinger
 */
public class TableDefiner {
	/**
	 * {@link ChannelNameRegistry Channel name registry} for the experiment.
	 */
	private final ChannelNameRegistry cnr;
	/**
	 * The experiment name.
	 */
	private final String experiment;
	/**
	 * {@link TableIdentityRegistry Table ID registry} for the experiment.
	 */
	private final TableIdentityRegistry tnr;
	/**
	 * {@link TableRegistry Table registry} for the experiment.
	 */
	private final TableRegistry tr;

	/**
	 * @param experiment
	 *            The experiment name.
	 * @param cnr
	 *            {@link ChannelNameRegistry Channel name registry} for the
	 *            experiment.
	 * @param tnr
	 *            {@link TableIdentityRegistry Table ID registry} for the
	 *            experiment.
	 * @param tr
	 *            {@link TableRegistry Table registry} for the experiment.
	 */
	public TableDefiner(final String experiment,
			final ChannelNameRegistry cnr, final TableIdentityRegistry tnr,
			final TableRegistry tr) {
		this.experiment = experiment;
		this.cnr = cnr;
		this.tnr = tnr;
		this.tr = tr;
	}

	/**
	 * Lookup database friendly names for all of the channels.
	 * @param tablename
	 *            Database friendly table name.
	 * @param channels
	 *            List of channels.
	 * @return Database friendly list.
	 */
	private List<String> lookupChannels(final String tablename,
			final List<String> channels) {
		List<String> result = new ArrayList<String>();
		for (String c : channels) {
			String id = cnr.addChannel(tablename, c);
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
	 * @return Table {@link TableIdentityI identity}.
	 */
	private TableIdentityI lookupTableId(final String name,
			final TableType type) {
		return tnr.addTable(experiment, name, type);
	}

	/**
	 * Establish a table.
	 * @param name
	 *            Table name
	 * @param type
	 *            Table {@link TableType type}.
	 * @param channels
	 *            List of channels.
	 * @return New table {@link TableColumnsI definition}.
	 */
	public final TableColumnsI define(final String name,
			final TableType type, final List<String> channels) {
		TableIdentityI tableid = lookupTableId(name, type);
		List<String> dataColumns = lookupChannels(tableid.getDbName(), channels);
		TableColumnsI result = new TableColumns(dataColumns, tableid);
		tr.setTable(name, result);
		return result;
	}
}
