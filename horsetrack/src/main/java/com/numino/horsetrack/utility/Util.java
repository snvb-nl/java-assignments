package com.numino.horsetrack.utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Util {
	public static void ln(Object s) {
		System.out.println(s);
	}

	public static void o(Object s) {
		System.out.print(s);
	}

	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static String fetchStartupValues() {
		String data = "";
		try (InputStream inputStream = Util.class.getResourceAsStream("/startupSettings.json");
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			data = reader.lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (Exception e) {

		}
		return data;
	}
}
