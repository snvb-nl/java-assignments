package com.numino.sbjs.jobs;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncJob {

	@Async("JobExecutor")
	public CompletableFuture<String> doWork(String someText) {
		return CompletableFuture.completedFuture(someText);
	}

	@Async("JobExecutor")
	public CompletableFuture<String> doWork() throws InterruptedException {
		Thread.sleep(100);
		return CompletableFuture.completedFuture("Work done");
	}
}
