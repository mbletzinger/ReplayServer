package org.nees.illinois.replay.data;

import java.util.List;

public interface DataUpdateI {
	public boolean createTable(TableType table, List<String> channels);

	public boolean removeTable(TableType table);

	public boolean update(TableType table, RateType rate, double[][] data);

	public void setExperiment(String experiment);

	public String getExperiment();
}
