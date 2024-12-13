package org.jnu.unimart.pojo;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer reportId;

//    @Column(name = "report_type", nullable = false)
//    private String reportType;

    @Column(name = "reporter_user_id", nullable = false)
    private Integer reporterUserId;

    @Column(name = "accused_user_id")
    private Integer accusedUserId;

    @Column(name = "transaction_id")
    private Integer transactionId;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "status", nullable = false)
    private String status = "pending";

    @Column(name = "handler_user_id")
    private Integer handlerUserId;


    @Column(name = "create_time", nullable = false, insertable = true, updatable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time", nullable = false, insertable = true, updatable = true)
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportImage> images;

    @PrePersist
    protected void onCreate() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
        if (updateTime == null) {
            updateTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
    // getters & setters

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }


    public Integer getReporterUserId() {
        return reporterUserId;
    }

    public void setReporterUserId(Integer reporterUserId) {
        this.reporterUserId = reporterUserId;
    }

    public Integer getAccusedUserId() {
        return accusedUserId;
    }

    public void setAccusedUserId(Integer accusedUserId) {
        this.accusedUserId = accusedUserId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getHandlerUserId() {
        return handlerUserId;
    }

    public void setHandlerUserId(Integer handlerUserId) {
        this.handlerUserId = handlerUserId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public List<ReportImage> getImages() {
        return images;
    }

    public void setImages(List<ReportImage> images) {
        this.images = images;
    }
}
