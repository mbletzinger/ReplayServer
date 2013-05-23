package org.nees.illinois.replay.conversions;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to parse and combine string lists. A list is combined into a
 * string and parsed from a string using a comma "," delimiter.
 * @author Michael Bletzinger
 */
public final class Str2CL {

	/**
	 * Prevent the construction of this class.
	 */
	private Str2CL() {
	}

	/**
	 * Splits a string into separate tokens.
	 * @param str
	 *            String to be parsed.
	 * @return String array.
	 */
	public static List<String> str2cl(final String str) {
		List<String> list = new ArrayList<String>();
		String[] chnls = str.toString().split(",");
		for (String c : chnls) {
			list.add(c);
		}
		return list;
	}

	/**
	 * Combines a string list.
	 * @param list
	 *            The list to be combined.
	 * @return The resulting string.
	 */
	public static String cl2str(final List<String> list) {
		String str = "";
		boolean first = true;
		for (String s : list) {
			str += (first ? "" : ",") + s;
			first = false;
		}
		return str;
	}

}
