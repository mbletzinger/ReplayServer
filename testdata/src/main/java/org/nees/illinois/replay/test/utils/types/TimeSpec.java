package org.nees.illinois.replay.test.utils.types;

/**
 * Wrapper for start and stop boundary values such as time, events, and row
 * numbers.
 * @param <T>
 *            datatype for the boundary values.
 * @author Michael Bletzinger
 */
public class TimeSpec<T> {
	/**
	 * Generic start time or event.
	 */
	private final T start;
	/**
	 * Generic stop time or event.
	 */
	private final T stop;

	/**
	 * @param start
	 *            Generic start time or event.
	 * @param stop
	 *            Generic stop time or event.
	 */
	public TimeSpec(final T start, final T stop) {
		super();
		this.start = start;
		this.stop = stop;
	}

	/**
	 * @return the start
	 */
	public final T getStart() {
		return start;
	}

	/**
	 * @return the stop
	 */
	public final T getStop() {
		return stop;
	}

}
