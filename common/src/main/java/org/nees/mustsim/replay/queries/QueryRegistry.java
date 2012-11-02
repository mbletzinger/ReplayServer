package org.nees.mustsim.replay.queries;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class QueryRegistry {

	private final Map<String, QuerySpec> queries = new HashMap<String, QuerySpec>();
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
