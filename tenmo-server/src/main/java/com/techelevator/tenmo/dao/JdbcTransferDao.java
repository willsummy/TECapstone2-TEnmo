package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;
    private final AccountDao accountDao;

    public JdbcTransferDao(DataSource dataSource, AccountDao accountDao) {
        this.jdbcTemplate = new JdbcTemplate((dataSource));
        this.accountDao = accountDao;
    }


    @Override
    public List<Transfer> listAllTransfersByUserId(Long userId) {
        List<Transfer> list = new ArrayList<>();

        String sql = "SELECT transfer_id, account_from, account_to, amount " +
                "FROM transfers " +
                "JOIN accounts ON account_from = account_id OR account_to = account_id " +
                "WHERE user_id = ?;";
        // these are actually looking for account_ids not user_ids

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        while (results.next()) {
            list.add(mapRowToTransfer(results));
        }

        return list;
    }

    @Override
    public Transfer getTransferDetailsByTransferId(Long transferId) {

        Transfer transfer = null;

        String sql = "SELECT transfer_id, transfer_status_id, transfer_type_id, account_from, account_to, amount " +
                "FROM transfers " +
                "WHERE transfer_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);

        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }

        return transfer;
    }

    @Override
    public boolean transfer(Transfer transfer) {
        // take in a transfer object, which will specify who its coming from and to
        // amount, then we can verify amount in account here
        String sql = "SELECT balance " +
                "FROM accounts " +
                "WHERE account_id = ?;";
        // replace transfer.getAccount_from();
        BigDecimal senderBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, transfer.getAccount_from());

        // pull out account balance of the sender
        // verify first, return false if not enough
        // all account start with 1000, but maybe check for nulls later
        if (senderBalance.compareTo(transfer.getAmount()) < 0) return false;

        // if true
        // do add and subtract, return true
        // take in reciever balance
        // calculate new balances of each
        // do sql updates for each
        sql = "SELECT balance " +
                "FROM accounts " +
                "WHERE account_id = ?;";
        BigDecimal recieverBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, transfer.getAccount_to());

        BigDecimal newSenderBalance = senderBalance.subtract(transfer.getAmount());
        BigDecimal newReceiverBalance = recieverBalance.add(transfer.getAmount());

        // two updates
        // sender
        Long sender = accountDao.findAccountById(transfer.getAccount_from()).getUser_id();
        accountDao.subtractFromBalance(newSenderBalance, sender);



        // receiver
        Long receiver = accountDao.findAccountById(transfer.getAccount_to()).getUser_id();
        accountDao.addToBalance(newReceiverBalance, receiver);



        // later this could change to be less simple
        return true;


    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();

        transfer.setTransfer_id(rowSet.getLong("transfer_id"));
        transfer.setTransfer_status_id(rowSet.getLong("transfer_status_id"));
        transfer.setTransfer_type_id(rowSet.getLong("transfer_type_id"));
        transfer.setAccount_from(rowSet.getLong("account_from"));
        transfer.setAccount_to(rowSet.getLong("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));

        return transfer;
    }
}
