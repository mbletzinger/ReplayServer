package org.nees.illinois.replay.test.db;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nees.illinois.replay.channels.ChannelNameRegistry;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.db.DbConnections;
import org.nees.illinois.replay.db.data.server.DbChannelNameSynch;
import org.nees.illinois.replay.db.statement.DbStatement;
import org.nees.illinois.replay.test.utils.ChannelLists;
import org.nees.illinois.replay.test.utils.ChannelLists.ChannelListType;

public class TestChannelLists {
	private DbConnections dbc;

	@Before
	public void setUp() throws Exception {
		dbc = new DbConnections("org.apache.derby.jdbc.ClientDriver", "TESTDB",
				"jdbc:derby://localhost:1527/");
	}

	@After
	public void tearDown() throws Exception {
		DbStatement dbSt = dbc.createDbStatement();
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
		DbStatement dbSt = dbc.createDbStatement();
		DbChannelNameSynch dbcs = new DbChannelNameSynch(cnr, dbSt);
		dbcs.synchronize();
		dbSt.close();
		ChannelNameRegistry cnr1 = new ChannelNameRegistry();
		dbSt = dbc.createDbStatement();
		dbcs = new DbChannelNameSynch(cnr1, dbSt);
		dbcs.initialize();
		Assert.assertEquals(cnr.toString(), cnr1.toString());
	}

}
