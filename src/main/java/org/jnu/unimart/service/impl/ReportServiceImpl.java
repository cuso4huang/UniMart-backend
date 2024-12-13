package org.jnu.unimart.service.impl;

import jakarta.transaction.Transactional;
import org.jnu.unimart.exception.ReportNotFoundException;
import org.jnu.unimart.pojo.Report;
import org.jnu.unimart.pojo.ReportImage;
import org.jnu.unimart.repository.ReportRepository;
import org.jnu.unimart.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportRepository reportRepository;

    /**
     * 创建举报表
     *
     * @param reporterUserId
     * @param accusedUserId
     * @param transactionId
     * @param description
     * @param imageUrls
     * @return
     */
    @Transactional
    @Override
    public Report createReport(Integer reporterUserId, Integer accusedUserId, Integer transactionId, String description, List<String> imageUrls) {
        Report report = new Report();
        report.setReporterUserId(reporterUserId);
        report.setAccusedUserId(accusedUserId);
        report.setTransactionId(transactionId);
        report.setDescription(description);
        report.setStatus("pending");
        Report saved = reportRepository.save(report);

        if (imageUrls != null && !imageUrls.isEmpty()) {
            List<ReportImage> images = imageUrls.stream().map(url -> {
                ReportImage img = new ReportImage();
                img.setReport(saved);
                img.setImageUrl(url);
                return img;
            }).toList();
            saved.setImages(images);
            reportRepository.save(saved);
        }

        return saved;
    }

    /**
     * 处理举报表
     * @param reportId
     * @param adminUserId
     * @param status
     * @param adminComment
     * @return
     */
    @Transactional
    @Override
    public Report handleReport(Integer reportId, Integer adminUserId, String status, String adminComment) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        report.setStatus(status);
        report.setHandlerUserId(adminUserId);
        // adminComment可另建字段或日志记录
        return reportRepository.save(report);
    }

    @Override
    public List<Report> getReportsByStatus(String status) {
        return reportRepository.findByStatus(status);
    }

    @Override
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    @Override
    public Report getReportById(Integer id) {
        Optional<Report> byId = reportRepository.findById(id);
        if(byId.isPresent()) {
            return byId.get();
        }
        throw new ReportNotFoundException("Report not found");
    }
}