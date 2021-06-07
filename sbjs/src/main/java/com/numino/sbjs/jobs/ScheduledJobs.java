package com.numino.sbjs.jobs;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import com.numino.sbjs.dbconnect.AsyncEventsRepository;
import com.numino.sbjs.dbmodel.AsyncEvents;

@Service
public class ScheduledJobs {
	private static Logger logger = LoggerFactory.getLogger(ScheduledJobs.class);

	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Autowired
	AsyncEventsRepository repo;

	@PostConstruct
	public void scheduleTasks() {
		threadPoolTaskScheduler.scheduleWithFixedDelay((new Runnable() {
			public void run() {
				dynamicTasksScheduleThreadPool();
			}
		}), 5000);
	}

	public void dynamicTasksScheduleThreadPool() {
		logger.info("Job started at - " + new Date());
		List<AsyncEvents> y = repo.getAllAsyncEvents();

		System.out.println("1 - " + repo.count() + " - " + new Date());
		System.out.println("2 - " + y.get(0).getEvent_type());
		System.out.println("3 - " + y.get(0).getEvent_subtype());

		logger.info("Job ended at - " + new Date());
	}

}
