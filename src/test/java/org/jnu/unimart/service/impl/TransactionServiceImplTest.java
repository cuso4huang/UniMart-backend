package org.jnu.unimart.service.impl;

import org.jnu.unimart.enums.PaymentMethod;
import org.jnu.unimart.enums.TransactionStatus;
import org.jnu.unimart.pojo.Transaction;
import org.jnu.unimart.service.PaymentService;
import org.jnu.unimart.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionServiceImplTest {

    @Autowired
    private TransactionService transactionService;

    @Test
    void createTransaction() {
        Transaction transaction = transactionService.createTransaction(11, 13, 2, PaymentMethod.WECHAT, 10.1);
        System.out.println(transaction);
    }

    @Test
    void updateTransactionStatus() {
        int id = 7;
        Transaction transactionById = transactionService.getTransactionById(7);
        Transaction transaction = transactionService.updateTransactionStatus(id, TransactionStatus.COMPLETED);
        System.out.println(transaction);
    }

    @Test
    void getTransactionsByBuyer() {
        List<Transaction> transactionsByBuyer = transactionService.getTransactionsByBuyer(11);
        System.out.println(transactionsByBuyer);
    }

    @Test
    void getTransactionsBySeller() {
        List<Transaction> transactionsBySeller = transactionService.getTransactionsBySeller(13);
        System.out.println(transactionsBySeller);
    }

    @Test
    void getTransactionById() {
        Transaction transactionById = transactionService.getTransactionById(7);
        System.out.println(transactionById);
    }
}