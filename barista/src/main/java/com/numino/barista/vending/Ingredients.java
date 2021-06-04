package com.numino.barista.vending;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.numino.barista.models.Drink;
import com.numino.barista.models.Stock;
import com.numino.barista.utility.Constants;
import com.numino.barista.utility.ExceptionLogger;
import com.numino.barista.utility.Util;

public class Ingredients {

	private Util util;
	private List<Stock> stocks;
	private List<Drink> drinks;
	private static Logger logger = LoggerFactory.getLogger(Ingredients.class);

	private static class IngredientsSingleton {
		private static final Ingredients INSTANCE = new Ingredients();
	}

	private Ingredients() {
		util = new Util();
		stocks = fetchStock();
		drinks = fetchDrinks();
	}

	public static Ingredients getInstance() {
		return IngredientsSingleton.INSTANCE;
	}

	public List<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}

	public List<Drink> getDrinks() {
		return drinks;
	}

	public void setDrinks(List<Drink> drinks) {
		this.drinks = drinks;
	}

	List<Stock> fetchStock() {
		try {
			var ingredientsText = util.fetchIngredientsJsonAsText();
			var ingredientsJson = JsonParser.parseString(ingredientsText).getAsJsonObject();
			var stocksArray = ingredientsJson.get("stock");

			Gson gson = new Gson();
			List<Stock> stocks = gson.fromJson(stocksArray, new TypeToken<ArrayList<Stock>>() {
			}.getType());
			return stocks == null ? new ArrayList<Stock>() : stocks;
		} catch (Exception e) {
			ExceptionLogger.LogErrorAndExit(logger, e, Constants.STOCK_PROCESS_EXCEPTION);
		}
		return new ArrayList<Stock>();
	}

	List<Drink> fetchDrinks() {
		try {
			var drinksText = util.fetchIngredientsJsonAsText();
			var drinksJson = JsonParser.parseString(drinksText).getAsJsonObject();
			var drinksArray = drinksJson.get("drinks");

			Gson gson = new Gson();
			List<Drink> drinks = gson.fromJson(drinksArray, new TypeToken<List<Drink>>() {
			}.getType());
			return drinks == null ? new ArrayList<Drink>() : drinks;
		} catch (Exception e) {
			ExceptionLogger.LogErrorAndExit(logger, e, Constants.DRINKS_PROCESS_EXCEPTION);
		}
		return new ArrayList<Drink>();
	}
}
