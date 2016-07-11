package com.pocopay.services.dto;

import lombok.Data;

@Data
public class TransactionDto {

    public static final Integer ACCOUNT_TO_ACCOUNT = 1;
    public static final Integer EXTERNAL_PAYMENT = 2;

    private Long id;
    private AccountDto to;
    private AccountDto from;
    private Long amount;
    private Integer type = ACCOUNT_TO_ACCOUNT;
}
