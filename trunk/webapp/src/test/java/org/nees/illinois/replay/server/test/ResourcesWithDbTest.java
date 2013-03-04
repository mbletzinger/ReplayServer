package org.nees.illinois.replay.server.test;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.restlet.guice.DbConfigType;
import org.nees.illinois.replay.restlet.guice.LocalRestletTestDbModule;
import org.nees.illinois.replay.server.test.utils.ResourceLoaderWithDb;
import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.nees.illinois.replay.test.utils.ChannelDataTestingLists.ChannelListType;
import org.nees.illinois.replay.test.utils.DatasetDirector.ExperimentNames;
import org.nees.illinois.replay.test.utils.DatasetDirector.QueryTypes;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

@Test(groups = { "resources-test" })
public class ResourcesWithDbTest {
	private ResourceLoaderWithDb rc;
	private boolean ismysql;
	private final DerbyDbControl derby = new DerbyDbControl();

	@Parameters("db")
	@BeforeClass
	public void beforeClass(@Optional("mysql") String db) {
		ismysql = db.equals("mysql");
		if (ismysql == false) {
			derby.startDerby();
		}
		rc = new ResourceLoaderWithDb("/data", ExperimentNames.HybridMasonry1,
				new LocalRestletTestDbModule(ismysql ? DbConfigType.LocalTestMySql : DbConfigType.LocalTestDerby), false);
	}

	@AfterClass
	public void afterClass() {
		rc.removeDatabase();
		if (ismysql == false) {
			derby.stopDerby();
		}
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