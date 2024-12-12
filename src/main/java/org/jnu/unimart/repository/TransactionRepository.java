// src/main/java/org/jnu/unimart/repository/TransactionRepository.java

package org.jnu.unimart.repository;

import org.jnu.unimart.pojo.Transaction;
import org.jnu.unimart.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByBuyerUserId(int buyerId);
    List<Transaction> findBySellerUserId(int sellerId);
    List<Transaction> findByBuyerUserIdAndTransactionStatus(int buyerId, TransactionStatus status);
    List<Transaction> findBySellerUserIdAndTransactionStatus(int sellerId, TransactionStatus status);
}
