package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.Map;

public interface AccountDao {

    Account findAccountById(Long account_id) throws AccountNotFoundException;

    Account findUserById(Long userId) throws AccountNotFoundException;

    BigDecimal getBalance(Long account_id);

    boolean transferFunds(BigDecimal amount, Long sender_id, Long receiver_id) throws AccountNotFoundException;

    String getUserNameByAccountID(Long account_id);

    public Map<Long, String> getAllAccountIdAndUsernames();
}
