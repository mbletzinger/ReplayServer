package org.nees.illinois.replay.events;

import org.nees.illinois.replay.common.types.TableIdentityI;

/**
 * Class to manage step numbers. A step number is a triplet of the form step,
 * substep, and correction step. This class converts the step number to string
 * and does comparisons.
 * @author Michael Bletzinger
 */
public class StepNumber implements IterationStepI {
	/**
	 * Correction step number.
	 */
	private final int correctionStep;

	/**
	 * Database ID of the step number.
	 */
	private final String id;

	/**
	 * Step index used for comparing with other steps.
	 */
	private double index;
	/**
	 * Source which recorded the event.
	 */
	private final TableIdentityI source;
	/**
	 * Step number.
	 */
	private final int step;
	/**
	 * Substep number.
	 */
	private final int substep;
	/**
	 * Time stamp of the step number.
	 */
	private final double time;

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
	 * @param source
	 *            Step index used for comparing with other steps.
	 */
	public StepNumber(final int step, final int substep,
			final int correctionStep, final double time,
			final String id, final TableIdentityI source) {
		super();
		this.step = step;
		this.substep = substep;
		this.correctionStep = correctionStep;
		this.time = time;
		this.id = id;
		this.source = source;
	}
	/**
	 * Constructor from a String. The "_" underline character is the delimiter.
	 * @param steps
	 *            Step number string.
	 * @param time
	 *            Time stamp of the step number.
	 * @param id
	 *            Database ID of the step number.
	 * @param source
	 *            Step index used for comparing with other steps.
	 */
	public StepNumber(final String steps, final double time,
			final String id, final TableIdentityI source) {
		super();
		String[] ssteps = steps.split("_");
		this.step = Integer.parseInt(ssteps[0]);
		this.substep = Integer.parseInt(ssteps[1]);
		this.correctionStep = Integer.parseInt(ssteps[2]);
		this.time = time;
		this.id = id;
		this.source = source;
	}

	@Override
	public final int compareTo(final EventI other) {
		Double me = new Double(this.time);
		Double them = new Double(other.getTime());
		return me.compareTo(them);
	}

	/**
	 * @return the correctionStep
	 */
	public final int getCorrectionStep() {
		return correctionStep;
	}

	@Override
	public final String getDescription() {
		return toString();
	}

	@Override
	public final String getid() {
		return id;
	}

	/**
	 * @return the index
	 */
	public final double getIndex() {
		return index;
	}

	@Override
	public final String getName() {
		return toString();
	}

	@Override
	public final TableIdentityI getSource() {
		return source;
	}

	/**
	 * @return the step
	 */
	public final int getStep() {
		return step;
	}

	@Override
	public final double getStepIndex() {
		return index;
	}

	/**
	 * @return the substep
	 */
	public final int getSubstep() {
		return substep;
	}

	@Override
	public final double getTime() {
		return time;
	}

	@Override
	public final EventType getType() {
		return EventType.StepNumber;
	}

	/**
	 * @param index the index to set
	 */
	public final void setIndex(final double index) {
		this.index = index;
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
