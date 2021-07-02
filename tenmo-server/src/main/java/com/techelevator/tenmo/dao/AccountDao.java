package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    //Find accounts and users.
    //findUserById(int userId) -- user id, findAccountById(int Id) -- account id
    Account findAccountById(Long account_id);

    Account findUserById(Long userId);

    //Has to be in big decimal. getBalance(int userId). get the balance from each user based on their id
    BigDecimal getBalance(Long account_id);

    //big decimal again. Love it. addToBalance( amount to add???, int id) based off of account id.
    BigDecimal addToBalance(BigDecimal amount, Long userId);

    //BIG DECIMAL. subtractFromBalance (amount to subtract??, int id) based off of account id.
    BigDecimal subtractFromBalance(BigDecimal amount, Long userId);

    boolean transferFunds(BigDecimal amount, Long sender_id, Long receiver_id);

    //
}
