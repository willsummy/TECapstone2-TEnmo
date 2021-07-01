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

    public JdbcTransferDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate((dataSource));
    }


    @Override
    public List<Transfer> listAllTransfersByUserId(Long userId) {
        List<Transfer> list = new ArrayList<>();

        String sql = "SELECT transfer_id, account_from, account_to, amount " +
                "FROM transfers " +
                "WHERE account_from = ? OR account_to = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);

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
        return false;
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
