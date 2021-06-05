package com.numino.horsetrack.utility;

public class Constants {
	public static final String INVENTORY = "Inventory";
	public static final String HORSES = "Horses";
	public static final String INVENTORY_ITEM = "$%d, %d";
	public static final String MENU_SPLIT = "=================================";
	public static final String NO_ITEMS = "No horses or inventory set.";
	public static final String RESTOCK_OPTION = "Press R to Restock";
	public static final String QUIT_OPTION = "Press Q to Quit";
	public static final String WINNING_OPTION = "Type W [1 to %d] to set wining horse number";
	public static final String BET_OPTION = "[1 to %d] <amount> specifies the horse wagered on and the amount of the bet";
	public static final String USER_CHOICE = "Your Choice:";
	public static final String RESTOCKED = "Cash restocked";
	public static final String RESTOCK_ISSUE = "Issue with restocking!";
	public static final String EXIT_APP = "Exiting application.";
	public static final String INVALID_COMMAND = "Invalid command: %s";
	public static final String INVALID_HORSE = "Invalid horse number: %s";
	public static final String INVALID_BET = "Invalid bet: %s";
	public static final String WON = "Won";
	public static final String LOST = "Lost";
	public static final String HORSE_ITEM = "%d, %s, %d, %s.";
	public static final String INSUFFICIENT_FUNDS = "Insufficient funds: $%d";
	public static final String PAYOUT = "Payout: %s, $%d";
	public static final String DISPENSING = "Dispensing:";
	public static final String DISPENSING_AMOUNT = "$%d - %d";
	public static final String NO_PAYOUT = "No Payout: %s";

	// Exception Constants
	public static final String STARTUP_FILE_EXCEPTION = "Exception while fetching startup settings data";
	public static final String CASH_PROCESS_EXCEPTION = "Exception while processing inventory data!";
	public static final String HORSE_PROCESS_EXCEPTION = "Exception while processing horses data!";
	public static final String CASH_LISTING_EXCEPTION = "Exception while listing inventory data!";
	public static final String HORSE_LISTING_EXCEPTION = "Exception while listing horse data!";
	public static final String INPUT_CHOICE_EXCEPTION = "Exception in user input - %s!";
	public static final String PROCESS_BET_EXCEPTION = "Exception while processing bet!";
	public static final String RESTOCK_EXCEPTION = "Exception while restocking!";
}
