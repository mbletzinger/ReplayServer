package org.nees.illinois.replay.test.db.utils;

import java.sql.Connection;

public interface DbManagement {

	public abstract void createDatabase(Connection connection);

	public abstract void removeDatabase(Connection connection);

	public abstract Connection generateConnection(boolean withDatabase);

	public abstract void closeConnection(Connection connection);
	
	public abstract String getExperiment();

}