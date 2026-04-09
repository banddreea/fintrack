package com.banddreea.tracker.controller;

import com.banddreea.tracker.dto.MonthlySummary;
import com.banddreea.tracker.entity.Transaction;
import com.banddreea.tracker.entity.User;
import com.banddreea.tracker.repository.UserRepository;
import com.banddreea.tracker.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/summary")
public class SummaryController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String summary (@AuthenticationPrincipal UserDetails userDetails, Model model) {

        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            List<MonthlySummary> summaries = accountService.getMonthlySummary(user.getId());

            for (MonthlySummary summary : summaries) {
                LocalDate date = LocalDate.of(summary.getYear(), summary.getMonth(), 1);
                String monthName = date.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
                summary.setMonthName(monthName);
            }

            model.addAttribute("summaries", summaries);

            return "summary";

        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/details")
    public String details (@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam int month,
                           @RequestParam int year,
                           Model model) {

        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            List<Transaction> transactions = accountService.getTransactions(user.getId(), month, year);

            model.addAttribute("transactions", transactions);
            model.addAttribute("month", month);
            model.addAttribute("year", year);

            LocalDate date = LocalDate.of(year, month, 1);
            String monthName = date.format(DateTimeFormatter.ofPattern("MMMM yyyy"));

            model.addAttribute("monthName", monthName);

            return "summary-details";

        } else {
            return "redirect:/login";
        }
    }
}
