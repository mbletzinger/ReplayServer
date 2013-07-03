package org.nees.illinois.replay.events;

import org.nees.illinois.replay.common.types.TableIdentityI;

/**
 * Class identifying the time of an event and the source of its timestamp.
 * @author Michael Bletzinger
 */
public class TimeAndSource {
	/**
	 * Source of the timestamp.
	 */
	private final TableIdentityI source;
	/**
 * Time in seconds of the event.
 */
private final double timeStamp;
	/**
	 *@param source
 * Source of the timestamp.
	 *@param timeStamp
 * Time in seconds of the event.
	 */
	public TimeAndSource(final TableIdentityI source, final double timeStamp) {
		this.source = source;
		this.timeStamp = timeStamp;
	}
		/**
		 * @return the source
		 */
		public final TableIdentityI getSource() {
			return source;
		}

	/**
	 * @return the timeStamp
	 */
	public final double getTimeStamp() {
		return timeStamp;
	}

}
