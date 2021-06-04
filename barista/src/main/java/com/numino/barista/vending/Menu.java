package com.numino.barista.vending;

import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.numino.barista.models.Stock;
import com.numino.barista.utility.Constants;
import com.numino.barista.utility.ExceptionLogger;
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

		if (ingredients.getDrinks().size() > 0 && ingredients.getStocks().size() > 0) {
			String choice = "";
			do {
				try {
					prepareMenu();
					choice = input.nextLine().toLowerCase().trim();

					Util.clearScreen();
					processUserChoice(choice);
				} catch (Exception e) {
					ExceptionLogger.LogErrorAndExit(logger, e, Constants.INPUT_CHOICE_EXCEPTION, choice);
				}
			} while (!choice.equals("q"));
		} else {
			Util.sopn(Constants.NO_ITEMS);
			ExceptionLogger.LogErrorAndExit(logger, null, Constants.NO_ITEMS);
		}
	}

	void processUserChoice(String choice) {
		if (choice.equals("r")) {
			vending.restock();
		} else if (choice.equals("q")) {
			Util.lnp(logger, Constants.EXIT_APP);
		} else if (StringUtils.isNumeric(choice)) {
			vending.processOrder(choice);
		} else {
			Util.sof(Constants.INVALID_CHOICE, choice);
		}
	}

	private void prepareMenu() {
		listStock();
		listRecipes();
		Util.sopn(Constants.RESTOCK_OPTION);
		Util.sopn(Constants.QUIT_OPTION);
		Util.sof(Constants.DRINK_OPTION, ingredients.getDrinks().size());
		Util.sopn(Constants.USER_CHOICE);
	}

	private void listStock() {
		Util.sopn(Constants.INGREDIENTS);
		try {
			ingredients.getStocks().forEach((stock) -> {
				Util.sopn(stock.getName() + " - " + stock.getStock());
			});
		} catch (Exception e) {
			ExceptionLogger.LogErrorAndExit(logger, e, Constants.STOCK_LISTING_EXCEPTION);
		}
		Util.sopn(Constants.MENU_SPLIT);
	}

	private void listRecipes() {
		Util.sopn(Constants.MENU);
		try {
			for (int cnt = 0; cnt < ingredients.getDrinks().size(); cnt++) {
				var recipe = ingredients.getDrinks().get(cnt);
				var itemCost = 0.0;
				var inStock = true;
				for (Map.Entry<String, Integer> pair : recipe.getIngredients().entrySet()) {
					Stock thisStock = ingredients.getStocks().stream()
							.filter(x -> x.getName().toLowerCase().equals(pair.getKey().toLowerCase())).findFirst()
							.get();
					if (thisStock.getStock() < pair.getValue()) {
						inStock = false;
					}
					itemCost = itemCost + (thisStock.getUnitCost() * pair.getValue());
				}
				Util.sof(Constants.MENU_ITEM, cnt + 1, recipe.getName(), itemCost, inStock);
			}
		} catch (Exception e) {
			ExceptionLogger.LogErrorAndExit(logger, e, Constants.DRINKS_LISTING_EXCEPTION);
		}
		Util.sopn(Constants.MENU_SPLIT);
	}
}
