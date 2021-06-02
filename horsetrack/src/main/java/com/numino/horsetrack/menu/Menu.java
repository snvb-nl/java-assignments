package com.numino.horsetrack.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.numino.horsetrack.models.Horse;
import com.numino.horsetrack.models.Inventory;
import com.numino.horsetrack.utility.Constants;
import com.numino.horsetrack.utility.Util;

public class Menu {

	private Util util;
	private List<Horse> horses;
	private List<Inventory> inventory;
	private int winnerHorse;
	private Scanner input = new Scanner(System.in);

	public Menu() {
		winnerHorse = 1;
		util = new Util();
		horses = fetchHorses();
		inventory = fetchInventory();
	}

	Menu(Util util) {
		this.util = util;
		horses = fetchHorses();
		inventory = fetchInventory();
	}

	public void loadMenuAndOps() {
		Util.clearScreen();

		if (this.horses.size() > 0 && this.inventory.size() > 0) {
			boolean validInput = true;
			do {
				try {
					prepareMenu();
					String line = input.nextLine().toLowerCase().trim();

					Util.clearScreen();
					validInput = processUserChoice(line);
				} catch (Exception e) {
					Util.f(Constants.INVALID_COMMAND, "");
				}
			} while (validInput);
		} else {
			Util.ln(Constants.NO_ITEMS);
		}
	}

	List<Horse> getHorses() {
		return horses;
	}

	List<Inventory> getInventory() {
		return inventory;
	}

	int getWinnerHorse() {
		return winnerHorse;
	}

	void prepareMenu() {
		listInventory();
		listHorses();
		Util.ln(Constants.RESTOCK_OPTION);
		Util.ln(Constants.QUIT_OPTION);
		Util.f(Constants.WINNING_OPTION, horses.size());
		Util.f(Constants.BET_OPTION, horses.size());
		Util.ln(Constants.USER_CHOICE);
	}

	boolean processUserChoice(String line) {
		String[] choices = line.split(" ");
		boolean validInput = true;
		if (choices.length == 1) {
			if (choices[0].equals("r")) {
				restock();
			} else if (choices[0].equals("q")) {
				Util.ln(Constants.EXIT_APP);
				validInput = false;
			} else {
				Util.f(Constants.INVALID_COMMAND, line);
			}
		} else if (choices.length == 2) {
			if (choices[0].equals("w")) {
				if (StringUtils.isNumeric(choices[1])
						&& (NumberUtils.toInt(choices[1]) > 0 && NumberUtils.toInt(choices[1]) <= horses.size())) {
					winnerHorse = NumberUtils.toInt(choices[1]);
				} else {
					Util.f(Constants.INVALID_HORSE, line);
				}
			} else if (StringUtils.isNumeric(choices[0])
					&& (NumberUtils.toInt(choices[0]) > 0 && NumberUtils.toInt(choices[0]) <= horses.size())) {
				if (StringUtils.isNumeric(choices[1])) {
					makeBetOnHorseNumber(NumberUtils.toInt(choices[0]), NumberUtils.toInt(choices[1]));
				} else {
					Util.f(Constants.INVALID_BET, line);
				}
			} else {
				Util.f(Constants.INVALID_HORSE, line);
			}
		} else {
			Util.f(Constants.INVALID_COMMAND, line);
		}
		return validInput;
	}

	void restock() {
		try {
			if (inventory.size() > 0) {
				for (Inventory item : inventory) {
					item.setQuantity(10);
				}
				Util.ln(Constants.RESTOCKED);
			}
		} catch (Exception e) {
			Util.ln(Constants.RESTOCK_ISSUE);
		}
	}

	private void listInventory() {
		Util.ln(Constants.INVENTORY);
		inventory.forEach((item) -> {
			Util.f(Constants.INVENTORY_ITEM, item.getDenomination(), item.getQuantity());
		});
		Util.ln(Constants.MENU_SPLIT);
	}

	private void listHorses() {
		Util.ln(Constants.HORSES);
		var horseCnt = horses.size();
		for (int cnt = 0; cnt < horseCnt; cnt++) {
			var horse = horses.get(cnt);
			var winLose = (cnt + 1) == winnerHorse ? Constants.WON : Constants.LOST;
			Util.f(Constants.HORSE_ITEM, cnt + 1, horse.getHorseName(), horse.getOdds(), winLose);
		}
		Util.ln(Constants.MENU_SPLIT);
	}

	private List<Horse> fetchHorses() {
		try {
			var horsesText = util.fetchStartupValues();
			var horsesJson = JsonParser.parseString(horsesText).getAsJsonObject();
			var horsesArray = horsesJson.get("horses");

			Gson gson = new Gson();
			return gson.fromJson(horsesArray, new TypeToken<ArrayList<Horse>>() {
			}.getType());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new ArrayList<Horse>();
	}

	private List<Inventory> fetchInventory() {
		try {
			var inventoryText = util.fetchStartupValues();
			var inventoryJson = JsonParser.parseString(inventoryText).getAsJsonObject();
			var inventoryArray = inventoryJson.get("inventory");

			Gson gson = new Gson();
			return gson.fromJson(inventoryArray, new TypeToken<List<Inventory>>() {
			}.getType());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new ArrayList<Inventory>();
	}

	private void makeBetOnHorseNumber(int horseNumber, int betAmount) {
		Horse horse = horses.get(horseNumber - 1);
		if (horseNumber == winnerHorse) {
			int totalCash = 0;
			for (Inventory item : inventory) {
				totalCash = totalCash + (item.getDenomination() * item.getQuantity());
			}
			var payoutAmount = horse.getOdds() * betAmount;

			int[] notes = inventory.stream().mapToInt(item -> item.getDenomination()).toArray();
			int[] noteCount = inventory.stream().mapToInt(item -> item.getQuantity()).toArray();

			List<Integer[]> results = calculateDispensingSet(notes, noteCount, new int[5], payoutAmount, 0);
			if (results.size() == 0) {
				Util.f(Constants.INSUFFICIENT_FUNDS, payoutAmount);
			} else {
				Integer[] set = results.get(results.size() - 1);
				for (int cnt = 0; cnt < set.length; cnt++) {
					inventory.get(cnt).setQuantity(inventory.get(cnt).getQuantity() - set[cnt]);
				}
				Util.f(Constants.PAYOUT, horse.getHorseName(), payoutAmount);
				Util.ln(Constants.DISPENSING);
				for (int cnt = 0; cnt < notes.length; cnt++) {
					if (set[cnt] != 0)
						Util.f(Constants.DISPENSING_AMOUNT, notes[cnt], set[cnt]);
				}
			}
		} else {
			Util.f(Constants.NO_PAYOUT, horse.getHorseName());
		}
	}

	private List<Integer[]> calculateDispensingSet(int[] notes, int[] noteCount, int[] variation, int price,
			int position) {
		List<Integer[]> list = new ArrayList<>();
		int value = compute(notes, variation);
		if (value < price) {
			for (int i = position; i < notes.length; i++) {
				if (noteCount[i] > variation[i]) {
					int[] newvariation = variation.clone();
					newvariation[i]++;
					List<Integer[]> newList = calculateDispensingSet(notes, noteCount, newvariation, price, i);
					if (newList != null) {
						list.addAll(newList);
					}
				}
			}
		} else if (value == price) {
			list.add(myCopy(variation));
		}
		return list;
	}

	private int compute(int[] values, int[] variation) {
		int ret = 0;
		for (int i = 0; i < variation.length; i++) {
			ret += values[i] * variation[i];
		}
		return ret;
	}

	private Integer[] myCopy(int[] ar) {
		Integer[] ret = new Integer[ar.length];
		for (int i = 0; i < ar.length; i++) {
			ret[i] = ar[i];
		}
		return ret;
	}
}
