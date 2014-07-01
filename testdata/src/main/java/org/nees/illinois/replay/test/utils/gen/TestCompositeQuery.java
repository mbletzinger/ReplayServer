package org.nees.illinois.replay.test.utils.gen;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.test.utils.types.MatrixMixType;
import org.nees.illinois.replay.test.utils.types.TestDataSource;

/**
 * Class which holds test lists of channels as well as their data sets for
 * merging.
 * @author Michael Bletzinger
 */
public class TestCompositeQuery {
	/**
	 * First list of channels.
	 */
	private final TestCompositeQuery existing;
	/**
	 * How the two sets are mixed.
	 */
	private final MatrixMixType mix;
	/**
	 * Name of the test query.
	 */
	private final String name;
	/**
	 * Channels to be merged.
	 */
	private final List<String> newChannels = new ArrayList<String>();
	/**
	 * Dataset that the new channels come from.
	 */
	private final TestDataSource source;

	/**
	 * @param mix
	 *            How the two sets are mixed.
	 * @param existing
	 *            First list of channels.
	 * @param newChannels
	 *            Channels to be merged.
	 * @param name
	 *            Name of the test query.
	 * @param source
	 *            Table that the new channels come from.
	 */
	public TestCompositeQuery(final MatrixMixType mix,
			final TestCompositeQuery existing,final List<String> newChannels,
			final String name,final TestDataSource source) {
		super();
		this.name = name;
		this.mix = mix;
		this.newChannels.addAll(newChannels);
		this.existing = existing;
		this.source = source;
	};

	/**
	 * Does the merge.
	 * @return return the merged matrix.
	 */
	public final List<String> combine() {
		List<String> result = new ArrayList<String>();
		List<String> all = new ArrayList<String>();

		if (existing != null) {
			all.addAll(existing.combine());
		}

		if (mix.equals(MatrixMixType.AddBefore)) {
			result.addAll(all);
			result.addAll(newChannels);
			return result;
		}

		if (mix.equals(MatrixMixType.AddAfter)) {
			result.addAll(newChannels);
			result.addAll(all);
			return result;
		}

		if (mix.equals(MatrixMixType.AddMiddle)) {
			return combineMiddle();
		}

		return combineMixed();

	}

	/**
	 * Add the new channels halfway between the existing channels.
	 * @return The combined result.
	 */
	private List<String> combineMiddle() {
		List<String> result = new ArrayList<String>();
		List<String> all = existing.combine();
		int middle = newChannels.size() / 2;
		result.addAll(newChannels.subList(0, middle));
		result.addAll(all);
		result.addAll(newChannels.subList(middle + 1, newChannels.size() - 1));
		return result;
	}

	/**
	 * Add the new channels alternating with the old ones.
	 * @return The combined result.
	 */
	private List<String> combineMixed() {
		List<String> result = new ArrayList<String>();
		List<String> all = existing.combine();
		int dsz = all.size();
		int osz = newChannels.size();
		int min = dsz > osz ? osz : dsz;
		for (int i = 0;i < min;i++) {
			result.add(newChannels.get(i));
			result.add(all.get(i));
		}
		if (dsz > osz) {
			result.addAll(all.subList(min, dsz - 1));
		} else {
			result.addAll(newChannels.subList(min, osz - 1));
		}
		return result;
	}

	/**
	 * @return the existing channels
	 */
	public final TestCompositeQuery getExisting() {
		return existing;
	}

	/**
	 * Get the existing channels as a {@link List}.
	 * @return The list.
	 */
	public final List<String> getExistingList() {
		List<String> result;
		if (existing == null) {
			result = new ArrayList<String>();
		} else {
			result = existing.combine();
		}
		return result;
	}

	/**
	 * @return the mix
	 */
	public final MatrixMixType getMix() {
		return mix;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return the new channels
	 */
	public final List<String> getNewChannels() {
		return newChannels;
	}

	/**
	 * @return the newTable
	 */
	public final TestDataSource getSource() {
		return source;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		String result = "/name=" + name + "\n\t/existing="
				+ (existing == null ? "none" : existing) + "\n\t/mix=" + mix
				+ "/source=" + source + "/new=";
		boolean first = true;
		for (String c : newChannels) {
			result += (first ? "" : ",") + "\n\t" + c;
		}
		return result;
	}

}
