package org.nees.illinois.replay.common.types;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper for start and stop boundary values such as time, events, and row
 * numbers.
 * @author Michael Bletzinger
 */
public class TimeBounds implements TimeBoundsI {
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory.getLogger(TimeBounds.class);
	/**
	 * list of event names referencing discrete times.
	 */
	private final List<String> nameList = new ArrayList<String>();
	/**
	 * start time.
	 */
	private final double start;
	/**
	 * Name of starting event.
	 */
	private final String startName;

	/**
	 * stop time.
	 */
	private final double stop;

	/**
	 * Name of the stopping event.
	 */
	private final String stopName;
	/**
	 * How the time frame is defined.
	 */
	private final TimeBoundsType type;

	/**
	 * @param start
	 *            start time.
	 * @param stop
	 *            stop time.
	 */
	public TimeBounds(final double start,final double stop) {
		this.type = TimeBoundsType.StartStopTime;
		this.start = start;
		this.stop = stop;
		this.startName = null;
		this.stopName = null;
	}

	/**
	 * @param start
	 *            start time.
	 * @param startName
	 *            Name of starting event.
	 * @param stop
	 *            stop time.
	 * @param stopName
	 *            Name of the stopping event.
	 */
	public TimeBounds(final double start,final String startName,final double stop,final String stopName) {
		if (startName == null) {
			log.error("Start event name cannot be null");
		}
		if (stopName == null) {
			log.error("Stop event name cannot be null");
		}
		this.type = TimeBoundsType.StartStopEvent;
		this.start = start;
		this.startName = startName;
		this.stop = stop;
		this.stopName = stopName;
	}

	/**
	 * @param names
	 *            list of event names referencing discrete times.
	 */
	public TimeBounds(final List<String> names) {
		this.nameList.addAll(names);
		this.type = TimeBoundsType.EventList;
		this.start = Double.NaN;
		this.stop = Double.NaN;
		this.startName = null;
		this.stopName = null;
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
		this.type = TimeBoundsType.StartStopEvent;
		this.startName = startName;
		this.stopName = stopName;
		this.start = Double.NaN;
		this.stop = Double.NaN;
	}

	@Override
	public final List<String> getNames() {
		return nameList;
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

	@Override
	public final TimeBoundsType getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		String result = "TimeBounds [";
		result += (Double.isNaN(start) ? "" : "start=" + start + ", ");
		result += (Double.isNaN(stop) ? "" : "stop=" + stop + ", ");
		result += (startName == null ? "" : "startName=" + startName + ", ");
		result += (stopName == null ? "" : "stopName=" + stopName);
		if(nameList.isEmpty() == false) {
			result += "list={";
			boolean first = true;
			for(String n : nameList) {
				result += (first ? "" : ",") + n;
				first = false;
			}
			result += "}";
		}
		result += "]";
		return result;
	}
}
