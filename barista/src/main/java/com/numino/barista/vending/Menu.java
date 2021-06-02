package com.numino.barista.vending;

import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.numino.barista.models.Stock;
import com.numino.barista.utility.Constants;
import com.numino.barista.utility.Util;

public class Menu {

	private Vending vending;
	private Scanner input = new Scanner(System.in);
	private Ingredients ingredients = Ingredients.getInstance();
	private static Logger logger = LoggerFactory.getLogger(Menu.class);

	public Menu() {
		vending = new Vending();
	}

	public void loadMenu() {
		Util.clearScreen();

		if (ingredients.getDrinks().size() > 0 && ingredients.getDrinks().size() > 0) {
			String choice;
			do {
				try {
					prepareMenu();
					choice = input.nextLine().toLowerCase().trim();

					Util.clearScreen();
					processUserChoice(choice);
				} catch (Exception e) {
					choice = "";
					showInvalidChoice(choice);
				}
			} while (!choice.equals("q"));
		} else {
			Util.ln(Constants.NO_ITEMS);
		}
	}

	void processUserChoice(String choice) {
		if (choice.equals("r")) {
			vending.restock();
		} else if (choice.equals("q")) {
			showExiting();
		} else if (StringUtils.isNumeric(choice)) {
			vending.processOrder(choice);
		} else {
			showInvalidChoice(choice);
		}
	}

	private void prepareMenu() {
		listStock();
		listRecipes();
		Util.ln(Constants.RESTOCK_OPTION);
		Util.ln(Constants.QUIT_OPTION);
		Util.f(Constants.DRINK_OPTION, ingredients.getDrinks().size());
		Util.ln(Constants.USER_CHOICE);
	}

	private void listStock() {
		Util.ln(Constants.INGREDIENTS);
		ingredients.getStocks().forEach((stock) -> {
			Util.ln(stock.getName() + " - " + stock.getStock());
		});
		Util.ln(Constants.MENU_SPLIT);
	}

	private void listRecipes() {
		Util.ln(Constants.MENU);

		for (int cnt = 0; cnt < ingredients.getDrinks().size(); cnt++) {
			var recipe = ingredients.getDrinks().get(cnt);
			var itemCost = 0.0;
			var inStock = true;
			for (Map.Entry<String, Integer> pair : recipe.getIngredients().entrySet()) {
				Stock thisStock = ingredients.getStocks().stream()
						.filter(x -> x.getName().toLowerCase().equals(pair.getKey().toLowerCase())).findFirst().get();
				if (thisStock.getStock() < pair.getValue()) {
					inStock = false;
				}
				itemCost = itemCost + (thisStock.getUnitCost() * pair.getValue());
			}
			Util.f(Constants.MENU_ITEM, cnt + 1, recipe.getName(), itemCost, inStock);
		}
		Util.ln(Constants.MENU_SPLIT);
	}

	private void showExiting() {
		Util.ln(Constants.EXIT_APP);
		logger.info(Constants.EXIT_APP);
	}

	private void showInvalidChoice(String choice) {
		Util.f(Constants.INVALID_CHOICE, choice);
		logger.info(String.format(Constants.INVALID_CHOICE, choice));
	}
}
