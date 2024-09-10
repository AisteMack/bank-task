package com.example.bank.mapper

import com.example.bank.dto.AccountViewDto
import com.example.bank.dto.AddressViewDto
import com.example.bank.dto.CreateAddressDto
import com.example.bank.dto.CreateCustomerDto
import com.example.bank.entities.Address
import com.example.bank.entities.Customer
import com.example.bank.enums.ECustomerType
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

class CustomerMapperTest extends Specification {

    def addressMapper = Mock(AddressMapper)
    def customerMapper = new CustomerMapper(addressMapper)

    def "Should map customer entity to dto"() {
        given:
        var customer = Customer.builder()
                .id(123L)
                .versionNum(1)
                .createdBy("test")
                .creationDate(LocalDateTime.now())
                .updatedBy("test")
                .lastModifiedDate(LocalDateTime.now())
                .name("name")
                .lastName("lastName")
                .birthDate(LocalDate.of(2000, 01, 01))
                .phoneNumber("+37060000000")
                .email("test@test.test")
                .customerType(ECustomerType.INDIVIDUAL)
                .addresses(List.of(Address.builder()
                        .build()))
                .build()
        when:
        var mappedCustomer = customerMapper.map(customer, List.of(AccountViewDto.builder().build()))
        then:
        1 * addressMapper.map(*_) >> AddressViewDto.builder().build()
        with(mappedCustomer) {
            mappedCustomer.id == 123L
            mappedCustomer.name == "name"
            mappedCustomer.lastName == "lastName"
            mappedCustomer.birthDate == LocalDate.of(2000, 01, 01)
            mappedCustomer.customerType == ECustomerType.INDIVIDUAL
            mappedCustomer.phoneNumber == "+37060000000"
            mappedCustomer.email == "test@test.test"
            mappedCustomer.addresses.size() == 1
            mappedCustomer.accounts.size() == 1
        }
    }

    def "Should map customer dto to entity"() {
        given:
        def customerDto = CreateCustomerDto.builder()
                .name("name")
                .lastName("lastName")
                .birthDate(LocalDate.of(2000, 01, 01))
                .phoneNumber("+37060000000")
                .email("test@test.test")
                .customerType(ECustomerType.INDIVIDUAL)
                .addresses(List.of(CreateAddressDto.builder().build()))
                .build()
        when:
        var mappedCustomer = customerMapper.map(customerDto)
        then:
        with(mappedCustomer) {
            mappedCustomer.name == "name"
            mappedCustomer.lastName == "lastName"
            mappedCustomer.birthDate == LocalDate.of(2000, 01, 01)
            mappedCustomer.customerType == ECustomerType.INDIVIDUAL
            mappedCustomer.phoneNumber == "+37060000000"
            mappedCustomer.email == "test@test.test"
        }
    }
}
