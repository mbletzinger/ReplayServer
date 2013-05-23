package org.nees.illinois.replay.conversions;

import java.io.IOException;

import org.restlet.representation.Representation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts {@link Representation representation} to a String list of channel
 * names.
 * @author Michael Bletzinger
 */
public class Representation2ChannelList {
	/**
	 * Converter.
	 */
	private final InputStream2ChannelList il2cl;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory
			.getLogger(Representation2ChannelList.class);

	/**
	 * Constructor which also does the conversion.
	 * @param rep
	 *            Representation to be converted.
	 */
	public Representation2ChannelList(final Representation rep) {
		super();
		InputStream2ChannelList i2c = null;
		try {
			i2c = new InputStream2ChannelList(rep.getStream());
		} catch (IOException e1) {
			try {
				log.error("Could not read representation \"" + rep.getText()
						+ "\"");
			} catch (IOException e) {
				log.error("Could not read representation \"" + rep + "\"");
			}
			il2cl = null;
			return;
		}
		il2cl = i2c;
	}

	/**
	 * @return the stream converter. You can get the channel list from this.
	 */
	public final InputStream2ChannelList getIl2cl() {
		return il2cl;
	}
}
