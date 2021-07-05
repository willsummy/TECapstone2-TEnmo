package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.Exceptions.AccountNotFoundException;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao){
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) throws AccountNotFoundException {
        Long user_id = userDao.findIdByUsername(principal.getName());
        return accountDao.findUserById(user_id).getBalance();
    }

    @RequestMapping(path = "/users/list", method = RequestMethod.GET)
    public List<User> listUsers(){
        return userDao.findAll();
    }

    @RequestMapping(path = "/username/{account_id}", method = RequestMethod.GET)
    public String findUserNameByAccountID(@PathVariable Long account_id) {
        return accountDao.getUserNameByAccountID(account_id);
    }

    @RequestMapping(path = "/usernames", method = RequestMethod.GET)
    public Map<Long, String> getMapOfUsersWithIds() {
        return accountDao.getAllAccountIdAndUsernames();
    }

    @RequestMapping(path = "/account/{user_id}", method = RequestMethod.GET)
    public Long getAccountIdByUserId(@PathVariable Long user_id) throws AccountNotFoundException {
        Account account = accountDao.findUserById(user_id);
        return account.getAccount_id();
    }


}
