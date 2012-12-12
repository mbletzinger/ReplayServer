package org.nees.illinois.replay.test.db;

import org.junit.Test;
import org.nees.illinois.replay.test.db.utils.DerbyDbControl;

public class TestStartNetworkDerby {
	private DerbyDbControl derby = new DerbyDbControl();

	@Test
	public void testStartDerby() {
		derby.startDerby();
	}

}
