package com.pocopay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.pocopay.service.AccountService;
import com.pocopay.services.dto.AccountDto;
import com.pocopay.services.dto.TransactionDto;

@Repository
public class DatabaseApiImpl {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AccountService accountService;

    private static final String TRANSACTION_BETWEEN_ACCOUNTS = "INSERT INTO transaction("
            + TransactionTable.FROM_ACCOUNT + ","
            + TransactionTable.TO_ACCOUNT + "," + TransactionTable.AMOUNT + "," + TransactionTable.TYPE
            + ") VALUES(?,?,?,?)";

    
    public Long save(TransactionDto transaction) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(TRANSACTION_BETWEEN_ACCOUNTS, new String[] { "id" });
                        if (transaction.getFrom() == null) {
                            ps.setNull(1, java.sql.Types.INTEGER);
                        } else {
                            ps.setLong(1, transaction.getFrom().getId());
                        }
                        ps.setLong(2, transaction.getTo().getId());
                        ps.setLong(3, transaction.getAmount());
                        ps.setLong(4, transaction.getType());
                        return ps;
                    }
                },
                keyHolder);
        return keyHolder.getKey().longValue();
    }


    public void save(AccountDto account) {
        try {
            saveAccount(account);
        } catch (DuplicateKeyException e) {
        }
    }

    private void saveAccount(AccountDto account) {
        jdbcTemplate.update("INSERT INTO account(name) VALUES(?)", account.getName());
        int id = this.jdbcTemplate.queryForObject(
                "SELECT id FROM account WHERE name = ?", Integer.class, account.getName());
        account.setId(Long.valueOf(id));
    }


    public List<TransactionDto> getTransactionsFor(AccountDto to) {

        return this.jdbcTemplate.query("SELECT * FROM transaction WHERE " + TransactionTable.TO_ACCOUNT + " = ? OR "
                + TransactionTable.FROM_ACCOUNT + " = ?",
                new Object[] { to.getId(), to.getId() },
                new RowMapper<TransactionDto>() {
                    public TransactionDto mapRow(ResultSet rs, int rowNum) throws SQLException {

                        TransactionDto transaction;
                        transaction = new TransactionDto();
                        transaction.setId(rs.getLong(TransactionTable.ID));
                        transaction.setAmount(rs.getLong(TransactionTable.AMOUNT));
                        transaction.setType(rs.getInt(TransactionTable.TYPE));
                        transaction.setFrom(accountService.getAccountById(rs.getLong(TransactionTable.FROM_ACCOUNT)));
                        transaction.setTo(accountService.getAccountById(rs.getLong(TransactionTable.TO_ACCOUNT)));

                        return transaction;
                    }
                });
    }

    public AccountDto getAccount(String accountName) {
        AccountDto account = this.jdbcTemplate.queryForObject(
                "SELECT * FROM account WHERE name = ?",
                new Object[] { accountName },
                new RowMapper<AccountDto>() {
                    public AccountDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        AccountDto account = new AccountDto();
                        account.setName(rs.getString("name"));
                        account.setId(rs.getLong("id"));
                        return account;
                    }
                });
        return account;
    }

    public AccountDto getAccount(Long id) {
        AccountDto account = null;
        try {
            account = this.jdbcTemplate.queryForObject(
                    "SELECT * FROM account WHERE id = ?",
                    new Object[] { id },
                    new RowMapper<AccountDto>() {
                        public AccountDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                            AccountDto account = new AccountDto();
                            account.setName(rs.getString("name"));
                            account.setId(rs.getLong("id"));
                            return account;
                        }
                    });
        } catch (EmptyResultDataAccessException e) {
            // TODO: EMPTY CATCH!
        }

        return account;
    }

    private interface TransactionTable {
        String ID = "id";
        String FROM_ACCOUNT = "from_account";
        String TO_ACCOUNT = "to_account";
        String AMOUNT = "amount";
        String TYPE = "type";
    }

}
