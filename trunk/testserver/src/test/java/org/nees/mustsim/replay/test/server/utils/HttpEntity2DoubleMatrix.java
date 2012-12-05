package org.nees.mustsim.replay.test.server.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.nees.mustsim.replay.conversions.InputStream2DoubleMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpEntity2DoubleMatrix {
	private final InputStream2DoubleMatrix in2dm;

	private final Logger log = LoggerFactory
			.getLogger(HttpEntity2DoubleMatrix.class);

	public HttpEntity2DoubleMatrix(HttpEntity ent) {
		super();
		InputStream2DoubleMatrix i2d = null;
		try {
			i2d = new InputStream2DoubleMatrix(ent.getContent());
		} catch (IOException e1) {
			log.error("Could not read representation \"" + ent.toString()
					+ "\"");
			in2dm = i2d;
			return;
		}
		in2dm = i2d;
	}

	/**
	 * @return the in2dm
	 */
	public InputStream2DoubleMatrix getIn2dm() {
		return in2dm;
	}

}
