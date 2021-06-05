package com.numino.horsetrack.menu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import com.numino.horsetrack.models.Cash;
import com.numino.horsetrack.models.Horse;
import com.numino.horsetrack.utility.Constants;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class MenuTest {

	private Menu menu;
	private Inventory inventory = Inventory.getInstance();
	// private Util mockUtil = mock(Util.class);
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final InputStream systemIn = System.in;

	@BeforeEach
	void setUp() {
		System.setOut(new PrintStream(outContent));
		final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.setLevel(Level.OFF); // turn off the default console logger
		// when(mockUtil.fetchStartupValues()).thenReturn(fetchMockfetchStartupValues());
		menu = new Menu();
		inventory.setHorses(fetchMockHorseData());
		inventory.setMoney(fetchMockCashData());
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
		assertEquals(Constants.EXIT_APP, outContent.toString().trim());
	}

	@Test
	@DisplayName("Test incomplete input to show 'invalid command' message")
	public void testProcessUserChoice_InvalidInput() {
		menu.processUserChoice("x");
		assertEquals(String.format(Constants.INVALID_COMMAND, "x"), outContent.toString().trim());
	}

	@Test
	@DisplayName("Test user input 'w' and horse number to set winning horse")
	public void testProcessUserChoice_InputWAndHorseNumberSetWinningHorse() {
		menu.processUserChoice("w 2");
		assertEquals(2, inventory.getWinningHorse());
	}

	@Test
	@DisplayName("Test payout when user bet on winning horse")
	public void testProcessUserChoice_PayoutWhenUserBetsOnWinningHorse() {
		menu.processUserChoice("w 2");
		menu.processUserChoice("2 12");
		assertEquals(2, inventory.getWinningHorse());
		assertEquals("Payout: Fort Utopia, $120\nDispensing:\n$20 - 1\n$100 - 1", outContent.toString().trim());
	}

	@Test
	@DisplayName("Test no payout when user bets on non-winning horse")
	public void testProcessUserChoice_NoPayoutWhenUserBetsOnNonWinningHorse() {
		menu.processUserChoice("w 2");
		menu.processUserChoice("1 10");
		assertEquals(2, inventory.getWinningHorse());
		assertEquals(String.format(Constants.NO_PAYOUT, "That Darn Gray"), outContent.toString().trim());
	}

	@Test
	@DisplayName("Test invalid bet amount when bet amount is not integer")
	public void testProcessUserChoice_InvalidBetWhenBetAmountIsNotinteger() {
		menu.processUserChoice("1 1.1");
		assertEquals(String.format(Constants.INVALID_BET, "1 1.1"), outContent.toString().trim());
	}

	private List<Horse> fetchMockHorseData() {
		List<Horse> horses = new ArrayList<Horse>();

		var horse1 = new Horse();
		horse1.setHorseName("That Darn Gray");
		horse1.setOdds(5);
		horses.add(horse1);

		var horse2 = new Horse();
		horse2.setHorseName("Fort Utopia");
		horse2.setOdds(10);
		horses.add(horse2);

		return horses;
	}

	private List<Cash> fetchMockCashData() {
		List<Cash> money = new ArrayList<Cash>();

		var cash1 = new Cash();
		cash1.setDenomination(1);
		cash1.setQuantity(10);
		money.add(cash1);

		var cash5 = new Cash();
		cash5.setDenomination(5);
		cash5.setQuantity(10);
		money.add(cash5);

		var cash10 = new Cash();
		cash10.setDenomination(10);
		cash10.setQuantity(10);
		money.add(cash10);

		var cash20 = new Cash();
		cash20.setDenomination(20);
		cash20.setQuantity(10);
		money.add(cash20);

		var cash100 = new Cash();
		cash100.setDenomination(100);
		cash100.setQuantity(10);
		money.add(cash100);

		return money;
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
