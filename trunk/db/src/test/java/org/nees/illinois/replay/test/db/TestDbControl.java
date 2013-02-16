package org.nees.illinois.replay.test.db;

import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestDbControl {
	private DerbyDbControl ddbc = new DerbyDbControl();
   @BeforeSuite
  public void beforeSuite() {
	   ddbc.startDerby();
  }

  @AfterSuite
  public void afterSuite() {
	   ddbc.stopDerby();
  }

}
