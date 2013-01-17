package org.nees.illinois.replay.test.data;

import java.util.List;

import org.nees.illinois.replay.data.DataQueryI;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.StepNumber;
import org.nees.illinois.replay.registries.ChannelNameRegistry;
import org.nees.illinois.replay.registries.ExperimentRegistries;
import org.nees.illinois.replay.registries.QuerySpec;
import org.nees.illinois.replay.test.utils.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class TestDataQuery implements DataQueryI {

	private ExperimentRegistries er;

	private final Logger log = LoggerFactory.getLogger(TestDataQuery.class);

	@Inject
	public TestDataQuery() {
		super();
	}

	@Override
	public DoubleMatrix doQuery(String name) {
		log.debug("Name query \"" + name + "\"");
		return generate(name, 40, RateType.STEP);
	}

	@Override
	public DoubleMatrix doQuery(String name, double start) {
		log.debug("Name query \"" + name + "\" with start " + start);
		return generate(name, 20, RateType.CONT);
	}

	@Override
	public DoubleMatrix doQuery(String name, double start, double stop) {
		log.debug("Name query \"" + name + "\" with start " + start
				+ " & stop " + stop);
		return generate(name, 10, RateType.CONT);
	}

	@Override
	public DoubleMatrix doQuery(String name, StepNumber start) {
		log.debug("Name query \"" + name + "\" with start step " + start);
		return generate(name, 15, RateType.STEP);
	}

	@Override
	public DoubleMatrix doQuery(String name, StepNumber start, StepNumber stop) {
		log.debug("Name query \"" + name + "\" with start step " + start
				+ " & stop step " + stop);
		return generate(name, 5, RateType.STEP);
	}

	private DoubleMatrix generate(String name, int rows, RateType rate) {
		if (er == null) {// Check to make sure restlet code sets the experiment
							// name
			log.error("Experiment name is not set");
			return null;
		}
		if (er.getExperiment() == null) {// Check to make sure restlet code sets
											// the experiment name
			log.error("Experiment name is not set");
			return null;
		}
		QuerySpec qs = er.getQueries().getQuery(name, rate);
		double[][] data = DataGenerator.initData(rate, rows, qs.getNoc()
				.getNumber(true), 0.5);
		DoubleMatrix result = new DoubleMatrix(data);
		return result;

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
				new QuerySpec(channels, name, cnr, RateType.CONT));
		er.getQueries().setQuery(name, RateType.STEP,
				new QuerySpec(channels, name, cnr, RateType.STEP));
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
