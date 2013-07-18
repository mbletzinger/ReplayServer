package org.nees.illinois.replay.test.db.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.nees.illinois.replay.db.statement.PrepStatementBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used to test prepared statements.
 * @author Michael Bletzinger
 */
public class TestPrepStatement {
	/**
	 * Statement builder.
	 */
	private final PrepStatementBuilder builder;
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory.getLogger(TestPrepStatement.class);

	/**
	 * @param dbTableName
	 *            Database table name.
	 * @param connection
	 *            JDBC connection.
	 */
	public TestPrepStatement(final String dbTableName,
			final Connection connection) {
		this.builder = new PrepStatementBuilder(connection, "INSERT INTO "
				+ dbTableName + "  VALUES(?,?)");
		builder.create();
	}

	/**
	 * Add two double fields to the statement.
	 * @param x1
	 *            first double.
	 * @param x2
	 *            second double.
	 * @return True if successful.
	 */
	public final boolean add(final double x1, final double x2) {
		PreparedStatement statement = builder.getStatement();
		try {
			statement.setDouble(1, x1);
			statement.setDouble(2, x2);
			statement.addBatch();
		} catch (SQLException e) {
			log.error("Cannot add x1 & x2 because ", e);
			return false;
		}
		return true;
	}

	/**
	 * @return the builder
	 */
	public final PrepStatementBuilder getBuilder() {
		return builder;
	}
}
