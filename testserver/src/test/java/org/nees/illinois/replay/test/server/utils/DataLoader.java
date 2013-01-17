package org.nees.illinois.replay.test.server.utils;

import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.restlet.client.DataQueryClient;
import org.nees.illinois.replay.restlet.client.DataTableClient;
import org.nees.illinois.replay.test.utils.ChannelLists;
import org.nees.illinois.replay.test.utils.DataGenerator;
import org.nees.illinois.replay.test.utils.ChannelLists.ChannelListType;

public class DataLoader {

	private final String experiment;
	private final String hostname;
	private boolean secondExperiment = false;

	public DataLoader(String hostname, String experiment,
			boolean secondExperiment) {
		super();
		this.hostname = hostname;
		this.experiment = experiment;
		this.secondExperiment = secondExperiment;
	}

	/**
	 * @return the experiment
	 */
	public String getExperiment() {
		return experiment;
	}

	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * @return the secondExperiment
	 */
	public boolean isSecondExperiment() {
		return secondExperiment;
	}

	/**
	 * @param secondExperiment
	 *            the secondExperiment to set
	 */
	public void setSecondExperiment(boolean secondExperiment) {
		this.secondExperiment = secondExperiment;
	}
	
	public void createTables() {
		ChannelLists cl = new ChannelLists();
		DataTableClient dtc = new DataTableClient(hostname, experiment);
		if(secondExperiment) {
			createTable(cl, dtc, ChannelListType.OM2);
			createTable(cl, dtc, ChannelListType.DAQ2);
		} else {
			createTable(cl, dtc, ChannelListType.OM);
			createTable(cl, dtc, ChannelListType.DAQ);
		}
	}
	
	private void createTable(ChannelLists cl, DataTableClient dtc, ChannelListType typ) {
			List<String> channels = cl.getChannels(typ);
			dtc.createTable(cl.getTt(typ), channels);
	}
	
	public void createQueries() {
		ChannelLists cl = new ChannelLists();
		DataQueryClient dqc = new DataQueryClient(hostname, experiment);
		if(secondExperiment) {
			createQuery(cl, dqc, ChannelListType.Query3);
			createQuery(cl, dqc, ChannelListType.Query4);
		} else {
			createQuery(cl, dqc, ChannelListType.Query1);
			createQuery(cl, dqc, ChannelListType.Query2);			
		}
	}
	private void createQuery(ChannelLists cl, DataQueryClient dqc, ChannelListType typ) {
		List<String> channels = cl.getChannels(typ);
		dqc.setQuery(typ.name(), channels);
	}
	
	public void uploadData() {
		ChannelLists cl = new ChannelLists();
		DataTableClient dtc = new DataTableClient(hostname, experiment);
		if(secondExperiment) {
			uploadDataTable(cl, dtc, ChannelListType.OM2);
			uploadDataTable(cl, dtc, ChannelListType.DAQ2);
		} else {
			uploadDataTable(cl, dtc, ChannelListType.OM);
			uploadDataTable(cl, dtc, ChannelListType.DAQ);			
		}
	}
	
	private void uploadDataTable(ChannelLists cl, DataTableClient dtc, ChannelListType typ) {
		List<String> channels = cl.getChannels(typ);

		int columns = channels.size();
		double[][] dataD = DataGenerator.initData(RateType.CONT, 20,
				columns, 0.02);
		dtc.addData(cl.getTt(typ), RateType.CONT, new DoubleMatrix(dataD));

		dataD = DataGenerator.initData(RateType.STEP, 20, columns, 0.02);
		dtc.addData(cl.getTt(typ), RateType.STEP, new DoubleMatrix(dataD));
}
}