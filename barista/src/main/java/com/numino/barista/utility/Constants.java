package com.numino.barista.utility;

public class Constants {
	//	Menu Constants
	public static final String MENU = "Menu";
	public static final String INGREDIENTS = "Ingredients";
	public static final String RECIPES = "Recipes";
	public static final String MENU_SPLIT = "=================================";
	public static final String MENU_ITEM = "%d - %s - $ %.2f - %s.";
	public static final String RESTOCK_OPTION = "Press R to Restock";
	public static final String QUIT_OPTION = "Press Q to Quit";
	public static final String DRINK_OPTION = "1 to %d to Order drink in menu";
	public static final String USER_CHOICE = "Your Choice:";
	public static final String INVALID_CHOICE = "Invalid selection: %s. Try Again.";
	public static final String DISPENSE_DRINK = "Dispensing drink - %s";
	public static final String OUTOF_STOCK = "Out of stock - %s.";
	public static final String EXIT_APP = "Exiting application.";
	public static final String INGREDIENTS_RESTOCKED = "Ingredients restocked.";
	public static final String RESTOCK_ISSUE = "Issue with restocking!";
	public static final String NO_ITEMS = "No ingredients or recipes found.";

	// Exception Constants
	public static final String INGREDIENTS_FILE_EXCEPTION = "Exception while fetching ingredients file data";
	public static final String STOCK_PROCESS_EXCEPTION = "Exception while processing stocks data!";
	public static final String DRINKS_PROCESS_EXCEPTION = "Exception while processing drinks data!";
	public static final String STOCK_LISTING_EXCEPTION = "Exception while listing stocks data!";
	public static final String DRINKS_LISTING_EXCEPTION = "Exception while listing drinks data!";
	public static final String INPUT_CHOICE_EXCEPTION = "Exception in user input - %s!";
	public static final String PROCESS_ORDER_EXCEPTION = "Exception while processing order!";
	public static final String RESTOCK_EXCEPTION = "Exception while restocking!";
}