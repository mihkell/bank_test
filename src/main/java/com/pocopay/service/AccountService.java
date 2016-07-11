package com.pocopay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pocopay.dao.DatabaseApiImpl;
import com.pocopay.services.dto.AccountDto;
import com.pocopay.services.dto.Transaction;

@Service
public class AccountService {
	
	@Autowired
	private DatabaseApiImpl databaseApi;
	
	@Autowired
	private TransactionService transactionService;

	public AccountDto createAccount(String accountName) {
		AccountDto account = new AccountDto();
		account.setName(accountName);
		
		databaseApi.save(account);
		initializeAccount(account);
		
		return account;
	}

    private void initializeAccount(AccountDto account) {
        addInitialFunds(account);
    }

    private void addInitialFunds(AccountDto account) {
        transactionService.makeTransaction(account, AccountDto.STARTING_FUNDS, Transaction.EXTERNAL_PAYMENT);
    }

    public AccountDto getAccountById(Long id) {
        return databaseApi.getAccount(id);
    }

    public AccountDto getAccountBy(String name) {
        return databaseApi.getAccount(name);
    }

}
