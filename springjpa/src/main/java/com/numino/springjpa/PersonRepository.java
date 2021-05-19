package com.numino.springjpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Long> {
	List<Person> findByName(String name);
}