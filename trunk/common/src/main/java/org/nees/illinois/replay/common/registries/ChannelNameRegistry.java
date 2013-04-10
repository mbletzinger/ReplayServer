package org.nees.illinois.replay.common.registries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.nees.illinois.replay.data.TableType;

/**
 * Class which maintains an experiment scope registry of all data channel names.
 * Each name is mapped to a database format friendly name. The class performs
 * lookups to switch between these name mappings.
 * 
 * @author Michael Bletzinger
 */
public class ChannelNameRegistry {

	/**
	 * Next index to use for a database friendly channel name
	 */
	private long afterLastChannel = 1;

	/**
	 * Map of data channel names to their database friendly counterparts. The
	 * map is concurrent because an experiment is expected to handle several
	 * concurrent requests.
	 */
	private final ConcurrentMap<String, String> names = new ConcurrentHashMap<String, String>();

	public String addChannel(TableType table, String channel) {
		String result = getId(channel);
		if (result == null) {
			result = newChannel(table);
			names.put(channel, result);
		}
		return result;
	}

	/**
	 * @return the afterLastChannel
	 */
	public synchronized long getAfterLastChannel() {
		return afterLastChannel;
	}

	public Map<String, String> getClone() {
		Map<String, String> result = new HashMap<String, String>();
		result.putAll(names);
		return result;
	}

	public String getId(String channel) {
		return names.get(channel);
	}

	/**
	 * @return the names
	 */
	public List<String> getNames() {
		List<String> keys = new ArrayList<String>();
		keys.addAll(names.keySet());
		Collections.sort(keys);
		return keys;
	}

	public void init(Map<String, String> values, long alc) {
		names.clear();
		names.putAll(values);
		afterLastChannel = alc;
	}

	public String getName(String id) {
		for (String key : names.keySet()) {
			if (id.equals(names.get(key))) {
				return key;
			}
		}
		return null;
	}

	private synchronized String newChannel(TableType table) {
		String result = table.toString().toLowerCase() + "_channel";
		result += afterLastChannel;
		afterLastChannel++;
		return result;
	}

	/**
	 * @param afterLastChannel
	 *            the afterLastChannel to set
	 */
	public synchronized void setAfterLastChannel(long afterLastChannel) {
		this.afterLastChannel = afterLastChannel;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "";
		boolean first = true;
		for (String k : getNames()) {
			result += (first ? "\n\t" : ",\n\t") + k + " = " + names.get(k);
			first = false;
		}
		return result + " last = " + afterLastChannel;
	}

	public List<String> names2Ids(List<String> list) {
		List<String> result = new ArrayList<String>();
		for (String n : list) {
			result.add(getId(n));
		}
		return result;
	}

	public List<String> ids2Names(List<String> list) {
		List<String> result = new ArrayList<String>();
		for (String n : list) {
			result.add(getId(n));
		}
		return result;
	}
}
