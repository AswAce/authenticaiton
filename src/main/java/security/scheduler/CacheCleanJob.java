package security.scheduler;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import security.service.CacheService;

public class CacheCleanJob implements Job {

    @Override
    public void execute(JobExecutionContext arg0) {
        JobDataMap dataMap = arg0.getJobDetail().getJobDataMap();
        CacheService cacheService = (CacheService) dataMap.get("cache");
        cacheService.removeExpiresTokens();
    }
}
