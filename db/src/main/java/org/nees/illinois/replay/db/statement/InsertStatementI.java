package org.nees.illinois.replay.db.statement;

import java.sql.Connection;
import java.util.List;

public interface InsertStatementI {
	public void start() throws Exception;
	public void add(List<Object> record) throws Exception;
	public void getBuilder();
	public void setConnection(Connection connection);

}
