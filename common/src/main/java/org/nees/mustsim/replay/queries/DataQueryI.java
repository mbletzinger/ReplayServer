package org.nees.mustsim.replay.queries;

import java.util.List;

import org.nees.mustsim.replay.data.DoubleMatrix;
import org.nees.mustsim.replay.data.StepNumber;


public interface DataQueryI {

	public boolean setQuery(String name, List<String> channels);
	public DoubleMatrix doQuery(String name);
	public DoubleMatrix doQuery(String name, double start);
	public DoubleMatrix doQuery(String name, double start, double stop);
	public DoubleMatrix doQuery(String name, StepNumber start);
	public DoubleMatrix doQuery(String name, StepNumber start, StepNumber stop);
	public boolean isQuery(String name);
}
