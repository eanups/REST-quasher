package org.venom.scheduler.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.Date;

public class EmailJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        System.out.println(" @@ Emailing Report at : " + new Date());
    }
}
