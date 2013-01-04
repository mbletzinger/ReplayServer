package org.nees.illinois.replay.db;

public class DerbyPools extends DbPools {

	public DerbyPools() {
		super("org.apache.derby.jdbc.ClientDriver", "jdbc:derby://localhost:1527/",null,null);
	}

	@Override
	public String filterUrl(String url, String experiment) {
		// TODO Auto-generated method stub
		return url + experiment + ";create=true";
	}

}
