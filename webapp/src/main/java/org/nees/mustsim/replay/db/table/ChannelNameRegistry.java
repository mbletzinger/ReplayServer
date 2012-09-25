package org.nees.mustsim.replay.db.table;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelNameRegistry {

	private long afterLastChannel = 1;

	private final Map<String, String> names = new ConcurrentHashMap<String, String>();

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

	public String getId(String channel) {
		return names.get(channel);
	}

	public void init(Map<String, String> values) {
		names.clear();
		names.putAll(values);
	}

	private synchronized String newChannel(TableType table) {
		String result = table.toString().toLowerCase(null) + "_channel";
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
	
	public Map<String, String> getClone() {
		Map<String, String> result = new HashMap<String, String>();
		result.putAll(names);
		return result;
	}
}
