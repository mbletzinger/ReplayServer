package org.nees.illinois.replay.test.utils.gen;

import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.events.Event;
import org.nees.illinois.replay.events.EventI;
import org.nees.illinois.replay.events.EventList;
import org.nees.illinois.replay.events.EventListI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class which generates different types of event lists.
 * @author Michael Bletzinger
 */
public class EventsGenerator {
	/**
	 * Current step number generated.
	 */
	private final int[] currentStep = { 1, 4, 3 };
	/**
	 * Current event number.
	 */
	private int eventNumber = 0;
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory.getLogger(EventsGenerator.class);

	/**
	 * Create a test event.
	 * @param timestamp
	 *            Time of the event.
	 * @param source
	 *            Source that recorded the event.
	 * @return a new event.
	 */
	private EventI createEvent(final double timestamp, final String source) {
		EventI result = new Event("Event Name " + eventNumber, timestamp,
				"An event that happened " + eventNumber, source);
		eventNumber++;
		return result;
	}

	/**
	 * Create a step number event.
	 * @param timestamp
	 *            Time of the event.
	 * @param source
	 *            Source that recorded the event.
	 * @return The event
	 */
	private EventI createStep(final double timestamp, final String source) {
		currentStep[0]++;
		final int maxSubstep = 20;
		currentStep[1] = (currentStep[1] + 1) % maxSubstep;
		final int maxCorrectionStep = 140;
		final int correctionStepInterval = 10;
		currentStep[2] = (currentStep[2] + correctionStepInterval)
				% maxCorrectionStep;
		String step = currentStep[0] + "_" + currentStep[1] + "_"
				+ currentStep[0];
		EventI result = new Event(step, timestamp, " Step " + step, source);
		eventNumber++;
		return result;
	}

	/**
	 * Create an event list using the times in the double matrix.
	 * @param dm
	 *            The continuous data used for timestamps.
	 * @param d
	 *            Number of continuous records to skip.
	 * @param source
	 *            The source that recorded these events
	 * @param newStepEvent
	 *            True if the event name should contain MUST-SIM step numbers.
	 * @return The event list.
	 */
	public final EventListI generate(final DoubleMatrixI dm, final int d,
			final String source, final boolean newStepEvent) {
		int count = 0;
		EventListI result = new EventList();
		for (List<Double> row : dm.toList()) {
			count++;
			if (count % d != 0) {
				continue;
			}
			EventI e = null;
			if (newStepEvent) {
				e = createStep(row.get(0), source);
			} else {
				e = createEvent(row.get(0), source);
			}
			result.addEvent(e);
		}
		log.debug("Created list " + result);
		return result;
	}
}
