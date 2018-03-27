package org.venom.scheduler.dao;


import org.venom.scheduler.report.entity.ReportSchedule;

import java.util.List;

public interface ReportDataAccess {
    void insert(ReportSchedule schedule);
    void insertBatch(List<ReportSchedule> schedules);
    List<ReportSchedule> loadAllCustomer();
    ReportSchedule findCustomerById(long id);
    String findReportNameById(long id);
    int getTotalSchedules();
}
