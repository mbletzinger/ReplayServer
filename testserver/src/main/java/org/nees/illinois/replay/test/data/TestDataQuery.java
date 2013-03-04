package org.nees.illinois.replay.test.data;

import java.util.List;

import org.nees.illinois.replay.data.DataQueryI;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.StepNumber;
import org.nees.illinois.replay.registries.ChannelNameRegistry;
import org.nees.illinois.replay.registries.ExperimentRegistries;
import org.nees.illinois.replay.registries.SavedQuery;
import org.nees.illinois.replay.test.utils.DatasetDirector;
import org.nees.illinois.replay.test.utils.ChannelDataTestingLists.ChannelListType;
import org.nees.illinois.replay.test.utils.DatasetDirector.QueryTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class TestDataQuery implements DataQueryI {

	private ExperimentRegistries er;
	private final DatasetDirector dd = new DatasetDirector();

	private final Logger log = LoggerFactory.getLogger(TestDataQuery.class);

	@Inject
	public TestDataQuery() {
		super();
	}

	@Override
	public DoubleMatrix doQuery(String name) {
		log.debug("Name query \"" + name + "\"");
		return dd.generate(QueryTypes.Step, ChannelListType.valueOf(name));
	}

	@Override
	public DoubleMatrix doQuery(String name, double start) {
		log.debug("Name query \"" + name + "\" with start " + start);
		return dd.generate(QueryTypes.ContWithStart, ChannelListType.valueOf(name));
	}

	@Override
	public DoubleMatrix doQuery(String name, double start, double stop) {
		log.debug("Name query \"" + name + "\" with start " + start
				+ " & stop " + stop);
		return dd.generate(QueryTypes.ContWithStop, ChannelListType.valueOf(name));
	}

	@Override
	public DoubleMatrix doQuery(String name, StepNumber start) {
		log.debug("Name query \"" + name + "\" with start step " + start);
		return dd.generate(QueryTypes.StepWithStart, ChannelListType.valueOf(name));
	}

	@Override
	public DoubleMatrix doQuery(String name, StepNumber start, StepNumber stop) {
		log.debug("Name query \"" + name + "\" with start step " + start
				+ " & stop step " + stop);
		return dd.generate(QueryTypes.StepWithStop, ChannelListType.valueOf(name));
	}

	@Override
	public boolean setQuery(String name, List<String> channels) {

		if (er == null) {// Check to make sure restlet code sets the experiment
							// name
			log.error("Experiment name is not set");
			return false;
		}
		if (er.getExperiment() == null) {// Check to make sure restlet code sets
											// the experiment name
			log.error("Experiment name is not set");
			return false;
		}
		ChannelNameRegistry cnr = er.getCnrClone();
		er.getQueries().setQuery(name, RateType.CONT,
				new SavedQuery(channels, name, cnr, RateType.CONT));
		er.getQueries().setQuery(name, RateType.STEP,
				new SavedQuery(channels, name, cnr, RateType.STEP));
		return true;
	}

	@Override
	public void setExperiment(ExperimentRegistries er) {
		this.er = er;
	}

	@Override
	public ExperimentRegistries getExperiment() {
		return er;
	}

	@Override
	public boolean isQuery(String name) {
		return er.getQueries().getQuery(name, RateType.CONT) != null;
	}
}
