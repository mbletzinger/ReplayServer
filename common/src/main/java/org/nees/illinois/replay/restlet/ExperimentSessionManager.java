package org.nees.illinois.replay.restlet;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

/**
 * This class manages experiment scoped sessions for the replay server. A
 * session consists of a set of {@link ExperimentRegistries registries} which
 * store queries, table definitions, etc for a particular experiment. They are
 * stored in the context map of the restlet server.
 * @author Michael Bletzinger
 */
public class ExperimentSessionManager {
	/**
	 * Source for attributes from the request URI.
	 */
	private final AttributeExtraction extract;
	/**
	 * Context map from the restlet server containing the experiment sessions.
	 */
	private final ConcurrentMap<String, Object> cxtAttrs;

	/**
	 * Constructor.
	 * @param cxtAttrs
	 *            Context map from the restlet server containing the experiment
	 *            sessions.
	 * @param reqAttrs
	 *            Attributes map parsed by the restlet server.
	 */
	public ExperimentSessionManager(
			final ConcurrentMap<String, Object> cxtAttrs,
			final Map<String, Object> reqAttrs) {
		super();
		this.extract = new AttributeExtraction(reqAttrs);
		this.cxtAttrs = cxtAttrs;
	}

	/**
	 * Get a set of registries for a particular experiment. This set is
	 * considered the session for the experiment.
	 * @param allowNewExperiments
	 *            True if an unregistered experiment name can return a new
	 *            session.
	 * @return The session.
	 */
	public final synchronized ExperimentRegistries getRegistries(
			final boolean allowNewExperiments) {
		String experiment = extract.getExperiment();
		ExperimentRegistries er = null;
		String key = experiment + ".registries";
		if (cxtAttrs.containsKey(key)) {
			er = (ExperimentRegistries) cxtAttrs.get(key);
		} else {
			// Force a runtime error for testing
			if (experiment.contains("ERR")) {
				throw new RuntimeException("Help me I died");
			}
			if (allowNewExperiments) {
				er = new ExperimentRegistries(experiment);
				cxtAttrs.put(key, er);
			} else {
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
						"\"" + experiment + "\" is not an experiment");
			}
		}
		return er;
	}
}
