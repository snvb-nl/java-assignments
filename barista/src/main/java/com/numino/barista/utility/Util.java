package com.numino.barista.utility;

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

	public static void f(String s, Object ... args ) {
		System.out.printf(s + "\n", args);
	}

	public static String Format(String s, Object ... args) {
		return String.format(s, args);
	}

	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public String fetchIngredientsJsonAsText() {
		String data = "";
		try (InputStream inputStream = getClass().getResourceAsStream("/ingredients.json");
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			data = reader.lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (Exception e) {

		}
		return data;
	}

}
