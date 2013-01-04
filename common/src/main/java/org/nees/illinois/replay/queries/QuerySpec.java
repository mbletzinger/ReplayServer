package org.nees.illinois.replay.queries;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.ChannelNameRegistry;
import org.nees.illinois.replay.data.NumberOfColumns;
import org.nees.illinois.replay.data.RateType;

public class QuerySpec {

	protected final String name;
	protected final NumberOfColumns noc;
	protected final List<String> queryOrder;

	public QuerySpec(List<String> channelOrder, String name,
			ChannelNameRegistry cnr, RateType rate) {
		super();
		this.queryOrder = new ArrayList<String>();
		for (String c : channelOrder) {
			this.queryOrder.add(cnr.getId(c));
		}
		this.name = name;
		this.noc = new NumberOfColumns(channelOrder.size(), rate);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the noc
	 */
	public NumberOfColumns getNoc() {
		return noc;
	}

	/**
	 * @return the channelOrder
	 */
	public List<String> getQueryOrder() {
		return queryOrder;
	}

}