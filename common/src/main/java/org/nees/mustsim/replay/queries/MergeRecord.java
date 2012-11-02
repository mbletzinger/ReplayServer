package org.nees.mustsim.replay.queries;

import java.util.ArrayList;
import java.util.List;

import org.nees.mustsim.replay.data.NumberOfColumns;
import org.nees.mustsim.replay.data.RateType;
import org.nees.mustsim.replay.data.StepNumber;

public class MergeRecord implements Comparable<MergeRecord> {
	private List<Double> list;
	private boolean merged = false;

	private NumberOfColumns noc;

	public MergeRecord(RateType rate, List<Double> list) {
		super();
		this.list = list;
		int dsz = NumberOfColumns.dataColumns(list.size(), rate);
		noc = new NumberOfColumns(dsz, rate);
	}

	public void append(List<Double> after) {
		int i = noc.getTimeNumber();
		list.addAll(after.subList(i, after.size()));
		int dsz = noc.getNumber(false) + after.size() - i;
		noc = new NumberOfColumns(dsz, noc.getRate());
		merged = true;
	}

	public void appendNulls(int num) {
		List<Double> nulls = new ArrayList<Double>();
		for (int n = 0; n < num; n++) {
			nulls.add(null);
		}
		append(nulls);
	}

	private int compareTo(double a, double b) {
		Double aD = new Double(a);
		Double bD = new Double(b);
		return aD.compareTo(bD);
	}

	@Override
	public int compareTo(MergeRecord o) {
		if (noc.getRate().equals(RateType.STEP)) {
			StepNumber aS = new StepNumber(list.get(1), list.get(2),
					list.get(3));
			StepNumber tS = new StepNumber(o.list.get(1), o.list.get(2),
					o.list.get(3));
			return aS.compareTo(tS);
		}
		return compareTo(list.get(0), o.list.get(0));
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MergeRecord == false) {
			return false;
		}
		return compareTo((MergeRecord) obj) == 0;
	}
	/**
	 * @return the list
	 */
	public List<Double> getList() {
		return list;
	}

	/**
	 * @return the noc
	 */
	public NumberOfColumns getNoc() {
		return noc;
	}

	/**
	 * @return the merged
	 */
	public boolean isMerged() {
		return merged;
	}

	public void prepend(List<Double> before) {
		int i = noc.getTimeNumber();
		list.addAll(i, before.subList(i, before.size()));
		int dsz = noc.getNumber(false) + before.size() - i;
		noc = new NumberOfColumns(dsz, noc.getRate());
		merged = true;
	}

	public void prependNulls(int num) {
		List<Double> nulls = new ArrayList<Double>();
		for (int n = 0; n < num; n++) {
			nulls.add(null);
		}
		prepend(nulls);
	}

	/**
	 * @param merged the merged to set
	 */
	public void setMerged(boolean merged) {
		this.merged = merged;
	}

	public int size() {
		return list.size();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "";
		boolean first = true;
		for(Double d : list) {
			result += (first ? "" : ", ") + d;
			first = false;
		}
		return result;
	}
}
