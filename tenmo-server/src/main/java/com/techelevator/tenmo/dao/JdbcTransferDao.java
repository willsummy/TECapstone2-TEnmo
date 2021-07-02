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
    public List<Transfer> listAllTransfersByUserId(Long userId) {
        List<Transfer> list = new ArrayList<>();

        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
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
    public boolean createTransfer(Transfer transfer) throws AccountNotFoundException {

        // this sends info to AccountDao to see if the transfer is possible
        // and also for AccountDao to edit balances
        // gets response to if it actually did it

        if (accountDao.transferFunds(transfer.getAmount(), transfer.getAccount_from(), transfer.getAccount_to())) {
            // do our sql create row
            String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (?, ?, ?, ?, ?);";

            jdbcTemplate.update(sql, 1, 1, transfer.getAccount_from(), transfer.getAccount_to(), transfer.getAmount());
            return true;
        } else {
            // don't create anything
            return false;

        }


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
