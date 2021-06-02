package com.numino.barista.vending;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import com.numino.barista.models.Drink;
import com.numino.barista.models.Stock;
import com.numino.barista.utility.Constants;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class VendingTest {

	private Menu menu;
	private Ingredients ingredients = Ingredients.getInstance();
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final InputStream systemIn = System.in;

	@BeforeEach
	void setUp() {
		System.setOut(new PrintStream(outContent));
		final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		logger.setLevel(Level.OFF); // turn off the default console logger
		menu = new Menu();
		ingredients.setDrinks(fetchMockDrinks());
		ingredients.setStocks(fetchMockStocks());
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
	@DisplayName("When user selects 1.Cafe Latte as input")
	public void testProcessUserChoice_DispenseWhenInputIsDrinkNo() {
		menu.processUserChoice("1");// Cafe Latte selected

		var stocks = ingredients.getStocks();

		var coffeeStock = stocks.stream().filter(stock -> stock.getName().toLowerCase().equals("espresso")).findFirst()
				.orElse(null);
		assertEquals(8, coffeeStock.getStock());// verify Cafe Latte stock has reduced
		assertEquals(String.format(Constants.DISPENSE_DRINK, "Cafe Latte"), outContent.toString().trim());// verify
																											// console
																											// text
	}

	@Test
	@DisplayName("When user selects invalid input")
	public void testProcessUserChoice_InvalidSelectionOnInvalidInput() {
		menu.processUserChoice("9");// invalid selection

		assertEquals(String.format(Constants.INVALID_CHOICE, "9"), outContent.toString().trim());// verify console text
	}

	@Test
	@DisplayName("When user selects restocking input")
	public void testProcessUserChoice_ResetStockOnRestockInput() {
		menu.processUserChoice("1");// Order drink to change stock levels

		menu.processUserChoice("r");// Re-stock level selection

		var stocks = ingredients.getStocks();
		var espressoStock = stocks.stream().filter(stock -> stock.getName().toLowerCase().equals("espresso"))
				.findFirst().orElse(null);
		assertEquals(10, espressoStock.getStock());// verify coffee stock is the same
	}

	private List<Drink> fetchMockDrinks() {
		List<Drink> drinks = new ArrayList<Drink>();

		var ingredients1 = new HashMap<String, Integer>();
		ingredients1.put("Espresso", 2);
		ingredients1.put("Steamed Milk", 1);
		var drink1 = new Drink();
		drink1.setName("Cafe Latte");
		drink1.setIngredients(ingredients1);
		drinks.add(drink1);

		var ingredients2 = new HashMap<String, Integer>();
		ingredients2.put("Espresso", 3);
		var drink2 = new Drink();
		drink2.setName("Caffe Americano");
		drink2.setIngredients(ingredients2);
		drinks.add(drink2);

		return drinks;
	}

	private List<Stock> fetchMockStocks() {
		List<Stock> stocks = new ArrayList<Stock>();

		var stock1 = new Stock();
		stock1.setName("Steamed Milk");
		stock1.setStock(10);
		stock1.setUnitCost((float) 0.35);
		stocks.add(stock1);

		var stock2 = new Stock();
		stock2.setName("Espresso");
		stock2.setStock(10);
		stock2.setUnitCost((float) 1.10);
		stocks.add(stock2);

		return stocks;
	}
}
