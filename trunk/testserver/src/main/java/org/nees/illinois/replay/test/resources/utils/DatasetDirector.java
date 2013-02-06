package org.nees.illinois.replay.test.resources.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.StepNumber;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.registries.ChannelNameRegistry;
import org.nees.illinois.replay.test.utils.ChannelLists;
import org.nees.illinois.replay.test.utils.ChannelLists.ChannelListType;
import org.nees.illinois.replay.test.utils.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class DatasetDirector {

	public enum ExperimentNames {
		HybridMasonry1, HybridMasonry2
	};

	public enum QueryTypes {
		ContWithStart, ContWithStop, Step, StepWithStart, StepWithStop
	};

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
	private final ChannelLists cl = new ChannelLists();
	private final ChannelNameRegistry expected2ndCnr = new ChannelNameRegistry();
	private final ChannelNameRegistry expectedCnr = new ChannelNameRegistry();
	private final Map<ExperimentNames, List<ChannelListType>> experimentQueries = new HashMap<ExperimentNames, List<ChannelListType>>();
	private final Map<ExperimentNames, List<ChannelListType>> experimentTables = new HashMap<ExperimentNames, List<ChannelListType>>();
	private final Logger log = LoggerFactory.getLogger(DatasetDirector.class);
	private final Map<QueryTypes, RateType> queryRates = new HashMap<QueryTypes, RateType>();
	private final Map<QueryTypes, Integer> queryTableSize = new HashMap<QueryTypes, Integer>();
	private final Map<QueryTypes, TimeSpec> queryTimes = new HashMap<DatasetDirector.QueryTypes, DatasetDirector.TimeSpec>();

	{
		List<ChannelListType> list = new ArrayList<ChannelListType>();
		list.add(ChannelListType.Query1);
		list.add(ChannelListType.Query2);
		experimentQueries.put(ExperimentNames.HybridMasonry1, list);
		list.clear();
		list.add(ChannelListType.Query3);
		list.add(ChannelListType.Query4);
		experimentQueries.put(ExperimentNames.HybridMasonry2, list);
		list.clear();
		list.add(ChannelListType.OM);
		list.add(ChannelListType.DAQ);
		experimentTables.put(ExperimentNames.HybridMasonry1, list);
		list.clear();
		list.add(ChannelListType.OM2);
		list.add(ChannelListType.DAQ2);
		experimentTables.put(ExperimentNames.HybridMasonry2, list);
		
		queryTableSize.put(QueryTypes.ContWithStart, 20);
		queryTableSize.put(QueryTypes.ContWithStop, 10);
		queryTableSize.put(QueryTypes.Step, 40);
		queryTableSize.put(QueryTypes.StepWithStart, 15);
		queryTableSize.put(QueryTypes.StepWithStop, 5);

		queryRates.put(QueryTypes.ContWithStart, RateType.CONT);
		queryRates.put(QueryTypes.ContWithStop, RateType.CONT);
		queryRates.put(QueryTypes.Step, RateType.STEP);
		queryRates.put(QueryTypes.StepWithStart, RateType.STEP);
		queryRates.put(QueryTypes.StepWithStop, RateType.STEP);
		
		
		queryTimes.put(QueryTypes.ContWithStart, new TimeSpec(new Double(224.23), null));
		queryTimes.put(QueryTypes.ContWithStop, new TimeSpec(new Double(224.23), new Double(230.48)));
		queryTimes.put(QueryTypes.Step, new TimeSpec(null, null));
		queryTimes.put(QueryTypes.StepWithStart, new TimeSpec(new StepNumber(1.0, 22.0, 3.0), null));
		queryTimes.put(QueryTypes.StepWithStop, new TimeSpec(new StepNumber(1.0, 22.0, 3.0), new StepNumber(3.0, 22.0, 1.0)));
	}

	public void addExpectedCnr(ExperimentNames experiment, TableType table,
			List<String> channels) {
		ChannelNameRegistry expected = experiment
				.equals(ExperimentNames.HybridMasonry1) ? expectedCnr
				: expected2ndCnr;
		for (String c : channels) {
			expected.addChannel(table, c);
		}
	}

	public void checkChannels(ChannelListType typ, List<String> channels) {
		checkChannels(cl.getChannels(typ), channels);
	}

	public void checkChannels(List<String> expected, List<String> actual) {
		log.debug("CHECKING  " + expected + "\nWITH " + actual);
		Assert.assertEquals(actual.size(), expected.size());
		for (int i = 0; i < actual.size(); i++) {
			Assert.assertEquals(actual.get(i), expected.get(i));
		}
	}

	public void checkData(QueryTypes qt, ChannelListType quy, DoubleMatrix data) {
		DoubleMatrix expected = generate(qt, quy);
		log.debug("For " + qt + " and " + quy);
		log.debug("CHECKING  expected " + expected + "\nWITH actual " + data);
		DataGenerator.compareData(expected.getData(), data.getData());
	}

	public void checkExpectedCnr(ExperimentNames experiment,
			ChannelNameRegistry cnr) {
		ChannelNameRegistry expected = experiment
				.equals(ExperimentNames.HybridMasonry1) ? expectedCnr
				: expected2ndCnr;
		log.debug("CHECKING  expected " + expected + "\nWITH actual " + cnr);
		Assert.assertEquals(cnr.toString(), expected.toString());
	}

	public DoubleMatrix generate(QueryTypes qt, ChannelListType quy) {
		int row = queryTableSize.get(qt);
		List<String> channels = cl.getChannels(quy);
		RateType rate = queryRates.get(qt);
		return generate(quy.name(), row, channels.size(), rate);
	}

	private DoubleMatrix generate(String name, int rows, int cols, RateType rate) {
//		log.debug("For " + rate + " query " + name + " creating " + rows + "x"
//				+ cols + " matrix");
		double[][] data = DataGenerator.initData(rows, cols, 0.5);
		DoubleMatrix result = new DoubleMatrix(data);
		log.debug("For " + rate + " query " + name + " creating " + result);
		return result;
	}
	public RateType getRate(QueryTypes qt) {
		return queryRates.get(qt);
	}
	public TimeSpec getTimes(QueryTypes qt) {
		return queryTimes.get(qt);
	}
}
