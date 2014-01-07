package org.nees.illinois.replay.events;

import org.nees.illinois.replay.common.types.EqualsWithNulls;

/**
 * Class to manage step numbers. A step number is a triplet of the form step,
 * substep, and correction step. This class converts the step number to string
 * and does comparisons.
 * @author Michael Bletzinger
 */
public class StepNumber implements EventI {
	/**
	 * Correction step number.
	 */
	private final int correctionStep;

	/**
	 * Source which recorded the event.
	 */
	private final String source;
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
	 * @param source
	 *            Step index used for comparing with other steps.
	 */
	public StepNumber(final int step, final int substep,
			final int correctionStep, final double time, final String source) {
		super();
		this.step = step;
		this.substep = substep;
		this.correctionStep = correctionStep;
		this.time = time;
		this.source = source;
	}

	/**
	 * Constructor from a String. The "_" underline character is the delimiter.
	 * @param steps
	 *            Step number string.
	 * @param time
	 *            Time stamp of the step number.
	 * @param source
	 *            Step index used for comparing with other steps.
	 */
	public StepNumber(final String steps, final double time, final String source) {
		super();
		String[] ssteps = steps.split("_");
		this.step = Integer.parseInt(ssteps[0]);
		this.substep = Integer.parseInt(ssteps[1]);
		this.correctionStep = Integer.parseInt(ssteps[2]);
		this.time = time;
		this.source = source;
	}

	@Override
	public final int compareTo(final EventI other) {
		Double me = new Double(this.time);
		Double them = new Double(other.getTime());
		return me.compareTo(them);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj) {
		EqualsWithNulls<String> eq = new EqualsWithNulls<String>();
		if (obj instanceof StepNumber == false) {
			return false;
		}

		StepNumber it = (StepNumber) obj;
		if (Double.compare(time, it.time) != 0) {
			return false;
		}
		if (step != it.step) {
			return false;
		}
		if (substep != it.substep) {
			return false;
		}
		if (correctionStep != it.correctionStep) {
			return false;
		}
		return eq.equate(source, it.source);
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
	public final String getName() {
		return toString();
	}

	@Override
	public final String getSource() {
		return source;
	}

	/**
	 * @return the step
	 */
	public final int getStep() {
		return step;
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return super.hashCode();
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
