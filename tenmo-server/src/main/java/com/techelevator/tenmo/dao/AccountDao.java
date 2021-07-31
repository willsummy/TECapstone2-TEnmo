package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.Map;

public interface AccountDao {

    Account findAccountByUsername(String username) throws AccountNotFoundException;

    Long getAccountIdByUserId(Long user_id);

    BigDecimal getBalance(String username);

  //  BigDecimal addToBalance(BigDecimal amount, Long userId) throws AccountNotFoundException;

  //  BigDecimal subtractFromBalance(BigDecimal amount, Long userId) throws AccountNotFoundException;

//    boolean sendFunds(BigDecimal amount, Long sender_id, Long receiver_id) throws AccountNotFoundException;

    String getUserNameByAccountID(Long account_id);

    public Map<Long, String> getAllAccountIdAndUsernames();
}
