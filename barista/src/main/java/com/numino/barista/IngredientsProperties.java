package com.numino.barista;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties
@PropertySource(value = { "classpath:ingredients.properties" }, ignoreResourceNotFound = true)
public class IngredientsProperties {

	private String database;
	private List<Item> items;

	public IngredientsProperties(List<Item> items, String database) {
		this.items = items;
		this.database = database;
	}

	public IngredientsProperties() {
	}

	public String getDatabase() {
		return this.database;
	}
	
	public void setDatabase(String database) {
		this.database = database;
	}
	
	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public static class Item {
		private String name;
		private String text;
		private Float unitCost;
		private int stock;

		public Item(String name, String text, Float unitCost, int stock) {
			this.name = name;
			this.text = text;
			this.unitCost = unitCost;
			this.stock = stock;

		}

		public Item() {
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public Float getUnitCost() {
			return unitCost;
		}

		public void setUnitCost(Float unitCost) {
			this.unitCost = unitCost;
		}

		public int getStock() {
			return stock;
		}

		public void setStock(int stock) {
			this.stock = stock;
		}

		@Override
		public String toString() {
			return "{" + this.getName() + ", " + this.getText() + ", " + ", " + this.getUnitCost() + ", "
					+ this.getStock() + "}";
		}
	}

	@Override
	public String toString() {
		return "{" + this.getDatabase() + ", " + this.getItems() + "}";
	}
}
