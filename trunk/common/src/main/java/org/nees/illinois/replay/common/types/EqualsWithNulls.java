package org.nees.illinois.replay.common.types;

/**
 * Class which equates objects that could be null.
 * @author Michael Bletzinger
 * @param <Item> datatype of the objects to be equated.
 */
public class EqualsWithNulls<Item> {
	/**
	 * Equate two items.
	 * @param a
	 *            first item.
	 * @param b
	 *            second item.
	 * @return true if the items are equal or both null.
	 */
	public final boolean equate(final Item a, final Item b) {
		if (a == null) {
			if (b != null) {
				return false;
			}
			return true;
		}
		if (b == null) {
			return false;
		}
		return a.equals(b);
	}
}
