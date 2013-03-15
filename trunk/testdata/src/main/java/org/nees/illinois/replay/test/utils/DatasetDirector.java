package org.nees.illinois.replay.test.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.StepNumber;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.registries.ChannelNameRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class DatasetDirector {

	public enum ExperimentNames {
		HybridMasonry1, HybridMasonry2
	};

	public enum QueryParaTypes {
		ContWithStart, ContWithStop, Step, StepWithStart, StepWithStop
	};

	public enum QueryPartsList {
		OmList, DaqList, AllList
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

	private final ChannelNameRegistry expected2ndCnr = new ChannelNameRegistry();
	private final ChannelNameRegistry expectedCnr = new ChannelNameRegistry();
	private final Map<ExperimentNames, ChannelListTestMaps> experimentChannelLists = new HashMap<DatasetDirector.ExperimentNames, ChannelListTestMaps>();
	private final Logger log = LoggerFactory.getLogger(DatasetDirector.class);
	private final Map<QueryParaTypes, RateType> queryRates = new HashMap<QueryParaTypes, RateType>();
	private final Map<QueryParaTypes, Integer> queryTableSize = new HashMap<QueryParaTypes, Integer>();
	private final Map<QueryParaTypes, TimeSpec> queryTimes = new HashMap<DatasetDirector.QueryParaTypes, DatasetDirector.TimeSpec>();

	{
		List<ChannelListType> list = new ArrayList<ChannelListType>();
		list.add(ChannelListType.QueryOm);
		list.add(ChannelListType.QueryDaq);
		list.clear();
		list.add(ChannelListType.QueryBefore);
		list.add(ChannelListType.QueryAfter);
		list.clear();
		list.add(ChannelListType.OM);
		list.add(ChannelListType.DAQ);

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

		queryTimes.put(QueryParaTypes.ContWithStart, new TimeSpec(
				new Double(222.0), null));
		queryTimes.put(QueryParaTypes.ContWithStop, new TimeSpec(new Double(222.0),
				new Double(223.0)));
		queryTimes.put(QueryParaTypes.Step, new TimeSpec(null, null));
		queryTimes.put(QueryParaTypes.StepWithStart, new TimeSpec(new StepNumber(
				1.0, 0.0, 1.0), null));
		queryTimes.put(QueryParaTypes.StepWithStop, new TimeSpec(new StepNumber(
				1.0, 0.0, 1.0), new StepNumber(3.0, 22.0, 1.0)));
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

	public void checkChannels(ExperimentNames experiment, ChannelListType typ,
			List<String> channels) {
		ChannelListTestMaps cl = experimentChannelLists.get(experiment);
		checkChannels(cl.getChannels(typ), channels);
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
		DoubleArrayDataGenerator.compareData(expected.getData(), data.getData());
	}

	public void checkExpectedCnr(ExperimentNames experiment,
			ChannelNameRegistry cnr) {
		ChannelNameRegistry expected = experiment
				.equals(ExperimentNames.HybridMasonry1) ? expectedCnr
				: expected2ndCnr;
		log.debug("CHECKING  expected " + expected + "\nWITH actual " + cnr);
		Assert.assertEquals(cnr.toString(), expected.toString());
	}

	public DoubleMatrix generate(ExperimentNames experiment, QueryParaTypes qt,
			ChannelListType quy) {
		ChannelListTestMaps cl = experimentChannelLists.get(experiment);
		int row = queryTableSize.get(qt);
		List<String> channels = cl.getChannels(quy);
		RateType rate = queryRates.get(qt);
		return generate(quy.name(), row, channels.size(), rate);
	}

	public DoubleMatrix generate(ChannelListType quy, QueryParaTypes qt, QueryPartsList qpl) {
		ChannelTestingList qctl  = experimentChannelLists.get(quy).getChannelLists(quy);
		List<String> channels;
		int row = queryTableSize.get(qt);
		RateType rate = queryRates.get(qt);
		if(qpl.equals(QueryPartsList.DaqList)) {
			channels = qctl.getExisting().combine();
		} else if (qpl.equals(QueryPartsList.OmList)) {
			channels = qctl.getNewChannels();
		} else {
			channels = qctl.combine();
		}
		return generate(quy.toString(),row,channels.size(),rate);
	}

	private DoubleMatrix generate(String name, int rows, int cols, RateType rate) {
		// log.debug("For " + rate + " query " + name + " creating " + rows +
		// "x"
		// + cols + " matrix");
		DoubleArrayDataGenerator dg = new DoubleArrayDataGenerator(rows, cols, 0.02, 222.0);
		double[][] data = dg.generate();
		DoubleMatrix result = new DoubleMatrix(data);
		log.debug("For " + rate + " list type " + name + " creating " + result);
		return result;
	}

	public RateType getRate(QueryParaTypes qt) {
		return queryRates.get(qt);
	}

	public TimeSpec getTimes(QueryParaTypes qt) {
		return queryTimes.get(qt);
	}
}
