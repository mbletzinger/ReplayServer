package org.nees.illinois.replay.db.data;

import org.nees.illinois.replay.common.types.TableDefinitionI;
import org.nees.illinois.replay.db.statement.StatementProcessor;

/**
 * Create a database table based on the {@link TableDefinitionI definition}.
 * @author Michael Bletzinger
 */
public class DataTableOps {
	/**
	 * Definition for the table.
	 */
	private final TableDefinitionI def;
	/**
	 * Database statement executor.
	 */
	private final StatementProcessor statement;

	/**
	 * @return the SQL statement to create the table.
	 */
	private String createTableStatement() {
		String result = "CREATE TABLE " + def.getTableId().getDbName() + "(";
		boolean first = true;
		for (String h : def.getColumns(true)) {
			result += (first ? "" : ", ") + h + " double NOT NULL";
			first = false;
		}
		result += ")";
		return result;
	}

	/**
	 * @param def
	 *            Definition for the table.
	 * @param statement
	 *            Database statement executor.
	 */
	public DataTableOps(final TableDefinitionI def,
			final StatementProcessor statement) {
		this.def = def;
		this.statement = statement;
	}

	/**
	 * Create a data table in the database.
	 * @return True if successful.
	 */
	public final boolean create() {
		String createStr = createTableStatement();
		boolean result = statement.execute(createStr);
		statement.close();
		return result;
	}

	/**
	 * Remove a table from the database.
	 * @return True iof successful.
	 */
	public final boolean remove() {
		boolean result = statement.execute("DROP TABLE "
				+ def.getTableId().getDbName());
		statement.close();
		return result;
	}
}
