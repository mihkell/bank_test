package com.pocopay.service;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pocopay.services.dto.AccountDto;
import com.pocopay.services.dto.Transaction;
import com.pocopay.test.MyWebControllerTest;

public class AccountServiceTest extends MyWebControllerTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    private String accountName;

    private AccountDto account;

    @Before
    public void before() {

        accountName = getAccountName();
        account = accountService.createAccount(accountName);
    }

    @Test
    public void shouldReturnNamedAccountObject() {
        assertTrue(account.getName() == accountName);
    }

    @Test
    public void shouldHaveSavedAccount() {
        AccountDto accountById = accountService.getAccountById(account.getId());
        assertTrue(account.equals(accountById));
    }

    @Test
    public void shouldSaveInitialTransaction() {
        List<Transaction> transactions = transactionService.getTransactionsFor(account);
        assertTrue(transactions.get(0).getType().equals(Transaction.EXTERNAL_PAYMENT));
    }

    @Test
    public void shouldGetAccountById() {
        AccountDto accountById = accountService.getAccountById(account.getId());
        assertTrue(account.equals(accountById));
    }

    @Test
    public void shouldGetAccountByName() {
        AccountDto accountByName = accountService.getAccountBy(account.getName());
        assertTrue(account.equals(accountByName));
    }

}
