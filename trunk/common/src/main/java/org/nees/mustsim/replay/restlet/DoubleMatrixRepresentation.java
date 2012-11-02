package org.nees.mustsim.replay.restlet;

import java.io.IOException;
import java.io.OutputStream;

import org.nees.mustsim.replay.conversions.Double2OutputStream;
import org.nees.mustsim.replay.data.DoubleMatrix;
import org.restlet.data.MediaType;
import org.restlet.representation.OutputRepresentation;

public class DoubleMatrixRepresentation extends OutputRepresentation {

	private final DoubleMatrix data;
	
	public DoubleMatrixRepresentation(long expectedSize, DoubleMatrix data) {
		super(MediaType.APPLICATION_JAVA, expectedSize);
		this.data = data;
	}

	@Override
	public void write(OutputStream os) throws IOException {
		Double2OutputStream db2os = new Double2OutputStream(os);
		db2os.writeData(data.getData());
	}

}
