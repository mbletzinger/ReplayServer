package org.nees.illinois.replay.test.db;

import org.nees.illinois.replay.test.db.utils.DerbyDbControl;
import org.testng.annotations.AfterSuite;

public class TestStopNetworkDerby {
	private DerbyDbControl derby = new DerbyDbControl();

	@AfterSuite
	public void testStopDerby() {
		derby.stopDerby();
	}

}
