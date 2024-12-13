package org.jnu.unimart.service.impl;

import org.jnu.unimart.pojo.Report;
import org.jnu.unimart.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ReportServiceImplTest {
    @Autowired
    private ReportService reportService;
//    @Transactional
    @Test
    void createReport() {
        Report report = reportService.createReport(11, 13, 8, "有问题", new ArrayList<>());
        System.out.println(report);
    }

    @Test
    void handleReport() {
        reportService.handleReport(5,11,"completed","没问题");
    }

    @Test
    void getReportsByStatus() {
        List<Report> reports = reportService.getReportsByStatus("completed");
        for(Report report : reports)
            System.out.println(report);
    }
}