package org.nees.mustsim.replay.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestCreateRemoveDatabase.class,
		TestTableStatements.class, TestTableUpdates.class, TestQueries.class })
public class AllTests {

}
