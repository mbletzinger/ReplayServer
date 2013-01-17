package org.nees.illinois.replay.test.server.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class HttpClientFunctions {

	private final Logger log = LoggerFactory
			.getLogger(HttpClientFunctions.class);

	public HttpClientFunctions() {
	}

	public HttpEntity execute(HttpRequestBase request, String uriString) {
		log.debug("executing " + uriString);
		HttpResponse response = sendRequest(request, uriString);
		log.debug("done with  " + uriString);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		HttpEntity entity = response.getEntity();

		Assert.assertNotNull(entity);
		return entity;

	}

	public HttpEntity executeFail(HttpRequestBase request, String uriString,
			int failCode) {
		log.debug("executing " + uriString);
		HttpResponse response = sendRequest(request, uriString);
		log.debug("done with " + uriString);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), failCode);
		HttpEntity entity = response.getEntity();
		return entity;
	}

	public HttpResponse sendRequest(HttpRequestBase request, String uriString) {
		HttpResponse response = null;
		HttpClient httpclient = new DefaultHttpClient();
		try {
			request.setURI(new URI(uriString));
		} catch (URISyntaxException e1) {
			log.error("URI : " + uriString + " could not be parsed", e1);
			Assert.fail();
		}

		try {
			response = httpclient.execute(request);
		} catch (ClientProtocolException e) {
			log.error("Protocol failure for " + uriString, e);
			Assert.fail();
		} catch (IOException e) {
			log.error("IO failure for " + uriString, e);
			Assert.fail();
		}
		log.debug("Request " + request + " cause Response " + response);
		return response;
	}

}
