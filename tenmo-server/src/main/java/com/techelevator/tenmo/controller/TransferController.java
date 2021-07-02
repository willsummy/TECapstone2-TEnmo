package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.Exceptions.AccountNotFoundException;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private AccountDao accountDao;
    private TransferDao transferDao;
    private UserDao userDao;

    public TransferController(AccountDao accountDao, TransferDao transferDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/list_transfers", method = RequestMethod.GET)
    public List<Transfer> listTransfersByUserId(Principal principal){
        Long user_id = userDao.findIdByUsername(principal.getName());
        return transferDao.listAllTransfersByUserId(user_id);

    }

    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransfer(@PathVariable Long id) {
        return transferDao.getTransferDetailsByTransferId(id);
    }


    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public boolean sendTransfer(@Valid @RequestBody Transfer transfer) throws AccountNotFoundException {
        return transferDao.createTransfer(transfer);
    }

    // We need to send the transfer request.
}
