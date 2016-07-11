package com.pocopay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pocopay.controller.dto.Result;
import com.pocopay.controller.dto.TransactionsForUserRequest;
import com.pocopay.service.AccountService;
import com.pocopay.service.TransactionService;
import com.pocopay.services.dto.AccountDto;
import com.pocopay.services.dto.TransactionDto;

@RestController
@RequestMapping("transactions")
public class TransactionsRestController extends PocoController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody Result post(@RequestBody TransactionsForUserRequest transactionsRequest) {
        AccountDto account = accountService.getAccountBy(transactionsRequest.getAccountName());

        List<TransactionDto> transactions = transactionService.getTransactionsFor(account);

        return ok(transactions);
    }
}
