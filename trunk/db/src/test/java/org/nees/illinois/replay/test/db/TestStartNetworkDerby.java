package org.nees.illinois.replay.test.db;

import org.nees.illinois.replay.test.db.utils.DerbyDbControl;
import org.testng.annotations.BeforeSuite;

public class TestStartNetworkDerby {
	private DerbyDbControl derby = new DerbyDbControl();

	@BeforeSuite
	public void testStartDerby() {
		derby.startDerby();
	}

}
