package com.numino.barista;

import static com.adelean.inject.resources.core.InjectResources.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class Menu {

	private List<Stock> stocks;
	private List<Recipe> recipes;
	private Scanner input = new Scanner(System.in);

	public void loadMenuAndOps() {
		stocks = fetchStock();
		recipes = fetchRecipes();

		char choice;
		do {
			var recipeCnt = prepareMenu();
			choice = input.next().toLowerCase().charAt(0);

			Util.clearScreen();
			if (choice == 'r') {
				if (restock()) {
					Util.ln("Ingredients restocked.");
				} else {
					Util.ln("Issue with restocking!");
				}
			} else if (choice == 'q') {
				Util.ln("Exiting application.");
			} else if (Character.isDigit(choice)) {
				int drinkItem = choice - '0';
				if (drinkItem > 0 && drinkItem <= recipeCnt) {
					processOrder(drinkItem);
				} else {
					Util.ln("Invalid selection: " + choice + ". Try again.");
				}
			} else {
				Util.ln("Invalid selection: " + choice + ". Try again.");
			}
		} while (choice != 'q');
	}

	private int prepareMenu() {
		Util.ln("Ingredients:");
		stocks.forEach((stock) -> {
			Util.ln(stock.getName() + " - " + stock.getStock());
		});
		Util.ln("==========================");
		Util.ln("Menu:");
		var recipeCnt = recipes.size();
		for (int cnt = 0; cnt < recipeCnt; cnt++) {
			var recipe = recipes.get(cnt);
			var itemCost = 0.0;
			var inStock = true;
			for (Map.Entry<String, Object> pair : recipe.getIngredients().entrySet()) {
				Stock thisStock = stocks.stream()
						.filter(x -> x.getName().toLowerCase().equals(pair.getKey().toLowerCase())).findFirst().get();
				if (thisStock.getStock() < ((Double) pair.getValue()).intValue()) {
					inStock = false;
				}
				itemCost = itemCost + (thisStock.getUnitCost() * ((Double) pair.getValue()).intValue());
			}
			System.out.printf("%d - %s - $ %.2f - %s.\n", cnt + 1, recipe.getName(), itemCost, inStock);
		}
		Util.ln("==========================");
		Util.ln("Press R to Restock");
		Util.ln("Press Q to Quit");
		Util.ln("1 to " + recipeCnt + " to Order drink in menu");
		Util.ln("Your choice: ");
		return recipeCnt;
	}

	private boolean restock() {
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

	private void processOrder(int drinkNo) {
		var drink = recipes.get(drinkNo - 1);
		Map<String, Integer> inStockIngredients = new HashMap<String, Integer>();
		boolean isInStock = true;
		for (Map.Entry<String, Object> pair : drink.getIngredients().entrySet()) {
			Stock thisStock = stocks.stream().filter(x -> x.getName().toLowerCase().equals(pair.getKey().toLowerCase()))
					.findFirst().get();
			if (thisStock.getStock() < ((Double) pair.getValue()).intValue()) {
				isInStock = false;
			} else {
				inStockIngredients.put(pair.getKey(), ((Double) pair.getValue()).intValue());
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
		} else {
			Util.ln("Out of stock - " + drink.getName() + ".");
		}
	}

	private List<Stock> fetchStock() {
		try {
			var ingredientsText = resource().withPath("ingredients.json").text();
			var ingredientsJson = JsonParser.parseString(ingredientsText).getAsJsonObject();
			var stocksArray = ingredientsJson.get("stock");

			Gson gson = new Gson();
			return gson.fromJson(stocksArray, new TypeToken<ArrayList<Stock>>() {
			}.getType());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new ArrayList<Stock>();
	}

	private List<Recipe> fetchRecipes() {
		try {
			var recipesText = resource().withPath("ingredients.json").text();
			var recipesJson = JsonParser.parseString(recipesText).getAsJsonObject();
			var recipesArray = recipesJson.get("recipes");

			Gson gson = new Gson();
			return gson.fromJson(recipesArray, new TypeToken<List<Recipe>>() {
			}.getType());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new ArrayList<Recipe>();
	}
}
