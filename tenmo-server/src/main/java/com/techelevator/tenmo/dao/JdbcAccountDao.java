package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao() { }

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public Account findAccountById(Long user_Id) {
        return null;
    }

    @Override
    public BigDecimal getBalance(Long userId) {
        String sqlString = "SELECT balance FROM accounts WHERE user_id = ?";
        SqlRowSet results = null;
        BigDecimal balance = null;

        try {
            res
        }

        return null;
    }

    @Override
    public boolean addToBalance(BigDecimal amount, Long userId) {
        return false;
    }

    @Override
    public boolean subtractFromBalance(BigDecimal amount, Long userId) {
        return false;
    }
}
