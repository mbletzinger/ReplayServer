package org.nees.illinois.replay.conversions;

import org.restlet.representation.ByteArrayRepresentation;
import org.restlet.representation.Representation;

public class DoubleMatrix2Representation {
	private final DoubleMatrix2OutputStream dm2os;

//	private final Logger log = LoggerFactory
//			.getLogger(DoubleMatrix2Representation.class);

	private final Representation rep;

	public DoubleMatrix2Representation(double[][] data) {
		super();
		dm2os = new DoubleMatrix2OutputStream(data);
		this.rep = new ByteArrayRepresentation(dm2os.getBuffer());
	}

	/**
	 * @return the dm2os
	 */
	public DoubleMatrix2OutputStream getDm2os() {
		return dm2os;
	}

	/**
	 * @return the representation
	 */
	public Representation getRep() {
		return rep;
	}
	
}
