package com.numino.barista.vending;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.numino.barista.models.Stock;
import com.numino.barista.utility.Constants;
import com.numino.barista.utility.ExceptionLogger;
import com.numino.barista.utility.Util;

public class Vending {

	private static Logger logger = LoggerFactory.getLogger(Vending.class);
	private Ingredients ingredients = Ingredients.getInstance();

	void restock() {
		try {
			if (ingredients.getStocks().size() > 0) {
				for (Stock stock : ingredients.getStocks()) {
					stock.setStock(10);
				}
			}
			Util.lnp(logger, Constants.INGREDIENTS_RESTOCKED);
		} catch (Exception e) {
			ExceptionLogger.LogErrorAndExit(logger, e, Constants.RESTOCK_EXCEPTION);
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
					Util.lnp(logger, Constants.DISPENSE_DRINK, drink.getName());
				} else {
					Util.lnp(logger, Constants.OUTOF_STOCK, drink.getName());
				}
			} else {
				Util.sof(Constants.INVALID_CHOICE, choice);
			}
		} catch (Exception e) {
			ExceptionLogger.LogErrorAndExit(logger, e, Constants.PROCESS_ORDER_EXCEPTION, choice);
		}
	}
}
