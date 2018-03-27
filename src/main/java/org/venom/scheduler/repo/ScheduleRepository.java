package org.venom.scheduler.repo;

import org.venom.scheduler.report.entity.ReportSchedule;
import org.springframework.data.repository.CrudRepository;

public interface ScheduleRepository extends CrudRepository<ReportSchedule, Long> {

}
