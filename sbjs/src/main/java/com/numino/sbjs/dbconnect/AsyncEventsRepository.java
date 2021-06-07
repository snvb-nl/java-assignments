package com.numino.sbjs.dbconnect;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.numino.sbjs.dbmodel.AsyncEvents;

public interface AsyncEventsRepository extends CrudRepository<AsyncEvents, String> {

	@Query(value = "select * from asynch_events", nativeQuery = true)
	public List<AsyncEvents> getAllAsyncEvents();
}
