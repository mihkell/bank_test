package com.pocopay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pocopay.dao.DatabaseApiImpl;
import com.pocopay.services.dto.AccountDto;
import com.pocopay.services.dto.TransactionDto;

@Service
public class TransactionService {
	
	@Autowired
	private DatabaseApiImpl databaseApi;
	
	@Autowired
	private AccountService accountService;

	public TransactionDto pay(AccountDto to, AccountDto from, Long amount) {
		
		TransactionDto transaction = new TransactionDto();
		transaction.setTo(to);
		transaction.setFrom(from);
		transaction.setAmount(amount);
		databaseApi.save(transaction);
		
		return transaction;
	}

	public List<TransactionDto> getTransactionsFor(AccountDto account) {
		return databaseApi.getTransactionsFor(account);
	}

    public TransactionDto makeTransaction(AccountDto to, Long startingFunds, Integer paymentType) {
        TransactionDto transaction = new TransactionDto();
        transaction.setAmount(AccountDto.STARTING_FUNDS);
        transaction.setTo(to);
        transaction.setType(TransactionDto.EXTERNAL_PAYMENT);
        
        Long transactionId = databaseApi.save(transaction);
        
        transaction.setId(Long.valueOf(transactionId));
        return transaction;
        
    }

    public TransactionDto makeTransaction(String toName, String fromName, Long amount) {
        AccountDto toAccount = accountService.getAccountBy(toName);
        AccountDto fromAccount = accountService.getAccountBy(fromName);
        
        TransactionDto transaction = new TransactionDto();
        transaction.setAmount(amount);
        transaction.setTo(toAccount);
        transaction.setFrom(fromAccount);;
        transaction.setType(TransactionDto.ACCOUNT_TO_ACCOUNT);
        
        Long transactionId = databaseApi.save(transaction);
        
        transaction.setId(Long.valueOf(transactionId));
        return transaction;
        
    }

}
