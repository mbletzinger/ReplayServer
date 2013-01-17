package org.nees.illinois.replay.test.server.utils;

import org.nees.illinois.replay.data.StepNumber;
import org.nees.illinois.replay.restlet.client.DataQueryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataQueryBadClient extends DataQueryClient {
	private InjectedErrorsType injectedError = InjectedErrorsType.None;
	private final Logger log = LoggerFactory
			.getLogger(DataQueryBadClient.class);

	public DataQueryBadClient(String hostname, String experiment) {
		super(hostname, experiment);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.nees.illinois.replay.restlet.client.DataQueryClient#buildUri(java
	 * .lang.String)
	 */
	@Override
	protected String buildUri(String name) {
		if (injectedError.equals(InjectedErrorsType.BadQuery)
				|| injectedError.equals(InjectedErrorsType.NoQuery)
				|| injectedError.equals(InjectedErrorsType.BadExperiment)
				|| injectedError.equals(InjectedErrorsType.ErrExperiment)
				|| injectedError.equals(InjectedErrorsType.NoExperiment)) {
			return injectError(super.buildUri(name), null, name);
		}
		return super.buildUri(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.nees.illinois.replay.restlet.client.DataQueryClient#buildUri(java
	 * .lang.String, java.lang.Double, java.lang.Double)
	 */
	@Override
	protected String buildUri(String name, Double start, Double stop) {
		if (injectedError.equals(InjectedErrorsType.BadStart)
				|| injectedError.equals(InjectedErrorsType.NoStart)
				|| injectedError.equals(InjectedErrorsType.BadStop)
				|| injectedError.equals(InjectedErrorsType.NoStop)
				|| injectedError.equals(InjectedErrorsType.BadExperiment)
				|| injectedError.equals(InjectedErrorsType.ErrExperiment)
				|| injectedError.equals(InjectedErrorsType.NoExperiment)) {
			return injectTimeError(super.buildUri(name, start, stop), start, stop);
		}
		return super.buildUri(name, start, stop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.nees.illinois.replay.restlet.client.DataQueryClient#buildUri(java
	 * .lang.String, org.nees.illinois.replay.data.StepNumber,
	 * org.nees.illinois.replay.data.StepNumber)
	 */
	@Override
	protected String buildUri(String name, StepNumber start, StepNumber stop) {
		if (injectedError.equals(InjectedErrorsType.BadStart)
				|| injectedError.equals(InjectedErrorsType.NoStart)
				|| injectedError.equals(InjectedErrorsType.BadStop)
				|| injectedError.equals(InjectedErrorsType.NoStop)
				|| injectedError.equals(InjectedErrorsType.BadExperiment)
				|| injectedError.equals(InjectedErrorsType.ErrExperiment)
				|| injectedError.equals(InjectedErrorsType.NoExperiment)) {
			return injectTimeError(super.buildUri(name, start, stop), start, stop);
		}
		return super.buildUri(name, start, stop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.nees.illinois.replay.restlet.client.DataQueryClient#buildUri(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	protected String buildUri(String name, String rate) {
		String result = injectError(super.buildUri(name, rate), rate, name);
		return result;
	}

	/**
	 * @return the injectedError
	 */
	public InjectedErrorsType getInjectedError() {
		return injectedError;
	}

	private String injectError(String uri, String rate, String query) {
		String result = uri;
		if (injectedError.equals(InjectedErrorsType.NoRate)) {
			result = uri.replace(rate, "");
		}
		if (injectedError.equals(InjectedErrorsType.BadRate)) {
			result = uri.replace(rate, "BadRate");
		}

		if (injectedError.equals(InjectedErrorsType.NoQuery)) {
			result = uri.replace(query, "");
		}
		if (injectedError.equals(InjectedErrorsType.BadQuery)) {
			result = uri.replace(query, "BadQuery");
		}
		if (injectedError.equals(InjectedErrorsType.NoExperiment)) {
			result = result.replace(getExperiment(), "");
		}
		if (injectedError.equals(InjectedErrorsType.BadExperiment)) {
			result = result.replace(getExperiment(), "BadExperiment");
		}
		if (injectedError.equals(InjectedErrorsType.ErrExperiment)) {
			result = result.replace(getExperiment(), "ERR_Experiment");
		}
		log.debug(injectedError + " produces " + result + " from " + uri);
		return result;
	}

	private String injectTimeError(String uri, Double start, Double stop) {
		String result = uri;
		if (injectedError.equals(InjectedErrorsType.NoStart)) {
			result = uri.replace(start.toString(), "");
		}
		if (injectedError.equals(InjectedErrorsType.BadStart)) {
			result = uri.replace(start.toString(), "01.Bad0");
		}

		if (injectedError.equals(InjectedErrorsType.NoStop)) {
			result = uri.replace(stop.toString(), "");
		}
		if (injectedError.equals(InjectedErrorsType.BadStop)) {
			result = uri.replace(stop.toString(), "01.Bad0");
		}
		if (injectedError.equals(InjectedErrorsType.NoExperiment)) {
			result = result.replace(getExperiment(), "");
		}
		if (injectedError.equals(InjectedErrorsType.BadExperiment)) {
			result = result.replace(getExperiment(), "BadExperiment");
		}
		if (injectedError.equals(InjectedErrorsType.ErrExperiment)) {
			result = result.replace(getExperiment(), "ERR_Experiment");
		}
		log.debug(injectedError + " produces " + result + " from " + uri);
		return result;
	}

	private String injectTimeError(String uri, StepNumber start, StepNumber stop) {
		String result = uri;
		if (injectedError.equals(InjectedErrorsType.NoStart)) {
			result = uri.replace(start.toString(), "");
		}
		if (injectedError.equals(InjectedErrorsType.BadStart)) {
			result = uri.replace(start.toString(),
					start.toString().replaceAll("_", ","));
		}

		if (injectedError.equals(InjectedErrorsType.NoStop)) {
			result = uri.replace(stop.toString(), "");
		}
		if (injectedError.equals(InjectedErrorsType.BadStop)) {
			result = uri.replace(stop.toString(),
					stop.toString().replaceAll("_", ","));
		}
		if (injectedError.equals(InjectedErrorsType.NoExperiment)) {
			result = result.replace(getExperiment(), "");
		}
		if (injectedError.equals(InjectedErrorsType.BadExperiment)) {
			result = result.replace(getExperiment(), "BadExperiment");
		}
		if (injectedError.equals(InjectedErrorsType.ErrExperiment)) {
			result = result.replace(getExperiment(), "ERR_Experiment");
		}
		log.debug(injectedError + " produces " + result + " from " + uri);
		return result;
	}

	/**
	 * @param injectedError
	 *            the injectedError to set
	 */
	public void setInjectedError(InjectedErrorsType injectedError) {
		this.injectedError = injectedError;
	}
}
