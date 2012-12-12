package org.nees.illinois.replay.conversions;

import java.io.IOException;

import org.restlet.representation.Representation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Representation2DoubleMatrix {
	private final InputStream2DoubleMatrix in2dm;

	private final Logger log = LoggerFactory
			.getLogger(Representation2DoubleMatrix.class);
	public Representation2DoubleMatrix(Representation rep) {
		super();
		InputStream2DoubleMatrix i2d = null;
		try {
			i2d = new InputStream2DoubleMatrix(rep.getStream());
		} catch (IOException e1) {
			try {
				log.error("Could not read representation \"" + rep.getText()
						+ "\"");
			} catch (IOException e) {
				log.error("Could not read representation \"" + rep + "\"");
			}
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
