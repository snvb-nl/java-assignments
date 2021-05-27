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
				prepareMenu();
				String line = input.nextLine();

				Util.clearScreen();
				validInput = processUserChoice(line);
			} while (validInput);
		} else {
			Util.ln((Object) "No horses or inventory set.");
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
		Util.ln("Inventory:");
		inventory.forEach((item) -> {
			Util.ln("$" + item.getDenomination() + ", " + item.getQuantity());
		});
		Util.ln("==========================");
		Util.ln("Horses:");
		var horseCnt = horses.size();
		for (int cnt = 0; cnt < horseCnt; cnt++) {
			var horse = horses.get(cnt);
			var winLose = (cnt + 1) == winnerHorse ? "won" : "lost";
			System.out.printf("%d, %s, %d, %s.\n", cnt + 1, horse.getHorseName(), horse.getOdds(), winLose);
		}
		Util.ln("==========================");
		Util.ln("Press R to Restock the cash inventory");
		Util.ln("Press Q to Quit");
		Util.ln("Type W [1 to " + horseCnt + "] to set wining horse number");
		Util.ln("[1 to " + horseCnt + "] <amount> specifies the horse wagered on and the amount of the bet");
		Util.ln("Your choice: ");
	}

	boolean processUserChoice(String line) {
		String[] choices = line.split(" ");
		boolean validInput = true;
		if (choices.length == 1) {
			if (choices[0].trim().toLowerCase().equals("r")) {
				if (restock()) {
					Util.ln("Cash restocked.");
				} else {
					Util.ln("Issue with restocking!");
				}
			} else if (choices[0].trim().toLowerCase().equals("q")) {
				Util.ln("Exiting application.");
				validInput = false;
			} else {
				Util.ln("Invalid command: " + line);
			}
		} else if (choices.length == 2) {
			if (choices[0].trim().toLowerCase().equals("w")) {
				if (StringUtils.isNumeric(choices[1])
						&& (NumberUtils.toInt(choices[1]) > 0 && NumberUtils.toInt(choices[1]) <= horses.size())) {
					winnerHorse = NumberUtils.toInt(choices[1]);
				} else {
					Util.ln("Invalid horse number: " + line);
				}
			} else if (StringUtils.isNumeric(choices[0])
					&& (NumberUtils.toInt(choices[0]) > 0 && NumberUtils.toInt(choices[0]) <= horses.size())) {
				if (StringUtils.isNumeric(choices[1])) {
					makeBetOnHorseNumber(NumberUtils.toInt(choices[0]), NumberUtils.toInt(choices[1]));
				} else {
					Util.ln("Invalid bet: " + line);
				}
			} else {
				Util.ln("Invalid horse number: " + line);
			}
		} else {
			Util.ln("Invalid command: " + line);
		}
		return validInput;
	}

	boolean restock() {
		try {
			if (inventory.size() > 0) {
				for (Inventory item : inventory) {
					item.setQuantity(10);
				}
				return true;
			}
		} catch (Exception e) {
			return false;
		}

		return false;
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
				Util.ln("Insufficient funds: $" + payoutAmount);
			} else {
				Integer[] set = results.get(results.size() - 1);
				for (int cnt = 0; cnt < set.length; cnt++) {
					inventory.get(cnt).setQuantity(inventory.get(cnt).getQuantity() - set[cnt]);
				}
				Util.ln("Payout: " + horse.getHorseName() + ", $" + payoutAmount);
				Util.ln("Dispensing:");
				for (int cnt = 0; cnt < notes.length; cnt++) {
					if (set[cnt] != 0)
						Util.ln("$" + notes[cnt] + " - " + set[cnt]);
				}
			}
		} else {
			Util.ln("No Payout: " + horse.getHorseName());
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
