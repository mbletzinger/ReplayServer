package org.nees.illinois.replay.restlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.common.registries.TableType;
import org.nees.illinois.replay.data.RateType;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used to parse the attributes from incoming "cool URI's". The attributes
 * are of the form "/name/value" in the URI. The Restlet code scans the
 * attribute tokens in the URI and delivers these as a map of strings and
 * objects. This class checks the presence of required attributes and reports
 * any problems with their values.
 * @author Michael Bletzinger
 */
public class AttributeExtraction {
	/**
	 * Enumerates all of the attribute rules that can be checked.
	 * @author Michael Bletzinger
	 */
	public enum AttributeRules {
		/**
		 * Request needs the name of the experiment.
		 */
		ExperimentNameRequired,
		/**
		 * Request needs the name of the query.
		 */
		QueryNameRequired,
		/**
		 * Request needs time boundaries.
		 */
		TimeBoundsRequired,
		/**
		 * Request needs the name of the source.
		 */
		SourceRequired,
		/**
		 * Request needs a rate for the data.
		 */
		RateRequired,
	};

	/**
	 * List of possible attributes.
	 * @author Michael Bletzinger
	 */
	public enum AttributeTypes {
		/**
		 * Experiment name.
		 */
		Experiment,
		/**
		 * Query name.
		 */
		Query,
		/**
		 * Either CONTINUOUS or DISCRETE.
		 */
		Rate,
		/**
		 * Start event name.
		 */
		Start,
		/**
		 * Stop event name.
		 */
		Stop,
		/**
		 * Start of the query in seconds.
		 */
		StartTime,
		/**
		 * End of the query in seconds.
		 */
		StopTime,
		/**
		 * Source name.
		 */
		Source
	}

	/**
	 * Map of parsed attribute values.
	 */
	private final Map<AttributeTypes, Object> attrs = new HashMap<AttributeTypes, Object>();
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
	 * Throw exceptions if attribute rules fail.
	 * @param rules
	 *            to check.
	 */
	private void checkRules(final List<AttributeRules> rules) {
		for (AttributeRules r : rules) {
			switch (r) {
			case ExperimentNameRequired:
				if (attrs.get(AttributeTypes.Experiment) == null) {
					throw new ResourceException(
							Status.CLIENT_ERROR_BAD_REQUEST,
							"Experiment name is required");
				}
				continue;
			case QueryNameRequired:
				if (attrs.get(AttributeTypes.Query) == null) {
					throw new ResourceException(
							Status.CLIENT_ERROR_BAD_REQUEST,
							"Query name is required");
				}
				continue;
			case SourceRequired:
				if (attrs.get(AttributeTypes.Source) == null) {
					throw new ResourceException(
							Status.CLIENT_ERROR_BAD_REQUEST,
							"Data source name is required");
				}
				continue;
			case RateRequired:
				if (attrs.get(AttributeTypes.Rate) == null) {
					throw new ResourceException(
							Status.CLIENT_ERROR_BAD_REQUEST,
							"Data rate is required.  Rate needs to be either CONTINUOUS or DISCRETE");
				}
				continue;
			case TimeBoundsRequired:
				boolean oneStart = (attrs.get(AttributeTypes.Start) != null)
				|| (attrs.get(AttributeTypes.StartTime) != null);
				boolean doubleStart = (attrs.get(AttributeTypes.Start) != null)
						&& (attrs.get(AttributeTypes.StartTime) != null);
				boolean doubleStop = (attrs.get(AttributeTypes.Stop) != null)
						&& (attrs.get(AttributeTypes.StopTime) != null);
				boolean boundsMismatch = (attrs.get(AttributeTypes.Stop) != null)
						&& (attrs.get(AttributeTypes.StartTime) != null);
				boundsMismatch = boundsMismatch
						|| ((attrs.get(AttributeTypes.StopTime) != null) && (attrs
								.get(AttributeTypes.Start) != null));

				if (oneStart) {
					throw new ResourceException(
							Status.CLIENT_ERROR_BAD_REQUEST,
							"Start name or event is required");
				}
				if (doubleStart || doubleStop) {
					throw new ResourceException(
							Status.CLIENT_ERROR_BAD_REQUEST,
							"Use only one start and one stop parameter");
				}
				if (boundsMismatch) {
					throw new ResourceException(
							Status.CLIENT_ERROR_BAD_REQUEST,
							"Time boundaries need to both be either events or times");
				}
			default:
				throw new ResourceException(
						Status.SERVER_ERROR_NOT_IMPLEMENTED,
						"Attribute rule not recognized.");
			}
		}
	}

	/**
	 * Main function for parsing URIs. The function will throw a restlet
	 * {@link ResourceException ResourceException} if any attributes are missing
	 * or malformed.
	 * @param rules
	 *            List of attributes rules for the URI.
	 */
	public final void extract(final List<AttributeRules> rules) {
		for (AttributeTypes aType : AttributeTypes.values()) {
			switch (aType) {

			case Experiment: {
				String val = extractString("experiment");
				if (val == null) {
					throw new ResourceException(
							Status.CLIENT_ERROR_BAD_REQUEST,
							"Need an experiment name");
				}
				attrs.put(AttributeTypes.Experiment, val);
			}
			continue;

			case Source: {
				TableType val = extractTable();
				if (val != null) {
					attrs.put(AttributeTypes.Source, val);
				}
			}
			continue;
			case Rate: {
				RateType val = extractRate();
				if (val != null) {
					attrs.put(AttributeTypes.Rate, val);
				}
			}
			continue;

			case Query: {
				String val = extractString("query");
				if (val != null) {
					attrs.put(AttributeTypes.Query, val);
				}
			}
			continue;

			case Start: {
				String val = null;
				val = extractEvent("start");
				if (val != null) {
					attrs.put(AttributeTypes.Start, val);
				}
			}
			continue;
			case Stop: {
				String val = null;
				val = extractEvent("stop");
				if (val != null) {
					attrs.put(AttributeTypes.Stop, val);
				}
			}
			case StartTime: {
				Double val = null;
				val = extractDouble("starttime");
				if (val != null) {
					attrs.put(AttributeTypes.StartTime, val);
				}
			}
			continue;
			case StopTime: {
				Double val = null;
				val = extractDouble("stoptime");
				if (val != null) {
					attrs.put(AttributeTypes.StopTime, val);
				}
			}
			default:
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
						"Attribute " + aType + " not recognized");
			}
		}
		checkRules(rules);
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
	 * Extract an event.
	 * @param label
	 *            Name of attribute.
	 * @return Event name.
	 */
	private String extractEvent(final String label) {
		String str = (String) uriAttrs.get(label);
		if (str == null) {
			return null;
		}
		if (str.equals("")) {
			return null;
		}
		return str;
	}

	/**
	 * Extract the rate type.
	 * @return The rate type.
	 */
	private RateType extractRate() {
		RateType rate = RateType.DISCRETE;
		String str = (String) uriAttrs.get("rate");
		if (str == null) {
			return rate;
		}
		if (str.equals("")) {
			return rate;
		}
		try {
			rate = RateType.valueOf(str);
		} catch (IllegalArgumentException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "\""
					+ str + " \" is not a RateType");
		}
		return rate;
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
	public final Map<AttributeTypes, Object> getAttrs() {
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
