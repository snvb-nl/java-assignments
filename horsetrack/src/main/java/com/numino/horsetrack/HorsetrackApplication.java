package com.numino.horsetrack;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.numino.horsetrack.menu.Menu;

@SpringBootApplication
public class HorsetrackApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(HorsetrackApplication.class, args);
	}

	@Override
	public void run(String... args) {
		var menu = new Menu();
		menu.loadMenuAndOps();
	}
}
