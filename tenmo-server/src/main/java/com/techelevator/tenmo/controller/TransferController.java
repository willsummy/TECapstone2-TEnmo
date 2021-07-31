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

    private TransferDao transferDao;

    public TransferController( TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "/transfers/list", method = RequestMethod.GET)
    public List<Transfer> listTransfersByUserId(Principal principal){
        return transferDao.listAllTransfersByUserId(principal.getName());
    }

    @RequestMapping(path = "/pending/list", method = RequestMethod.GET)
    public List<Transfer> listPendingTransfersByUserId(Principal principal) {
        return transferDao.listPendingTransfersByUserId(principal.getName());
    }

    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
    public Transfer getTransfer(@PathVariable Long id, Principal principal) {
        return transferDao.getTransferDetailsByTransferId(id, principal.getName());
    }


    @RequestMapping(path = "/transfer/send", method = RequestMethod.POST)
    public boolean sendTransfer(@Valid @RequestBody Transfer transfer) throws AccountNotFoundException {
        return transferDao.createTransfer(transfer); // 2 representing sent transfer
    }

    // need route for requesting transfer
    @RequestMapping(path = "transfer/request", method = RequestMethod.POST)
    public boolean requestTransfer(@Valid @RequestBody Transfer transfer) throws AccountNotFoundException {
        return transferDao.createTransfer(transfer); // 1 representing request transfer
    }

    @RequestMapping(path = "transfer/accept", method = RequestMethod.PUT) // editing a transfer to accepted
    public boolean acceptTransfer(@Valid @RequestBody Transfer transfer) throws AccountNotFoundException {
        return transferDao.acceptTransfer(transfer);
    }

    @RequestMapping(path = "transfer/reject", method = RequestMethod.PUT) // editing a transfer to rejected
    public boolean rejectTransfer(@Valid @RequestBody Transfer transfer) {
        return transferDao.rejectTransfer(transfer);
    }

}
