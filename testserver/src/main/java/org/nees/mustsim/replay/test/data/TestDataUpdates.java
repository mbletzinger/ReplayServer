package org.nees.mustsim.replay.test.data;

import java.util.List;

import org.nees.illinois.replay.channels.ChannelNameRegistry;
import org.nees.illinois.replay.channels.ChannelUpdates;
import org.nees.illinois.replay.data.DataUpdatesI;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.TableType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class TestDataUpdates implements DataUpdatesI {

	private final ChannelUpdates cu;
	
	private double[][] data;

	private String experiment;

	private final Logger log = LoggerFactory.getLogger(TestDataUpdates.class);

	@Inject
	public TestDataUpdates(ChannelNameRegistry cnr) {
		super();
		this.cu = new ChannelUpdates(cnr);
	}

	@Override
	public boolean createTable(TableType table, List<String> channels) {

		if(experiment == null) {// Check to make sure restlet code sets the experiment name
			log.error("Experiment name is not set");
			return false;
		}

		cu.lookupChannels(table, channels);
		return true;
	}

	/**
	 * @return the cu
	 */
	public ChannelUpdates getCu() {
		return cu;
	}

	/**
	 * @return the data
	 */
	public double[][] getData() {
		return data;
	}

	@Override
	public String getExperiment() {
		return experiment;
	}

	@Override
	public boolean removeTable(TableType table) {
		return true;
	}

	@Override
	public void setExperiment(String experiment) {
		this.experiment = experiment;
	}

	@Override
	public boolean update(TableType table, RateType rate, double[][] data) {
		if(experiment == null) {// Check to make sure restlet code sets the experiment name
			log.error("Experiment name is not set");
			return false;
		}
		this.data = data;
		return true;
	}

}
