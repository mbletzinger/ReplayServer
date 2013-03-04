package org.nees.illinois.replay.server.test.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.data.DbDataUpdates;
import org.nees.illinois.replay.db.statement.DbTablesMap;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.nees.illinois.replay.registries.ExperimentRegistries;
import org.nees.illinois.replay.test.resources.utils.ResourceLoader;
import org.nees.illinois.replay.test.utils.ChannelDataTestingLists.ChannelListType;
import org.nees.illinois.replay.test.utils.DatasetDirector.ExperimentNames;
import org.nees.illinois.replay.test.utils.DatasetDirector.QueryTypes;
import org.nees.illinois.replay.test.utils.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.AssertJUnit;

import com.google.inject.AbstractModule;

public class ResourceLoaderWithDb extends ResourceLoader {


	/* (non-Javadoc)
	 * @see org.nees.illinois.replay.test.resources.utils.ResourceLoader#checkQueryData(org.nees.illinois.replay.test.resources.utils.DatasetDirector.QueryTypes, org.nees.illinois.replay.test.utils.ChannelLists.ChannelListType, org.nees.illinois.replay.data.DoubleMatrix)
	 */
	@Override
	public void checkQueryData(QueryTypes qt, ChannelListType quy,
			DoubleMatrix data) {
		List<String> channels = getCl().getChannels(quy);
		int columns = channels.size();
		double[][] expected = DataGenerator.initData(getUploadRows(),
				columns - 4, 0.02); // remove 4 time columns
		log.debug("For " + qt + " and " + quy);
		DataGenerator.compareData(expected, data.getData());
	}

	private final Logger log = LoggerFactory
			.getLogger(ResourceLoaderWithDb.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.nees.illinois.replay.test.resources.utils.ResourceLoader#checkDataset
	 * (org.nees.illinois.replay.test.utils.ChannelLists.ChannelListType)
	 */
	@Override
	public void checkDataset(ChannelListType typ) {
		DbDataUpdates ddu = (DbDataUpdates) getDtsR().getUpdates();
		DbPools pools = ddu.getPools();

		List<String> channels = getCl().getChannels(typ);
		int columns = channels.size();
		for (RateType rate : RateType.values()) {
			StatementProcessor dbSt = pools.createDbStatement(getExperiment(),
					false);
			ExperimentRegistries er = ddu.getExperiment();

			ResultSet rs = dbSt.query("SELECT * FROM "
					+ ((DbTablesMap) er.getChnlNamesMgmt()).tableName(getCl()
							.getTt(typ), rate));
			columns = 0;
			try {
				columns = rs.getMetaData().getColumnCount();
			} catch (SQLException e) {
				log.error("ResultSet has no metadata because ", e);
				dbSt.closeQuery(rs);
				dbSt.close();
				AssertJUnit.fail();
			}

			double[][] expected = DataGenerator.initData(getUploadRows(),
					columns - 4, 0.02); // remove 4 time columns
			log.debug("For " + typ + " and " + rate);

			AssertJUnit.assertEquals(expected[0].length, columns);
			double[][] rsData = new double[expected.length][columns];
			int r = 0;
			try {
				while (rs.next()) {
					log.debug("Processing rs row " + r);
					for (int i = 0; i < columns; i++) {
						rsData[r][i] = rs.getDouble(i + 1);
					}
					r++;
				}
			} catch (SQLException e) {
				log.error("Result Set fetch failed because ", e);
				dbSt.closeQuery(rs);
				dbSt.close();
				AssertJUnit.fail();
			}
			DataGenerator.compareData(expected, rsData);
			dbSt.closeQuery(rs);
			dbSt.close();
		}
	}

	public void removeDatabase() {
		DbDataUpdates ddu = (DbDataUpdates) getDtsR().getUpdates();
		DbPools pools = ddu.getPools();
		try {
			pools.getOps().removeDatabase(getExperiment());
		} catch (Exception e) {
			log.error("Failed to remove " + getExperiment()
					+ " because ", e);
		}

	}

	public ResourceLoaderWithDb(String appRoot, ExperimentNames experiment,
			AbstractModule module, boolean secondExperiment) {
		super(appRoot, experiment, module, secondExperiment);
	}

}
