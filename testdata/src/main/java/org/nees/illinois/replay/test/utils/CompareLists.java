package org.nees.illinois.replay.test.utils;

import java.util.List;

import org.testng.Assert;

public class CompareLists<Item> {
	public void compare(List<Item> actual, List<Item> expected) {
		Assert.assertEquals(actual.size(), expected.size());
		for( int e = 0; e < expected.size(); e++) {
			Assert.assertEquals(actual.get(e), expected.get(e),"At " + e + " out of " + expected.size());
		}
	}
}
