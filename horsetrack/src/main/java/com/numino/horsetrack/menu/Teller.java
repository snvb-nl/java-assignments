package com.numino.horsetrack.menu;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.numino.horsetrack.models.Cash;
import com.numino.horsetrack.models.Horse;
import com.numino.horsetrack.utility.CashDispenser;
import com.numino.horsetrack.utility.Constants;
import com.numino.horsetrack.utility.ExceptionLogger;
import com.numino.horsetrack.utility.Util;

public class Teller {

	private static Logger logger = LoggerFactory.getLogger(Teller.class);
	private Inventory inventory = Inventory.getInstance();

	void restock() {
		try {
			if (inventory.getMoney().size() > 0) {
				for (Cash item : inventory.getMoney()) {
					item.setQuantity(10);
				}
				Util.lnp(logger, Constants.RESTOCKED);
			}
		} catch (Exception e) {
			ExceptionLogger.LogErrorAndExit(logger, e, Constants.RESTOCK_EXCEPTION);
			Util.sopn(Constants.RESTOCK_ISSUE);
		}
	}

	void placeBetOnHorseNumber(int horseNumber, int betAmount) {
		Horse horse = inventory.fetchHorses().get(horseNumber - 1);
		if (horseNumber == inventory.getWinningHorse()) {
			int totalCash = 0;
			for (Cash item : inventory.getMoney()) {
				totalCash = totalCash + (item.getDenomination() * item.getQuantity());
			}
			var payoutAmount = horse.getOdds() * betAmount;

			int[] notes = inventory.getMoney().stream().mapToInt(item -> item.getDenomination()).toArray();
			int[] noteCount = inventory.getMoney().stream().mapToInt(item -> item.getQuantity()).toArray();

			List<Integer[]> results = CashDispenser.calculateDispensingSet(notes, noteCount, new int[5], payoutAmount,
					0);
			if (results.size() == 0) {
				Util.sof(Constants.INSUFFICIENT_FUNDS, payoutAmount);
			} else {
				dispensePayout(results.get(results.size() - 1), notes, payoutAmount, horse);
			}
		} else {
			Util.sof(Constants.NO_PAYOUT, horse.getHorseName());
		}
	}

	private void dispensePayout(Integer[] fundsInDenomination, int[] notes, int payoutAmount, Horse horse) {
		for (int cnt = 0; cnt < fundsInDenomination.length; cnt++) {
			inventory.getMoney().get(cnt)
					.setQuantity(inventory.getMoney().get(cnt).getQuantity() - fundsInDenomination[cnt]);
		}
		Util.sof(Constants.PAYOUT, horse.getHorseName(), payoutAmount);
		Util.sopn(Constants.DISPENSING);
		for (int cnt = 0; cnt < notes.length; cnt++) {
			if (fundsInDenomination[cnt] != 0)
				Util.sof(Constants.DISPENSING_AMOUNT, notes[cnt], fundsInDenomination[cnt]);
		}
	}

}
