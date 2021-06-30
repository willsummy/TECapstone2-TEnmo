package com.techelevator.tenmo.dao;

public interface TransferDao {
    //we need to get trasnfer id from transferId. getTransferById(int transactionId)

    //list all transfers from account or user id?? its a one to one relationship so if we used
    //by user id we could just join the tables in the sql string.
    // ?? public List<Transfers> getAllTransfers(int userId or int id)???
    //                                            user id      account id

    //get transfer info from user id from ^^. transfer id? from example 5 in read me

    // sendTransfer(from what user, to what user, and the amount)
    // make sure the senders balance > what he is sending

}
