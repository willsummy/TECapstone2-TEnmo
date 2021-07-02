package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
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
    public Account findAccountById(Long account_id) {
        String sqlString = "SELECT account_id, user_id, balance FROM accounts WHERE account_id = ?";
        Account account = null;
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sqlString, account_id);
            account = mapRowToAccount(result);
        } catch (DataAccessException e) {
            System.out.println("Cannot access data");
        }
        return account;
    }

    @Override
    public Account findUserById(Long user_id) {
        String sqlString = "SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?";
        Account account = null;
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sqlString, user_id);
            account = mapRowToAccount(result);
        } catch (DataAccessException e) {
            System.out.println("Cannot access data");
        }
        return account;
    }



    @Override
    public BigDecimal getBalance(Long user_id) {
        String sqlString = "SELECT balance FROM accounts WHERE user_id = ?";
        SqlRowSet results = null;
        BigDecimal balance = null;

        try {
            results = jdbcTemplate.queryForRowSet(sqlString, user_id);
            if(results.next()) {
                balance = results.getBigDecimal("balance");
            }
        } catch (DataAccessException e) {
            System.out.println("Cannot access data");
        }

        return balance;
    }

    @Override
    public BigDecimal addToBalance(BigDecimal amount, Long user_id) {
       Account account = findAccountById(user_id);
       BigDecimal updatedBalance = account.getBalance().add(amount); // if BigDecimal cannot be into JSON, switch to double or long.
        System.out.println(updatedBalance);
        String sqlString = "UPDATE accounts SET balance = ? WHERE user_id = ?";
        try {
            jdbcTemplate.update(sqlString, updatedBalance, user_id);
        } catch (DataAccessException e) {
            System.out.println("Cannot access data");
        }
        return account.getBalance();
    }

    @Override
    public BigDecimal subtractFromBalance(BigDecimal amount, Long user_id) {
        Account account = findAccountById(user_id);
        BigDecimal updatedBalance = account.getBalance().subtract(amount);
        String sqlString = "UPDATE accounts SET balance = ? WHERE user_id = ?";
        try {
            jdbcTemplate.update(sqlString, updatedBalance, user_id);
        } catch (DataAccessException e) {
            System.out.println("Cannot access data");
        }
        return account.getBalance();
    }


    private Account mapRowToAccount(SqlRowSet result) {
        Account account = new Account();
        account.setBalance(result.getBigDecimal("balance"));
        account.setAccount_id(result.getLong("account_id"));
        account.setUser_id(result.getLong("user_id"));
        return account;
    }

}

