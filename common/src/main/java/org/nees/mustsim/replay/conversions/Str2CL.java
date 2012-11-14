package org.nees.mustsim.replay.conversions;

import java.util.ArrayList;
import java.util.List;

public class Str2CL {
	
	public static List<String> str2cl(String str) {
		List<String> list = new ArrayList<String>();
		String[] chnls = str.toString().split(",");
		for (String c : chnls) {
			list.add(c);
		}
		return list;
	}
	public static String cl2str(List<String> list) {
		String str = "";
		boolean first = true;
		for (String s : list) {
			str += (first ? "" : ",") + s;
			first = false;
		}
		return str;
	}

}
