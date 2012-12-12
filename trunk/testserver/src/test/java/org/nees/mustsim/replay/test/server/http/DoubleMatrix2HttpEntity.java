package org.nees.mustsim.replay.test.server.http;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.nees.mustsim.replay.conversions.DoubleMatrix2OutputStream;

public class DoubleMatrix2HttpEntity {
	private final DoubleMatrix2OutputStream dm2os;

//	private final Logger log = LoggerFactory
//			.getLogger(DoubleMatrix2Representation.class);

	private final HttpEntity ent;

	public DoubleMatrix2HttpEntity(double[][] data) {
		super();
		dm2os = new DoubleMatrix2OutputStream(data);
		this.ent = new ByteArrayEntity(dm2os.getBuffer());
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
	public HttpEntity getEnt() {
		return ent;
	}
	
}
