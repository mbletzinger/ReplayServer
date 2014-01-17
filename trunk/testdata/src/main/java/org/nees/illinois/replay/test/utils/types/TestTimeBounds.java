package org.nees.illinois.replay.test.utils.types;

import java.util.List;

import org.nees.illinois.replay.common.types.TimeBounds;
import org.nees.illinois.replay.common.types.TimeBoundsI;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.events.EventI;
import org.nees.illinois.replay.events.EventListI;
/**
 * Test version of TimeBoundsI that includes indexes to the expected data set.
 * @author Michael Bletzinger
 *
 */
public class TestTimeBounds implements TimeBoundsI {
	/**
	 * Index of the starting row or event.
	 */
	private final int startIdx;
	/**
	 * Index of the last row or event.
	 */
	private final int stopIdx;
	/**
	 * Reference version of TimeBounds.
	 */
	private final TimeBounds tb;

	/**
	 *@param startIdx
	 * Index of the starting row or event.
	 *@param stopIdx
	 * Index of the last row or event.
	 *@param tset
	 * Data where the boundaries are extracted.
	 */
	public TestTimeBounds(final int startIdx,final int stopIdx,final DoubleMatrixI tset) {
		this.startIdx = startIdx;
		this.stopIdx = stopIdx;
		tb = new TimeBounds(tset.value(startIdx, 0), tset.value(stopIdx, 0));
	}

	/**
	 *@param startIdx
	 * Index of the starting row or event.
	 *@param stopIdx
	 * Index of the last row or event.
	 *@param tset
	 * Data where the boundaries are extracted.
	 */
	public TestTimeBounds(final int startIdx,final int stopIdx,final EventListI tset) {
		this.startIdx = startIdx;
		this.stopIdx = stopIdx;
		List<EventI> elist = tset.getEvents();
		tb = new TimeBounds(elist.get(startIdx).getName(), elist.get(stopIdx).getName());
	}

	@Override
	public final double getStart() {
		return tb.getStart();
	}

	/**
	 * @return the startIdx
	 */
	public final int getStartIdx() {
		return startIdx;
	}

	@Override
	public final String getStartName() {
		return tb.getStartName();
	}

	@Override
	public final double getStop() {
		return tb.getStop();
	}

	/**
	 * @return the stopIdx
	 */
	public final int getStopIdx() {
		return stopIdx;
	}

	@Override
	public final String getStopName() {
		return tb.getStopName();
	}

}
