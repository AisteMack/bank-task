package com.example.bank.services

import com.example.bank.entities.Customer
import com.example.bank.mapper.AccountMapper
import com.example.bank.repositories.AccountRepository
import spock.lang.Specification

class AccountServiceTest extends Specification {

    def accountRepository = Mock(AccountRepository)
    def accountMapper = Mock(AccountMapper)
    def accountService = new AccountService(accountRepository, accountMapper)

    def "Should create account and not throw exception"() {
        given:
        def customer = Customer.builder()
                .name("Name")
                .lastName("Lastname")
                .build()
        when:
        accountService.createAccount(customer)
        then:
        1 * accountRepository.save({
            it ->
                it.accountNumber.startsWith("LT00") &&
                        it.accountNumber.size() == 20 &&
                        it.accountNumber[4..-1].isNumber() &&
                        it.customers == List.of(customer)
        })
        noExceptionThrown()
    }
}
