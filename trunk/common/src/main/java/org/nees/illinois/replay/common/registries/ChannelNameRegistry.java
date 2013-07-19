package org.nees.illinois.replay.common.registries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Class which maintains an experiment scope registry of all data channel names.
 * Each name is mapped to a database format friendly name. The class performs
 * lookups to switch between these name mappings.
 * @author Michael Bletzinger
 */
public class ChannelNameRegistry implements Cloneable {

	/**
	 * Next index to use for a database friendly channel name.
	 */
	private long afterLastChannel = 1;

	/**
	 * Map of data channel names to their database friendly counterparts. The
	 * map is concurrent because an experiment is expected to handle several
	 * concurrent requests.
	 */
	private final ConcurrentMap<String, String> namesMap = new ConcurrentHashMap<String, String>();

	/**
	 * Adds a channel name to the registry.
	 * @param table
	 *            Associated database table.
	 * @param channel
	 *            Name to be added.
	 * @return Database friendly version of the name.
	 */
	public final String addChannel(final String table, final String channel) {
		String result = getId(channel);
		if (result == null) {
			result = newChannel(table);
			namesMap.put(channel, result);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final Object clone() {
		ChannelNameRegistry result = new ChannelNameRegistry();
		result.init(namesMap, afterLastChannel);
		return result;
	}

	/**
	 * @return the afterLastChannel
	 */
	public final synchronized long getAfterLastChannel() {
		return afterLastChannel;
	}

	/**
	 * Return the database friendly name.
	 * @param channel
	 *            Experiment channel name.
	 * @return Database friendly version.
	 */
	public final String getId(final String channel) {
		return namesMap.get(channel);
	}

	/**
	 * Reverse lookup a database friendly channel id.
	 * @param id
	 *            channel id.
	 * @return Original channel name.
	 */
	public final String getName(final String id) {
		for (String key : namesMap.keySet()) {
			if (id.equals(namesMap.get(key))) {
				return key;
			}
		}
		return null;
	}

	/**
	 * @return the names
	 */
	public final List<String> getNames() {
		List<String> keys = new ArrayList<String>();
		keys.addAll(namesMap.keySet());
		Collections.sort(keys);
		return keys;
	}

	/**
	 * @return the names map. Don't use this unless you are needing the entire map.
	 */
	public final Map<String, String> getNamesMap() {
		return namesMap;
	}

	/**
	 * Maps a list of database friendly channel id's to channel names using
	 * reverse lookup.
	 * @param list
	 *            List of id's.
	 * @return List of names.
	 */
	public final List<String> ids2Names(final List<String> list) {
		List<String> result = new ArrayList<String>();
		for (String n : list) {
			result.add(getId(n));
		}
		return result;
	}

	/**
	 * Initialization function used to synchronize with the database. For
	 * internal use only.
	 * @param values
	 *            Map of channel names.
	 * @param alc
	 *            Last unused index.
	 */

	public final void init(final Map<String, String> values, final long alc) {
		namesMap.clear();
		namesMap.putAll(values);
		afterLastChannel = alc;
	}

	/**
	 * Maps a list of database channel names to friendly channel id's.
	 * @param list
	 *            List of names.
	 * @return List of id's.
	 */
	public final List<String> names2Ids(final List<String> list) {
		List<String> result = new ArrayList<String>();
		for (String n : list) {
			result.add(getId(n));
		}
		return result;
	}

	/**
	 * Create a database friendly channel name.
	 * @param table
	 *            Name of database table.
	 * @return New database friendly id.
	 */
	private synchronized String newChannel(final String table) {
		String result = table.toLowerCase() + "_channel";
		result += afterLastChannel;
		afterLastChannel++;
		return result;
	}

	/**
	 * @param afterLastChannel
	 *            the afterLastChannel to set
	 */
	public final synchronized void setAfterLastChannel(
			final long afterLastChannel) {
		this.afterLastChannel = afterLastChannel;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		String result = "";
		boolean first = true;
		for (String k : getNames()) {
			result += (first ? "\n\t" : ",\n\t") + k + " = " + namesMap.get(k);
			first = false;
		}
		return result + " last = " + afterLastChannel;
	}
}
