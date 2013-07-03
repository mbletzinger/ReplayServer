package org.nees.illinois.replay.subresource;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;

/**
 * Each experiment comes with a set of registries to complement the database
 * contents. This interface which manages the registry set for an experiment.
 * @author Michael Bletzinger
 */
public interface SubResourceI {

	/**
	 * Sets the registries for this particular experiment.
	 * @param experiment
	 *            The collection of registries.
	 */
	void setExperiment(ExperimentRegistries experiment);

	/**
	 * Returns the registries associated with the current experiment.
	 * @return The collection of registries.
	 */
	ExperimentRegistries getExperiment();

}
