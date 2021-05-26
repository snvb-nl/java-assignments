package com.numino.horsetrack.menu;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.numino.horsetrack.menu.Menu;
import com.numino.horsetrack.utility.Util;

public class MenuTest {

	private Menu menu;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final InputStream systemIn = System.in;
	private final PrintStream originalOut = System.out;

	@BeforeEach
	void setUp() {
		System.setOut(new PrintStream(outContent));
		menu = new Menu();
	}

	@AfterEach
	void clearOut() {
		System.setOut(originalOut);
		System.setIn(systemIn);
	}

	@Test
	@DisplayName("Test user input 'q' to exit the application")
	public void testProcessUserChoice_ExitWhenInputIsQ() {
		menu.processUserChoice("q");
		assertEquals("Exiting application.\n", outContent.toString());
	}

	@Test
	@DisplayName("Test incomplete input to show 'invalid command' message")
	public void testProcessUserChoice_InvalidInput() {
		menu.processUserChoice("x");
		assertEquals("Invalid command: x\n", outContent.toString());
	}

	@Test
	@DisplayName("Test user input 'w' and horse number to set winning horse")
	public void testProcessUserChoice_InputWAndHorseNumberSetWinningHorse() {
		menu.processUserChoice("w 2");
		assertEquals(2, menu.getWinnerHorse());
	}

	@Test
	@DisplayName("Test user input 'w' and horse number to set winning horse")
	public void testProcessUserChoice_InputWinnerHorseNumberAndBetAmountToPayout() {
		menu.processUserChoice("w 3");
		menu.processUserChoice("3 10");
		assertEquals(1, menu.getWinnerHorse());
	}
}
