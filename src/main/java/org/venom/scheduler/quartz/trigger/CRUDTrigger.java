package org.venom.scheduler.quartz.trigger;

import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.venom.scheduler.quartz.job.JobFactory;
import org.venom.scheduler.quartz.job.ReportJob;
import org.venom.scheduler.report.entity.CronSchedule;

import java.util.TimeZone;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.TriggerKey.triggerKey;

public class CRUDTrigger {

    private Scheduler scheduler;

    private static final String GROUP = "report";

    public CRUDTrigger(ApplicationContext context) {
        scheduler = (Scheduler) context.getBean("schedulerFactoryBean");
    }

    public String addEmailTrigger(String name, int minutes, int repeatCount) {

        String message = "Email Scheduled job for : " + name;

        JobDetail emailJob = JobFactory.getJobInstance(name+"_email");

        Trigger trigger = newTrigger()
                .withIdentity(name, GROUP)
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInMinutes(minutes)
                        .withRepeatCount(repeatCount))
                .build();

        emailJob.getJobDataMap().put(ReportJob.NAME, name);

        try {
            scheduler.scheduleJob(emailJob, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            message = "ERROR :" + e.getMessage();
        }
        return message;

    }


    public String addCronTrigger(CronSchedule schedule) {

        //cronExpression = "0 40 00 16 MAR ? 2018";
        final String name = schedule.getTriggerName();
        final String zone = schedule.getTimeZoneId();
        final String cron = schedule.getCronExpression();
        String message = "Scheduled Cron Report at timezone: " + zone;

        JobDetail finalizeJob = JobFactory.getJobInstance(name+"_finalize");

        CronTrigger trigger = newTrigger()
                .withIdentity(name, GROUP)
                .withSchedule(cronSchedule(cron)
                        .inTimeZone(TimeZone.getTimeZone(zone)))
                .forJob(finalizeJob.getKey())
                .build();

        finalizeJob.getJobDataMap().put(ReportJob.NAME, name);

        try {
            scheduler.scheduleJob(finalizeJob, trigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
            message = "ERROR: " + e.getMessage();
        }

        return message;

    }

    public String updateCronTrigger(String oldName, CronSchedule schedule) {
        final String name = schedule.getTriggerName();
        final String zone = schedule.getTimeZoneId();
        final String cron = schedule.getCronExpression();
        String message = "Updated " + name + " with the schedule : " + cron + " : " + zone;
        Trigger oldTrigger;
        try {
            oldTrigger = scheduler.getTrigger(triggerKey(oldName, GROUP));
            TriggerBuilder tb = oldTrigger.getTriggerBuilder();

            Trigger newTrigger = tb.withSchedule(cronSchedule(cron).
                    inTimeZone(TimeZone.getTimeZone(zone)))
                    .build();

            //@TODO Check for ERRORs
            //scheduler.getJobDetail(JobKey.jobKey(name+"_finalize")).getJobDataMap().put(ReportJob.NAME, name);

            scheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            message = "ERROR : " + e.getMessage();
        }
        return message;

    }


    public  String deleteCronTrigger(String name) {
        String message = "Delete performed on trigger: " + name;
        try {
            scheduler.unscheduleJob(triggerKey(name, GROUP));
        } catch (SchedulerException e) {
            e.printStackTrace();
            message = "ERROR: Deleting trigger with Name: " + name;
        }
        return message;
    }

    public String deleteAllJobs() {
        String message = "BOOM: Deleting all Jobs and Triggers !! ";
        try {
            scheduler.clear();

        } catch (SchedulerException e) {
            e.printStackTrace();
            message = "ERROR: Deleting all jobs and triggers ";
        }
        return message;
    }
}
