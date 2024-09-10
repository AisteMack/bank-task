package com.example.bank.mapper

import com.example.bank.dto.AccountViewDto
import com.example.bank.entities.Account
import com.example.bank.entities.Customer
import spock.lang.Specification

class AccountMapperTest extends Specification {

    def accountMapper = new AccountMapper()

    def "Should map account"() {
        given:
        var account = Account.builder()
                .accountNumber("LT0011111111111111")
                .customers(List.of(Customer.builder().build()))
                .build()
        when:
        var mappedAccount = accountMapper.map(account)
        then:
        mappedAccount == AccountViewDto.builder()
                .accountNumber("LT0011111111111111")
                .build()
        noExceptionThrown()
    }
}
