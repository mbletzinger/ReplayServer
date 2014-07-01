package org.nees.illinois.replay.test.utils.types;

import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrixI;

/**
 * Class which contains a list of channels from a source and the expected query
 * data for each channel.
 * @author Michael Bletzinger
 */
public class SimpleTestQueryResults {
	/**
	 * Source of the data.
	 */
	private final TestDataSource src;
	/**
	 * channel names acting as column headers.
	 */
	private final List<String> channels;
	/**
	 * Test query results.
	 */
	private final DoubleMatrixI data;

	/**
	 * @param src
	 *            Source of the data.
	 * @param channels
	 *            channel names acting as column headers.
	 * @param data
	 *            Test query results.
	 */
	public SimpleTestQueryResults(final TestDataSource src,final List<String> channels,
			final DoubleMatrixI data) {
		this.src = src;
		this.channels = channels;
		this.data = data;
	}

	/**
	 * @return the channels
	 */
	public final List<String> getChannels() {
		return channels;
	}

	/**
	 * @return the data
	 */
	public final DoubleMatrixI getData() {
		return data;
	}

	/**
	 * @return the src
	 */
	public final TestDataSource getSrc() {
		return src;
	}
}
