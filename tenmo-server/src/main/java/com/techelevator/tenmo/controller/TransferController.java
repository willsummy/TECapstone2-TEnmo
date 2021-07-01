package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private AccountDao accountDao;
    private TransferDao transferDao;

    public TransferController(AccountDao accountDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "/user/{id}/transfers", method = RequestMethod.GET)
    public List<Transfer> listTransfersByTransferId(@PathVariable Long id){
        return transferDao.listAllTransfersByUserId(id);
    }

    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
    public Transfer getTransfer(@PathVariable Long id) {
        return transferDao.getTransferDetailsByTransferId(id);
    }


    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public boolean sendTransfer(@Valid @RequestBody Transfer transfer) {
        return transferDao.transfer(transfer);
    }

    // We need to send the transfer request.
}
