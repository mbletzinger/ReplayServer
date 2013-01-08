package org.nees.illinois.replay.restlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.StepNumber;
import org.nees.illinois.replay.data.TableType;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttributeExtraction {
	public enum RequiredAttrType {
		Experiment, Table, Rate, Start, Stop, Query, Double, StepNumber
	};

	final Map<RequiredAttrType, Object> attrs = new HashMap<RequiredAttrType, Object>();
	final ConcurrentMap<String, Object> reqAttrs;
	private final Logger log = LoggerFactory
			.getLogger(AttributeExtraction.class);

	public AttributeExtraction(ConcurrentMap<String, Object> reqAttrs) {
		super();
		this.reqAttrs = reqAttrs;
	}

	public void extract(List<RequiredAttrType> required) {
		boolean isStepNumber = required.contains(RequiredAttrType.StepNumber);
		for (RequiredAttrType aType : required) {
			if (aType.equals(RequiredAttrType.Experiment)) {
				String val = extractString("experiment");
				if (val == null) {
					throw new ResourceException(
							Status.CLIENT_ERROR_BAD_REQUEST,
							"Need an experiment name");
				}
				attrs.put(RequiredAttrType.Experiment, val);
				continue;
			}
			if (aType.equals(RequiredAttrType.Table)) {
				TableType val = extractTable();
				if (val == null) {
					throw new ResourceException(
							Status.CLIENT_ERROR_BAD_REQUEST,
							"Need an table name");
				}
				attrs.put(RequiredAttrType.Table, val);
				continue;
			}
			if (aType.equals(RequiredAttrType.Rate)) {
				RateType val = extractRate();
				if (val == null) {
					throw new ResourceException(
							Status.CLIENT_ERROR_BAD_REQUEST, "Need a rate");
				}
				attrs.put(RequiredAttrType.Rate, val);
				continue;
			}
			if (aType.equals(RequiredAttrType.Query)) {
				String val = extractString("query");
				if (val == null) {
					throw new ResourceException(
							Status.CLIENT_ERROR_BAD_REQUEST,
							"Need an query name");
				}
				attrs.put(RequiredAttrType.Query, val);
				continue;
			}
			if (aType.equals(RequiredAttrType.Start)) {
				Object val = null;
				if (isStepNumber) {
					val = extractStepNumber("start");
				} else {
					val = extractDouble("start");
				}
				if (val == null && ! isStepNumber) { // Starting step number is optional
					throw new ResourceException(
							Status.CLIENT_ERROR_BAD_REQUEST,
							"Need a start for the query");
				}
				attrs.put(RequiredAttrType.Start, val);
				continue;
			}
			if (aType.equals(RequiredAttrType.Stop)) {
				Object val = null;
				if (isStepNumber) {
					val = extractStepNumber("stop");
				} else {
					val = extractDouble("stop");
				}
				if (val != null) { //Stop attribute is optional
					attrs.put(RequiredAttrType.Stop, val);
				}
				continue;
			}
		}
	}

	private TableType extractTable() {
		String str = (String) reqAttrs.get("table");
		if (str == null) {
			return null;
		}
		if (str.equals("")) {
			return null;
		}
		TableType tbl;
		try {
			tbl = TableType.valueOf(str);
		} catch (IllegalArgumentException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "\""
					+ str + " \" is not a TableType");
		}
		return tbl;
	}

	private RateType extractRate() {
		String str = (String) reqAttrs.get("rate");
		if (str == null) {
			return null;
		}
		if (str.equals("")) {
			return null;
		}
		RateType rate;
		try {
			rate = RateType.valueOf(str);
		} catch (IllegalArgumentException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "\""
					+ str + " \" is not a RateType");
		}
		return rate;
	}

	private String extractString(String label) {
		String str = (String) reqAttrs.get(label);
		return str;
	}

	/**
	 * @return the attrs
	 */
	public Map<RequiredAttrType, Object> getAttrs() {
		return attrs;
	}

	private StepNumber extractStepNumber(String label) {
		String str = (String) reqAttrs.get(label);
		if (str == null) {
			return null;
		}
		if (str.equals("")) {
			return null;
		}
		StepNumber result = null;
		try {
			result = new StepNumber(str);
		} catch (Exception e) {
			log.error("Step \"" + str + "\" could not be parsed");
			return null;
		}
		return result;
	}

	private Double extractDouble(String label) {
		String str = (String) reqAttrs.get(label);
		if (str == null) {
			return null;
		}
		if (str.equals("")) {
			return null;
		}
		Double result = null;
		try {
			result = new Double(str);
		} catch (Exception e) {
			log.error("Double \"" + str + "\" could not be parsed");
			return null;
		}
		return result;
	}

}
