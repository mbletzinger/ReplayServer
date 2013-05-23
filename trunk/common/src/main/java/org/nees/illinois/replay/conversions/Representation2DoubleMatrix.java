package org.nees.illinois.replay.conversions;

import java.io.IOException;

import org.restlet.representation.Representation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts a {@link Representation representation} into a double matrix.
 * @author Michael Bletzinger
 */
public class Representation2DoubleMatrix {
	/**
	 * Converter.
	 */
	private final InputStream2DoubleMatrix in2dm;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory
			.getLogger(Representation2DoubleMatrix.class);

	/**
	 * Constructor which also does the conversion.
	 * @param rep
	 */
	public Representation2DoubleMatrix(final Representation rep) {
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
	 * @return the converter which also contains the resulting double matrix.
	 */
	public final InputStream2DoubleMatrix getIn2dm() {
		return in2dm;
	}

}
