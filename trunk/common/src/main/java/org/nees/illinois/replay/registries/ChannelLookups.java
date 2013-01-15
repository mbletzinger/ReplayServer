package org.nees.illinois.replay.registries;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.TableType;



public class ChannelLookups {

	private final ChannelNameRegistry cnr;

	
	public ChannelLookups() {
		cnr = new ChannelNameRegistry();
	}

	public ChannelLookups(ChannelNameRegistry cnr) {
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
	
	public ChannelLookups clone() {
		ChannelNameRegistry clone = new ChannelNameRegistry();
		clone.init(cnr.getClone(), cnr.getAfterLastChannel());
		return new ChannelLookups(clone);
	}

}