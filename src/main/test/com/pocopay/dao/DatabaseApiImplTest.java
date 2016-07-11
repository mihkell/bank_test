package com.pocopay.dao;

import static com.pocopay.test.MyWebControllerTest.getAccountName;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.pocopay.service.AccountService;
import com.pocopay.services.dto.AccountDto;
import com.pocopay.services.dto.Transaction;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring-core-config.xml")
@Transactional
public class DatabaseApiImplTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private DatabaseApiImpl databaseApiImpl;

    private String accountName;

    private String accountName2;

    private AccountDto returnedAccount;

    private AccountDto account;

    @Before
    public void setup() {
        accountName = getAccountName();
        accountName2 = getAccountName();
        
        account = createAccount(accountName);
        databaseApiImpl.save(account);
        returnedAccount = databaseApiImpl.getAccount(accountName);
    }

    private AccountDto createAccount(String accountName) {
        AccountDto account = new AccountDto();
        account.setName(accountName);
        return account;
    }

    @Test
    public void shuldSaveAccount() {
        
        assertTrue(account.getId() != null);
        assertTrue(account.equals(returnedAccount));
    }
    
    @Test
    public void shouldGetAccountById(){
        returnedAccount = databaseApiImpl.getAccount(account.getId());
        assertTrue(account.equals(returnedAccount));
    }

    @Test
    public void shouldHaveZeroTransactionsAfterCreatingAccountInDatabaseApi() {
        List<Transaction> transactionsFor = databaseApiImpl.getTransactionsFor(account);
        assertTrue(transactionsFor != null);
        assertEquals(transactionsFor.size(), 0);
    }
    
    @Test
    public void shuldSaveTransaction() {
        returnedAccount = accountService.createAccount(getAccountName());
        Transaction transaction = new Transaction();
        transaction.setTo(returnedAccount);
        transaction.setFrom(account);
        transaction.setAmount(20L);
        Long transactionId = databaseApiImpl.save(transaction);
        assertTrue(transactionId != null);
        
        List<Transaction> transactionsFor = databaseApiImpl.getTransactionsFor(returnedAccount);
        assertEquals(2, transactionsFor.size());
    }
}
