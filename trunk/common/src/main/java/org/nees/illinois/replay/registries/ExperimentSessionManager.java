package org.nees.illinois.replay.registries;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.nees.illinois.replay.restlet.AttributeExtraction;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ExperimentSessionManager {

		private final AttributeExtraction extract;
		private final ConcurrentMap<String, Object> cxtAttrs;
		private final ExperimentModule guiceMod;

		public ExperimentSessionManager(ConcurrentMap<String, Object> cxtAttrs, Map<String,Object> reqAttrs, ExperimentModule guiceMod) {
			super();
			this.extract = new AttributeExtraction(reqAttrs);
			this.cxtAttrs = cxtAttrs;
			this.guiceMod = guiceMod;
		}

		public synchronized ExperimentRegistries getRegistries() {
			String experiment = extract.getExperiment();
			ExperimentRegistries er = null;
			String key = experiment + ".registries";
			if(cxtAttrs.containsKey(key)) {
				er = (ExperimentRegistries)cxtAttrs.get(key);
			} else {
				guiceMod.setExperiment(experiment);
				Injector injector = Guice.createInjector(guiceMod);
				er = injector.getInstance(ExperimentRegistries.class);
				er.setLookups(injector.getProvider(ChannelLookups.class));
				cxtAttrs.put(key, er);
			}
			return er;
		}
}
