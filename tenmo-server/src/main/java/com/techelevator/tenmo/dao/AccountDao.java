package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.Map;

public interface AccountDao {

    //Find accounts and users.
    //findUserById(int userId) -- user id, findAccountById(int Id) -- account id
    Account findAccountById(Long account_id) throws AccountNotFoundException;

    Account findUserById(Long userId) throws AccountNotFoundException;

    //Has to be in big decimal. getBalance(int userId). get the balance from each user based on their id
    BigDecimal getBalance(Long account_id);

    //big decimal again. Love it. addToBalance( amount to add???, int id) based off of account id.
    BigDecimal addToBalance(BigDecimal amount, Long userId) throws AccountNotFoundException;

    //BIG DECIMAL. subtractFromBalance (amount to subtract??, int id) based off of account id.
    BigDecimal subtractFromBalance(BigDecimal amount, Long userId) throws AccountNotFoundException;

    boolean transferFunds(BigDecimal amount, Long sender_id, Long receiver_id) throws AccountNotFoundException;

    //
    String getUserNameByAccountID(Long account_id);

    public Map<Long, String> getAllAccountIdAndUsernames();
}
