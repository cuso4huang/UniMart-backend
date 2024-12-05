package org.jnu.unimart.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

// 举报表
@Entity
@Data
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

}
