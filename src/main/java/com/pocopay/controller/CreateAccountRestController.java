package com.pocopay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pocopay.controller.dto.CreateUserRequest;
import com.pocopay.controller.dto.Result;
import com.pocopay.service.AccountService;
import com.pocopay.services.dto.AccountDto;

@RestController
@RequestMapping("createuser")
public class CreateAccountRestController extends PocoController {
    
    @Autowired
    AccountService accountService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody Result post(@RequestBody CreateUserRequest userRequest) {
        AccountDto account = accountService.createAccount(userRequest.getName());
        return ok(account);
    }
}
