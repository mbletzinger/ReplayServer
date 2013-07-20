package org.nees.illinois.replay.db.registry.synch;

/**
 * Class to handle String elements. No parsing needed obviously.
 * @author Michael Bletzinger
 */
public class StringDecoder extends ParseElement<String> {

	@Override
	public final String parse(final String str) {
		return str;
	}
}
