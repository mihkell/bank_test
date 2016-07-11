package com.pocopay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pocopay.controller.dto.PayRequest;
import com.pocopay.controller.dto.Result;
import com.pocopay.service.TransactionService;
import com.pocopay.services.dto.TransactionDto;

@RestController
@RequestMapping("pay")
public class PayRestController extends PocoController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody Result post(@RequestBody PayRequest payRequest) {
        TransactionDto transaction = transactionService.makeTransaction(payRequest.getTo(), payRequest.getFrom(),
                payRequest.getAmount());
        return ok(transaction);
    }

}
