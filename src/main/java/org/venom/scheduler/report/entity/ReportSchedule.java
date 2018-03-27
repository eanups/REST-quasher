package org.venom.scheduler.report.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "QrtzReportSchedule")
public class ReportSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String reportName;

    private String crontext;


    public void setId(long id) {
        this.id = id;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public void setCrontext(String crontext) {
        this.crontext = crontext;
    }

    public long getId() {
        return id;
    }

    public String getReportName() {
        return reportName;
    }

    public String getCrontext() {
        return crontext;
    }
}
