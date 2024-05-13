package edu.hitsz.bim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;

@Component
public class DynamicScheduler {

    private final TaskScheduler scheduler;
    private ScheduledFuture<?> scheduledFuture;

    @Autowired
    private DynamicPollingService pollingService;

    @Autowired
    public DynamicScheduler(@Qualifier("taskScheduler") TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void startPolling(String uuid) {
        if (scheduledFuture == null || scheduledFuture.isCancelled()) {
            // 注意这里使用了 Lambda 表达式，并正确地传递了 uuid 参数
            scheduledFuture = scheduler.schedule(
                    () -> pollingService.pollOperation(uuid),
                    new PeriodicTrigger(10, TimeUnit.SECONDS)
            );
        }
    }

    public void stopPolling() {
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(false);
        }
    }
}
