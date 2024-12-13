package org.jnu.unimart.controller;

import jakarta.validation.Valid;
import org.jnu.unimart.payload.ReportRequest;
import org.jnu.unimart.pojo.Report;
import org.jnu.unimart.security.UserDetailsImpl;
import org.jnu.unimart.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ReportController 负责处理与举报（Report）相关的所有 HTTP 请求。
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * 创建新举报，仅已登录的用户（ROLE_USER 或 ROLE_ADMIN）可以创建
     *
     * @param reportRequest 需要创建的举报信息 accusedIdUserId,transactionId,reason,imageUrls
     * @param user          当前已认证的用户信息
     * @return 返回创建的举报信息，并设置 201 状态
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Report> createReport(
            @Valid @RequestBody ReportRequest reportRequest,
            @AuthenticationPrincipal UserDetailsImpl user) {

        // 调用 Service 层创建举报
        Report createdReport = reportService.createReport(user.getId(),reportRequest.getAccusedUserId(),
                reportRequest.getTransactionId(),reportRequest.getReason(),reportRequest.getImageUrl());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdReport);
    }

    /**
     * 获取所有举报，仅具有管理员权限的用户可以访问
     *
     * @return 返回所有举报信息
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * 根据举报 ID 获取举报详情，仅具有管理员权限的用户可以访问
     *
     * @param id 举报 ID
     * @return 返回指定的举报信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Report> getReportById(@PathVariable Integer id) {
        Report report = reportService.getReportById(id);
        return ResponseEntity.ok(report);
    }

    // 根据需要添加更多控制器方法，如更新或删除举报
}
