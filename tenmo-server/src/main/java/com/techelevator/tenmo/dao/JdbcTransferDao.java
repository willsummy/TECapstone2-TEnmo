package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exceptions.AccountNotFoundException;
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
    public List<Transfer> listAllTransfersByUserId(String username) {
        List<Transfer> list = new ArrayList<>();

        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from AS sender_account, account_to AS receiver_account, amount, sender.user_id AS sender_id, receiver.user_id AS receiver_id, sendername, receivername " +
                "FROM transfers t " +
                "JOIN (" +
                "SELECT username AS sendername, accounts.account_id, users.user_id " +
                "FROM accounts " +
                "JOIN users " +
                "ON accounts.user_id = users.user_id ) sender " +
                "ON t.account_from = sender.account_id " +
                "JOIN ( " +
                "SELECT username AS receivername, accounts.account_id, users.user_id " +
                "FROM accounts " +
                "JOIN users " +
                "ON accounts.user_id = users.user_id ) receiver " +
                "ON t.account_to = receiver.account_id " +
                "WHERE sendername = ? or receivername = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username, username);

        while (results.next()) {
            list.add(mapRowToTransfer(results));
        }

        return list;
    }

    @Override
    public List<Transfer> listPendingTransfersByUserId(String username) {
        List<Transfer> list = new ArrayList<>();

        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from AS sender_account, account_to AS receiver_account, amount, sender.user_id AS sender_id, receiver.user_id AS receiver_id, sendername, receivername " +
                "FROM transfers t " +
                "JOIN (" +
                "SELECT username AS sendername, accounts.account_id, users.user_id " +
                "FROM accounts " +
                "JOIN users " +
                "ON accounts.user_id = users.user_id ) sender " +
                "ON t.account_from = sender.account_id " +
                " " +
                "JOIN ( " +
                "SELECT username AS receivername, accounts.account_id, users.user_id " +
                "FROM accounts " +
                "JOIN users " +
                "ON accounts.user_id = users.user_id ) receiver " +
                "ON t.account_to = receiver.account_id " +
                "WHERE (sendername = ? or receivername = ?) AND transfer_status_id = 1;"; // 1 representing pending

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);

        while (results.next()) {
            list.add(mapRowToTransfer(results));
        }

        return list;
    }

    @Override
    public Transfer getTransferDetailsByTransferId(Long transferId, String username) {

        Transfer transfer = null;

        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from AS sender_account, account_to AS receiver_account, amount, sender.user_id AS sender_id, receiver.user_id AS receiver_id, sendername, receivername " +
                "FROM transfers t " +
                "JOIN (" +
                "SELECT username AS sendername, accounts.account_id, users.user_id " +
                "FROM accounts " +
                "JOIN users " +
                "ON accounts.user_id = users.user_id ) sender " +
                "ON t.account_from = sender.account_id " +
                " " +
                "JOIN ( " +
                "SELECT username AS receivername, accounts.account_id, users.user_id " +
                "FROM accounts " +
                "JOIN users " +
                "ON accounts.user_id = users.user_id ) receiver " +
                "ON t.account_to = receiver.account_id " +
                "WHERE (sendername = ? OR receivername = ?) AND transfer_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,  username, username, transferId);

        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }

        return transfer;
    }

    @Override
    public boolean createTransfer(Transfer transfer, int type) throws AccountNotFoundException {

        if (type == 2) { // sending transfer
            if (accountDao.sendFunds(transfer.getAmount(), transfer.getSender_account(), transfer.getReceiver_account())) {

                String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                        "VALUES (?, ?, ?, ?, ?);";

                jdbcTemplate.update(sql, 2, 2, transfer.getSender_account(), transfer.getReceiver_account(), transfer.getAmount());
                return true;
            } else {
                return false;

            }
        } else if (type == 1) { //receiving transfer
            String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (?, ?, ?, ?, ?);";

            jdbcTemplate.update(sql, 1, 1, transfer.getSender_account(), transfer.getReceiver_account(), transfer.getAmount());
            return true;
            // create a pending request transfer in transfers table, no funds are moved yet

        } else return false;




    }

    @Override
    public boolean acceptTransfer(Transfer transfer) throws AccountNotFoundException {
        // attempt to update funds in account balances

        if (accountDao.sendFunds(transfer.getAmount(), transfer.getSender_account(), transfer.getReceiver_account())) {

            String sql = "UPDATE transfers " +
                    "SET transfer_status_id = 2 " +
                    "WHERE transfer_id = ?;";

            jdbcTemplate.update(sql, transfer.getTransfer_id());
            return true;

        } else return false;

        // if that is successful, update the transfer data and return true
    }

    @Override
    public boolean rejectTransfer(Transfer transfer) {
        String sql = "UPDATE transfers " +
                "SET transfer_status_id = 3 " +
                "WHERE transfer_id = ?;";

        jdbcTemplate.update(sql, transfer.getTransfer_id());
        return true;
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();

        transfer.setTransfer_id(rowSet.getLong("transfer_id"));
        transfer.setTransfer_status_id(rowSet.getLong("transfer_status_id"));
        transfer.setTransfer_type_id(rowSet.getLong("transfer_type_id"));
        transfer.setSender_account(rowSet.getLong("sender_account"));
        transfer.setSender_id(rowSet.getLong("sender_id"));
        transfer.setReceiver_account(rowSet.getLong("receiver_account"));
        transfer.setReceiver_id(rowSet.getLong("receiver_id"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        transfer.setSendername(rowSet.getString("sendername"));
        transfer.setReceivername(rowSet.getString("receivername"));

        return transfer;
    }
}
