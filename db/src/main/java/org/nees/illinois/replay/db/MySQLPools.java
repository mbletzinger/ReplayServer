package org.nees.illinois.replay.db;

public class MySQLPools extends DbPools {

	public MySQLPools() {
		super("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/","replay","nees@mustsim08");
	}

	@Override
	public String filterUrl(String url, String experiment) {
		// TODO Auto-generated method stub
		return null;
	}

}
