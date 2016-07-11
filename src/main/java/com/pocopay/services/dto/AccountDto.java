package com.pocopay.services.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto {
    public static final Long STARTING_FUNDS = 2000L;

    private Long id;
    private String name;

    public boolean equals(AccountDto account) {
        if (account == null)
            return false;
        return this.getId().equals(account.getId()) && this.getName().equals(account.getName());
    }

}
