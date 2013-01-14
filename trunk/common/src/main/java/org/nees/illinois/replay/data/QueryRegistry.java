package org.nees.illinois.replay.data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

public class QueryRegistry {

	private final ConcurrentMap<String, QuerySpec> queries = new ConcurrentHashMap<String, QuerySpec>();
	private final Logger log = Logger.getLogger(QueryRegistry.class);
	public QuerySpec getQuery(String name) {
		return queries.get(name);
	}
	public void setQuery(String name, QuerySpec dq) {
		if (queries.containsKey(name)) {
			log.info("Replacing Query \"" + name + "\"");
		} else {
			log.info("Adding Query \"" + name + "\"");
		}
			queries.put(name, dq);
	}
	
}
