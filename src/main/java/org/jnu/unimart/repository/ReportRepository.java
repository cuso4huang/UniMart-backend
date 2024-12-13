package org.jnu.unimart.repository;

import org.jnu.unimart.pojo.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findByReporterUserIdOrderByCreateTimeDesc(Integer reporterUserId);
    List<Report> findByStatus(String status);
}