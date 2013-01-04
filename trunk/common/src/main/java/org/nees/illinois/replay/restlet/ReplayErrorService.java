package org.nees.illinois.replay.restlet;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.service.StatusService;

public class ReplayErrorService extends StatusService {
	public final ConcurrentMap<Request, String> traces = new ConcurrentHashMap<Request, String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.restlet.service.StatusService#getRepresentation(org.restlet.data.
	 * Status, org.restlet.Request, org.restlet.Response)
	 */
	@Override
	public Representation getRepresentation(Status status, Request request,
			Response response) {
		if (status.isServerError()) {
			String trace = traces.get(request);
			traces.remove(request);
			trace.replaceAll("\\n", "<br>");
			trace = "Request: " + request + " created an internal server error<br>Trace:<br>" + trace;
			StringRepresentation rep = new StringRepresentation(trace.toCharArray());
			return rep;
		}
		return super.getRepresentation(status, request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.restlet.service.StatusService#getStatus(java.lang.Throwable,
	 * org.restlet.Request, org.restlet.Response)
	 */
	@Override
	public Status getStatus(Throwable thrown, Request req, Response rsp) {
		StringWriter trace = new StringWriter();
		PrintWriter pw = new PrintWriter(trace);
		thrown.printStackTrace(pw);
		traces.put(req, trace.getBuffer().toString());
		return super.getStatus(thrown, req, rsp);
	}

}
