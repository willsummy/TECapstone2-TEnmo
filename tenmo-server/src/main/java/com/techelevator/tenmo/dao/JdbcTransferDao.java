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
            
        }

        return list;
    }

    @Override
    public Transfer getTransferDetailsByTransferId(Long transferId) {
        return null;
    }

    @Override
    public boolean transfer(Transfer transfer) {
        return false;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();



        return transfer;
    }
}
