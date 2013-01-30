package org.nees.illinois.replay.db;

public class MySqlFilters implements DbPoolFilters {


	@Override
	public String filterUrl(String url, String experiment) {
		return url + experiment;
	}

}
