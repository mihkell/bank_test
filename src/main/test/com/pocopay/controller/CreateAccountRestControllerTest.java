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
import com.pocopay.controller.dto.CreateUserRequest;
import com.pocopay.service.AccountService;
import com.pocopay.test.MyWebControllerTest;

public class CreateAccountRestControllerTest extends MyWebControllerTest {

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    protected AccountService accountService;

    protected MockMvc mockMvc;
    protected ObjectMapper mapper;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        mapper = new ObjectMapper();
    }

    @Test
    public void shouldReturnInitialUserObject() throws Exception {
        String name = getAccountName();
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setName(name);

        byte[] jsonToSend = mapper.writeValueAsBytes(userRequest);
        this.mockMvc
                .perform(post("/createuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToSend))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true))
                .andExpect(jsonPath("$.content.name").value(name))
                .andExpect(jsonPath("$.content.id").isNumber())
                .andReturn();
    }
}
