package com.banddreea.tracker.repository;

import com.banddreea.tracker.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByAccountId (int accountId);

    Page<Transaction> findByAccountId (int accountId, Pageable pageable);
}
