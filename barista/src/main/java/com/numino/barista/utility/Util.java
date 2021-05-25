package com.numino.barista.utility;

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

}
