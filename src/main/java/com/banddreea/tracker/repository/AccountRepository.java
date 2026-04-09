package com.banddreea.tracker.repository;

import com.banddreea.tracker.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByUserId (int userId);
}
