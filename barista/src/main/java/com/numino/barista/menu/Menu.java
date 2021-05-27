package com.numino.barista.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.numino.barista.models.Recipe;
import com.numino.barista.models.Stock;
import com.numino.barista.utility.Util;

public class Menu {

	private Util util;
	private List<Stock> stocks;
	private List<Recipe> recipes;
	private Scanner input = new Scanner(System.in);
	private static Logger logger = LogManager.getLogger("alllogger");

	public Menu() {
		util = new Util();
		stocks = fetchStock();
		recipes = fetchRecipes();
	}

	Menu(Util util) {
		this.util = util;
		stocks = fetchStock();
		recipes = fetchRecipes();
	}

	public void loadMenuAndOps() {
		Util.clearScreen();

		if (this.stocks.size() > 0 && this.recipes.size() > 0) {
			char choice;
			do {
				prepareMenu();
				choice = input.next().toLowerCase().charAt(0);

				Util.clearScreen();
				processUserChoice(choice);
			} while (choice != 'q');
		} else {
			Util.ln((Object) "No ingredients or recipes found.");
		}
	}

	List<Stock> getStocks() {
		return stocks;
	}

	List<Recipe> getRecipes() {
		return recipes;
	}

	void prepareMenu() {
		Util.ln("Ingredients:");
		stocks.forEach((stock) -> {
			Util.ln(stock.getName() + " - " + stock.getStock());
		});
		Util.ln("==========================");
		Util.ln("Menu:");

		for (int cnt = 0; cnt < recipes.size(); cnt++) {
			var recipe = recipes.get(cnt);
			var itemCost = 0.0;
			var inStock = true;
			for (Map.Entry<String, Integer> pair : recipe.getIngredients().entrySet()) {
				Stock thisStock = stocks.stream()
						.filter(x -> x.getName().toLowerCase().equals(pair.getKey().toLowerCase())).findFirst().get();
				if (thisStock.getStock() < pair.getValue()) {
					inStock = false;
				}
				itemCost = itemCost + (thisStock.getUnitCost() * pair.getValue());
			}
			System.out.printf("%d - %s - $ %.2f - %s.\n", cnt + 1, recipe.getName(), itemCost, inStock);
		}
		Util.ln("==========================");
		Util.ln("Press R to Restock");
		Util.ln("Press Q to Quit");
		Util.ln("1 to " + recipes.size() + " to Order drink in menu");
		Util.ln("Your choice: ");
	}

	void processUserChoice(char choice) {
		if (choice == 'r') {
			if (restock()) {
				Util.ln("Ingredients restocked.");
				logger.debug("Ingredients restocked.");
			} else {
				Util.ln("Issue with restocking!");
			}
		} else if (choice == 'q') {
			Util.ln("Exiting application.");
			logger.debug("Application exited.");
		} else if (Character.isDigit(choice)) {
			int drinkItem = choice - '0';
			if (drinkItem > 0 && drinkItem <= recipes.size()) {
				processOrder(drinkItem);
			} else {
				Util.ln("Invalid selection: " + choice + ". Try again.");
				logger.debug("Invalid selection: " + choice + ". Try again.");
			}
		} else {
			Util.ln("Invalid selection: " + choice + ". Try again.");
			logger.debug("Invalid selection: " + choice + ". Try again.");
		}
	}

	void processOrder(int drinkNo) {
		var drink = recipes.get(drinkNo - 1);
		Map<String, Integer> inStockIngredients = new HashMap<String, Integer>();
		boolean isInStock = true;
		for (Map.Entry<String, Integer> pair : drink.getIngredients().entrySet()) {
			Stock thisStock = stocks.stream().filter(x -> x.getName().toLowerCase().equals(pair.getKey().toLowerCase()))
					.findFirst().get();
			if (thisStock.getStock() < pair.getValue()) {
				isInStock = false;
			} else {
				inStockIngredients.put(pair.getKey(), pair.getValue());
			}
		}
		if (isInStock) {
			for (Map.Entry<String, Integer> entry : inStockIngredients.entrySet()) {
				stocks.forEach(stock -> {
					if (stock.getName().toLowerCase().equals(entry.getKey().toLowerCase())) {
						stock.setStock(stock.getStock() - entry.getValue());
					}
				});
			}
			Util.ln("Dispensing drink - " + drink.getName());
			logger.debug("Dispensing drink - " + drink.getName());
		} else {
			Util.ln("Out of stock - " + drink.getName() + ".");
			logger.debug("Out of stock - " + drink.getName() + ".");
		}
	}

	boolean restock() {
		try {
			if (stocks.size() > 0) {
				for (Stock stock : stocks) {
					stock.setStock(10);
				}
				return true;
			}
		} catch (Exception e) {
			return false;
		}

		return false;
	}

	List<Stock> fetchStock() {
		try {
			var ingredientsText = util.fetchIngredientsJsonAsText();
			var ingredientsJson = JsonParser.parseString(ingredientsText).getAsJsonObject();
			var stocksArray = ingredientsJson.get("stock");

			Gson gson = new Gson();
			return gson.fromJson(stocksArray, new TypeToken<ArrayList<Stock>>() {
			}.getType());
		} catch (Exception e) {
			logger.error("Exception occured", e);
		}
		return new ArrayList<Stock>();
	}

	List<Recipe> fetchRecipes() {
		try {
			var recipesText = util.fetchIngredientsJsonAsText();
			var recipesJson = JsonParser.parseString(recipesText).getAsJsonObject();
			var recipesArray = recipesJson.get("recipes");

			Gson gson = new Gson();
			return gson.fromJson(recipesArray, new TypeToken<List<Recipe>>() {
			}.getType());
		} catch (Exception e) {
			logger.error("Exception occured", e);
		}
		return new ArrayList<Recipe>();
	}
}
