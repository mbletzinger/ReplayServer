package org.nees.illinois.replay.data;

import java.util.List;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;

public interface DataUpdateI {
	public boolean createTable(TableType table, List<String> channels);

	public boolean removeTable(TableType table);

	public boolean update(TableType table, RateType rate, double[][] data);

	public void setExperiment(ExperimentRegistries experiment);

	public ExperimentRegistries getExperiment();
}
