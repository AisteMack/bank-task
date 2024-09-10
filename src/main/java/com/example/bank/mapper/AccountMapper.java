package com.example.bank.mapper;

import com.example.bank.dto.AccountViewDto;
import com.example.bank.entities.Account;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountViewDto map(@Nonnull Account account) {
        return AccountViewDto.builder()
                .accountNumber(account.getAccountNumber())
                .build();
    }
}
