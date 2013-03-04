package org.nees.illinois.replay.test.server.utils;

import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.restlet.client.DataQueryClient;
import org.nees.illinois.replay.restlet.client.DataTableClient;
import org.nees.illinois.replay.test.resources.utils.DatasetLoaderI;
import org.nees.illinois.replay.test.utils.ChannelDataTestingLists;
import org.nees.illinois.replay.test.utils.DataGenerator;
import org.nees.illinois.replay.test.utils.ChannelDataTestingLists.ChannelListType;

public class RestletLoader implements DatasetLoaderI {

	private final String experiment;
	private final String hostname;
	private boolean secondExperiment = false;

	public RestletLoader(String hostname, String experiment,
			boolean secondExperiment) {
		super();
		this.hostname = hostname;
		this.experiment = experiment;
		this.secondExperiment = secondExperiment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.nees.illinois.replay.test.server.utils.DatasetLoader#createQueries()
	 */
	@Override
	public void createQueries() {
		ChannelDataTestingLists cl = new ChannelDataTestingLists();
		DataQueryClient dqc = new DataQueryClient(hostname, experiment);
		if (secondExperiment) {
			createQuery(cl, dqc, ChannelListType.Query3);
			createQuery(cl, dqc, ChannelListType.Query4);
		} else {
			createQuery(cl, dqc, ChannelListType.Query1);
			createQuery(cl, dqc, ChannelListType.Query2);
		}
	}

	private void createQuery(ChannelDataTestingLists cl, DataQueryClient dqc,
			ChannelListType typ) {
		List<String> channels = cl.getChannels(typ);
		dqc.setQuery(typ.name(), channels);
	}

	private void createTable(ChannelDataTestingLists cl, DataTableClient dtc,
			ChannelListType typ) {
		List<String> channels = cl.getChannels(typ);
		dtc.createTable(cl.getTt(typ), channels);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.nees.illinois.replay.test.server.utils.DatasetLoader#createTables()
	 */
	@Override
	public void createTables() {
		ChannelDataTestingLists cl = new ChannelDataTestingLists();
		DataTableClient dtc = new DataTableClient(hostname, experiment);
		if (secondExperiment) {
			createTable(cl, dtc, ChannelListType.OM2);
			createTable(cl, dtc, ChannelListType.DAQ2);
		} else {
			createTable(cl, dtc, ChannelListType.OM);
			createTable(cl, dtc, ChannelListType.DAQ);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.nees.illinois.replay.test.server.utils.DatasetLoader#getExperiment()
	 */
	@Override
	public String getExperiment() {
		return experiment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.nees.illinois.replay.test.server.utils.DatasetLoader#getHostname()
	 */
	@Override
	public String getHostname() {
		return hostname;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.nees.illinois.replay.test.server.utils.DatasetLoader#isSecondExperiment
	 * ()
	 */
	@Override
	public boolean isSecondExperiment() {
		return secondExperiment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.nees.illinois.replay.test.server.utils.DatasetLoader#setSecondExperiment
	 * (boolean)
	 */
	@Override
	public void setSecondExperiment(boolean secondExperiment) {
		this.secondExperiment = secondExperiment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.nees.illinois.replay.test.server.utils.DatasetLoader#uploadData()
	 */
	@Override
	public void uploadData() {
		ChannelDataTestingLists cl = new ChannelDataTestingLists();
		DataTableClient dtc = new DataTableClient(hostname, experiment);
		if (secondExperiment) {
			uploadDataTable(cl, dtc, ChannelListType.OM2);
			uploadDataTable(cl, dtc, ChannelListType.DAQ2);
		} else {
			uploadDataTable(cl, dtc, ChannelListType.OM);
			uploadDataTable(cl, dtc, ChannelListType.DAQ);
		}
	}

	private void uploadDataTable(ChannelDataTestingLists cl, DataTableClient dtc,
			ChannelListType typ) {
		List<String> channels = cl.getChannels(typ);

		int columns = channels.size();
		double[][] dataD = DataGenerator.initData(20, columns, 0.02);
		dtc.addData(cl.getTt(typ), RateType.CONT, new DoubleMatrix(dataD));

		dataD = DataGenerator.initData(20, columns, 0.02);
		dtc.addData(cl.getTt(typ), RateType.STEP, new DoubleMatrix(dataD));
	}
}