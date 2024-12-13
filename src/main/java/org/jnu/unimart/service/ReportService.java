package org.jnu.unimart.service;

import jakarta.transaction.Transactional;
import org.jnu.unimart.pojo.Report;

import java.util.List;

public interface ReportService {

    @Transactional
    Report createReport(Integer reporterUserId, Integer accusedUserId, Integer transactionId, String description, List<String> imageUrls);

    @Transactional
    Report handleReport(Integer reportId, Integer adminUserId, String status, String adminComment);

    List<Report> getReportsByStatus(String status);

    List<Report> getAllReports();

    Report getReportById(Integer id);
}
