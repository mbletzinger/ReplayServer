package org.nees.mustsim.replay.db.data;

public class StepNumber implements Comparable<StepNumber> {
	private final long step;
	private final long substep;
	private final long correctionStep;
	

	public StepNumber(int step, int substep, int correctionStep) {
		super();
		this.step = step;
		this.substep = substep;
		this.correctionStep = correctionStep;
	}

	public StepNumber(double step, double substep, double correctionStep) {
		super();
		this.step = Math.round(step);
		this.substep = Math.round(substep);
		this.correctionStep = Math.round(correctionStep);
	}

	@Override
	public int compareTo(StepNumber other) {
		if(other.step != step) {
			return compareToLong(step, other.step);
		}
		if(other.substep != substep) {
			return compareToLong(substep, other.substep);
		}
		return compareToLong(correctionStep, other.correctionStep);
		
	}

	private int compareToLong(long me, long other) {
		Long meL = new Long(me);
		Long othL = new Long(other);
		return meL.compareTo(othL);
	}
}
