package org.nees.illinois.replay.test.db;

import org.nees.illinois.replay.common.registries.ChannelNameRegistry;
import org.nees.illinois.replay.common.registries.TableRegistry;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.registry.synch.DbChannelNameSynch;
import org.nees.illinois.replay.db.registry.synch.DbTableDefinitionsSynch;
import org.nees.illinois.replay.db.registry.synch.RegistrySynchI;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.nees.illinois.replay.test.db.utils.DbTestsModule;
import org.nees.illinois.replay.test.utils.DatasetsDirector;
import org.nees.illinois.replay.test.utils.TestDatasetParameters;
import org.nees.illinois.replay.test.utils.types.ExperimentNames;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Test registry synchronization.
 * @author Michael Bletzinger
 */
public class TestRegistrySynchronization {
	/**
	 * Pools under test.
	 */
	private DbPools pools;
	/**
	 * Experiment test name.
	 */
	private final String experiment = "HybridMasonry1";
	/**
	 * True if database used for testing is MySQL.
	 */
	private boolean ismysql;
	/**
	 * Extra controls for Derby.
	 */
	private final DerbyDbControl ddbc = new DerbyDbControl();

	/**
	 * Stand up a database for testing.
	 * @param db
	 *            label of database to set up.
	 * @throws Exception
	 *             If something goes wrong with the database.
	 */
	@Parameters("db")
	@BeforeClass
	public final void setUp(@Optional("derby") final String db) throws Exception {
		DbTestsModule guiceMod = new DbTestsModule(db);
		Injector injector = Guice.createInjector(guiceMod);
		pools = injector.getInstance(DbPools.class);
		ismysql = db.equals("mysql");
		if (ismysql == false) {
			ddbc.startDerby();
		}
	}

	/**
	 * Shut down the database and clean up everything.
	 * @throws Exception
	 *             If something goes wrong with the database.
	 */
	@AfterClass
	public final void tearDown() throws Exception {
		pools.getOps().removeDatabase(experiment);
		pools.close();
		if (ismysql == false) {
			ddbc.stopDerby();
		}
	}

	/**
	 * Test the synchronization of the channel name registry.
	 */
	@Test
	public final void testChannelNameRegistrySynch() {
		TestDatasetParameters lists = new TestDatasetParameters(false, experiment);
		ChannelNameRegistry cnr = new ChannelNameRegistry();
		lists.fillCnr(cnr);
		StatementProcessor dbSt = pools.createDbStatement(experiment, true);
		RegistrySynchI dbcs = new DbChannelNameSynch(cnr, dbSt);
		dbcs.save();
		dbSt.close();
		ChannelNameRegistry cnr1 = new ChannelNameRegistry();
		dbSt = pools.createDbStatement(experiment, false);
		dbcs = new DbChannelNameSynch(cnr1, dbSt);
		dbcs.load();
		AssertJUnit.assertEquals(cnr.toString(), cnr1.toString());
	}
	/**
	 * Test the synchronization of the table registries.
	 */
	@Test
	public final void testTableRegistrySynch() {
		final int numberOfRows = 40;
		final int numberOfEvents = 10;
		DatasetsDirector dd = new DatasetsDirector(ExperimentNames.HybridMasonry1, numberOfRows, numberOfEvents);
		TestDatasetParameters set = dd.getParameters();
		TableRegistry tr = new TableRegistry();
		set.fillTblr(tr);
		StatementProcessor dbSt = pools.createDbStatement(experiment, true);
		RegistrySynchI dbSynch = new DbTableDefinitionsSynch(tr, dbSt);
		dbSynch.save();
		dbSt.close();
		TableRegistry tr1 = new TableRegistry();
		dbSt = pools.createDbStatement(experiment, false);
		dbSynch = new DbTableDefinitionsSynch(tr1, dbSt);
		dbSynch.load();
		dbSt.close();
		dd.checkExpectedTableRegistry(tr1);
	}
}
