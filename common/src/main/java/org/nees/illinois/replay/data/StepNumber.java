package org.nees.illinois.replay.data;


/**
 * Class to manage step numbers. A step number is a triplet of the form step,
 * substep, and correction step. This class converts the step number to string
 * and does comparisons.
 * @author Michael Bletzinger
 */
public class StepNumber implements Comparable<StepNumber> {
	/**
	 * Correction step number.
	 */
	private final long correctionStep;
	/**
	 * Step number.
	 */
	private final long step;
	/**
	 * Substep number.
	 */
	private final long substep;

	/**
	 * Constructor for doubles from a data matrix.
	 * @param step
	 *            Step number.
	 * @param substep
	 *            Substep number.
	 * @param correctionStep
	 *            Correction step number.
	 */
	public StepNumber(final double step, final double substep, final double correctionStep) {
		super();
		this.step = Math.round(step);
		this.substep = Math.round(substep);
		this.correctionStep = Math.round(correctionStep);
	}

	/**
	 * Constructor for an integer triple.
	 * @param step
	 *            Step number.
	 * @param substep
	 *            Substep number.
	 * @param correctionStep
	 *            Correction step number.
	 */
	public StepNumber(final int step, final int substep, final int correctionStep) {
		super();
		this.step = step;
		this.substep = substep;
		this.correctionStep = correctionStep;
	}

	/**
	 * Constructor from a String. The "_" underline character is the delimiter.
	 * @param steps
	 *            Step number string.
	 */
	public StepNumber(final String steps) {
		super();
		String[] ssteps = steps.split("_");
		this.step = Integer.parseInt(ssteps[0]);
		this.substep = Integer.parseInt(ssteps[1]);
		this.correctionStep = Integer.parseInt(ssteps[2]);
	}

	@Override
	public final int compareTo(final StepNumber other) {
		if (other.step != step) {
			return compareToLong(step, other.step);
		}
		if (other.substep != substep) {
			return compareToLong(substep, other.substep);
		}
		return compareToLong(correctionStep, other.correctionStep);

	}

	/**
	 * Compare long values.
	 * @param me
	 *            First value.
	 * @param other
	 *            Second value.
	 * @return comparison.
	 */
	private int compareToLong(final long me, final long other) {
		Long meL = new Long(me);
		Long othL = new Long(other);
		return meL.compareTo(othL);
	}

	/**
	 * @return the correctionStep
	 */
	public final long getCorrectionStep() {
		return correctionStep;
	}

	/**
	 * @return the step
	 */
	public final long getStep() {
		return step;
	}

	/**
	 * @return the substep
	 */
	public final long getSubstep() {
		return substep;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return step + "_" + substep + "_" + correctionStep;
	}
}
