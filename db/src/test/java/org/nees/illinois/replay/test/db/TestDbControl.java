package org.nees.illinois.replay.test.db;

import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

/**
 * Class to turn Derby on and off.
 * @author Michael Bletzinger
 */
public class TestDbControl {
	/**
	 * Derby controls.
	 */
	private DerbyDbControl ddbc = new DerbyDbControl();

	/**
	 * Turn Derby on.
	 */
	@BeforeSuite
	public final void beforeSuite() {
		ddbc.startDerby();
	}

	/**
	 * Turn Derby off.
	 */
	@AfterSuite
	public final void afterSuite() {
		ddbc.stopDerby();
	}

}
