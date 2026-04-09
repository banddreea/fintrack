package com.banddreea.tracker.service;

import com.banddreea.tracker.entity.Account;
import com.banddreea.tracker.entity.Transaction;
import com.banddreea.tracker.entity.User;
import com.banddreea.tracker.repository.AccountRepository;
import com.banddreea.tracker.repository.TransactionRepository;
import com.banddreea.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class DemoResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public void reset() {

        Optional<User> optionalUser = userRepository.findByUsername("demo");

        if (optionalUser.isEmpty()) {
            return;
        }

        User user = optionalUser.get();

        Optional<Account> optionalAccount = accountRepository.findByUserId(user.getId());

        if (optionalAccount.isEmpty()) {
            return;
        }

        Account account = optionalAccount.get();

        transactionRepository.deleteAll(transactionRepository.findByAccountId(account.getId()));

        Random random = new Random();
        List<Transaction> transactions = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for ( int i = 5; i >= 0; i--) {
            LocalDate month = now.minusMonths(i).withDayOfMonth(1);

            BigDecimal freelance = BigDecimal.valueOf(random.nextInt(801) + 100);
            BigDecimal bonus = BigDecimal.valueOf(random.nextInt(1501) + 300);
            BigDecimal groceries = BigDecimal.valueOf(random.nextInt(351) + 50).add(BigDecimal.valueOf(0.5));
            BigDecimal utilities = BigDecimal.valueOf(random.nextInt(451) + 50).add(BigDecimal.valueOf(0.5));
            BigDecimal travel = BigDecimal.valueOf(random.nextInt(1301) + 200).add(BigDecimal.valueOf(0.5));

            transactions.add(tx(account, "2500.00", "INCOME", "Monthly salary", month.withDayOfMonth(1)));
            transactions.add(tx(account, String.valueOf(freelance), "INCOME", "Freelance project", month.withDayOfMonth(5)));
            transactions.add(tx(account, String.valueOf(bonus), "INCOME", "Bonus income", month.withDayOfMonth(10)));
            transactions.add(tx(account, "950.00", "EXPENSE", "Rent", month.withDayOfMonth(2)));
            transactions.add(tx(account, String.valueOf(groceries), "EXPENSE", "Groceries", month.withDayOfMonth(14)));
            transactions.add(tx(account, String.valueOf(utilities), "EXPENSE", "Utilities", month.withDayOfMonth(7)));
            transactions.add(tx(account, String.valueOf(travel), "EXPENSE", "Travel", month.withDayOfMonth(18)));
        }

        transactionRepository.saveAll(transactions);

        BigDecimal balance = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            if (transaction.getType().equals("INCOME")) {
                balance = balance.add(transaction.getAmount());
            } else {
                balance = balance.subtract(transaction.getAmount());
            }
        }

        account.setBalance(balance);
        accountRepository.save(account);
    }

    private Transaction tx (Account account, String amount, String type, String description, LocalDate date) {

        Transaction t = new Transaction();
        t.setAccount(account);
        t.setAmount(new BigDecimal(amount));
        t.setType(type);
        t.setDescription(description);
        t.setDate(date);

        return t;
    }
}
