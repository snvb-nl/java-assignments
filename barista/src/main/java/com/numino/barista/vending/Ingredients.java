package com.numino.barista.vending;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.numino.barista.models.Drink;
import com.numino.barista.models.Stock;
import com.numino.barista.utility.Util;

public class Ingredients {

	private Util util;
	private List<Stock> stocks;
	private List<Drink> drinks;

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
			return gson.fromJson(stocksArray, new TypeToken<ArrayList<Stock>>() {
			}.getType());
		} catch (Exception e) {

		}
		return new ArrayList<Stock>();
	}

	List<Drink> fetchDrinks() {
		try {
			var drinksText = util.fetchIngredientsJsonAsText();
			var drinksJson = JsonParser.parseString(drinksText).getAsJsonObject();
			var drinksArray = drinksJson.get("drinks");

			Gson gson = new Gson();
			return gson.fromJson(drinksArray, new TypeToken<List<Drink>>() {
			}.getType());
		} catch (Exception e) {

		}
		return new ArrayList<Drink>();
	}
}
