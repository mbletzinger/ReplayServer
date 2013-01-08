package org.nees.mustsim.replay.test.data;

import java.util.List;

import org.nees.illinois.replay.data.ChannelNameRegistry;
import org.nees.illinois.replay.data.DataQueryI;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.QueryRegistry;
import org.nees.illinois.replay.data.QuerySpec;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.StepNumber;
import org.nees.illinois.replay.test.utils.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class TestDataQuery implements DataQueryI {

	private final ChannelNameRegistry cnr;

	private final QueryRegistry contQr = new QueryRegistry();

	private final QueryRegistry stepQr = new QueryRegistry();

	private String experiment;
	
	private final Logger log = LoggerFactory.getLogger(TestDataQuery.class);

	@Inject
	public TestDataQuery(ChannelNameRegistry cnr) {
		super();
		this.cnr = cnr;
	}

	@Override
	public DoubleMatrix doQuery(String name) {
		log.debug("Name query \"" + name + "\"");
		return generate(name, 40, RateType.STEP);
	}

	@Override
	public DoubleMatrix doQuery(String name, double start) {
		log.debug("Name query \"" + name + "\" with start " +  start);
		return generate(name, 20, RateType.CONT);
	}

	@Override
	public DoubleMatrix doQuery(String name, double start, double stop) {
		log.debug("Name query \"" + name + "\" with start " +  start + " & stop " + stop);
		return generate(name, 10, RateType.CONT);
	}

	@Override
	public DoubleMatrix doQuery(String name, StepNumber start) {
		log.debug("Name query \"" + name + "\" with start step " +  start);
		return generate(name, 15, RateType.STEP);
	}

	@Override
	public DoubleMatrix doQuery(String name, StepNumber start, StepNumber stop) {
		log.debug("Name query \"" + name + "\" with start step " +  start + " & stop step " + stop);
		return generate(name, 5, RateType.STEP);
	}

	private DoubleMatrix generate(String name, int rows, RateType rate) {
		if(experiment == null) {// Check to make sure restlet code sets the experiment name
			log.error("Experiment name is not set");
			return null;
		}
		// Force a runtime error
		if(experiment.contains("ERR")) {
			throw new RuntimeException("Help me I died");
		}
		QuerySpec qs = rate.equals(RateType.CONT) ? contQr.getQuery(name)
				: stepQr.getQuery(name);
		double[][] data = DataGenerator.initData(rate, rows, qs.getNoc()
				.getNumber(true), 0.5);
		DoubleMatrix result = new DoubleMatrix(data);
		return result;

	}

	/**
	 * @return the cnr
	 */
	public ChannelNameRegistry getCnr() {
		return cnr;
	}

	/**
	 * @return the contQr
	 */
	public QueryRegistry getContQr() {
		return contQr;
	}

	/**
	 * @return the stepQr
	 */
	public QueryRegistry getStepQr() {
		return stepQr;
	}

	@Override
	public boolean isQuery(String name) {
		return stepQr.getQuery(name) != null;
	}

	@Override
	public boolean setQuery(String name, List<String> channels) {

		if(experiment == null) {// Check to make sure restlet code sets the experiment name
			log.error("Experiment name is not set");
			return false;
		}
		// Force a runtime error
		if(experiment.contains("ERR")) {
			throw new RuntimeException("Help me I died");
		}
		contQr.setQuery(name, new QuerySpec(channels, name, cnr, RateType.CONT));
		stepQr.setQuery(name, new QuerySpec(channels, name, cnr, RateType.STEP));
		return true;
	}

	@Override
	public void setExperiment(String experiment) {
		this.experiment = experiment;
	}

	@Override
	public String getExperiment() {
		return experiment;
	}
}
