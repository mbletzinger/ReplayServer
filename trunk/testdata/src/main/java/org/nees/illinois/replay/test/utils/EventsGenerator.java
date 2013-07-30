package org.nees.illinois.replay.test.utils;

import java.util.List;

import org.nees.illinois.replay.common.registries.EventIdGenerator;
import org.nees.illinois.replay.common.types.TableIdentityI;
import org.nees.illinois.replay.data.DoubleMatrix;
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
	 * Current step number generated.
	 */
	private StepNumber currentStep;
	/**
	 * Generator for unique event ids.
	 */
	private final EventIdGenerator generator;
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory.getLogger(EventsGenerator.class);
	/**
	 * Type of event. Currently only step numbers are supported.
	 */
	private final EventType type;

	/**
	 * @param type
	 *            Type of event. Currently only step numbers are supported.
	 * @param generator
	 *            Generator for unique event ids.
	 */
	public EventsGenerator(final EventType type, final EventIdGenerator generator) {
		this.type = type;
		this.generator = generator;
	}

	/**
	 * Create an event. Only {@link StepNumber} for now.
	 * @param timestamp
	 *            Time of the event.
	 * @param source
	 *            Source that recorded the event.
	 * @return The event.
	 */
	private EventI create(final double timestamp, final TableIdentityI source) {
		EventI result = null;
		switch (type) {
		case StepNumber:
			result = createStep(timestamp, source);
			return result;
		default:
			log.error("Generation of " + type + " not implemented yet");
		}
		return result;
	}

	/**
	 * Create a step number.
	 * @param timestamp
	 *            Time of the event.
	 * @param source
	 *            Source that recorded the event.
	 * @return The step number..
	 */
	private StepNumber createStep(final double timestamp,
			final TableIdentityI source) {
		int step = currentStep.getStep() + 1;
		final int maxSubstep = 20;
		int subStep = (currentStep.getSubstep() + 1) % maxSubstep;
		final int maxCorrectionStep = 140;
		final int correctionStepInterval = 10;
		int correction = (currentStep.getCorrectionStep() + correctionStepInterval)
				% maxCorrectionStep;
		String id = generator.create(type);
		StepNumber result = new StepNumber(step, subStep, correction,
				timestamp, id, source);
		currentStep = result;
		return result;
	}

	/**
	 * Create an event list using the times in the double matrix.
	 * @param dm
	 *            The continuous data used for timestamps.
	 * @param interval
	 *            Number of continuous records to skip.
	 * @param source
	 *            The source that recorded these events
	 * @return The event list.
	 */
	public final EventListI generate(final DoubleMatrix dm, final int interval,
			final TableIdentityI source) {
		int count = 0;
		EventListI result = new EventList(source);
		for (List<Double> row : dm.toList()) {
			count++;
			if (count % interval != 0) {
				continue;
			}
			EventI e = create(row.get(0), source);
			result.addEvent(e);
		}
		return result;
	}
}
