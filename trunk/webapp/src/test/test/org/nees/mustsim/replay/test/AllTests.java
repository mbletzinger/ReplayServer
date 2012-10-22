package org.nees.mustsim.replay.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestCreateRemoveDatabase.class,
		TestTableUpdates.class,
		TestDbStatement.class,
		TestChannelLists.class,
		TestDataStatements.class,
		TestInterpolate.class,
		TestMerge.class,
		TestDbQuery.class
		})
public class AllTests {

}
