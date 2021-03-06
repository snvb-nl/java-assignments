package com.numino.horsetrack.menu;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.numino.horsetrack.utility.Constants;
import com.numino.horsetrack.utility.ExceptionLogger;
import com.numino.horsetrack.utility.Util;

public class Menu {

	private Teller teller;
	private Scanner input = new Scanner(System.in);
	private Inventory inventory = Inventory.getInstance();
	private static Logger logger = LoggerFactory.getLogger(Menu.class);

	public Menu() {
		teller = new Teller();
	}

	public void loadMenuAndOps() {
		Util.clearScreen();

		if (inventory.getHorses().size() > 0 && inventory.getMoney().size() > 0) {
			boolean validInput = true;
			do {
				try {
					prepareMenu();
					String line = input.nextLine().toLowerCase().trim();

					Util.clearScreen();
					validInput = processUserChoice(line);
				} catch (Exception e) {
					ExceptionLogger.LogErrorAndExit(logger, e, Constants.INPUT_CHOICE_EXCEPTION);
				}
			} while (validInput);
		} else {
			Util.sopn(Constants.NO_ITEMS);
			ExceptionLogger.LogErrorAndExit(logger, null, Constants.NO_ITEMS);
		}
	}

	boolean processUserChoice(String line) {
		String[] choices = line.split(" ");
		boolean continueToAccept = true;
		try {
			if (choices.length == 1) {
				continueToAccept = processSingleInput(choices);
			} else if (choices.length == 2) {
				processDualInput(choices);
			} else {
				Util.sof(Constants.INVALID_COMMAND, line);
			}
		} catch (Exception e) {
			ExceptionLogger.LogErrorAndExit(logger, e, Constants.INPUT_CHOICE_EXCEPTION, line);
		}

		return continueToAccept;
	}

	private void prepareMenu() {
		listInventory();
		listHorses();
		Util.sopn(Constants.RESTOCK_OPTION);
		Util.sopn(Constants.QUIT_OPTION);
		Util.sof(Constants.WINNING_OPTION, inventory.getHorses().size());
		Util.sof(Constants.BET_OPTION, inventory.fetchHorses().size());
		Util.sopn(Constants.USER_CHOICE);
	}

	private void listInventory() {
		Util.sopn(Constants.INVENTORY);
		try {
			inventory.getMoney().forEach((item) -> {
				Util.sof(Constants.INVENTORY_ITEM, item.getDenomination(), item.getQuantity());
			});
		} catch (Exception e) {
			ExceptionLogger.LogErrorAndExit(logger, e, Constants.CASH_LISTING_EXCEPTION);
		}
		Util.sopn(Constants.MENU_SPLIT);
	}

	private void listHorses() {
		Util.sopn(Constants.HORSES);
		try {
			var horseCnt = inventory.getHorses().size();
			for (int cnt = 0; cnt < horseCnt; cnt++) {
				var horse = inventory.getHorses().get(cnt);
				var winLose = (cnt + 1) == inventory.getWinningHorse() ? Constants.WON : Constants.LOST;
				Util.sof(Constants.HORSE_ITEM, cnt + 1, horse.getHorseName(), horse.getOdds(), winLose);
			}
		} catch (Exception e) {
			ExceptionLogger.LogErrorAndExit(logger, e, Constants.HORSE_LISTING_EXCEPTION);
		}

		Util.sopn(Constants.MENU_SPLIT);
	}

	private boolean processSingleInput(String[] choices) {
		// Exit App
		if (choices[0].equals("q")) {
			Util.sopn(Constants.EXIT_APP);
			return false;
		}
		// Restock choice
		else if (choices[0].equals("r")) {
			teller.restock();
		}
		// Invalid input
		else {
			Util.sof(Constants.INVALID_COMMAND, StringUtils.join(choices, " "));
		}
		return true;
	}

	private void processDualInput(String[] choices) {
		// Set winning horse
		if (choices[0].equals("w")) {
			verifyAndSetWinningHorse(choices);
		}
		// Check for valid horse number
		else if (StringUtils.isNumeric(choices[0]) && (NumberUtils.toInt(choices[0]) > 0
				&& NumberUtils.toInt(choices[0]) <= inventory.getHorses().size())) {
			verifyAndPlaceBet(choices);
		}
		// Invalid horse number
		else {
			Util.sof(Constants.INVALID_HORSE, StringUtils.join(choices, " "));
		}
	}

	private void verifyAndSetWinningHorse(String[] choices) {
		// Check for correct horse number
		if (StringUtils.isNumeric(choices[1]) && (NumberUtils.toInt(choices[1]) > 0
				&& NumberUtils.toInt(choices[1]) <= inventory.getHorses().size())) {
			inventory.setWinningHorse(NumberUtils.toInt(choices[1]));
		}
		// Invalid horse number as input
		else {
			Util.sof(Constants.INVALID_HORSE, StringUtils.join(choices, " "));
		}
	}

	private void verifyAndPlaceBet(String[] choices) {
		// Check for valid bet value
		if (StringUtils.isNumeric(choices[1])) {
			teller.placeBetOnHorseNumber(NumberUtils.toInt(choices[0]), NumberUtils.toInt(choices[1]));
		}
		// Invalid bet amount
		else {
			Util.sof(Constants.INVALID_BET, StringUtils.join(choices, " "));
		}
	}

}
