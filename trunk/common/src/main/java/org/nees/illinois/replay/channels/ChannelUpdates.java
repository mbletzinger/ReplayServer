package org.nees.illinois.replay.channels;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.TableType;


public class ChannelUpdates {

	private final ChannelNameRegistry cnr;

	public ChannelUpdates(ChannelNameRegistry cnr) {
		super();
		this.cnr = cnr;
	}

	/**
	 * @return the cnr
	 */
	public ChannelNameRegistry getCnr() {
		return cnr;
	}

	public List<String> lookupChannels(TableType table, List<String> channels) {
		List<String> result = new ArrayList<String>();
		for (String c : channels) {
			String dc = cnr.addChannel(table, c);
			result.add(dc);
		}
		return result;
	}

}