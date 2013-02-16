package org.nees.illinois.replay.db;

import java.sql.Connection;

public interface DbOperationsI {
	public String filterUrl(String url, String experiment);
	public void createDatabase(String experiment) throws Exception;
	public boolean isDatabase(String experiment) throws Exception;
	public void removeDatabase(String experiment) throws Exception;
	public Connection generateConnection(boolean withDatabase) throws Exception;
	public void closeConnection(Connection connection) throws Exception;
	public void setExperiment(String experiment);
	public String getExperiment();
}
