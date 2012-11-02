package org.nees.mustsim.replay.data;

public class StepNumber implements Comparable<StepNumber> {
	private final long correctionStep;

	private final long step;

	private final long substep;

	public StepNumber(double step, double substep, double correctionStep) {
		super();
		this.step = Math.round(step);
		this.substep = Math.round(substep);
		this.correctionStep = Math.round(correctionStep);
	}
	public StepNumber(String steps) {
		super();
		String [] ssteps = steps.split("_");
		this.step = Integer.parseInt(ssteps[0]);
		this.substep = Integer.parseInt(ssteps[1]);
		this.correctionStep = Integer.parseInt(ssteps[2]);
	}
	public StepNumber(int step, int substep, int correctionStep) {
		super();
		this.step = step;
		this.substep = substep;
		this.correctionStep = correctionStep;
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

	/**
	 * @return the correctionStep
	 */
	public long getCorrectionStep() {
		return correctionStep;
	}

	/**
	 * @return the step
	 */
	public long getStep() {
		return step;
	}

	/**
	 * @return the substep
	 */
	public long getSubstep() {
		return substep;
	}
}
