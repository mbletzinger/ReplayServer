package org.nees.illinois.replay.registries;

import java.util.concurrent.ConcurrentMap;

import org.nees.illinois.replay.restlet.AttributeExtraction;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ExperimentSessionManager {

		private final AttributeExtraction extract;
		private final ConcurrentMap<String, Object> reqAttrs;
		private final ExperimentModule guiceMod;

		public ExperimentSessionManager(ConcurrentMap<String, Object> reqAttrs, ExperimentModule guiceMod) {
			super();
			this.extract = new AttributeExtraction(reqAttrs);
			this.reqAttrs = reqAttrs;
			this.guiceMod = guiceMod;
		}

		public synchronized ExperimentRegistries getRegistries() {
			String experiment = extract.getExperiment();
			ExperimentRegistries er = null;
			if(reqAttrs.containsKey(experiment)) {
				er = (ExperimentRegistries)reqAttrs.get(experiment + ".registries");
			} else {
				guiceMod.setExperiment(experiment);
				Injector injector = Guice.createInjector(guiceMod);
				er = injector.getInstance(ExperimentRegistries.class);
				er.setLookups(injector.getProvider(ChannelLookups.class));
				reqAttrs.put(experiment + ".registries", er);
			}
			return er;
		}
}
