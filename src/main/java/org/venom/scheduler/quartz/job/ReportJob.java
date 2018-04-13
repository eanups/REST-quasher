package org.venom.scheduler.quartz.job;

import org.quartz.Job;
import org.quartz.JobDataMap;

import org.quartz.JobExecutionContext;


import java.util.Date;

public class ReportJob implements Job, JobConstants {


    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap map = context.getJobDetail().getJobDataMap();
        String name = map.getString(NAME);
        System.out.println(" ## Report Scheduler Job [ " + name +
                " ] ##\tFired at: " + new Date());
    }
}
