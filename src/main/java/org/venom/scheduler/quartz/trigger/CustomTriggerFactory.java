package org.venom.scheduler.quartz.trigger;

import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

import java.util.Date;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.venom.scheduler.quartz.trigger.CRUDTrigger.GROUP;

public class CustomTriggerFactory {

    /**
     * Gets a simple trigger
     * @return Simple Trigger instance
     */
    public static Trigger getSimpleTrigger(String triggerName, String jobName, Date time) {
        SimpleTrigger trigger = (SimpleTrigger) newTrigger()
                .withIdentity(triggerName, "report")
                .startAt(time) // some Date
                .forJob(jobName, "report") // identify job with name, group strings
                .build();
        return trigger;
    }

    static Trigger getMinutesTrigger(String triggerName, int minutes, int repeatCount) {
        return newTrigger()
                .withIdentity(triggerName, GROUP)
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInMinutes(minutes)
                        .withRepeatCount(repeatCount))
                .build();
    }

    static Trigger getSecondsTrigger(String triggerName, int seconds, int repeatCount) {
        return newTrigger()
                .withIdentity(triggerName, GROUP)
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(seconds)
                        .withRepeatCount(repeatCount))
                .build();
    }

}
