package com.numino.barista.menu;

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

import com.numino.barista.utility.Util;

public class MenuTest {

	private Menu menu;
	private static final Util mockUtil = mock(Util.class);
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final InputStream systemIn = System.in;

	@BeforeEach
	void setUp() {
		System.setOut(new PrintStream(outContent));
		when(mockUtil.fetchIngredientsJsonAsText()).thenReturn(fetchMockIngredientsJsonText());
		menu = new Menu(mockUtil);
	}

	@AfterEach
	void clearOut() {
		System.setIn(systemIn);
		menu = null;
	}

	@Test
	@DisplayName("Test user input 'q' to exit the application")
	public void testProcessUserChoice_ExitWhenInputIsQ() {
		menu.processUserChoice('q');
		assertEquals("Exiting application.\n", outContent.toString());
	}

	@Test
	@DisplayName("When user selects 1.Cafe Latte as input")
	public void testProcessUserChoice_DispenseWhenInputIsDrinkNo() {
		menu.processUserChoice('1');// Cafe Latte selected

		var stocks = menu.getStocks();

		var coffeeStock = stocks.stream().filter(stock -> stock.getName().toLowerCase().equals("espresso")).findFirst()
				.orElse(null);
		assertEquals(8, coffeeStock.getStock());// verify Cafe Latte stock has reduced
		assertEquals("Dispensing drink - Cafe Latte\n", outContent.toString());// verify console text
	}

	@Test
	@DisplayName("When user selects invalid input")
	public void testProcessUserChoice_InvalidSelectionOnInvalidInput() {
		menu.processUserChoice('9');// invalid selection

		assertEquals("Invalid selection: 9. Try again.\n", outContent.toString());// verify console text
	}

	@Test
	@DisplayName("When user selects restocking input")
	public void testProcessUserChoice_ResetStockOnRestockInput() {
		menu.processUserChoice('1');// Order drink to change stock levels

		menu.processUserChoice('r');// Re-stock level selection

		var stocks = menu.getStocks();
		var espressoStock = stocks.stream().filter(stock -> stock.getName().toLowerCase().equals("espresso"))
				.findFirst().orElse(null);
		assertEquals(10, espressoStock.getStock());// verify coffee stock is the same
	}

	private String fetchMockIngredientsJsonText() {
		return "{\n" + "   \"stock\":[\n" + "      {\n" + "         \"name\":\"Steamed Milk\",\n"
				+ "         \"stock\":10,\n" + "         \"unitCost\":0.35\n" + "      },\n" + "      {\n"
				+ "         \"name\":\"Espresso\",\n" + "         \"stock\":10,\n" + "         \"unitCost\":1.10\n"
				+ "      }\n" + "   ],\n" + "   \"recipes\":[\n" + "      {\n" + "         \"name\":\"Cafe Latte\",\n"
				+ "         \"ingredients\":{\n" + "            \"Espresso\":2,\n" + "            \"Steamed Milk\":1\n"
				+ "         }\n" + "      },\n" + "      {\n" + "         \"name\":\"Caffe Americano\",\n"
				+ "         \"ingredients\":{\n" + "            \"Espresso\":3\n" + "         }\n" + "      }\n"
				+ "   ]\n" + "}";
	}
}
