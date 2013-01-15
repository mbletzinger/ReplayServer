package org.nees.illinois.replay.registries;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;
import org.nees.illinois.replay.data.RateType;

public class QueryRegistry {

	private final ConcurrentMap<String, QuerySpec> queries = new ConcurrentHashMap<String, QuerySpec>();
	private final Logger log = Logger.getLogger(QueryRegistry.class);
	public QuerySpec getQuery(String name, RateType rate) {
		return queries.get(name + "_" + rate);
	}
	public void setQuery(String name, RateType rate, QuerySpec dq) {
		if (queries.containsKey(name)) {
			log.info("Replacing Query \"" + name + "\"");
		} else {
			log.info("Adding Query \"" + name + "\"");
		}
			queries.put(name+ "_" + rate, dq);
	}
	
}
