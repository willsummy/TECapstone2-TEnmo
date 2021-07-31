package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> listAllTransfersByUserId(String username);

    List<Transfer> listPendingTransfersByUserId(String username);

    Transfer getTransferDetailsByTransferId(Long transferId, String username);

    boolean createTransfer(Transfer transfer) throws AccountNotFoundException;

    boolean acceptTransfer(Transfer transfer) throws AccountNotFoundException;

    boolean rejectTransfer(Transfer transfer);

}
