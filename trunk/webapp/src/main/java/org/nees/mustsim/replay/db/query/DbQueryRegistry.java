package org.nees.mustsim.replay.db.query;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

public class DbQueryRegistry {

	private final Map<String, DbQuerySpec> queries = new ConcurrentHashMap<String, DbQuerySpec>();
	private final Logger log = Logger.getLogger(DbQueryRegistry.class);
	public DbQuerySpec getQuery(String name) {
		return queries.get(name);
	}
	public void setQuery(String name, DbQuerySpec dq) {
		if (queries.containsKey(name)) {
			log.info("Replacing Query \"" + name + "\"");
		} else {
			log.info("Adding Query \"" + name + "\"");
		}
			queries.put(name, dq);
	}
	
}
