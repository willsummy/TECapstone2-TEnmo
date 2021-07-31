package com.techelevator.tenmo.model;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class Account {
    // this table in the database has a 1to1 relationship to the user table

    private String username;
    private Long account_id; // primary key
    @NotBlank
    private Long user_id; // unique foreign key, 1to1
    @NotBlank
    private BigDecimal balance;

    public Account() {}

    public Account(Long user_id, BigDecimal balance) {
        this.user_id = user_id;
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
