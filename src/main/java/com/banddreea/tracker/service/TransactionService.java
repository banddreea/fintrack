package com.banddreea.tracker.service;

import com.banddreea.tracker.entity.Account;
import com.banddreea.tracker.entity.Transaction;
import com.banddreea.tracker.repository.AccountRepository;
import com.banddreea.tracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    public List<Transaction> getTransactions (int userId) {

        Account account = accountService.getAccount(userId);
        List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());

        transactions.sort((a, b) -> b.getDate().compareTo(a.getDate()));

        return transactions;
    }

    public Optional<Transaction> getTransactionById(int id) {
        return transactionRepository.findById(id);
    }

    public Page<Transaction> getTransactionsPaginated (int userId, int page) {

        Account account = accountService.getAccount(userId);

        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "date"));

        return transactionRepository.findByAccountId(account.getId(), pageRequest);
    }

    public void addTransaction (int userId,
                                BigDecimal amount,
                                String type,
                                String description,
                                LocalDate date) {

        Account account = accountService.getAccount(userId);

        Transaction transaction = new Transaction();
            transaction.setAccount(account);
            transaction.setAmount(amount);
            transaction.setType(type);
            transaction.setDescription(description);
            transaction.setDate(date);

        transactionRepository.save(transaction);

        accountService.updateBalance(account, amount, type);
    }

    public void deleteTransaction (int transactionId, int userId) {

        Account account = accountService.getAccount(userId);

        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);

        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();

            if (transaction.getAccount().getId() == account.getId()) {
                if (transaction.getType().equals("INCOME")) {
                    account.setBalance(account.getBalance().subtract(transaction.getAmount()));
                } else {
                    account.setBalance(account.getBalance().add(transaction.getAmount()));
                }

                accountRepository.save(account);
                transactionRepository.deleteById(transactionId);
            }
        }
    }

    public void editTransaction ( int transactionId, int userId,
                                  BigDecimal newAmount, String newType,
                                  String newDescription, LocalDate newDate) {

        Account account = accountService.getAccount(userId);

        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);

        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();

            if (transaction.getAccount().getId() == account.getId()) {
                if (transaction.getType().equals("INCOME")) {
                    account.setBalance(account.getBalance().subtract(transaction.getAmount()));
                } else {
                    account.setBalance(account.getBalance().add(transaction.getAmount()));
                }

                if (newType.equals("INCOME")) {
                    account.setBalance((account.getBalance().add(newAmount)));
                } else {
                    account.setBalance(account.getBalance().subtract(newAmount));
                }

                transaction.setAmount(newAmount);
                transaction.setType(newType);
                transaction.setDescription(newDescription);
                transaction.setDate(newDate);

                transactionRepository.save(transaction);
                accountRepository.save(account);
            }
        }
    }
}
