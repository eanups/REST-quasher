package org.venom.scheduler.quartz.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.util.Date;

public class EmailJob implements Job, JobConstants {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDataMap map = jobExecutionContext.getJobDetail().getJobDataMap();
        String name = map.getString(NAME);
        System.out.println(" @@ Emailing Report  [ " + name +
                " ]  at: " + new Date());
    }
}
