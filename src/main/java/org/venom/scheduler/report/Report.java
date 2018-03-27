package org.venom.scheduler.report;

public class Report {
    private final long reportID;
    private final String content;

    public Report(long reportID, String content) {
        this.reportID = reportID;
        this.content = content;
    }

    public long getReportID() {
        return reportID;
    }

    public String getContent() {
        return content;
    }
}
