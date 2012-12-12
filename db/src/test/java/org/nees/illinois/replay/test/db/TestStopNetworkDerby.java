package org.nees.illinois.replay.test.db;

import org.junit.Test;
import org.nees.illinois.replay.test.db.utils.DerbyDbControl;

public class TestStopNetworkDerby {
	private DerbyDbControl derby = new DerbyDbControl();

	@Test
	public void testStartDerby() {
		derby.stopDerby();
	}

}
