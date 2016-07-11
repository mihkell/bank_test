package com.pocopay.controller.dto;

import lombok.Data;

@Data
public class PayRequest {

    private String to;
    private String from;
    private Long amount;

}
