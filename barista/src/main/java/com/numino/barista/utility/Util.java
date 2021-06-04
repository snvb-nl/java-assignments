package com.numino.barista.utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {
	private static Logger logger = LoggerFactory.getLogger(Util.class);

	/**
	 * Print text with new line
	 *
	 * @param s
	 */
	public static void sopn(Object s) {
		System.out.println(s);
	}

	/**
	 * Print text only
	 *
	 * @param s
	 */
	public static void sop(Object s) {
		System.out.print(s);
	}

	/**
	 * Print formatted string by including the arguments
	 *
	 * @param text
	 * @param args
	 */
	public static void sof(String text, Object... args) {
		System.out.printf(text + '\n', args);
	}

	/**
	 * Print formatted string in console and log the same
	 *
	 * @param logr
	 * @param text
	 * @param args
	 */
	public static void lnp(Logger logr, String text, Object... args) {
		logr.info(String.format(text, args));
		System.out.println(String.format(text, args) + '\n');
	}

	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public String fetchIngredientsJsonAsText() {
		String data = "";
		try {
			InputStream inputStream = getClass().getResourceAsStream("/ingredients.json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			data = reader.lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (Exception e) {
			ExceptionLogger.LogErrorAndExit(logger, e, Constants.INGREDIENTS_FILE_EXCEPTION);
		}
		return data;
	}
}
