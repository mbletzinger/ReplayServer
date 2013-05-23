package org.nees.illinois.replay.conversions;

import java.util.List;

import org.restlet.representation.ByteArrayRepresentation;
import org.restlet.representation.Representation;

/**
 * Converts a channel list into an output stream and wraps inside a restlet
 * {@link Representation representation}.
 * @author Michael Bletzinger
 */
public class ChannelList2Representation {
	/**
	 * channel list converter.
	 */
	private final ChannelList2OutputStream cl2os;

	// private final Logger log = LoggerFactory
	// .getLogger(ChannelList2Representation.class);
	/**
	 * Resulting representation.
	 */
	private final Representation rep;

	/**
	 * Constructor.
	 * @param channels
	 *            List of channel names to convert.
	 */
	public ChannelList2Representation(final List<String> channels) {
		super();
		cl2os = new ChannelList2OutputStream(channels);
		this.rep = new ByteArrayRepresentation(cl2os.getBuffer());
	}

	/**
	 * @return the converter. Used only for unit testing.
	 */
	public final ChannelList2OutputStream getCl2os() {
		return cl2os;
	}

	/**
	 * @return the resulting representation
	 */
	public final Representation getRep() {
		return rep;
	}
}
