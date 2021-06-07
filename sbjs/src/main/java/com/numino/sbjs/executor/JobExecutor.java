package com.numino.sbjs.executor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.numino.sbjs.config.ExecutorProperties;
import com.numino.sbjs.utility.Constants;
import com.numino.sbjs.utility.ExceptionLogger;

@Configuration
@EnableAsync
public class JobExecutor implements AsyncConfigurer {

	private static Logger logger = LoggerFactory.getLogger(JobExecutor.class);

	@Autowired
	ExecutorProperties execProps;

	@Override
	@Bean(name = "JobExecutor")
	public Executor getAsyncExecutor() {
		try {
			ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
			executor.setCorePoolSize(execProps.getCorePoolSize());
			executor.setMaxPoolSize(execProps.getMaxPoolSize());
			executor.setQueueCapacity(execProps.getQueueCapacity());
			executor.setThreadNamePrefix(execProps.getThreadPrefixName() + " - ");
			executor.initialize();

			return executor;
		} catch (Exception e) {
			ExceptionLogger.LogErrorAndExit(logger, e, Constants.EXECUTOR_INIT_EXCEPTION);
		}
		return null;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncUncaughtExceptionHandler() {
			@Override
			public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {

				System.out.println("Exception Cause - " + throwable.getMessage());
				System.out.println("Method name - " + method.getName());
				for (Object param : obj) {
					System.out.println("Parameter value - " + param);
				}
			}
		};
	}
}
