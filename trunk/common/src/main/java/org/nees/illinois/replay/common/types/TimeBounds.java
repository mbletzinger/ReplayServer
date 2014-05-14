package org.nees.illinois.replay.common.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper for start and stop boundary values such as time, events, and row
 * numbers.
 * @author Michael Bletzinger
 */
public class TimeBounds implements TimeBoundsI {
	/**
	 * start time.
	 */
	private final double start;
	/**
	 * stop time.
	 */
	private final double stop;
	/**
	 * Name of starting event.
	 */
	private final String startName;
	/**
	 * Name of the stopping event.
	 */
	private final String stopName;

	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory.getLogger(TimeBounds.class);

	/**
	 * @param start
	 *            start time.
	 * @param stop
	 *            stop time.
	 */
	public TimeBounds(final double start,final double stop) {
		this.start = start;
		this.stop = stop;
		this.startName = null;
		this.stopName = null;
	}

	/**
	 * @param start
	 *            start time.
	 * @param stop
	 *            stop time.
	 * @param startName
	 *            Name of starting event.
	 * @param stopName
	 *            Name of the stopping event.
	 */
	public TimeBounds(final double start,final double stop,final String startName,final String stopName) {
		this.start = start;
		this.stop = stop;
		this.startName = startName;
		this.stopName = stopName;
	}

	/**
	 * @param startName
	 *            Name of starting event.
	 * @param stopName
	 *            Name of the stopping event.
	 */
	public TimeBounds(final String startName,final String stopName) {
		if (startName == null) {
			log.error("Start event name cannot be null");
		}
		if (stopName == null) {
			log.error("Stop event name cannot be null");
		}
		this.startName = startName;
		this.stopName = stopName;
		this.start = Double.NaN;
		this.stop = Double.NaN;
	}

	/*
	 * (non-Javadoc)
	 * @see org.nees.illinois.replay.common.types.TimeBoundsI#getStart()
	 */
	@Override
	public final double getStart() {
		return start;
	}

	/*
	 * (non-Javadoc)
	 * @see org.nees.illinois.replay.common.types.TimeBoundsI#getStartName()
	 */
	@Override
	public final String getStartName() {
		return startName;
	}

	/*
	 * (non-Javadoc)
	 * @see org.nees.illinois.replay.common.types.TimeBoundsI#getStop()
	 */
	@Override
	public final double getStop() {
		return stop;
	}

	/*
	 * (non-Javadoc)
	 * @see org.nees.illinois.replay.common.types.TimeBoundsI#getStopName()
	 */
	@Override
	public final String getStopName() {
		return stopName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		String result = "TimeBounds [";
		result += (Double.isNaN(start) ? "" : "start=" + start + ", ");
		result += (Double.isNaN(stop) ? "" : "stop=" + stop + ", ");
		result += (startName == null ? "" : "startName=" + startName + ", ");
		result += (stopName == null ? "" : "stopName=" + stopName);
		result += "]";
		return result;
	}
}
