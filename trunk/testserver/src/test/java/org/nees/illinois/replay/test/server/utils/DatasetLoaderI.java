package org.nees.illinois.replay.test.server.utils;

public interface DatasetLoaderI {

	public abstract void createQueries();

	public abstract void createTables();

	/**
	 * @return the experiment
	 */
	public abstract String getExperiment();

	/**
	 * @return the hostname
	 */
	public abstract String getHostname();

	/**
	 * @return the secondExperiment
	 */
	public abstract boolean isSecondExperiment();

	/**
	 * @param secondExperiment
	 *            the secondExperiment to set
	 */
	public abstract void setSecondExperiment(boolean secondExperiment);

	public abstract void uploadData();

}