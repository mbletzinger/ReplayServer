package org.nees.illinois.replay.test.db;

import java.sql.Connection;

import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.data.DbChannelNameSynch;
import org.nees.illinois.replay.db.statement.DbStatement;
import org.nees.illinois.replay.registries.ChannelNameRegistry;
import org.nees.illinois.replay.test.db.utils.DbManagement;
import org.nees.illinois.replay.test.db.utils.DbTestsModule;
import org.nees.illinois.replay.test.db.utils.MySqlCreateRemoveDatabase;
import org.nees.illinois.replay.test.utils.ChannelLists;
import org.nees.illinois.replay.test.utils.ChannelLists.ChannelListType;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestChannelLists {
	private DbPools dbc;
	final String experiment = "HybridMasonry1";
	private boolean ismysql;

	@Parameters("db")
	@BeforeClass
	public void setUp(@Optional("derby") String db) throws Exception {
		DbTestsModule guiceMod = new DbTestsModule(db);
		guiceMod.setExperiment("HybridMasonry1");
		Injector injector = Guice.createInjector(guiceMod);
		dbc = injector.getInstance(DbPools.class);
		ismysql = db.equals("mysql");
		if (ismysql) {
			DbManagement mscrdb = new MySqlCreateRemoveDatabase(
					dbc, guiceMod.getExperiment());
			Connection connection = mscrdb.generateConnection(false);
			mscrdb.createDatabase(connection);
			mscrdb.closeConnection(connection);
		}
}

	@AfterClass
	public void tearDown() throws Exception {
		DbStatement dbSt = dbc.createDbStatement(experiment);
		DbChannelNameSynch dbcs = new DbChannelNameSynch(null, dbSt);
		dbcs.removeTable();
		dbSt.close();
		dbc.close();
		if (ismysql) {
			DbManagement mscrdb = new MySqlCreateRemoveDatabase(
					dbc, experiment);
			Connection connection = mscrdb.generateConnection(false);
			mscrdb.removeDatabase(connection);
			mscrdb.closeConnection(connection);
		}
	}

	@Test
	public void testChannelList() {
		ChannelLists lists = new ChannelLists();
		ChannelNameRegistry cnr = new ChannelNameRegistry();
		for (String c : lists.getChannels(ChannelListType.OM)) {
			cnr.addChannel(TableType.OM, c);
		}
		DbStatement dbSt = dbc.createDbStatement(experiment);
		DbChannelNameSynch dbcs = new DbChannelNameSynch(cnr, dbSt);
		dbcs.synchronize();
		dbSt.close();
		ChannelNameRegistry cnr1 = new ChannelNameRegistry();
		dbSt = dbc.createDbStatement(experiment);
		dbcs = new DbChannelNameSynch(cnr1, dbSt);
		dbcs.initialize();
		AssertJUnit.assertEquals(cnr.toString(), cnr1.toString());
	}

}
