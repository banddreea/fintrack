package com.banddreea.tracker.service;

import com.banddreea.tracker.dto.MonthlySummary;
import com.banddreea.tracker.entity.Account;
import com.banddreea.tracker.entity.Transaction;
import com.banddreea.tracker.repository.AccountRepository;
import com.banddreea.tracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Account getAccount(int userId) {

        Optional<Account> optionalAccount = accountRepository.findByUserId(userId);

        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        } else {
            throw new RuntimeException("Account not found for user id: " + userId);
        }
    }

    public BigDecimal getTotalTransactions(int accountId, String type) {

        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        BigDecimal total = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            if (transaction.getType().equals(type)) {
                total = total.add(transaction.getAmount());
            }
        }

        return total;
    }

    public BigDecimal getTotalIncome(int accountId) {
        return getTotalTransactions(accountId, "INCOME");
    }

    public BigDecimal getTotalExpense(int accountId) {
        return getTotalTransactions(accountId, "EXPENSE");
    }

    public void updateBalance(Account account, BigDecimal amount, String type) {

        if (type.equals("INCOME")) {
            account.setBalance(account.getBalance().add(amount));
        } else {
            account.setBalance(account.getBalance().subtract(amount));
        }

        accountRepository.save(account);
    }

    public List<MonthlySummary> getMonthlySummary (int userId) {

        Account account = getAccount(userId);
        List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());

        List<MonthlySummary> summaries = new ArrayList<>();

        for (Transaction transaction : transactions) {
            int year = transaction.getDate().getYear();
            int month = transaction.getDate().getMonthValue();

            MonthlySummary existingMonthlySummary = null;
            for (MonthlySummary summary : summaries) {
                if (summary.getYear() == year && summary.getMonth() == month) {
                    existingMonthlySummary = summary;
                    break;
                }
            }

            if (existingMonthlySummary == null) {
                BigDecimal income = BigDecimal.ZERO;
                BigDecimal expense = BigDecimal.ZERO;

                if (transaction.getType().equals("INCOME")) {
                    income = transaction.getAmount();
                } else {
                    expense = transaction.getAmount();
                }

                summaries.add(new MonthlySummary(year, month, income, expense));
            } else {
                if (transaction.getType().equals("INCOME")) {
                    existingMonthlySummary.setTotalIncome(existingMonthlySummary.getTotalIncome().add(transaction.getAmount()));
                } else {
                    existingMonthlySummary.setTotalExpense(existingMonthlySummary.getTotalExpense().add(transaction.getAmount()));
                }
            }
        }

        for (int i = 0; i < summaries.size() - 1; i++) {
            for (int j = i + 1; j < summaries.size(); j++) {
                MonthlySummary a = summaries.get(i);
                MonthlySummary b = summaries.get(j);

                if (a.getYear() > b.getYear() || (a.getYear() == b.getYear() && a.getMonth() > b.getMonth())) {
                    summaries.set(i, b);
                    summaries.set(j, a);
                }
            }
        }

        return summaries;
    }

    public List<Transaction> getTransactions (int userId, int month, int year) {

        Account account = getAccount(userId);
        List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());

        List<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getDate().getMonthValue() == month && transaction.getDate().getYear() == year) {
                filteredTransactions.add(transaction);
            }
        }

        filteredTransactions.sort((a, b) -> b.getDate().compareTo(a.getDate()));

        return filteredTransactions;
    }
}
