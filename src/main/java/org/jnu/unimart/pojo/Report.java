package org.jnu.unimart.pojo;

import jakarta.persistence.*;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

// 举报表
@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private int reportId;
    @Column(name = "repoter_id")
    private int reporterId; //举报者id
    @Column(name = "report_reason")
    private String reportReason; // 举报内容
    @Column(name = "reported_product_id")
    private int reportedProductId; // 举报商品id
    @Column(name = "report_time")
    private LocalDateTime reportTime; //举报时间
    @Column(name = "handling_status")
    private int handlingStatus; // 处理状态：0：待处理，1：已处理

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getReporterId() {
        return reporterId;
    }

    public void setReporterId(int reporterId) {
        this.reporterId = reporterId;
    }

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }

    public int getReportedProductId() {
        return reportedProductId;
    }

    public void setReportedProductId(int reportedProductId) {
        this.reportedProductId = reportedProductId;
    }

    public LocalDateTime getReportTime() {
        return reportTime;
    }

    public void setReportTime(LocalDateTime reportTime) {
        this.reportTime = reportTime;
    }

    public int getHandlingStatus() {
        return handlingStatus;
    }

    public void setHandlingStatus(int handlingStatus) {
        this.handlingStatus = handlingStatus;
    }

    public String toString() {
        return "Report{" +
                "reportId=" + reportId +
                ", reporterId=" + reporterId +
                ", reportReason='" + reportReason + '\'' +
                ", reportedProductId=" + reportedProductId +
                ", reportTime=" + reportTime +
                ", handlingStatus=" + handlingStatus +
                '}';
    }
}
