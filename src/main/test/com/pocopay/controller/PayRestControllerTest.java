package com.pocopay.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocopay.controller.dto.PayRequest;
import com.pocopay.service.AccountService;
import com.pocopay.services.dto.AccountDto;
import com.pocopay.test.MyWebControllerTest;

public class PayRestControllerTest extends MyWebControllerTest {

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private AccountService accountService;

    private MockMvc mockMvc;
    protected ObjectMapper mapper;

    private String accountName;
    private String accountName2;
    private AccountDto account;
    private AccountDto account2;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mapper = new ObjectMapper();

        accountName = getAccountName();
        accountName2 = getAccountName();
        account = accountService.createAccount(accountName);
        account2 = accountService.createAccount(accountName2);
    }

    @Test
    public void shouldMakePayment() throws Exception {
        PayRequest payRequest = new PayRequest();
        payRequest.setTo(account.getName());
        payRequest.setFrom(account2.getName());
        payRequest.setAmount(25L);
        byte[] jsonToSend = mapper.writeValueAsBytes(payRequest);
        this.mockMvc.perform(post("/pay")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonToSend))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true))
                .andExpect(jsonPath("$.content.to.name").value(account.getName()));
    }

}
