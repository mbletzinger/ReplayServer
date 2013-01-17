package org.nees.illinois.replay.test.server.utils;

import java.util.ArrayList;
import java.util.List;

public enum InjectedErrorsType {
	BadExperiment, 
	BadQuery, 
	BadRate, 
	BadStart, 
	BadStop, 
	BadTable, 
	ErrExperiment, 
	NoExperiment, 
	None, 
	NoQuery, 
	NoRate,
	NoStart,
	NoStop,
	NoTable;
	
	private List<Integer> badCode = new ArrayList<Integer>();
	{
		badCode.add(400);
		badCode.add(400);
		badCode.add(400);
		badCode.add(400);
		badCode.add(400);
		badCode.add(400);
		badCode.add(500);
		badCode.add(404);
		badCode.add(200);
		badCode.add(404);
		badCode.add(404);
		badCode.add(404);
		badCode.add(404);
		badCode.add(404);		
	}
	public int ExpectedCode() {
		return badCode.get(ordinal());
	}
}
