package com.example.bank.services

import com.example.bank.dto.CreateAddressDto
import com.example.bank.entities.Address
import com.example.bank.entities.Customer
import com.example.bank.mapper.AddressMapper
import com.example.bank.repositories.AddressRepository
import spock.lang.Specification

class AddressServiceTest extends Specification {
    def addressRepository = Mock(AddressRepository)
    def addressMapper = Mock(AddressMapper)
    def addressService = new AddressService(addressRepository, addressMapper)

    def "Should create address and not throw exception"() {
        given:
        var customer = Customer.builder()
                .name("Name")
                .lastName("Lastname")
                .build()
        var addressDto = CreateAddressDto.builder()
                .country("Country")
                .city("City")
                .street("Street")
                .houseNumber(3)
                .build()
        var address = Address.builder()
                .country("Country")
                .city("City")
                .street("Street")
                .houseNumber(3)
                .customer(customer)
                .build()
        when:
        addressService.createAddresses(List.of(addressDto), customer)
        then:
        1 * addressMapper.map(addressDto, customer) >> address
        1 * addressRepository.save(address)
        noExceptionThrown()
    }

    def "Should not create address and not throw exception when list of addresses is empty"() {
        given:
        var customer = Customer.builder()
                .name("Name")
                .lastName("Lastname")
                .build()
        when:
        addressService.createAddresses(Collections.emptyList(), customer)
        then:
        0 * addressMapper.map(*_)
        0 * addressRepository.save(*_)
        noExceptionThrown()
    }


}
