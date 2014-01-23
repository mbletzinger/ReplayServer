package org.nees.illinois.replay.test.utils.gen.tricks;

import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrixI;

/**
 * Class which reorganizes the columns of a double matrix according to before
 * and after channel lists.
 * @author Michael Bletzinger
 */
public class ChannelDataOrganizer {
	/**
	 * List of channels corresponding to column headers of the input matrix.
	 */
	private final List<String> inChannels;
	/**
	 * List of channels corresponding to column headers of the reorganized
	 * matrix.
	 */
	private final List<String> outChannels;

	/**
	 * @param inChannels
	 *            List of channels corresponding to column headers of the input
	 *            matrix.
	 * @param outChannels
	 *            List of channels corresponding to column headers of the
	 *            reorganized matrix.
	 */
	public ChannelDataOrganizer(final List<String> inChannels,final List<String> outChannels) {
		this.inChannels = inChannels;
		this.outChannels = outChannels;
	}

	/**
	 * Perform the reorganization of the matrix.
	 * @param data
	 *            Input matrix.
	 * @return reorganized matrix.
	 */
	public final DoubleMatrixI reorganize(final DoubleMatrixI data) {
		SubsetSlicer slicer = new SubsetSlicer(data);
		for (String c : outChannels) {
			int i = inChannels.indexOf(c);
			if (i < 0) {
				continue;
			}
			slicer.addSlice(i);
		}
		return slicer.slice();
	}
}
