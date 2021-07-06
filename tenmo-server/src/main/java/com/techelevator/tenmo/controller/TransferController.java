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

    @RequestMapping(path = "/transfers/list", method = RequestMethod.GET)
    public List<Transfer> listTransfersByUserId(Principal principal){
        Long user_id = userDao.findIdByUsername(principal.getName());
        return transferDao.listAllTransfersByUserId(user_id);
    }

    @RequestMapping(path = "/pending/list", method = RequestMethod.GET)
    public List<Transfer> listPendingTransfersByUserId(Principal principal) {
        Long user_id = userDao.findIdByUsername(principal.getName());
        return transferDao.listPendingTransfersByUserId(user_id);
    }

    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
    public Transfer getTransfer(@PathVariable Long id, Principal principal) {
        return transferDao.getTransferDetailsByTransferId(id, principal.getName());
    }


    @RequestMapping(path = "/transfer/send", method = RequestMethod.POST)
    public boolean sendTransfer(@Valid @RequestBody Transfer transfer) throws AccountNotFoundException {
        return transferDao.createTransfer(transfer, 2); // 2 representing sent transfer
    }

    // need route for requesting transfer
    @RequestMapping(path = "transfer/request", method = RequestMethod.POST)
    public boolean requestTransfer(@Valid @RequestBody Transfer transfer) throws AccountNotFoundException {
        return transferDao.createTransfer(transfer, 1); // 1 representing request transfer
    }

    @RequestMapping(path = "transfer/accept", method = RequestMethod.PUT) // editing a transfer to accepted
    public boolean acceptTransfer(@Valid @RequestBody Transfer transfer) {
        return false;
    }

    @RequestMapping(path = "transfer/reject", method = RequestMethod.PUT) // editing a transfer to rejected
    public boolean rejectTransfer(@Valid @RequestBody Transfer transfer) {
        return false;
    }

}
