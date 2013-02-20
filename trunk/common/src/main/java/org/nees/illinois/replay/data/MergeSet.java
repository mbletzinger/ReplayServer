package org.nees.illinois.replay.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MergeSet {
	private final List<MergeRecord> accum = new ArrayList<MergeRecord>();
	private final Logger log = LoggerFactory.getLogger(MergeSet.class);
	private final RateType rate;

	public MergeSet(RateType rate) {
		super();
		this.rate = rate;
	}

	public int getColumnSize(boolean withTime) {
		return accum.get(0).getNoc().getNumber(withTime);
	}

	public List<List<Double>> getRecords() {
		List<List<Double>> result = new ArrayList<List<Double>>();
		Collections.sort(accum);
		for (MergeRecord r : accum) {
			result.add(r.getList());
		}
		return result;
	}

	public boolean isEmpty() {
		return accum.isEmpty();
	}

	public void merge(List<List<Double>> toMerge) {
		List<MergeRecord> tmr = new ArrayList<MergeRecord>();
		for (List<Double> r : toMerge) {
			tmr.add(new MergeRecord(rate, r));
		}
		String msg = "Merging ";
		for (MergeRecord mr : tmr) {
			msg += "\n\t[" + mr + "]";
		}
		log.debug(msg);
		if (accum.isEmpty()) {
			accum.addAll(tmr);
			return;
		}

		int tsz = tmr.get(0).size();
		int asz = accum.get(0).size();

		log.debug("toMerge column " + tsz + " Accum columns " + asz);

		for (MergeRecord r : tmr) {
			int i = accum.indexOf(r);
			if (i < 0) {
				continue;
			}
			accum.get(i).append(r.getList());
			r.setMerged(true);
		}
		for (MergeRecord r : accum) {
			if (r.isMerged()) {
				r.setMerged(false);
			} else {
				r.appendNulls(tsz);
			}
		}
		for (MergeRecord r : tmr) {
			if (r.isMerged() == false) {
				r.prependNulls(asz);
				accum.add(r);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "";
		for (MergeRecord r : accum) {
			result += "\n\t[" + r + "]";
		}
		return result;
	}
}
