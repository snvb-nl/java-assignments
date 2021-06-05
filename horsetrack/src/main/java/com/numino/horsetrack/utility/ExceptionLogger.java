package com.numino.horsetrack.utility;

import org.slf4j.Logger;

public class ExceptionLogger {
	public static void LogErrorAndExit(Logger logger, Exception e, String text, Object... args) {
		if (args != null) {
			logger.error(String.format(text, args), e);
		} else {
			logger.error(text, e);
		}
		System.exit(0);
	}
}
