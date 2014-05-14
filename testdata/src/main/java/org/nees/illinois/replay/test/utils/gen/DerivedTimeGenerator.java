package org.nees.illinois.replay.test.utils.gen;

import org.nees.illinois.replay.test.utils.types.MatrixMixType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that creates a second time generator which is related to another
 * generator by a mix type.
 * @author Michael Bletzinger
 */
public class DerivedTimeGenerator {
	/**
	 * Interval used in the time sequence derivation. Can represent the end of
	 * the first or second time sequences or the gap in the first sequence where
	 * the second occurs.
	 */
	private final double interval;
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory
			.getLogger(DerivedTimeGenerator.class);
	/**
	 * Relationship between the two time sequences.
	 */
	private final MatrixMixType mix;

	/**
	 * @param mix
	 *            Relationship between the two time sequences.
	 * @param interval
	 *            Interval used in the time sequence derivation. Can represent
	 *            the end of the first or second time sequences or the gap in
	 *            the first sequence where the second occurs.
	 */
	public DerivedTimeGenerator(final MatrixMixType mix,
			final double interval) {
		this.mix = mix;
		this.interval = interval;
	}

	/**
	 * Generates a derived time generator. Note that for the matrix mix type
	 * AddMiddle, the function will add gap parameters to the original
	 * generator.
	 * @param origTimeGen
	 *            Original time generator.
	 * @return the derived generator.
	 */
	public final TimeGenerator derive(final TimeGenerator origTimeGen) {
		switch (mix) {
		case AddAfter:
			double start = origTimeGen.getStartTime() + interval;
			double timeMulti = origTimeGen.getTimeMultiplier();
			return new TimeGenerator(timeMulti, start);
		case AddInterleaved:
			timeMulti = origTimeGen.getTimeMultiplier();
			start = origTimeGen.getStartTime() + timeMulti / 2.0;
			return new TimeGenerator(timeMulti, start);
		case AddBefore:
			start = origTimeGen.getStartTime() - interval;
			timeMulti = origTimeGen.getTimeMultiplier();
			return new TimeGenerator(timeMulti, start);
		case AddMerged:
			return new TimeGenerator(origTimeGen);
		case AddMiddle:
			double gapStart = interval / 2;
			origTimeGen.setGapSize(interval);
			origTimeGen.setGapStartTime(gapStart);
			start = origTimeGen.getStartTime() + gapStart;
			timeMulti = origTimeGen.getTimeMultiplier();
			return new TimeGenerator(timeMulti, start);
		default:
			log.error(mix + " is not a recognized matrix mix type");
			return null;
		}
	}

	/**
	 * @return the interval
	 */
	public final double getInterval() {
		return interval;
	}

	/**
	 * @return the mix
	 */
	public final MatrixMixType getMix() {
		return mix;
	}
}
