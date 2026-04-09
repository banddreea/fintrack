package com.banddreea.tracker;

import com.banddreea.tracker.entity.Account;
import com.banddreea.tracker.entity.User;
import com.banddreea.tracker.repository.AccountRepository;
import com.banddreea.tracker.repository.UserRepository;
import com.banddreea.tracker.service.DemoResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DemoResetService demoResetService;

    @Override
    public void run (String... args) {
        if (userRepository.existsByUsername("demo")) {
            return;
        }

        User demo = new User();
        demo.setUsername("demo");
        demo.setEmail("demo@tracker.com");
        demo.setPassword(passwordEncoder.encode("demo1234"));

        userRepository.save(demo);

        Account account = new Account();
        account.setUser(demo);
        account.setBalance(BigDecimal.ZERO);

        accountRepository.save(account);
        demoResetService.reset();
    }
}
