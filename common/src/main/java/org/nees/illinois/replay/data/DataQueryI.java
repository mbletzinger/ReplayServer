package org.nees.illinois.replay.data;

import java.util.List;


public interface DataQueryI {

	public boolean setQuery(String name, List<String> channels);

	public DoubleMatrix doQuery(String name);

	public DoubleMatrix doQuery(String name, double start);

	public DoubleMatrix doQuery(String name, double start, double stop);

	public DoubleMatrix doQuery(String name, StepNumber start);

	public DoubleMatrix doQuery(String name, StepNumber start, StepNumber stop);

	public boolean isQuery(String name);

	public void setExperiment(String experiment);

	public String getExperiment();
}
