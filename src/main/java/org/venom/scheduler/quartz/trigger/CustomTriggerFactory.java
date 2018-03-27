package org.venom.scheduler.quartz.trigger;

import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

import java.util.Date;

import static org.quartz.TriggerBuilder.newTrigger;

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
}
