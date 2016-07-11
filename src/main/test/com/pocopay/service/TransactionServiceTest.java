package com.pocopay.service;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pocopay.services.dto.AccountDto;
import com.pocopay.services.dto.Transaction;
import com.pocopay.test.MyWebControllerTest;

public class TransactionServiceTest extends MyWebControllerTest {

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountService accountService = new AccountService();

    private AccountDto to;
    private AccountDto from;
    private Long amount = 100L;

    @Before
    public void before() {

        to = accountService.createAccount(getAccountName());
        from = accountService.createAccount(getAccountName());
    }
    
    @Test
    public void shouldHaveSpecifiedFields_To_From_Amount_Id() {
        Transaction transaction = transactionService.pay(to, from, amount);

        assertTrue(transaction.getTo().equals(to));
        assertTrue(transaction.getFrom().equals(from));
        assertTrue(transaction.getAmount().equals(amount));
    }

    @Test
    public void shouldHave3Transaction() {
        transactionService.pay(to, from, amount);
        transactionService.pay(to, from, amount);
        transactionService.pay(from, from, amount);

        List<Transaction> transactions = transactionService
                .getTransactionsFor(to);

        assertTrue(transactions.size() > 0);
        assertTrue(transactions.size() == 3);
    }

    @Test
    public void shouldMakeTransactionWithMakeTransactionMethod() {
        Transaction transaction = transactionService.makeTransaction(to, AccountDto.STARTING_FUNDS,
                Transaction.EXTERNAL_PAYMENT);

        assertTrue(transaction.getId() != null);
    }

    @Test
    public void shouldMakeTransactionBetweenAccountsByIds() {
        Long amount = 25L;
        Transaction transaction = transactionService.makeTransaction(to.getName(), from.getName(), amount);
        assertTrue(transaction.getId() != null);
        assertTrue(transaction.getAmount().equals(amount));

    }

}
