package org.venom.scheduler.quartz.job;


import org.quartz.JobDetail;
import static org.quartz.JobBuilder.newJob;

public class JobFactory {

    private static final String GROUP = "report";

    private static final String EMAIL = "email";
    private static final String FINALIZE = "finalize";


    public static JobDetail getJobInstance(String jobName) {
        JobDetail jobDetail;
        if (jobName.toLowerCase().contains(EMAIL)){
             jobDetail = newJob(EmailJob.class)
                .withIdentity(jobName, GROUP)
                    .build();
        } else if (jobName.toLowerCase().contains(FINALIZE)) {
            jobDetail = newJob(ReportJob.class)
                    .withIdentity(jobName, GROUP)
                    .build();
        } else {
            throw new AssertionError("Invalid JOB !");
        }
        return jobDetail;
    }

}
