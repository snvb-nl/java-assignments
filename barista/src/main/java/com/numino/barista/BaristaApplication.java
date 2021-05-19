package com.numino.barista;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BaristaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BaristaApplication.class, args);
	}

	@Override
	public void run(String... args) {
		var menu = new Menu();
		menu.loadMenuAndOps();
	}
}
