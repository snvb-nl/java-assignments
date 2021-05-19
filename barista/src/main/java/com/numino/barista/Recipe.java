package com.numino.barista;

import java.util.Map;

public class Recipe {
	private String name;
	private Map<String, Object> ingredients;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getIngredients() {
		return ingredients;
	}

	public void setIngredients(Map<String, Object> ingredients) {
		this.ingredients = ingredients;
	}
}
