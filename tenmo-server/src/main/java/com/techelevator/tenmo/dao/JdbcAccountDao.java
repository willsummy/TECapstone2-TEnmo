package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    @Override
    public Account findAccountById(Long account_id) throws AccountNotFoundException {
        String sqlString = "SELECT account_id, user_id, balance FROM accounts WHERE account_id = ?;";
        Account account = null;
            SqlRowSet result = jdbcTemplate.queryForRowSet(sqlString, account_id);
            if (result.next()) {
                account = mapRowToAccount(result);
            }

        return account;
    }

    @Override
    public Account findUserById(Long user_id) throws AccountNotFoundException {
        String sqlString = "SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?;";
        Account account = null;
            SqlRowSet result = jdbcTemplate.queryForRowSet(sqlString, user_id);
            if(result.next()) {
                account = mapRowToAccount(result);
            }
        return account;
    }



    @Override
    public BigDecimal getBalance(Long account_id) throws DataAccessException {
        String sqlString = "SELECT balance FROM accounts WHERE account_id = ?";
        BigDecimal balance = null;

            SqlRowSet results = jdbcTemplate.queryForRowSet(sqlString, account_id);
            if(results.next()) {
                balance = results.getBigDecimal("balance");
            }
            else {
                System.out.println("Cannot access data");
            }
        return balance;
    }

//    @Override
//    public BigDecimal addToBalance(BigDecimal amount, Long user_id) throws AccountNotFoundException {
//       Account account = findAccountById(user_id);
//       BigDecimal updatedBalance = account.getBalance().add(amount); // if BigDecimal cannot be into JSON, switch to double or long.
//        System.out.println(updatedBalance);
//        String sqlString = "UPDATE accounts SET balance = ? WHERE user_id = ?";
//        try {
//            jdbcTemplate.update(sqlString, updatedBalance, user_id);
//        } catch (DataAccessException e) {
//            System.out.println("Cannot access data");
//        }
//        return account.getBalance();
//    }
//
//    @Override
//    public BigDecimal subtractFromBalance(BigDecimal amount, Long user_id) throws AccountNotFoundException {
//        Account account = findAccountById(user_id);
//        BigDecimal updatedBalance = account.getBalance().subtract(amount);
//        String sqlString = "UPDATE accounts SET balance = ? WHERE user_id = ?";
//        try {
//            jdbcTemplate.update(sqlString, updatedBalance, user_id);
//        } catch (DataAccessException e) {
//            System.out.println("Cannot access data");
//        }
//        return account.getBalance();
//    }

    @Override
    public boolean transferFunds(BigDecimal amount, Long sender_account_id, Long receiver_account_id) throws AccountNotFoundException {

        if (getBalance(sender_account_id).compareTo(amount) == -1) return false;

        BigDecimal oldSenderBalance = findAccountById(sender_account_id).getBalance();
        BigDecimal oldReceiverBalance = findAccountById(receiver_account_id).getBalance();
        BigDecimal newSenderBalance = oldSenderBalance.subtract(amount);
        BigDecimal newReceiverBalance = oldReceiverBalance.add(amount);


        String sql = "START TRANSACTION; " +
                "UPDATE accounts " +
                "SET balance = ? " + // new updated sender balance
                "WHERE account_id = ?; " + // sender_account_id
                "UPDATE accounts " +
                "SET balance = ? " + // new updated receiver balance
                "WHERE account_id = ?; " + // receiver_account_id
                "COMMIT;";

        jdbcTemplate.update(sql, newSenderBalance, sender_account_id, newReceiverBalance, receiver_account_id);

        if (findAccountById(sender_account_id).getBalance().compareTo(oldSenderBalance) == 0 //if both accounts haven't changed
        && findAccountById(receiver_account_id).getBalance().compareTo(oldReceiverBalance) == 0 //transfer didn't happen
        ) {
            return false;
        }

        // check if any change happened and send boolean based on that
        return true;
    }

    @Override
    public String getUserNameByAccountID(Long account_id) {
        String username = null;

        String sql = "SELECT username " +
                "FROM users u " +
                "LEFT JOIN accounts a ON u.user_id = a.user_id " +
                "LEFT JOIN transfers t ON a.account_id = t.account_from OR a.account_id = t.account_to " +
                "WHERE a.account_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account_id);

        if (results.next()) {
            username = results.getString("username");
        }


        return username;
    }

    @Override
    public Map<Long, String> getAllAccountIdAndUsernames() {
        Map<Long, String> map = new HashMap<>();

        String sql = "SELECT DISTINCT u.username, a.account_id " +
                "FROM users u " +
                "LEFT JOIN accounts a ON u.user_id = a.user_id " +
                "LEFT JOIN transfers t ON a.account_id = t.account_from OR a.account_id = t.account_to;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        while (results.next()) {
            Long id = results.getLong("account_id");
            String username = results.getString("username");
            map.put(id, username);
        }

        return map;
    }

    private Account mapRowToAccount(SqlRowSet result) {
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(result.getDouble("balance")));
        account.setAccount_id(result.getLong("account_id"));
        account.setUser_id(result.getLong("user_id"));
        return account;
    }



}

