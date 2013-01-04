package org.nees.illinois.replay.restlet;

import org.nees.illinois.replay.providers.RestletResponseProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class ReplayLoggerFilter implements Logger {
	private final Logger clientLogger;
	private final Logger log = LoggerFactory
			.getLogger(ReplayLoggerFilter.class);
	private final RestletResponseProvider respWrap;

	@Override
	public void debug(String arg0) {
		clientLogger.debug(arg0);
	}

	@Override
	public void debug(String arg0, Object arg1) {
		clientLogger.debug(arg0, arg1);

	}

	@Override
	public void debug(String arg0, Object... arg1) {
		clientLogger.debug(arg0, arg1);

	}

	@Override
	public void debug(String arg0, Throwable arg1) {
		clientLogger.debug(arg0, arg1);

	}

	@Override
	public void debug(Marker arg0, String arg1) {
		clientLogger.debug(arg0, arg1);

	}

	@Override
	public void debug(String arg0, Object arg1, Object arg2) {
		clientLogger.debug(arg0, arg1, arg2);

	}

	@Override
	public void debug(Marker arg0, String arg1, Object arg2) {
		clientLogger.debug(arg0, arg1, arg2);

	}

	@Override
	public void debug(Marker arg0, String arg1, Object... arg2) {
		clientLogger.debug(arg0, arg1, arg2);

	}

	@Override
	public void debug(Marker arg0, String arg1, Throwable arg2) {
		clientLogger.debug(arg0, arg1, arg2);

	}

	@Override
	public void debug(Marker arg0, String arg1, Object arg2, Object arg3) {
		clientLogger.debug(arg0, arg1, arg2, arg3);

	}

	@Override
	public void error(String arg0) {
		clientLogger.error(arg0);

	}

	@Override
	public void error(String arg0, Object arg1) {
		clientLogger.error(arg0, arg1);

	}

	@Override
	public void error(String arg0, Object... arg1) {
		clientLogger.error(arg0, arg1);

	}

	@Override
	public void error(String arg0, Throwable arg1) {
		clientLogger.error(arg0, arg1);

	}

	@Override
	public void error(Marker arg0, String arg1) {
		clientLogger.error(arg0, arg1);

	}

	@Override
	public void error(String arg0, Object arg1, Object arg2) {
		clientLogger.error(arg0, arg1, arg2);

	}

	@Override
	public void error(Marker arg0, String arg1, Object arg2) {
		clientLogger.error(arg0, arg1, arg2);

	}

	@Override
	public void error(Marker arg0, String arg1, Object... arg2) {
		clientLogger.error(arg0, arg1, arg2);

	}

	@Override
	public void error(Marker arg0, String arg1, Throwable arg2) {
		clientLogger.error(arg0, arg1, arg2);

	}

	@Override
	public void error(Marker arg0, String arg1, Object arg2, Object arg3) {
		clientLogger.error(arg0, arg1, arg2, arg3);

	}

	@Override
	public String getName() {
		return clientLogger.getName();
	}

	@Override
	public void info(String arg0) {
		clientLogger.info(arg0);

	}

	@Override
	public void info(String arg0, Object arg1) {
		clientLogger.info(arg0, arg1);

	}

	@Override
	public void info(String arg0, Object... arg1) {
		clientLogger.info(arg0, arg1);

	}

	@Override
	public void info(String arg0, Throwable arg1) {
		clientLogger.info(arg0, arg1);

	}

	@Override
	public void info(Marker arg0, String arg1) {
		clientLogger.info(arg0, arg1);

	}

	@Override
	public void info(String arg0, Object arg1, Object arg2) {
		clientLogger.info(arg0, arg1, arg2);

	}

	@Override
	public void info(Marker arg0, String arg1, Object arg2) {
		clientLogger.info(arg0, arg1, arg2);

	}

	@Override
	public void info(Marker arg0, String arg1, Object... arg2) {
		clientLogger.info(arg0, arg1, arg2);

	}

	@Override
	public void info(Marker arg0, String arg1, Throwable arg2) {
		clientLogger.info(arg0, arg1, arg2);

	}

	@Override
	public void info(Marker arg0, String arg1, Object arg2, Object arg3) {
		clientLogger.info(arg0, arg1, arg2, arg3);

	}

	@Override
	public boolean isDebugEnabled() {
		return clientLogger.isDebugEnabled();
	}

	@Override
	public boolean isDebugEnabled(Marker arg0) {
		return clientLogger.isDebugEnabled(arg0);
	}

	@Override
	public boolean isErrorEnabled() {
		return clientLogger.isErrorEnabled();
	}

	@Override
	public boolean isErrorEnabled(Marker arg0) {
		return clientLogger.isErrorEnabled(arg0);
	}

	@Override
	public boolean isInfoEnabled() {
		return clientLogger.isInfoEnabled();
	}

	@Override
	public boolean isInfoEnabled(Marker arg0) {
		return clientLogger.isInfoEnabled(arg0);
	}

	@Override
	public boolean isTraceEnabled() {
		return clientLogger.isTraceEnabled();
	}

	@Override
	public boolean isTraceEnabled(Marker arg0) {
		return clientLogger.isTraceEnabled(arg0);
	}

	@Override
	public boolean isWarnEnabled() {
		return clientLogger.isWarnEnabled();
	}

	@Override
	public boolean isWarnEnabled(Marker arg0) {
		return clientLogger.isWarnEnabled(arg0);
	}

	@Override
	public void trace(String arg0) {
		clientLogger.trace(arg0);

	}

	@Override
	public void trace(String arg0, Object arg1) {
		clientLogger.trace(arg0, arg1);

	}

	@Override
	public void trace(String arg0, Object... arg1) {
		clientLogger.trace(arg0, arg1);

	}

	@Override
	public void trace(String arg0, Throwable arg1) {
		clientLogger.trace(arg0, arg1);

	}

	@Override
	public void trace(Marker arg0, String arg1) {
		clientLogger.trace(arg0, arg1);

	}

	@Override
	public void trace(String arg0, Object arg1, Object arg2) {
		clientLogger.trace(arg0, arg1, arg2);

	}

	@Override
	public void trace(Marker arg0, String arg1, Object arg2) {
		clientLogger.trace(arg0, arg1, arg2);

	}

	@Override
	public void trace(Marker arg0, String arg1, Object... arg2) {
		clientLogger.trace(arg0, arg1, arg2);

	}

	@Override
	public void trace(Marker arg0, String arg1, Throwable arg2) {
		clientLogger.trace(arg0, arg1, arg2);

	}

	@Override
	public void trace(Marker arg0, String arg1, Object arg2, Object arg3) {
		clientLogger.trace(arg0, arg1, arg2, arg3);

	}

	@Override
	public void warn(String arg0) {
		clientLogger.warn(arg0);

	}

	@Override
	public void warn(String arg0, Object arg1) {
		clientLogger.warn(arg0);

	}

	@Override
	public void warn(String arg0, Object... arg1) {
		clientLogger.warn(arg0, arg1);

	}

	@Override
	public void warn(String arg0, Throwable arg1) {
		clientLogger.warn(arg0, arg1);

	}

	@Override
	public void warn(Marker arg0, String arg1) {
		clientLogger.warn(arg0, arg1);

	}

	@Override
	public void warn(String arg0, Object arg1, Object arg2) {
		clientLogger.warn(arg0, arg1, arg2);

	}

	@Override
	public void warn(Marker arg0, String arg1, Object arg2) {
		clientLogger.warn(arg0, arg1, arg2);

	}

	@Override
	public void warn(Marker arg0, String arg1, Object... arg2) {
		clientLogger.warn(arg0, arg1, arg2);

	}

	@Override
	public void warn(Marker arg0, String arg1, Throwable arg2) {
		clientLogger.warn(arg0, arg1, arg2);

	}

	@Override
	public void warn(Marker arg0, String arg1, Object arg2, Object arg3) {
		clientLogger.warn(arg0, arg1, arg2);

	}

}
