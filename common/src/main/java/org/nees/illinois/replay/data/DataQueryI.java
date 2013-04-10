package org.nees.illinois.replay.data;

import java.util.List;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;


public interface DataQueryI {

	public boolean setQuery(String name, List<String> channels);

	public DoubleMatrix doQuery(String name);

	public DoubleMatrix doQuery(String name, double start);

	public DoubleMatrix doQuery(String name, double start, double stop);

	public DoubleMatrix doQuery(String name, StepNumber start);

	public DoubleMatrix doQuery(String name, StepNumber start, StepNumber stop);

	public boolean isQuery(String name);

	public void setExperiment(ExperimentRegistries experiment);

	public ExperimentRegistries getExperiment();
}
