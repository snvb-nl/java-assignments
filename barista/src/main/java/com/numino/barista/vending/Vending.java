package com.numino.barista.vending;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.numino.barista.models.Stock;
import com.numino.barista.utility.Constants;
import com.numino.barista.utility.Util;

public class Vending {

	private Ingredients ingredients = Ingredients.getInstance();
	private static Logger logger = LoggerFactory.getLogger(Menu.class);

	void restock() {
		try {
			if (ingredients.getStocks().size() > 0) {
				for (Stock stock : ingredients.getStocks()) {
					stock.setStock(10);
				}
			}
			Util.ln(Constants.INGREDIENTS_RESTOCKED);
			logger.info(Constants.INGREDIENTS_RESTOCKED);

		} catch (Exception e) {
			Util.ln(Constants.RESTOCK_ISSUE);
		}
	}

	void processOrder(String choice) {
		try {
			int drinkItem = NumberUtils.toInt(choice);
			if (drinkItem > 0 && drinkItem <= ingredients.getDrinks().size()) {
				var drink = ingredients.getDrinks().get(drinkItem - 1);
				Map<String, Integer> inStockIngredients = new HashMap<String, Integer>();
				boolean isInStock = true;
				for (Map.Entry<String, Integer> pair : drink.getIngredients().entrySet()) {
					Stock thisStock = ingredients.getStocks().stream()
							.filter(x -> x.getName().toLowerCase().equals(pair.getKey().toLowerCase())).findFirst()
							.get();
					if (thisStock.getStock() < pair.getValue()) {
						isInStock = false;
					} else {
						inStockIngredients.put(pair.getKey(), pair.getValue());
					}
				}
				if (isInStock) {
					for (Map.Entry<String, Integer> entry : inStockIngredients.entrySet()) {
						ingredients.getStocks().forEach(stock -> {
							if (stock.getName().toLowerCase().equals(entry.getKey().toLowerCase())) {
								stock.setStock(stock.getStock() - entry.getValue());
							}
						});
					}
					Util.f(Constants.DISPENSE_DRINK, drink.getName());
					logger.debug(String.format(Constants.DISPENSE_DRINK, drink.getName()));
				} else {
					Util.f(Constants.OUTOF_STOCK, drink.getName());
					logger.debug(String.format(Constants.OUTOF_STOCK, drink.getName()));
				}
			} else {
				showInvalidChoice(choice);
			}
		} catch (Exception e) {
			showInvalidChoice(choice);
		}
	}

	private void showInvalidChoice(String choice) {
		Util.f(Constants.INVALID_CHOICE, choice);
		logger.info(String.format(Constants.INVALID_CHOICE, choice));
	}
}
