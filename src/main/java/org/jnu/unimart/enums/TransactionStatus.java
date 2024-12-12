// src/main/java/org/jnu/unimart/enums/TransactionStatus.java

package org.jnu.unimart.enums;

public enum TransactionStatus {
    PENDING_PAYMENT, // 0：待支付
    PAID,            // 1：已支付
    COMPLETED,       // 2：已完成
    CANCELLED        // 3：已取消
}
