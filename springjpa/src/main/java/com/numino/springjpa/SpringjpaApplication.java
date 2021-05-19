package com.numino.springjpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringjpaApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringjpaApplication.class);

	@Autowired
    private PersonRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(SpringjpaApplication.class, args);
	}

	@Override
	public void run(String... args) {
		log.info("Starting Application...");

		repository.save(new Person("SRI"));
        repository.save(new Person("AKS"));
        repository.save(new Person("VBV"));

        System.out.println("\nfindAll()");
        repository.findAll().forEach(x -> System.out.println(x));

        System.out.println("\nfindById(1L)");
        repository.findById(1l).ifPresent(x -> System.out.println(x));

        System.out.println("\nfindByName('Node')");
        repository.findByName("Node").forEach(x -> System.out.println(x));
	}
}
