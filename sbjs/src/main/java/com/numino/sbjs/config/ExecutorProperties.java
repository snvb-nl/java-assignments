package com.numino.sbjs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// TODO: work on immutable properties if possible - constructorbinding
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "executor")
public class ExecutorProperties {

	private int corePoolSize = 10;

	private int maxPoolSize = 10;

	private int queueCapacity = 100;

	private String threadPrefixName = "sbjs_prefix";

	public String getThreadPrefixName() {
		return threadPrefixName;
	}

	public void setThreadPrefixName(String threadPrefixName) {
		this.threadPrefixName = threadPrefixName;
	}

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getQueueCapacity() {
		return queueCapacity;
	}

	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}
}
