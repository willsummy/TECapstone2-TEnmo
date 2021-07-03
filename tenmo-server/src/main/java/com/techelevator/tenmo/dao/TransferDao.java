package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> listAllTransfersByUserId(Long userId);

    Transfer getTransferDetailsByTransferId(Long transferId);

    boolean createTransfer(Transfer transfer) throws AccountNotFoundException;

}
