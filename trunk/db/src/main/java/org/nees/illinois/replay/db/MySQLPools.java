package org.nees.illinois.replay.db;

public class MySQLPools extends DbPools {

	public MySQLPools() {
		super("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/","mustsim","Erd12beben");
	}

	@Override
	public String filterUrl(String url, String experiment) {
		// TODO Auto-generated method stub
		return null;
	}

}
