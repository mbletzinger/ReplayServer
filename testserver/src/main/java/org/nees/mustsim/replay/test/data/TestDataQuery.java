package org.nees.mustsim.replay.test.data;

import java.util.List;

import org.nees.mustsim.replay.channels.ChannelNameRegistry;
import org.nees.mustsim.replay.data.DoubleMatrix;
import org.nees.mustsim.replay.data.RateType;
import org.nees.mustsim.replay.data.StepNumber;
import org.nees.mustsim.replay.queries.DataQueryI;
import org.nees.mustsim.replay.queries.QueryRegistry;
import org.nees.mustsim.replay.queries.QuerySpec;
import org.nees.mustsim.replay.test.utils.DataGenerator;

public class TestDataQuery implements DataQueryI {

	private final ChannelNameRegistry cnr;
	private final QueryRegistry contQr = new QueryRegistry();
	private final QueryRegistry stepQr = new QueryRegistry();

	public TestDataQuery(ChannelNameRegistry cnr) {
		super();
		this.cnr = cnr;
	}

	@Override
	public DoubleMatrix doQuery(String name) {
		return generate(name, 40, RateType.STEP);
	}

	@Override
	public DoubleMatrix doQuery(String name, double start) {
		return generate(name, 20, RateType.CONT);
	}

	@Override
	public DoubleMatrix doQuery(String name, double start, double stop) {
		return generate(name, 10, RateType.CONT);
	}

	@Override
	public DoubleMatrix doQuery(String name, StepNumber start) {
		return generate(name, 15, RateType.STEP);
	}

	@Override
	public DoubleMatrix doQuery(String name, StepNumber start, StepNumber stop) {
		return generate(name, 5, RateType.STEP);
	}

	@Override
	public boolean isQuery(String name) {
		return stepQr.getQuery(name) != null;
	}

	@Override
	public boolean setQuery(String name, List<String> channels) {
		contQr.setQuery(name, new QuerySpec(channels, name, cnr, RateType.CONT));
		stepQr.setQuery(name, new QuerySpec(channels, name, cnr, RateType.STEP));
		return true;
	}

	private DoubleMatrix generate(String name, int rows, RateType rate) {
		QuerySpec qs = rate.equals(RateType.CONT) ? contQr.getQuery(name) : stepQr.getQuery(name);
		double[][] data = DataGenerator.initData(rate, rows, qs.getNoc()
				.getNumber(true), 0.5);
		DoubleMatrix result = new DoubleMatrix(data, data[0].length);
		return result;

	}
}
