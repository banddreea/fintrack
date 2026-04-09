package com.banddreea.tracker.service;

import com.banddreea.tracker.entity.Account;
import com.banddreea.tracker.entity.User;
import com.banddreea.tracker.repository.AccountRepository;
import com.banddreea.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(String username, String email, String password) {

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already in use");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        Account account = new Account();
            account.setUser(user);

        accountRepository.save(account);
    }
}
