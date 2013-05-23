package org.nees.illinois.replay.restlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.common.registries.TableType;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.StepNumber;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used to parse the attributes from incoming "cool URI's". The attributes
 * are of the form "/name/value" in the URI. The Restlet code scans the
 * attribute tokens in the URI and delivers these as a map of strings and objects. This
 * class checks the presence of required attributes and reports any problems
 * with their values.
 * @author Michael Bletzinger
 */
public class AttributeExtraction {
	/**
	 * List of possible attributes.
	 * @author Michael Bletzinger
	 */
	public enum RequiredAttrType {
		/**
		 * Experiment name.
		 */
		Experiment,
		/**
		 * Query name.
		 */
		Query,
		/**
		 * Either CONT or STEP.
		 */
		Rate,
		/**
		 * Start time in seconds or step number.
		 */
		Start,
		/**
		 * Stop time in seconds or step number.
		 */
		Stop,
		/**
		 * Table name.
		 */
		Table
	};

	/**
	 * Map of parsed attribute values.
	 */
	private final Map<RequiredAttrType, Object> attrs = new HashMap<RequiredAttrType, Object>();
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory
			.getLogger(AttributeExtraction.class);
	/**
	 * Map of attributes from the URI.
	 */
	private final Map<String, Object> uriAttrs;

	/**
	 * Constructor.
	 * @param uriAttrs
	 *            Map of attributes from the URI.
	 */
	public AttributeExtraction(final Map<String, Object> uriAttrs) {
		super();
		this.uriAttrs = uriAttrs;
		log.debug("Req attributes are " + uriAttrs);
	}

	/**
	 * Main function for parsing URIs. The function will throw a restlet
	 * {@link ResourceException ResourceException} if any attributes are missing
	 * or malformed.
	 * @param required
	 *            List of required attributes for the URI.
	 */
	public final void extract(final List<RequiredAttrType> required) {
		boolean isStepNumber = false;
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
				isStepNumber = val.equals(RateType.STEP);
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
				if (val == null && !isStepNumber) { // Starting step number is
													// optional
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
				if (val != null) { // Stop attribute is optional
					attrs.put(RequiredAttrType.Stop, val);
				}
				continue;
			}
		}
	}

	/**
	 * Extract a double number.
	 * @param label
	 *            Name of attribute.
	 * @return The double number.
	 */
	private Double extractDouble(final String label) {
		String str = (String) uriAttrs.get(label);
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
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "\""
					+ str + " \" is not a valid time value");
		}
		return result;
	}

	/**
	 * Extract the rate type.
	 * @return The rate type.
	 */
	private RateType extractRate() {
		String str = (String) uriAttrs.get("rate");
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

	/**
	 * Extract a step number.
	 * @param label
	 *            Name of attribute.
	 * @return Step number value.
	 */
	private StepNumber extractStepNumber(final String label) {
		String str = (String) uriAttrs.get(label);
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
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "\""
					+ str + " \" is not a valid step number");
		}
		return result;
	}

	/**
	 * Extract a string value.
	 * @param label
	 *            Name of attribute.
	 * @return value.
	 */
	private String extractString(final String label) {
		String str = (String) uriAttrs.get(label);
		return str;
	}

	/**
	 * Extract the table name.
	 * @return The table name.
	 */
	private TableType extractTable() {
		String str = (String) uriAttrs.get("table");
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

	/**
	 * @return the parsed attributes.
	 */
	public final Map<RequiredAttrType, Object> getAttrs() {
		return attrs;
	}

	/**
	 * Get experiment name.
	 * @return experiment string.
	 */
	public final String getExperiment() {
		return extractString("experiment");
	}

}
