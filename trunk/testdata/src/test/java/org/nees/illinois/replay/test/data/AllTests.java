package org.nees.illinois.replay.test.data;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestInterpolate.class, TestMerge.class,
		TestStreamConversion.class })
public class AllTests {

}
