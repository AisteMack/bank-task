package com.example.bank.services;

import com.example.bank.dto.AccountViewDto;
import com.example.bank.entities.Account;
import com.example.bank.entities.Customer;
import com.example.bank.mapper.AccountMapper;
import com.example.bank.repositories.AccountRepository;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final Random random = new Random();
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional
    public void createAccount(@Nonnull Customer customer) {
        var bban = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10);
            bban.append(digit);
        }
        var account = Account.builder()
                .accountNumber("LT00" + bban)
                .build();
        account.addCustomer(customer);
        accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public List<AccountViewDto> getAccounts(@Nonnull Long customerId) {
        return accountRepository.findAllByCustomerId(customerId)
                .stream()
                .map(accountMapper::map)
                .toList();
    }
}
