package com.banddreea.tracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table (name = "users")
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;

    @NotBlank (message = "Username is required")
    @Column (name = "username", unique = true, nullable = false)
    private String username;

    @NotBlank (message = "Email is required")
    @Email (message = "Please enter a valid email address")
    @Column (name = "email", unique = true, nullable = false)
    private String email;

    @NotBlank (message = "Password is required")
    @Column (name = "password", nullable = false)
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
