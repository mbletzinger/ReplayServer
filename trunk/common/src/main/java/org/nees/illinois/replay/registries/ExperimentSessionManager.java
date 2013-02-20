package org.nees.illinois.replay.registries;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.nees.illinois.replay.restlet.AttributeExtraction;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

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

		public synchronized ExperimentRegistries getRegistries(boolean allowNewExperiments) throws ResourceException {
			String experiment = extract.getExperiment();
			ExperimentRegistries er = null;
			String key = experiment + ".registries";
			if(cxtAttrs.containsKey(key)) {
				er = (ExperimentRegistries)cxtAttrs.get(key);
			} else {
				// Force a runtime error for testing
				if (experiment.contains("ERR")) {
					throw new RuntimeException("Help me I died");
				}
				if(allowNewExperiments) {
				guiceMod.setExperiment(experiment);
				Injector injector = Guice.createInjector(guiceMod);
				er = injector.getInstance(ExperimentRegistries.class);
				er.setLookups(injector.getProvider(ChannelNameManagement.class));
				cxtAttrs.put(key, er);
				} else {
					throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "\"" + experiment + "\" is not an experiment");
				}
			}
			return er;
		}
}
