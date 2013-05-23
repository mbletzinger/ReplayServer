package org.nees.illinois.replay.conversions;

import org.restlet.representation.ByteArrayRepresentation;
import org.restlet.representation.Representation;

/**
 * Converts a channel list into an output stream and wraps inside a restlet
 * {@link Representation representation}.
 * @author Michael Bletzinger
 */
public class DoubleMatrix2Representation {
	/**
	 * Converter.
	 */
	private final DoubleMatrix2OutputStream dm2os;

	// private final Logger log = LoggerFactory
	// .getLogger(DoubleMatrix2Representation.class);
	/**
	 * The resulting representation.
	 */
	private final Representation rep;

	/**
	 * Constructor.
	 * @param data
	 *            Input data.
	 */
	public DoubleMatrix2Representation(final double[][] data) {
		super();
		dm2os = new DoubleMatrix2OutputStream(data);
		this.rep = new ByteArrayRepresentation(dm2os.getBuffer());
	}

	/**
	 * @return the dm2os
	 */
	public final DoubleMatrix2OutputStream getDm2os() {
		return dm2os;
	}

	/**
	 * @return the representation
	 */
	public final Representation getRep() {
		return rep;
	}

}
