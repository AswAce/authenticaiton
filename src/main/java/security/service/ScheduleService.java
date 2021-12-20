package security.service;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import security.scheduler.CacheCleanJob;

@Component
public class ScheduleService implements CommandLineRunner {

    private final Scheduler scheduler;
    private final CacheService cacheService;

    public ScheduleService(CacheService cacheService) throws SchedulerException {
        this.cacheService = cacheService;
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        this.scheduler = schedulerFactory.getScheduler();
    }

    @Override
    public void run(String... args) throws Exception {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("cache", cacheService);
        JobDetail jobA = JobBuilder.newJob(CacheCleanJob.class).
                withIdentity("jobA", "group2").
                usingJobData(jobDataMap).
                build();

        Trigger triggerA = TriggerBuilder.newTrigger().
                withIdentity("triggerA", "group2").startNow().
                withPriority(15).
                withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(720).
                        repeatForever()).build();

        scheduler.scheduleJob(jobA, triggerA);
        scheduler.start();

    }
}
