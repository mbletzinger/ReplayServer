package org.nees.mustsim.replay.db.query;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

public class DbQueryRegistry {

	private final Map<String, DbSelect> queries = new ConcurrentHashMap<String, DbSelect>();
	private final Logger log = Logger.getLogger(DbQueryRegistry.class);
	public DbSelect getQuery(String name) {
		return queries.get(name);
	}
	public void setQuery(String name, DbSelect dq) {
		if (queries.containsKey(name)) {
			log.info("Replacing Query \"" + name + "\"");
		} else {
			log.info("Adding Query \"" + name + "\"");
		}
			queries.put(name, dq);
	}
	
}
