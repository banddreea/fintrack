package com.banddreea.tracker.controller;

import com.banddreea.tracker.dto.MonthlySummary;
import com.banddreea.tracker.entity.Account;
import com.banddreea.tracker.entity.Transaction;
import com.banddreea.tracker.entity.User;
import com.banddreea.tracker.repository.UserRepository;
import com.banddreea.tracker.service.AccountService;
import com.banddreea.tracker.service.DemoResetService;
import com.banddreea.tracker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DemoResetService demoResetService;

    @GetMapping("/dashboard")
    public String dashboard (@AuthenticationPrincipal UserDetails userDetails, Model model) {

        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Account account = accountService.getAccount(user.getId());
            BigDecimal totalIncome = accountService.getTotalIncome(account.getId());
            BigDecimal totalExpense = accountService.getTotalExpense(account.getId());

            List<Transaction> transactions = transactionService.getTransactions(user.getId());

            List<MonthlySummary> summaries = accountService.getMonthlySummary(user.getId());

            model.addAttribute("username", user.getUsername());
            model.addAttribute("balance", account.getBalance());
            model.addAttribute("totalIncome", totalIncome);
            model.addAttribute("totalExpense", totalExpense);
            model.addAttribute("transactions",  transactions);

            model.addAttribute("summaries", summaries);

            return "dashboard";

        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/demo/reset")
    public String resetDemo(@AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails.getUsername().equals("demo")) {
            demoResetService.reset();
        }

        return "redirect:/dashboard";
    }
}
