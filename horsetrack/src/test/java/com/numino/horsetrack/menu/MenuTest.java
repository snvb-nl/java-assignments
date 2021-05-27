package com.numino.horsetrack.menu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.numino.horsetrack.utility.Util;

public class MenuTest {

	private Menu menu;
	private Util mockUtil = mock(Util.class);
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final InputStream systemIn = System.in;

	@BeforeEach
	void setUp() {
		System.setOut(new PrintStream(outContent));
		when(mockUtil.fetchStartupValues()).thenReturn(fetchMockfetchStartupValues());
		menu = new Menu();
	}

	@AfterEach
	void clearOut() {
		System.setIn(systemIn);
		menu = null;
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
	@DisplayName("Test payout when user bet on winning horse")
	public void testProcessUserChoice_PayoutWhenUserBetsOnWinningHorse() {
		menu.processUserChoice("w 2");
		menu.processUserChoice("2 12");
		assertEquals(2, menu.getWinnerHorse());
		assertEquals("Payout: Fort Utopia, $120\nDispensing:\n$20 - 1\n$100 - 1\n", outContent.toString());
	}

	@Test
	@DisplayName("Test no payout when user bets on non-winning horse")
	public void testProcessUserChoice_NoPayoutWhenUserBetsOnNonWinningHorse() {
		menu.processUserChoice("w 2");
		menu.processUserChoice("1 10");
		assertEquals(2, menu.getWinnerHorse());
		assertEquals("No Payout: That Darn Gray\n", outContent.toString());
	}

	@Test
	@DisplayName("Test invalid bet amount when bet amount is not integer")
	public void testProcessUserChoice_InvalidBetWhenBetAmountIsNotinteger() {
		menu.processUserChoice("1 1.1");
		assertEquals("Invalid bet: 1 1.1\n", outContent.toString());
	}

	private String fetchMockfetchStartupValues() {
		return "{\n" + "	\"inventory\": [\n" + "		{\n" + "			\"denomination\": 1,\n"
				+ "			\"quantity\": 10\n" + "		},\n" + "		{\n" + "			\"denomination\": 5,\n"
				+ "			\"quantity\": 10\n" + "		},\n" + "		{\n" + "			\"denomination\": 10,\n"
				+ "			\"quantity\": 10\n" + "		},\n" + "		{\n" + "			\"denomination\": 20,\n"
				+ "			\"quantity\": 10\n" + "		},\n" + "		{\n" + "			\"denomination\": 100,\n"
				+ "			\"quantity\": 10\n" + "		}\n" + "	],\n" + "	\"horses\": [\n" + "		{\n"
				+ "			\"horseName\": \"That Darn Gray\",\n" + "			\"odds\": 5\n" + "		},\n"
				+ "		{\n" + "			\"horseName\": \"Fort Utopia\",\n" + "			\"odds\": 10\n" + "		}\n"
				+ "	]\n" + "}";
	}
}
