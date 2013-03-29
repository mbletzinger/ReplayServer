package org.nees.illinois.replay.test.utils;

import java.util.ArrayList;
import java.util.List;

public class ChannelTestingList {
	
	private final ChannelTestingList existing;

	private final MatrixMixType mix;

	private final String name;

	private final List<String> newChannels = new ArrayList<String>();;

	public ChannelTestingList(MatrixMixType mix,
			ChannelTestingList existing, List<String> newChannels,
			String name) {
		super();
		this.name = name;
		this.mix = mix;
		this.newChannels.addAll(newChannels);
		this.existing = existing;
	}

	public List<String> combine() {
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

	private List<String> combineMiddle() {
		List<String> result = new ArrayList<String>();
		List<String> all = existing.combine();
		int middle = newChannels.size() / 2;
		result.addAll(newChannels.subList(0, middle));
		result.addAll(all);
		result.addAll(newChannels.subList(middle + 1, newChannels.size() - 1));
		return result;
	}

	private List<String> combineMixed() {
		List<String> result = new ArrayList<String>();
		List<String> all = existing.combine();
		int dsz = all.size();
		int osz = newChannels.size();
		int min = dsz > osz ? osz : dsz;
		for (int i = 0; i < min; i++) {
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
	public ChannelTestingList getExisting() {
		return existing;
	}
	
	public List<String> getExistingList() {
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
	public MatrixMixType getMix() {
		return mix;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the new channels
	 */
	public List<String> getNewChannels() {
		return newChannels;
	}

}
