package org.nees.illinois.replay.events;

/**
 * Class to manage step numbers. A step number is a triplet of the form step,
 * substep, and correction step. This class converts the step number to string
 * and does comparisons.
 * @author Michael Bletzinger
 */
public class StepNumber implements Comparable<StepNumber>, IterationStepI {
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
	 * Database ID of the step number.
	 */
	private final String id;
	/**
	 * Time stamp of the step number.
	 */
	private final TimeAndSource time;

	/**
	 * Step index used for comparing with other steps.
	 */
	private final double index;

	/**
	 * Constructor for doubles from a data matrix.
	 * @param step
	 *            Step number.
	 * @param substep
	 *            Substep number.
	 * @param correctionStep
	 *            Correction step number.
	 * @param time
	 *            timestamp for step number.
	 * @param id
	 *            Database ID of the step number.
	 * @param index
	 *            Step index used for comparing with other steps.
	 */
	public StepNumber(final double step, final double substep,
			final double correctionStep, final TimeAndSource time,
			final String id, final double index) {
		super();
		this.step = Math.round(step);
		this.substep = Math.round(substep);
		this.correctionStep = Math.round(correctionStep);
		this.time = time;
		this.id = id;
		this.index = index;
	}

	/**
	 * Constructor for an integer triple.
	 * @param step
	 *            Step number.
	 * @param substep
	 *            Substep number.
	 * @param correctionStep
	 *            Correction step number.
	 * @param time
	 *            Time stamp of the step number.
	 * @param id
	 *            Database ID of the step number.
	 * @param index
	 *            Step index used for comparing with other steps.
	 */
	public StepNumber(final int step, final int substep,
			final int correctionStep, final TimeAndSource time,
			final String id, final double index) {
		super();
		this.step = step;
		this.substep = substep;
		this.correctionStep = correctionStep;
		this.time = time;
		this.id = id;
		this.index = index;
	}

	/**
	 * Constructor from a String. The "_" underline character is the delimiter.
	 * @param steps
	 *            Step number string.
	 * @param time
	 *            Time stamp of the step number.
	 * @param id
	 *            Database ID of the step number.
	 * @param index
	 *            Step index used for comparing with other steps.
	 */
	public StepNumber(final String steps, final TimeAndSource time,
			final String id, final double index) {
		super();
		String[] ssteps = steps.split("_");
		this.step = Integer.parseInt(ssteps[0]);
		this.substep = Integer.parseInt(ssteps[1]);
		this.correctionStep = Integer.parseInt(ssteps[2]);
		this.time = time;
		this.id = id;
		this.index = index;
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

	@Override
	public final String getName() {
		return toString();
	}

	@Override
	public final String getid() {
		return id;
	}

	@Override
	public final String getDescription() {
		return toString();
	}

	@Override
	public final double getStepIndex() {
		return index;
	}

	@Override
	public final TimeAndSource getTime() {
		return time;
	}

	@Override
	public final EventType getType() {
		return EventType.StepNumber;
	}
}
