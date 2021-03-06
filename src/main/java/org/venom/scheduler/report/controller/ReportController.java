package org.venom.scheduler.report.controller;


import org.venom.scheduler.quartz.trigger.CRUDTrigger;
import org.venom.scheduler.repo.CronSchRepository;

import org.venom.scheduler.report.entity.CronSchedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping(path="/schedule")
public class ReportController {

    @Autowired
    private CronSchRepository cronSchRepository;

    private CRUDTrigger crudTrigger;


    public ReportController(ApplicationContext context) {
        crudTrigger = new CRUDTrigger(context);
    }



    @GetMapping(path="/cron")
    public @ResponseBody Iterable<CronSchedule> getAllCronSchedules() {
        return cronSchRepository.findAll();
    }



    @PostMapping("/cron")
    public @ResponseBody String createSchedule(@RequestBody CronSchedule schedule) {

        return crudTrigger.addCronTrigger(schedule);

    }


    @PostMapping("/email/{name}/{min}/{repeat}")
    public @ResponseBody String emailSchedule(@PathVariable String name, @PathVariable int min,
                                              @PathVariable int repeat) {
        return crudTrigger.addEmailTrigger(name +"_time=" + min + "_rep=" + repeat, min, repeat);
    }

    @DeleteMapping("/cron/{triggerName}")
    public @ResponseBody String deleteCronSchedule(@PathVariable String triggerName) {
        return crudTrigger.deleteCronTrigger(triggerName);
    }

    @DeleteMapping("/cron/all")
    public @ResponseBody String deleteSchedules() {
        return crudTrigger.deleteAllJobs();
    }


    @PutMapping("/cron/{triggerName}")
    public @ResponseBody String updateSchedule(@RequestBody CronSchedule schedule,
                                               @PathVariable String triggerName) {
        CronSchedule scheduleFound = cronSchRepository.findOne(schedule.getTriggerName());
        if (scheduleFound == null)
            return "ERROR: no matching triggerName";
        return crudTrigger.updateCronTrigger(triggerName, schedule);
    }



}
