package org.nees.illinois.replay.events;

import org.nees.illinois.replay.common.types.EqualsWithNulls;

/**
 * Class containing an iteration step.
 * @author Michael Bletzinger
 */
public class IterationStep implements IterationStepI {

	/**
	 * Optional description of the event.
	 */
	private final String description;

	/**
	 * Name of the event.
	 */
	private final String name;

	/**
	 * Source that originated the event.
	 */
	private final String source;
	/**
	 * Index used to sequence different iterations.
	 */
	private final double stepIndex;
	/**
	 * time of the event in seconds after 1900.
	 */
	private final double time;
	/**
	 * @param name
	 *            Source that originated the event.
	 * @param time
	 *            time of the event in seconds after 1900.
	 * @param description
	 *            Optional description of the event.
	 * @param source
	 *            Source that originated the event.
	 * @param stepIndex
	 *            Index used to sequence different iterations.
	 */
	public IterationStep(final String name, final double time,
			final String description, final String source,
			final double stepIndex) {
		this.name = name;
		this.time = time;
		this.description = description;
		this.source = source;
		this.stepIndex = stepIndex;
	}
	@Override
	public final int compareTo(final EventI o) {
		return Double.compare(stepIndex, ((IterationStepI) o).getStepIndex());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj) {
		EqualsWithNulls<String> eq = new EqualsWithNulls<String>();
		if (obj instanceof IterationStep == false) {
			return false;
		}

		IterationStep it = (IterationStep) obj;
		if (name.equals(it.name) == false) {
			return false;
		}
		if (Double.compare(time, it.time) != 0) {
			return false;
		}
		if (Double.compare(stepIndex, it.stepIndex) != 0) {
			return false;
		}
		if (eq.equate(description, it.description) == false) {
			return false;
		}
		return eq.equate(source, it.source);
	}

	@Override
	public final String getDescription() {
		return description;
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final String getSource() {
		return source;
	}

	@Override
	public final double getStepIndex() {
		return stepIndex;
	}

	@Override
	public final double getTime() {
		return time;
	}

	@Override
	public final EventType getType() {
		return EventType.Defined;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return super.hashCode();
	}

}
