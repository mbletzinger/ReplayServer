package org.nees.illinois.replay.registries;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.nees.illinois.replay.data.RateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryRegistry {

	private final ConcurrentMap<String, SavedQuery> queries = new ConcurrentHashMap<String, SavedQuery>();
	private final Logger log = LoggerFactory.getLogger(QueryRegistry.class);

	public SavedQuery getQuery(String name, RateType rate) {
		return queries.get(name + "_" + rate);
	}

	public void setQuery(String name, RateType rate, SavedQuery dq) {
		if (queries.containsKey(name)) {
			log.info("Replacing Query \"" + name + "_" + rate + "\"");
		} else {
			log.info("Adding Query \"" + name + "_" + rate + "\"");
		}
		queries.put(name + "_" + rate, dq);
	}

}
