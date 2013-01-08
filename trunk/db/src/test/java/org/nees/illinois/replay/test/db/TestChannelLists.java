package org.nees.illinois.replay.test.db;

import org.nees.illinois.replay.data.ChannelNameRegistry;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.DerbyPools;
import org.nees.illinois.replay.db.data.DbChannelNameSynch;
import org.nees.illinois.replay.db.statement.DbStatement;
import org.nees.illinois.replay.test.utils.ChannelLists;
import org.nees.illinois.replay.test.utils.ChannelLists.ChannelListType;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestChannelLists {
	private DbPools dbc;
	final String experiment = "HybridMasonry1";

	@BeforeClass
	public void setUp() throws Exception {
		dbc = new DerbyPools();
	}

	@AfterClass
	public void tearDown() throws Exception {
		DbStatement dbSt = dbc.createDbStatement(experiment);
		DbChannelNameSynch dbcs = new DbChannelNameSynch(null, dbSt);
		dbcs.removeTable();
		dbSt.close();
		dbc.close();
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
