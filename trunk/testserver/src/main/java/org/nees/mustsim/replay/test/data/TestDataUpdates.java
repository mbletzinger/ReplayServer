package org.nees.mustsim.replay.test.data;

import java.util.List;

import org.nees.mustsim.replay.channels.ChannelNameRegistry;
import org.nees.mustsim.replay.channels.ChannelUpdates;
import org.nees.mustsim.replay.data.DataUpdatesI;
import org.nees.mustsim.replay.data.RateType;
import org.nees.mustsim.replay.data.TableType;

public class TestDataUpdates implements DataUpdatesI {

	/**
	 * @return the data
	 */
	public double[][] getData() {
		return data;
	}

	private double [][] data;
	/**
	 * @return the cu
	 */
	public ChannelUpdates getCu() {
		return cu;
	}

	private final ChannelUpdates cu;
	
	@Override
	public boolean createTable(TableType table, List<String> channels) {
		cu.lookupChannels(table, channels);
		return true;
	}

	@Override
	public boolean removeTable(TableType table) {
		return true;
	}

	@Override
	public boolean update(TableType table, RateType rate, double[][] data) {
		this.data = data;
		return true;
	}

	public TestDataUpdates(ChannelNameRegistry cnr) {
		super();
		this.cu = new ChannelUpdates(cnr);
	}

}
