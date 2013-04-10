package org.nees.illinois.replay.common.registries;

import com.google.inject.AbstractModule;

public abstract class ExperimentModule extends AbstractModule {
	private String experiment;

	/**
	 * @return the experiment
	 */
	public String getExperiment() {
		return experiment;
	}

	/**
	 * @param experiment
	 *            the experiment to set
	 */
	public void setExperiment(String experiment) {
		this.experiment = experiment;
	}

}
