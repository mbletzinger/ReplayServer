package org.nees.illinois.replay.db.statement;

import java.sql.Connection;


/**
 * Class for managing insertion prepared statements.
 * @author Michael Bletzinger
 */
public abstract class InsertStatement {

	/**
	 * Statement builder.
	 */
	private final PrepStatementProcessor builder;

	/**
	 * @param connection
	 *            JDBC connection.
	 * @param prep
	 *            Initial string for prepared statement.
	 */
	public InsertStatement(final Connection connection, final String prep) {
		this.builder = new PrepStatementProcessor(connection, prep);
		builder.create();
	}

	/**
	 * @return the builder
	 */
	public final PrepStatementProcessor getBuilder() {
		return builder;
	}

}
