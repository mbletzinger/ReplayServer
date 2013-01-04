package org.nees.illinois.replay.providers;

import org.restlet.Response;

public class RestletResponseProvider {

	private Response response;

	/**
	 * @return the response
	 */
	public Response getResponse() {
		return response;
	}

	/**
	 * @param response
	 *            the response to set
	 */
	public void setResponse(Response response) {
		this.response = response;
	}

}
