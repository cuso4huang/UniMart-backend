// src/main/java/org/jnu/unimart/service/PaymentService.java

package org.jnu.unimart.service;

import org.jnu.unimart.pojo.Transaction;

public interface PaymentService {
    Transaction simulatePayment(Transaction transaction);
    Transaction simulateRefund(Transaction transaction);
}
