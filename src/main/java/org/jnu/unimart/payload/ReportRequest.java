package org.jnu.unimart.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * ReportRequest 用于接收创建举报的请求数据。
 */
public class ReportRequest {

    @NotNull(message = "accusedUserId 是必需的")
    private Integer accusedUserId;

    @NotNull(message = "transactionId 是必需的")
    private Integer transactionId;

    @NotNull(message = "reason 是必需的")
    @Size(max = 500, message = "reason 长度不能超过 500 字符")
    private String reason;

    private List<String> imageUrl;

    public @NotNull(message = "accusedUserId 是必需的") Integer getAccusedUserId() {
        return accusedUserId;
    }

    public void setAccusedUserId(@NotNull(message = "accusedUserId 是必需的") Integer accusedUserId) {
        this.accusedUserId = accusedUserId;
    }

    public @NotNull(message = "transactionId 是必需的") Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(@NotNull(message = "transactionId 是必需的") Integer transactionId) {
        this.transactionId = transactionId;
    }

    public @NotNull(message = "reason 是必需的") @Size(max = 500, message = "reason 长度不能超过 500 字符") String getReason() {
        return reason;
    }

    public void setReason(@NotNull(message = "reason 是必需的") @Size(max = 500, message = "reason 长度不能超过 500 字符") String reason) {
        this.reason = reason;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }
}
