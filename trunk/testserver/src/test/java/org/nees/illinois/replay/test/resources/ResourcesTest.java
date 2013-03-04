package org.nees.illinois.replay.test.resources;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.test.resources.utils.ResourceLoader;
import org.nees.illinois.replay.test.server.guice.ResourcesTestModule;
import org.nees.illinois.replay.test.utils.ChannelDataTestingLists.ChannelListType;
import org.nees.illinois.replay.test.utils.DatasetDirector.ExperimentNames;
import org.nees.illinois.replay.test.utils.DatasetDirector.QueryTypes;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Test(groups = { "resources-test" })
public class ResourcesTest {
	private ResourceLoader rc;

	@BeforeTest
	public void setup() {
		rc = new ResourceLoader("/test/data", ExperimentNames.HybridMasonry1,
				new ResourcesTestModule(), false);
	}

	@Test
	public void testCreateTables() {
		for (ChannelListType clt : ChannelListType.values()) {
			if (clt.equals(ChannelListType.Query1)) {
				break;
			}
			rc.createTable(clt);
			rc.checkTable(clt);
		}
	}

	@Test(dependsOnMethods = { "testSetQueries",  "testUploadData"})
	public void testDoQueries() {
		List<ChannelListType> queries = new ArrayList<ChannelListType>();
		queries.add(ChannelListType.Query1);
		queries.add(ChannelListType.Query2);
		for (ChannelListType quy : queries) {
			for (QueryTypes qt : QueryTypes.values()) {
				DoubleMatrix dm = rc.doQuery(qt, quy);
				rc.checkQueryData(qt, quy, dm);
			}
		}
	}

	@Test
	public void testSetQueries() {
		rc.createQuery(ChannelListType.Query1);
		rc.checkQuery(ChannelListType.Query1);
		rc.createQuery(ChannelListType.Query2);
		rc.checkQuery(ChannelListType.Query2);
	}

	@Test(dependsOnMethods = { "testCreateTables" })
	public void testUploadData() {
		for (ChannelListType clt : ChannelListType.values()) {
			if (clt.equals(ChannelListType.Query1)) {
				break;
			}
			rc.uploadDataset(clt);
			rc.checkDataset(clt);
		}
	}
}