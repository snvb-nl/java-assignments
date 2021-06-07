package com.numino.sbjs.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.numino.sbjs.config.ExecutorProperties;
import com.numino.sbjs.jobs.ScheduledJobs;
import com.numino.sbjs.utility.Constants;

@Configuration
@EnableScheduling
public class PollScheduler implements SchedulingConfigurer {

	private static Logger logger = LoggerFactory.getLogger(PollScheduler.class);

	@Autowired
	ExecutorProperties execProps;

	@Autowired
	ScheduledJobs jobs;

	@Bean(name = "PollScheduler")
	public ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(5);
		scheduler.setThreadNamePrefix("dispatch-");
		scheduler.setAwaitTerminationSeconds(600);
		scheduler.setErrorHandler(throwable -> logger.error(Constants.SCHEDULE_TASK_EXCEPTION, throwable));
		scheduler.setWaitForTasksToCompleteOnShutdown(true);
		return scheduler;
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setTaskScheduler(taskScheduler());
	}

}
