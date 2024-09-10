package com.example.bank.mapper

import com.example.bank.dto.AddressViewDto
import com.example.bank.dto.CreateAddressDto
import com.example.bank.entities.Address
import com.example.bank.entities.Customer
import spock.lang.Specification

import java.time.LocalDateTime

class AddressMapperTest extends Specification {

    def addressMapper = new AddressMapper()

    def "Should map address entity to dto"() {
        given:
        var address = Address.builder()
                .id(123L)
                .versionNum(1)
                .createdBy("test")
                .creationDate(LocalDateTime.now())
                .updatedBy("test")
                .lastModifiedDate(LocalDateTime.now())
                .country("Country")
                .city("City")
                .street("Street")
                .houseNumber(3)
                .customer(Customer.builder().build())
                .build()
        when:
        var mappedAddress = addressMapper.map(address)
        then:
        mappedAddress == AddressViewDto.builder()
                .id(123L)
                .country("Country")
                .city("City")
                .street("Street")
                .houseNumber(3)
                .build()
    }

    def "Should map address dto to entity"() {
        given:
        var addressDto = CreateAddressDto.builder()
                .country("Country")
                .city("City")
                .street("Street")
                .houseNumber(3)
                .build()
        var customer = Customer.builder().build()
        when:
        var mappedAddress = addressMapper.map(addressDto, customer)
        then:
        with(mappedAddress) {
            mappedAddress.country == "Country"
            mappedAddress.city == "City"
            mappedAddress.street == "Street"
            mappedAddress.houseNumber == 3
            mappedAddress.customer == customer
        }
    }
}
