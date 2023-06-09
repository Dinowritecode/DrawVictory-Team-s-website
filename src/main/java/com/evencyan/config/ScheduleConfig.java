package com.evencyan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ScheduleConfig {

    @Bean(name = "genericTaskScheduler")
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);                        // 线程池大小
        scheduler.setThreadNamePrefix("genericTaskScheduler-");   // 线程名称
        scheduler.setAwaitTerminationSeconds(60);         // 等待时长
        scheduler.setWaitForTasksToCompleteOnShutdown(true);// 调度器shutdown被调用时等待当前被调度的任务完成
        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return scheduler;
    }
}