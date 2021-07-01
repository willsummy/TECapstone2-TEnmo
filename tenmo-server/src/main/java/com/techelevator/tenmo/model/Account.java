package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    // this table in the database has a 1to1 relationship to the user table

    private Long account_id; // primary key
    private Long user_id; // unique foreign key, 1to1
    private BigDecimal balance;

    public Account() {}

    public Account(Long user_id, BigDecimal balance) {
        this.user_id = user_id;
        this.balance = balance;
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
