package org.nees.illinois.replay.test.server.utils;

import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.restlet.client.DataTableClient;

public class DataTableBadClient extends DataTableClient {
	private InjectedErrorsType injectedError = InjectedErrorsType.None;

	public DataTableBadClient(String hostname, String experiment) {
		super(hostname, experiment);
	}

	/* (non-Javadoc)
	 * @see org.nees.illinois.replay.restlet.client.DataTableClient#buildUri(org.nees.illinois.replay.data.TableType, org.nees.illinois.replay.data.RateType)
	 */
	@Override
	protected String buildUri(TableType table, RateType rate) {
		String result =  super.buildUri(table, rate);
		if(injectedError.equals(InjectedErrorsType.NoTable)) {
			result = result.replace(table.toString(), "");
		}
		if(injectedError.equals(InjectedErrorsType.BadTable)) {
			result = result.replace(table.toString(), "BadTable");
		}
		if(injectedError.equals(InjectedErrorsType.NoRate)) {
			result = result.replace(rate.toString(), "");
		}
		if(injectedError.equals(InjectedErrorsType.BadRate)) {
			result = result.replace(rate.toString(), "BadRate");
		}
		if(injectedError.equals(InjectedErrorsType.NoExperiment)) {
			result = result.replace(getExperiment(), "");
		}
		if(injectedError.equals(InjectedErrorsType.BadExperiment)) {
			result = result.replace(getExperiment(), "HybridMasonryBad");
		}
		if(injectedError.equals(InjectedErrorsType.ErrExperiment)) {
			result = result.replace(getExperiment(), "HybridMasonryERR");
		}
		return result;
	}

	/**
	 * @return the injectedError
	 */
	public InjectedErrorsType getInjectedError() {
		return injectedError;
	}

	/**
	 * @param injectedError the injectedError to set
	 */
	public void setInjectedError(InjectedErrorsType injectedError) {
		this.injectedError = injectedError;
	}

}
