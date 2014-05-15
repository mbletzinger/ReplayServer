package org.nees.illinois.replay.test.db.utils;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.common.types.TableDefinitionI;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.nees.illinois.replay.test.utils.CompareLists;
import org.nees.illinois.replay.test.utils.gen.DoubleMatrixGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.AssertJUnit;

/**
 * Class to check the table definition against the database.
 * @author Michael Bletzinger
 */
public class DbTableCheck {
	/**
	 * Name of database.
	 */
	private final String experiment;
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory.getLogger(DbTableCheck.class);
	/**
	 * Database pool.
	 */
	private final DbPools pools;

	/**
	 * @param pools
	 *            Database pool.
	 * @param experiment
	 *            Name of database.
	 */
	public DbTableCheck(final DbPools pools, final String experiment) {
		this.pools = pools;
		this.experiment = experiment;
	}

	/**
	 * Verify that a table is in the database.
	 * @param tableid
	 *            name of the table
	 * @param columnNames
	 *            channel list for the table.
	 */
	public final void checkTable(final String tableid,
			final List<String> columnNames) {
		DatabaseMetaData databaseMetaData = null;
		try {
			databaseMetaData = pools.fetchConnection(experiment, false)
					.getMetaData();
		} catch (SQLException e1) {
			log.error("Metadata query failed ", e1);
			Assert.fail();
		}
		ResultSet result = null;
		try {
			result = databaseMetaData.getTables(null, null, null, null);
		} catch (SQLException e1) {
			log.error("Metadata query failed ", e1);
			Assert.fail();
		}
		boolean found = false;
		try {
			while (result.next()) {
				final int nameColumn = 3;
				String tableName = result.getString(nameColumn);
				log.debug("Checking table name " + tableName);
				if (tableName.equals(tableid)) {
					found = true;
				}
			}
		} catch (SQLException e1) {
			log.error("Metadata query failed ", e1);
			Assert.fail();
		}
		Assert.assertTrue(found);
		try {
			result = databaseMetaData.getColumns(null, null, tableid, null);
		} catch (SQLException e) {
			log.error("Metadata query failed ", e);
			Assert.fail();
		}
		List<String> actual = new ArrayList<String>();
		try {
			while (result.next()) {
				final int columnNameColumn = 4;
				String columnName = result.getString(columnNameColumn);
				actual.add(columnName);
				// int columnType = result.getInt(5);
			}
		} catch (SQLException e) {
			log.error("Metadata query failed ", e);
			Assert.fail();
		}
		try {
			databaseMetaData.getConnection().close();
		} catch (SQLException e) {
			log.debug("close failed but who cares");
		}
		CompareLists<String> cmp = new CompareLists<String>();
		cmp.compare(actual, columnNames);
	}

	/**
	 * Checks the contents of the database table.
	 * @param def
	 *            table parameters.
	 * @param expected
	 *            expected values.
	 */
	public final void checkTableContents(final TableDefinitionI def,
			final DoubleMatrix expected) {
		String tblName = def.getTableId();
		StatementProcessor dbSt = pools.createDbStatement(experiment, false);
		ResultSet rs = dbSt.query("SELECT * FROM \"" + tblName + "\"");
		double[][] actual = new double[expected.toList().size()][def.getColumns(true).size()];
		log.info("Results allocated size is [" + actual.length + "]["
				+ actual[0].length + "]");
		int r = 0;
		try {
			while (rs.next()) {
				int c = 0;
				for (String colName : def.getColumns(true)) {
					actual[r][c] = rs.getDouble(colName);
					c++;
				}
				r++;
			}
		} catch (SQLException e) {
			log.error("Result Set fetch failed because ", e);
			AssertJUnit.fail();
		}
		dbSt.closeQuery(rs);
		dbSt.close();
		DoubleMatrixGenerator.compareData(actual, expected.getData());
	}
}
