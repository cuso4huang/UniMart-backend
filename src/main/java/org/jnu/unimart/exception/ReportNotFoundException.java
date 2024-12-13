package org.jnu.unimart.exception;

public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException(String reportNotFound) {
        super(reportNotFound);
    }
}
