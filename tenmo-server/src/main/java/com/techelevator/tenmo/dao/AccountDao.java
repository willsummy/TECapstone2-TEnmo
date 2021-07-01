package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDao {

    //Find accounts and users.
    //findUserById(int userId) -- user id, findAccountById(int Id) -- account id
    Account findAccountById(Long userId);

    //Has to be in big decimal. getBalance(int userId). get the balance from each user based on their id
    BigDecimal getBalance(Long userId);

    //big decimal again. Love it. addToBalance( amount to add???, int id) based off of account id.
    boolean addToBalance(BigDecimal amount, Long userId);

    //BIG DECIMAL. subtractFromBalance (amount to subtract??, int id) based off of account id.
    boolean subtractFromBalance(BigDecimal amount, Long userId);

    //
}
