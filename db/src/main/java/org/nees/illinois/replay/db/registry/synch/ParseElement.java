package org.nees.illinois.replay.db.registry.synch;
/**
 * Container to parse datatypes.
 * @author Michael Bletzinger
 *
 * @param <T>
 * Datatype of item to be converted from a string.
 */
public abstract class ParseElement<T> {
/**
 * Converts the string to datatype T.
 *@param str
 *Input string.
 *@return
 *Datatype.
 */
	public abstract T parse(String str);
}
