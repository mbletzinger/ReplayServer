package org.nees.illinois.replay.test.data;

import java.util.List;

import org.nees.illinois.replay.data.DataUpdateI;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.registries.ChannelNameManagement;
import org.nees.illinois.replay.registries.ExperimentRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class TestDataUpdates implements DataUpdateI {

	private ChannelNameManagement cu;
	
	private double[][] data;

	private ExperimentRegistries er;

	private final Logger log = LoggerFactory.getLogger(TestDataUpdates.class);

	@Inject
	public TestDataUpdates() {
		super();
	}

	@Override
	public boolean createTable(TableType table, List<String> channels) {

		this.cu = er.getChnlNamesMgmt();

		if(er == null) {// Check to make sure restlet code sets the experiment name
			log.error("Experiment is not set");
			return false;
		}

		if(er.getExperiment() == null) {// Check to make sure restlet code sets the experiment name
			log.error("Experiment name is not set");
			return false;
		}
		
		cu.lookupChannels(table, channels);
		return true;
	}

	/**
	 * @return the cu
	 */
	public ChannelNameManagement getCu() {
		return cu;
	}

	/**
	 * @return the data
	 */
	public double[][] getData() {
		return data;
	}

	@Override
	public ExperimentRegistries getExperiment() {
		return er;
	}

	@Override
	public boolean removeTable(TableType table) {
		return true;
	}

	@Override
	public void setExperiment(ExperimentRegistries er) {
		this.er = er;
	}

	@Override
	public boolean update(TableType table, RateType rate, double[][] data) {
		if(er == null) {// Check to make sure restlet code sets the experiment name
			log.error("Experiment name is not set");
			return false;
		}
		this.data = data;
		return true;
	}

}
