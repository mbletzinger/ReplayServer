package org.nees.illinois.replay.test.server.restlet;

import java.util.Map;

import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UriTestServerResource extends ServerResource {
	
	private final Logger log = LoggerFactory
			.getLogger(UriTestServerResource.class);
	@Get
	public Representation get() {
		return process();
	}

	@Put
	public Representation put() {
		return process();
	}
	
	@Post
	public Representation post() {
		return process();
	}

	private Representation process() {
		String result;
		Map<String, Object> attrs = getRequestAttributes();
		Reference ref = getReference();
		result = ref.toString();
		for(String s : attrs.keySet()) {
			result += "\n\t" + s + "=" + attrs.get(s).toString();
		}
		log.debug("Processed \"" + result + " \"");
		Representation rep = new StringRepresentation(result.toCharArray());
		return rep;
	}
}
