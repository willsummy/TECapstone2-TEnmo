package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exceptions.AccountNotFoundException;
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
    public Account findAccountById(Long account_id) throws AccountNotFoundException {
        String sqlString = "SELECT account_id, user_id, balance FROM accounts WHERE account_id = ?;";
        Account account = null;
        //try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sqlString, account_id);
            if (result.next()) {
                account = mapRowToAccount(result);
            }


        // } catch (Exception e) {
          // e.printStackTrace();
            // throw new AccountNotFoundException();
       //  }
        return account;
    }

    @Override
    public Account findUserById(Long user_id) throws AccountNotFoundException {
        String sqlString = "SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?;";
        Account account = null;
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sqlString, user_id);
            account = mapRowToAccount(result);
        } catch (Exception e) {
            throw new AccountNotFoundException();
        }
        return account;
    }



    @Override
    public BigDecimal getBalance(Long account_id) throws DataAccessException {
        String sqlString = "SELECT balance FROM accounts WHERE user_id = ?";
        SqlRowSet results = null;
        BigDecimal balance = null;

        try {
            results = jdbcTemplate.queryForRowSet(sqlString, account_id);
            if(results.next()) {
                balance = results.getBigDecimal("balance");
            }
        } catch (DataAccessException e) {
            System.out.println("Cannot access data");
        }

        return balance;
    }

    @Override
    public BigDecimal addToBalance(BigDecimal amount, Long user_id) throws AccountNotFoundException {
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
    public BigDecimal subtractFromBalance(BigDecimal amount, Long user_id) throws AccountNotFoundException {
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

    @Override
    public boolean transferFunds(BigDecimal amount, Long sender_account_id, Long receiver_account_id) throws AccountNotFoundException {
        // verify sender has enough money
        if (getBalance(sender_account_id).compareTo(amount) == -1) return false;

        // if they do
        // create large sql string with transaction
        // if any part fails, it gets rolled back

        // do the math before this
        BigDecimal oldSenderBalance = findAccountById(sender_account_id).getBalance();
        BigDecimal oldReceiverBalance = findAccountById(receiver_account_id).getBalance();
        BigDecimal newSenderBalance = oldSenderBalance.subtract(amount);
        BigDecimal newReceiverBalance = oldReceiverBalance.subtract(amount);

        //two update methods
        String sql = "START TRANSACTION; " +
                "UPDATE accounts " +
                "SET balance = ? " + // new updated sender balance
                "WHERE account_id = ?; " + // sender_account_id
                "UPDATE accounts " +
                "SET balance = ? " + // new updated receiver balance
                "WHERE account_id = ?; " + // receiver_account_id
                "COMMIT;";

        jdbcTemplate.update(sql, newSenderBalance, sender_account_id, newReceiverBalance, receiver_account_id);

        if (findAccountById(sender_account_id).getBalance().compareTo(oldSenderBalance) == 0
        && findAccountById(receiver_account_id).getBalance().compareTo(oldReceiverBalance) == 0
        ) {
            return false;
        }

        // check if any change happened and send boolean based on that
        return true;
    }

    private Account mapRowToAccount(SqlRowSet result) {
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(result.getDouble("balance")));
        account.setAccount_id(result.getLong("account_id"));
        account.setUser_id(result.getLong("user_id"));
        return account;
    }

}

