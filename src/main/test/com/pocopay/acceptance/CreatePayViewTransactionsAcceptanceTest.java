package com.pocopay.acceptance;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocopay.controller.dto.CreateUserRequest;
import com.pocopay.controller.dto.PayRequest;
import com.pocopay.controller.dto.TransactionsForUserRequest;
import com.pocopay.service.AccountService;
import com.pocopay.test.MyWebControllerTest;

public class CreatePayViewTransactionsAcceptanceTest extends MyWebControllerTest {

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    protected AccountService accountService;

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
    }

    @Test
    public void shouldReturnInitialUserObject() throws Exception {
        createUsers();
        paytoAccount2();
        account2HasTwoTransactions();
    }

    private void createUsers() throws JsonProcessingException, Exception {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setName(accountName);

        byte[] jsonToSend = mapper.writeValueAsBytes(userRequest);
        this.mockMvc
                .perform(post("/createuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToSend))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true))
                .andExpect(jsonPath("$.content.name").value(accountName))
                .andExpect(jsonPath("$.content.id").isNumber())
                .andReturn();

        userRequest = new CreateUserRequest();
        userRequest.setName(accountName2);
        jsonToSend = mapper.writeValueAsBytes(userRequest);
        this.mockMvc
                .perform(post("/createuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToSend))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true))
                .andExpect(jsonPath("$.content.name").value(accountName2))
                .andExpect(jsonPath("$.content.id").isNumber())
                .andReturn();
    }

    private void paytoAccount2() throws Exception {
        PayRequest payRequest = new PayRequest();
        payRequest.setTo(accountName2);
        payRequest.setFrom(accountName);
        payRequest.setAmount(25L);
        byte[] jsonToSend = mapper.writeValueAsBytes(payRequest);
        this.mockMvc.perform(post("/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToSend))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true))
                .andExpect(jsonPath("$.content.to.name").value(accountName2));

    }

    private void account2HasTwoTransactions() throws Exception {
        TransactionsForUserRequest transactionsForUser = new TransactionsForUserRequest();
        transactionsForUser.setAccountName(accountName2);

        byte[] jsonToSend = mapper.writeValueAsBytes(transactionsForUser);
        MockHttpServletRequestBuilder post = post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToSend);
        this.mockMvc
                .perform(post)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)));

    }

}
