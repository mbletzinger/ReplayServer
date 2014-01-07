package org.nees.illinois.replay.test.utils.data;

import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.events.Event;
import org.nees.illinois.replay.events.EventI;
import org.nees.illinois.replay.events.EventList;
import org.nees.illinois.replay.events.EventListI;
import org.nees.illinois.replay.events.EventType;
import org.nees.illinois.replay.events.StepNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class which generates different types of event lists.
 * @author Michael Bletzinger
 */
public class EventsGenerator {
	/**
	 * Current event number.
	 */
	private final int eventNumber = 0;
	/**
	 * Current step number generated.
	 */
	private StepNumber currentStep;
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory.getLogger(EventsGenerator.class);
	/**
	 * Type of event. Currently only step numbers are supported.
	 */
	private final EventType type;
	/**
	 * Initial step numbers.
	 */
	private final int[] initialStep = { 1, 4, 3 };

	/**
	 * @param type
	 *            Type of event. Currently only step numbers are supported.
	 */
	public EventsGenerator(final EventType type) {
		this.type = type;
	}

	/**
	 * Create an event. Only {@link StepNumber} for now.
	 * @param timestamp
	 *            Time of the event.
	 * @param source
	 *            Source that recorded the event.
	 * @return The event.
	 */
	private EventI create(final double timestamp, final String source) {
		EventI result = null;
		switch (type) {
		case StepNumber:
			result = createStep(timestamp, source);
			return result;
		case Event:
			result = createEvent(timestamp, source);
			return result;
		default:
			log.error("Generation of " + type + " not implemented yet");
		}
		return result;
	}

	/**
	 * Create a test event.
	 * @param timestamp
	 *            Time of the event.
	 * @param source
	 *            Source that recorded the event.
	 * @return a new event.
	 */
	private EventI createEvent(final double timestamp, final String source) {
		return new Event("Event Name " + eventNumber, timestamp,
				"This an event that happened " + 1, source);
	}

	/**
	 * Create a step number.
	 * @param timestamp
	 *            Time of the event.
	 * @param source
	 *            Source that recorded the event.
	 * @return The step number..
	 */
	private StepNumber createStep(final double timestamp, final String source) {
		if (currentStep == null) {
			currentStep = new StepNumber(initialStep[0], initialStep[1],
					initialStep[2], timestamp, source);
			return currentStep;
		}
		int step = currentStep.getStep() + 1;
		final int maxSubstep = 20;
		int subStep = (currentStep.getSubstep() + 1) % maxSubstep;
		final int maxCorrectionStep = 140;
		final int correctionStepInterval = 10;
		int correction = (currentStep.getCorrectionStep() + correctionStepInterval)
				% maxCorrectionStep;
		StepNumber result = new StepNumber(step, subStep, correction,
				timestamp, source);
		currentStep = result;
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
	 * @return The event list.
	 */
	public final EventListI generate(final DoubleMatrixI dm, final int d,
			final String source) {
		int count = 0;
		EventListI result = new EventList();
		for (List<Double> row : dm.toList()) {
			count++;
			if (count % d != 0) {
				continue;
			}
			EventI e = create(row.get(0), source);
			result.addEvent(e);
		}
		return result;
	}
}
