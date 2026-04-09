package com.banddreea.tracker.controller;

import com.banddreea.tracker.entity.Transaction;
import com.banddreea.tracker.entity.User;
import com.banddreea.tracker.repository.UserRepository;
import com.banddreea.tracker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String transactions (@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam(defaultValue = "0") int page,
                                Model model) {

        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Page<Transaction> transactionPage = transactionService.getTransactionsPaginated(user.getId(), page);

            model.addAttribute("username", user.getUsername());

            model.addAttribute("transactions", transactionPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", transactionPage.getTotalPages());

            return "transactions";

        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/add")
    public String addTransaction (@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestParam BigDecimal amount,
                                  @RequestParam String type,
                                  @RequestParam String description,
                                  @RequestParam LocalDate date,
                                  RedirectAttributes redirectAttributes) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            redirectAttributes.addFlashAttribute("error", "Amount must be greater than zero.");
            return "redirect:/transactions";
        }

        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            transactionService.addTransaction(user.getId(), amount, type, description,date);

            return "redirect:/transactions";

        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteTransaction (@PathVariable int id,
                                     @AuthenticationPrincipal UserDetails userDetails,
                                     @RequestParam(defaultValue = "/transactions") String redirect) {

        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            transactionService.deleteTransaction(id, user.getId());

            return "redirect:" + redirect;

        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/edit/{id}")
    public String editPage (@PathVariable int id,
                            @AuthenticationPrincipal UserDetails userDetails,
                            Model model) {

        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        Optional<Transaction> optionalTransaction = transactionService.getTransactionById(id);

        if (optionalUser.isPresent() && optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();
            User user = optionalUser.get();

            if (transaction.getAccount().getUser().getId() != user.getId()) {
                return "redirect:/transactions";
            }

            model.addAttribute("transaction", transaction);
            return "edit-transaction";
        }

        return "redirect:/transactions";
    }

    @PostMapping("/edit/{id}")
    public String editTransaction (@PathVariable int id,
                                   @AuthenticationPrincipal UserDetails userDetails,
                                   @RequestParam BigDecimal amount,
                                   @RequestParam String type,
                                   @RequestParam String description,
                                   @RequestParam LocalDate date,
                                   @RequestParam(defaultValue = "/transactions") String redirect,
                                   RedirectAttributes redirectAttributes) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            redirectAttributes.addFlashAttribute("error", "Amount must be greater than zero.");
            return "redirect:/transactions/edit/" + id;
        }

        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            transactionService.editTransaction(id, user.getId(), amount, type, description, date);
        }

        return "redirect:" + redirect;

    }
}
