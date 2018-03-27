package org.venom.scheduler.repo;

import org.venom.scheduler.report.entity.CronSchedule;
import org.springframework.data.repository.CrudRepository;

public interface CronSchRepository extends CrudRepository<CronSchedule, String> {


}
