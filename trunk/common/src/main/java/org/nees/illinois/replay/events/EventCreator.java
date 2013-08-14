package org.nees.illinois.replay.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create an event object based on the type.
 * @author Michael Bletzinger
 */
public class EventCreator {
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory.getLogger(EventCreator.class);

	/**
	 * Create an event based on the event type.
	 * @param typeString
	 *            Type of event.
	 * @param time
	 *            Time of event.
	 * @param name
	 *            Name of the event.
	 * @param description
	 *            Description of the event.
	 * @param source
	 *            Source where the event came from.
	 * @param stepIndex
	 *            Step index of the event. Use null if not available.
	 * @return the event object.
	 */
	public final EventI createEvent(final String typeString, final double time,
			final String name, final String description, final String source,
			final Double stepIndex) {
		EventType type = null;
		try {
			type = EventType.valueOf(typeString);
		} catch (IllegalArgumentException e) {
			log.error(typeString + " is not a valid event type for " + name);
			return null;
		}
		EventI result = null;
		switch (type) {
		case Defined:
		case Iteration:
			result = new Event(name, time, source, description, stepIndex);
			break;
		case StepNumber:
			StepNumber stepN = new StepNumber(name, time, source);
			if (stepIndex != null) {
				stepN.setIndex(stepIndex);
			}
			result = stepN;
			break;
		default:
			log.error(type + " in " + name + " is not an event type");
		}
		return result;
	}

}
