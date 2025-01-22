package com.autjuan.utils;



import org.apache.velocity.runtime.log.LogManager;
import org.codehaus.classworlds.Configurator;
import org.slf4j.Logger;

public class LoggerUtil {

	private static final Logger logger;

	static {
		Configurator.setLevel(LogManager.getRootLogger().getName(), org.apache.logging.log4j.Level.DEBUG);
		logger = LogManager.getLogger(LoggerUtil.class);
	}

	public static Logger getLogger() {
		String callingClassName = Thread.currentThread().getStackTrace()[2].getClassName();
		return LogManager.getLogger(callingClassName);
	}

	public static void info(String message) {
		logger.info(message);
	}

	public static void error(String message) {
		logger.error(message);
	}

	public static void error(String message, Exception e) {
		logger.error(message, e);
	}

	public static void debug(String message) {
		logger.debug(message);
	}

	public static void warn(String message) {
		logger.warn(message);
	}
}