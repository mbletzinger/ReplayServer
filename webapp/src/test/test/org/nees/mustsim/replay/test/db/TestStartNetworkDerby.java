package org.nees.mustsim.replay.test.db;

import org.junit.Test;
import org.nees.mustsim.replay.test.utils.DerbyDbControl;

public class TestStartNetworkDerby {
	private DerbyDbControl derby = new DerbyDbControl();

	@Test
	public void testStartDerby() {
		derby.startDerby();
	}

}
