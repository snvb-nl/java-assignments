package com.numino.horsetrack.menu;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.numino.horsetrack.models.Cash;
import com.numino.horsetrack.models.Horse;
import com.numino.horsetrack.utility.Constants;
import com.numino.horsetrack.utility.ExceptionLogger;
import com.numino.horsetrack.utility.Util;

public class Inventory {

	private Util util;
	private List<Cash> money;
	private List<Horse> horses;
	private int winningHorse;
	private static Logger logger = LoggerFactory.getLogger(Inventory.class);

	private static class InventorySingleton {
		private static final Inventory INSTANCE = new Inventory();
	}

	private Inventory() {
		winningHorse = 1;
		util = new Util();
		money = fetchMoney();
		horses = fetchHorses();
	}

	public static Inventory getInstance() {
		return InventorySingleton.INSTANCE;
	}

	public List<Cash> getMoney() {
		return money;
	}

	public void setMoney(List<Cash> money) {
		this.money = money;
	}

	public List<Horse> getHorses() {
		return horses;
	}

	public void setHorses(List<Horse> horses) {
		this.horses = horses;
	}

	int getWinningHorse() {
		return winningHorse;
	}

	void setWinningHorse(int winningHorse) {
		this.winningHorse = winningHorse;
	}

	List<Cash> fetchMoney() {
		try {
			var moneyText = util.fetchStartupValues();
			var moneyJson = JsonParser.parseString(moneyText).getAsJsonObject();
			var moneyArray = moneyJson.get("money");

			Gson gson = new Gson();
			List<Cash> money = gson.fromJson(moneyArray, new TypeToken<ArrayList<Cash>>() {
			}.getType());
			return money == null ? new ArrayList<Cash>() : money;
		} catch (Exception e) {
			ExceptionLogger.LogErrorAndExit(logger, e, Constants.CASH_PROCESS_EXCEPTION);
		}
		return new ArrayList<Cash>();
	}

	List<Horse> fetchHorses() {
		try {
			var horsesText = util.fetchStartupValues();
			var horsesJson = JsonParser.parseString(horsesText).getAsJsonObject();
			var horsesArray = horsesJson.get("horses");

			Gson gson = new Gson();
			List<Horse> horses = gson.fromJson(horsesArray, new TypeToken<List<Horse>>() {
			}.getType());
			return horses == null ? new ArrayList<Horse>() : horses;
		} catch (Exception e) {
			ExceptionLogger.LogErrorAndExit(logger, e, Constants.HORSE_PROCESS_EXCEPTION);
		}
		return new ArrayList<Horse>();
	}
}
