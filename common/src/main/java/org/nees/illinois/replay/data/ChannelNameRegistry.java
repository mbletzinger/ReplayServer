package org.nees.illinois.replay.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChannelNameRegistry {

	private long afterLastChannel = 1;

	private final Map<String, String> names = new HashMap<String, String>();

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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "";
		boolean first = true;
		for(String k : getNames()) {
			result += (first ? "" : ", ") + k +" = " + names.get(k);
			first = false;
		}
		return result + " last = " + afterLastChannel;
	}
}
