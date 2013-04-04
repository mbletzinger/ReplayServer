package org.nees.illinois.replay.test.db;

import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.data.DbChannelNameSynch;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.nees.illinois.replay.registries.ChannelNameRegistry;
import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.nees.illinois.replay.test.db.utils.DbTestsModule;
import org.nees.illinois.replay.test.utils.ChannelListTestMaps;
import org.nees.illinois.replay.test.utils.ChannelListType;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestChannelLists {
	private DbPools pools;
	final String experiment = "HybridMasonry1";
	private boolean ismysql;
	private final DerbyDbControl ddbc = new DerbyDbControl();

	@Parameters("db")
	@BeforeClass
	public void setUp(@Optional("derby") String db) throws Exception {
		DbTestsModule guiceMod = new DbTestsModule(db);
		guiceMod.setExperiment("HybridMasonry1");
		Injector injector = Guice.createInjector(guiceMod);
		pools = injector.getInstance(DbPools.class);
		ismysql = db.equals("mysql");
		if (ismysql == false) {
			ddbc.startDerby();
		}
	}

	@AfterClass
	public void tearDown() throws Exception {
		StatementProcessor dbSt = pools.createDbStatement(experiment,false);
		DbChannelNameSynch dbcs = new DbChannelNameSynch(null, dbSt);
		dbcs.removeTable();
		dbSt.close();
		pools.getOps().removeDatabase(experiment);
		pools.close();
		if (ismysql == false) {
			ddbc.stopDerby();
		}
	}

	@Test
	public void testChannelList() {
		ChannelListTestMaps lists = new ChannelListTestMaps(false, experiment);
		ChannelNameRegistry cnr = new ChannelNameRegistry();
		for (String c : lists.getChannels(ChannelListType.OM)) {
			cnr.addChannel(TableType.OM, c);
		}
		StatementProcessor dbSt = pools.createDbStatement(experiment,true);
		DbChannelNameSynch dbcs = new DbChannelNameSynch(cnr, dbSt);
		dbcs.synchronize();
		dbSt.close();
		ChannelNameRegistry cnr1 = new ChannelNameRegistry();
		dbSt = pools.createDbStatement(experiment,false);
		dbcs = new DbChannelNameSynch(cnr1, dbSt);
		dbcs.initialize();
		AssertJUnit.assertEquals(cnr.toString(), cnr1.toString());
	}

}
