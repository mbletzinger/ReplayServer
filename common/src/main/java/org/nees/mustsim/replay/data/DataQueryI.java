package org.nees.mustsim.replay.data;

import java.util.List;


public interface DataQueryI {

	public boolean setQuery(String name, List<String> channels);
	public DoubleMatrix doQuery(String name);
	public DoubleMatrix doQuery(String name, double start);
	public DoubleMatrix doQuery(String name, double start, double stop);
	public boolean isQuery(String name);
}
