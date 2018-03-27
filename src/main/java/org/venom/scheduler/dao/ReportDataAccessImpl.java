package org.venom.scheduler.dao;

import org.venom.scheduler.report.entity.ReportSchedule;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.List;

public class ReportDataAccessImpl extends JdbcDaoSupport implements ReportDataAccess {

    private final static String TABLE = "pbpreport.QRTZ_schedule";

    @Override
    public void insert(ReportSchedule schedule) {
        String sql = "INSERT INTO " + TABLE +
                " (id, crontext, reportName) VALUES (?, ?, ?)" ;
        getJdbcTemplate().update(sql, new Object[]{
                schedule.getId(), schedule.getCrontext(), schedule.getReportName()
        });
    }

    @Override
    public void insertBatch(List<ReportSchedule> schedules) {

    }

    @Override
    public List<ReportSchedule> loadAllCustomer() {
        return null;
    }

    @Override
    public ReportSchedule findCustomerById(long id) {
        return null;
    }

    @Override
    public String findReportNameById(long id) {
        return null;
    }

    @Override
    public int getTotalSchedules() {
        String sql = "SELECT Count(*) FROM " + TABLE ;
        int total = getJdbcTemplate().queryForObject(sql, Integer.class);
        return total;
    }
}
