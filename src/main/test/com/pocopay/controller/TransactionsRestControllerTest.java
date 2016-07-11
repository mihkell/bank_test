package com.pocopay.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocopay.controller.dto.TransactionsForUserRequest;
import com.pocopay.service.AccountService;
import com.pocopay.service.TransactionService;
import com.pocopay.test.MyWebControllerTest;

public class TransactionsRestControllerTest extends MyWebControllerTest {

    @Autowired
    protected WebApplicationContext wac;
    @Autowired
    protected AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    protected MockMvc mockMvc;
    protected ObjectMapper mapper;

    private String accountName;

    private String accountName2;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mapper = new ObjectMapper();
        
        accountName = getAccountName();
        accountName2 = getAccountName();
        accountService.createAccount(accountName);
        accountService.createAccount(accountName2);
    }

    @Test
    public void shouldReturnTransactionRelatedToAccount() throws Exception {
        TransactionsForUserRequest transactionsForUser = new TransactionsForUserRequest();
        transactionsForUser.setAccountName(accountName);
        

        byte[] jsonToSend = mapper.writeValueAsBytes(transactionsForUser);
        MockHttpServletRequestBuilder post = post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToSend);
        this.mockMvc
                .perform(post)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)));

        Long amount = 31L;
        transactionService.makeTransaction(accountName, accountName2, amount);

        this.mockMvc
                .perform(post)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)));

        amount = 33L;
        transactionService.makeTransaction(accountName2, accountName, amount);

        this.mockMvc
                .perform(post)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(3)));

    }

}
