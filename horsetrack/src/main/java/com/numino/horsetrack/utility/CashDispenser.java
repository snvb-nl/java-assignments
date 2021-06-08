package com.numino.horsetrack.utility;

import java.util.ArrayList;
import java.util.List;

public class CashDispenser {

	public static List<Integer[]> calculateDispensingSet(int[] notes, int[] noteCount, int[] variation, int price,
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

	private static int compute(int[] values, int[] variation) {
		int ret = 0;
		for (int i = 0; i < variation.length; i++) {
			ret += values[i] * variation[i];
		}
		return ret;
	}

	private static Integer[] myCopy(int[] ar) {
		Integer[] ret = new Integer[ar.length];
		for (int i = 0; i < ar.length; i++) {
			ret[i] = ar[i];
		}
		return ret;
	}

}
