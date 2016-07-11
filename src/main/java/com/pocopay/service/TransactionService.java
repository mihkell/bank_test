package com.pocopay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pocopay.dao.DatabaseApiImpl;
import com.pocopay.services.dto.AccountDto;
import com.pocopay.services.dto.Transaction;

@Service
public class TransactionService {
	
	@Autowired
	private DatabaseApiImpl databaseApi;
	
	@Autowired
	private AccountService accountService;

	public Transaction pay(AccountDto to, AccountDto from, Long amount) {
		
		Transaction transaction = new Transaction();
		transaction.setTo(to);
		transaction.setFrom(from);
		transaction.setAmount(amount);
		databaseApi.save(transaction);
		
		return transaction;
	}

	public List<Transaction> getTransactionsFor(AccountDto account) {
		return databaseApi.getTransactionsFor(account);
	}

    public Transaction makeTransaction(AccountDto to, Long startingFunds, Integer paymentType) {
        Transaction transaction = new Transaction();
        transaction.setAmount(AccountDto.STARTING_FUNDS);
        transaction.setTo(to);
        transaction.setType(Transaction.EXTERNAL_PAYMENT);
        
        Long transactionId = databaseApi.save(transaction);
        
        transaction.setId(Long.valueOf(transactionId));
        return transaction;
        
    }

    public Transaction makeTransaction(String toName, String fromName, Long amount) {
        AccountDto toAccount = accountService.getAccountBy(toName);
        AccountDto fromAccount = accountService.getAccountBy(fromName);
        
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTo(toAccount);
        transaction.setFrom(fromAccount);;
        transaction.setType(Transaction.ACCOUNT_TO_ACCOUNT);
        
        Long transactionId = databaseApi.save(transaction);
        
        transaction.setId(Long.valueOf(transactionId));
        return transaction;
        
    }

}
