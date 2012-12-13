package org.nees.illinois.replay.test.db;

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
		TestDbQuery.class
		})
public class AllTests {

}