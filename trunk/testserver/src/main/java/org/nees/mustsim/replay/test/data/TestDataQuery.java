package org.nees.mustsim.replay.test.data;

import java.util.List;

import org.nees.mustsim.replay.data.DataQueryI;
import org.nees.mustsim.replay.data.DoubleMatrix;
import org.nees.mustsim.replay.data.StepNumber;

public class TestDataQuery implements DataQueryI {

	@Override
	public boolean setQuery(String name, List<String> channels) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DoubleMatrix doQuery(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DoubleMatrix doQuery(String name, double start) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DoubleMatrix doQuery(String name, double start, double stop) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DoubleMatrix doQuery(String name, StepNumber start) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DoubleMatrix doQuery(String name, StepNumber start, StepNumber stop) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isQuery(String name) {
		// TODO Auto-generated method stub
		return false;
	}

}
