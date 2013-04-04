package org.nees.illinois.replay.test.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.StepNumber;
import org.nees.illinois.replay.registries.ChannelNameRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class DatasetDirector {

	public enum ExperimentNames {
		HybridMasonry1, HybridMasonry2
	}

	public enum QueryParaTypes {
		ContWithStart, ContWithStop, Step, StepWithStart, StepWithStop
	}

	public class TimeSpec {
		private final Object start;
		private final Object stop;

		public TimeSpec(Object start, Object stop) {
			super();
			this.start = start;
			this.stop = stop;
		}

		/**
		 * @return the start
		 */
		public Object getStart() {
			return start;
		}

		/**
		 * @return the stop
		 */
		public Object getStop() {
			return stop;
		}

	}

	private final ChannelListTestMaps cltm;

	private final ChannelNameRegistry expectedCnr = new ChannelNameRegistry();

	private final ExperimentNames experiment;

	private final Logger log = LoggerFactory.getLogger(DatasetDirector.class);;

	private final Map<QueryParaTypes, RateType> queryRates = new HashMap<QueryParaTypes, RateType>();;

	private final Map<QueryParaTypes, Integer> queryTableSize = new HashMap<QueryParaTypes, Integer>();

	private final Map<QueryParaTypes, TimeSpec> queryTimes = new HashMap<DatasetDirector.QueryParaTypes, DatasetDirector.TimeSpec>();
	{
		queryTableSize.put(QueryParaTypes.ContWithStart, 20);
		queryTableSize.put(QueryParaTypes.ContWithStop, 10);
		queryTableSize.put(QueryParaTypes.Step, 40);
		queryTableSize.put(QueryParaTypes.StepWithStart, 15);
		queryTableSize.put(QueryParaTypes.StepWithStop, 5);

		queryRates.put(QueryParaTypes.ContWithStart, RateType.CONT);
		queryRates.put(QueryParaTypes.ContWithStop, RateType.CONT);
		queryRates.put(QueryParaTypes.Step, RateType.STEP);
		queryRates.put(QueryParaTypes.StepWithStart, RateType.STEP);
		queryRates.put(QueryParaTypes.StepWithStop, RateType.STEP);

		queryTimes.put(QueryParaTypes.ContWithStart, new TimeSpec(new Double(
				222.0), null));
		queryTimes.put(QueryParaTypes.ContWithStop, new TimeSpec(new Double(
				222.0), new Double(223.0)));
		queryTimes.put(QueryParaTypes.Step, new TimeSpec(null, null));
		queryTimes.put(QueryParaTypes.StepWithStart, new TimeSpec(
				new StepNumber(1.0, 0.0, 1.0), null));
		queryTimes.put(QueryParaTypes.StepWithStop, new TimeSpec(
				new StepNumber(1.0, 0.0, 1.0), new StepNumber(3.0, 22.0, 1.0)));
	}
	public DatasetDirector(ExperimentNames experiment) {
		super();
		this.experiment = experiment;
		this.cltm = new ChannelListTestMaps(experiment.equals(ExperimentNames.HybridMasonry2), experiment.name());
		this.cltm.fillCnr(expectedCnr);
	}
	public void checkChannels(ChannelListType typ,
			List<String> channels) {
		checkChannels(cltm.getChannels(typ), channels);
	}
	public void checkChannels(List<String> expected, List<String> actual) {
		log.debug("CHECKING  " + expected + "\nWITH " + actual);
		Assert.assertEquals(actual.size(), expected.size());
		for (int i = 0; i < actual.size(); i++) {
			Assert.assertEquals(actual.get(i), expected.get(i));
		}
	}
	public void checkData(ExperimentNames experiment, QueryParaTypes qt,
			ChannelListType quy, DoubleMatrix data) {
		DoubleMatrix expected = generate(experiment, qt, quy);
		log.debug("For " + qt + " and " + quy);
		log.debug("CHECKING  expected " + expected + "\nWITH actual " + data);
		DoubleArrayDataGenerator
				.compareData(data.getData(), expected.getData());
	}
	public void checkExpectedCnr(ExperimentNames experiment,
			ChannelNameRegistry cnr) {
		log.debug("CHECKING  expected " +expectedCnr + "\nWITH actual " + cnr);
		Assert.assertEquals(cnr.toString(), expectedCnr.toString());
	}

	public DoubleMatrix generate(ExperimentNames experiment, QueryParaTypes qt,
			ChannelListType quy) {
		int row = queryTableSize.get(qt);
		List<String> channels = cltm.getChannels(quy);
		RateType rate = queryRates.get(qt);
		return generate(quy.name(), row, channels.size(), rate);
	}

	private DoubleMatrix generate(String name, int rows, int cols, RateType rate) {
		// log.debug("For " + rate + " query " + name + " creating " + rows +
		// "x"
		// + cols + " matrix");
		DoubleArrayDataGenerator dg = new DoubleArrayDataGenerator(rows, cols,
				0.02, 222.0);
		double[][] data = dg.generate();
		DoubleMatrix result = new DoubleMatrix(data);
		log.debug("For " + rate + " list type " + name + " creating " + result);
		return result;
	}

	public ChannelDataGenerator generateQueryData(ChannelListType quy,
			QueryParaTypes qt, MatrixMixType rowMix) {
		ChannelTestingList qctl = cltm.getChannelLists(quy);
		int row = queryTableSize.get(qt);
		return new ChannelDataGenerator(qctl, rowMix, row);
	}

	/**
	 * @return the cltm
	 */
	public ChannelListTestMaps getCltm() {
		return cltm;
	}

	public ChannelListTestMaps getCltm(ExperimentNames e) {
		return cltm;
	}

	/**
	 * @return the expectedCnr
	 */
	public ChannelNameRegistry getExpectedCnr() {
		return expectedCnr;
	}

	/**
	 * @return the experiment
	 */
	public ExperimentNames getExperiment() {
		return experiment;
	}

	/**
	 * @return the queryRates
	 */
	public Map<QueryParaTypes, RateType> getQueryRates() {
		return queryRates;
	}

	/**
	 * @return the queryTableSize
	 */
	public Map<QueryParaTypes, Integer> getQueryTableSize() {
		return queryTableSize;
	}

	/**
	 * @return the queryTimes
	 */
	public Map<QueryParaTypes, TimeSpec> getQueryTimes() {
		return queryTimes;
	}

	public RateType getRate(QueryParaTypes qt) {
		return queryRates.get(qt);
	}

	public TimeSpec getTimes(QueryParaTypes qt) {
		return queryTimes.get(qt);
	}
}
