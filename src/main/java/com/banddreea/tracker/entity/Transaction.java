package com.banddreea.tracker.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table ( name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn (name = "account_id", nullable = false)
    private Account account;

    @Column (nullable = false)
    private BigDecimal amount;

    @Column (nullable = false)
    private String type;

    private String description;

    private LocalDate date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
