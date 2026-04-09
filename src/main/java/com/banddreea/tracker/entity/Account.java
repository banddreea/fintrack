package com.banddreea.tracker.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table (name = "accounts")
public class Account {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn (name = "user_id", nullable = false)
    private User user;

    @Column (nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
