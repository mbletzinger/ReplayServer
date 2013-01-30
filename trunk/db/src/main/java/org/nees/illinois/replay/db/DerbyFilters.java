package org.nees.illinois.replay.db;

public class DerbyFilters  implements DbPoolFilters  {

	@Override
	public String filterUrl(String url, String experiment) {
		// TODO Auto-generated method stub
		return url + experiment + ";create=true";
	}

}
