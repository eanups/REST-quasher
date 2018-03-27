package org.venom.scheduler.report.controller;


import org.venom.scheduler.quartz.job.JobFactory;

import org.venom.scheduler.quartz.job.ReportJob;
import org.venom.scheduler.repo.CronSchRepository;
import org.venom.scheduler.repo.ScheduleRepository;
import org.venom.scheduler.report.entity.CronSchedule;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.TimeZone;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.TriggerKey.triggerKey;


@Controller
@RequestMapping(path="/schedule")
public class ReportController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CronSchRepository cronSchRepository;

    private Scheduler scheduler;


    private static final String GROUP = "report";

    public ReportController(ApplicationContext context) {
        scheduler = (Scheduler) context.getBean("schedulerFactoryBean");
    }

    private String addEmailTrigger(String name, int minutes, int repeatCount) {

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


    private String addCronTrigger(CronSchedule schedule) {

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

    private String updateCronTrigger(String oldName, CronSchedule schedule) {
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


    private String deleteCronTrigger(String name) {
        String message = "Delete performed on trigger: " + name;
        try {
            scheduler.unscheduleJob(triggerKey(name, GROUP));
        } catch (SchedulerException e) {
            e.printStackTrace();
            message = "ERROR: Deleting trigger with Name: " + name;
        }
        return message;
    }

    private String deleteAllJobs() {
        String message = "BOOM: Deleting all Jobs and Triggers !! ";
        try {
            scheduler.clear();

        } catch (SchedulerException e) {
            e.printStackTrace();
            message = "ERROR: Deleting all jobs and triggers ";
        }
        return message;
    }


    @GetMapping(path="/cron")
    public @ResponseBody Iterable<CronSchedule> getAllCronSchedules() {
        return cronSchRepository.findAll();
    }



    @PostMapping("/cron")
    public @ResponseBody String createSchedule(@RequestBody CronSchedule schedule) {

        return addCronTrigger(schedule);

        //URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{triggerName}")
        //.buildAndExpand(message).toUri();
        //return ResponseEntity.created(location).build();

    }


    @PostMapping("/email/every/{min}/{repeat}")
    public @ResponseBody String emailSchedule(@PathVariable int min, @PathVariable int repeat) {
        return addEmailTrigger("every_" + min + repeat, min, repeat);
    }

    @DeleteMapping("/cron/{triggerName}")
    public @ResponseBody String deleteCronSchedule(@PathVariable String triggerName) {
        //cronSchRepository.delete(triggerName);
        return deleteCronTrigger(triggerName);
    }

    @DeleteMapping("/cron/all")
    public @ResponseBody String deleteSchedules() {
        return deleteAllJobs();
    }



    @PutMapping("/cron/{triggerName}")
    public @ResponseBody String updateSchedule(@RequestBody CronSchedule schedule,
                                               @PathVariable String triggerName) {
        CronSchedule scheduleFound = cronSchRepository.findOne(schedule.getTriggerName());
        if (scheduleFound == null)
            return "ERROR: no matching triggerName";
        return updateCronTrigger(triggerName, schedule);
        //return ResponseEntity.noContent().build();
    }



}
