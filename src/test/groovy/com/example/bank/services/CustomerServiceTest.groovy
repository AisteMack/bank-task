package com.example.bank.services

import com.example.bank.dto.*
import com.example.bank.entities.Address
import com.example.bank.entities.Customer
import com.example.bank.enums.ECustomerType
import com.example.bank.exceptions.ServiceValidationException
import com.example.bank.mapper.CustomerMapper
import com.example.bank.repositories.CustomerRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

import java.time.LocalDate

class CustomerServiceTest extends Specification {

    def accountService = Mock(AccountService)
    def addressService = Mock(AddressService)
    def customerRepository = Mock(CustomerRepository)
    def customerMapper = Mock(CustomerMapper)
    def messageSourceService = Mock(MessageSourceService)
    def customerService = new CustomerService(accountService, addressService, customerRepository, customerMapper, messageSourceService)

    def setup() {
        messageSourceService.getMessage("customer.exists") >> "customer exists"
        messageSourceService.getMessage("address.not.exists") >> "address not exists"
        messageSourceService.getMessage("customer.not.exists") >> "customer not exists"
    }

    def "Should return customer list and not throw exception"() {
        given:
        var customer = Customer.builder()
                .id(123L)
                .name("Name")
                .lastName("Lastname")
                .build()
        var customerDto = CustomerViewDto.builder()
                .name("Name")
                .lastName("Lastname")
                .build()
        var accounts = List.of(AccountViewDto.builder().build())
        when:
        var result = customerService.getCustomers("term", 0, 1)
        then:
        1 * customerRepository.findBySearchTerm("term", PageRequest.of(0, 1)) >> new PageImpl<>(List.of(customer))
        1 * accountService.getAccounts(123L) >> accounts
        1 * customerMapper.map(customer, accounts) >> customerDto
        with(result) {
            result.totalCustomerCount == 1
            result.customers.get(0).getName() == "Name"
            result.customers.get(0).getLastName() == "Lastname"
        }
    }

    def "Should return customer list when search term is null"() {
        given:
        var customer = Customer.builder()
                .id(123L)
                .name("Name")
                .lastName("Lastname")
                .build()
        var customerDto = CustomerViewDto.builder()
                .name("Name")
                .lastName("Lastname")
                .build()
        var accounts = List.of(AccountViewDto.builder().build())
        when:
        var result = customerService.getCustomers(null, 0, 1)
        then:
        1 * customerRepository.findAll(PageRequest.of(0, 1)) >> new PageImpl<>(List.of(customer))
        1 * accountService.getAccounts(123L) >> accounts
        1 * customerMapper.map(customer, accounts) >> customerDto
        with(result) {
            result.totalCustomerCount == 1
            result.customers.get(0).getName() == "Name"
            result.customers.get(0).getLastName() == "Lastname"
        }
    }

    def "createCustomer: Should throw exception when customer already exists"() {
        given:
        var customerDto = CreateCustomerDto.builder()
                .name("name")
                .lastName("lastName")
                .birthDate(LocalDate.of(2000, 01, 01))
                .phoneNumber("+37060000000")
                .email("test@test.test")
                .customerType(ECustomerType.INDIVIDUAL)
                .addresses(List.of(CreateAddressDto.builder().build()))
                .build()

        when:
        customerService.createCustomer(customerDto)
        then:
        1 * customerRepository.findCustomerByNameAndLastNameAndPhoneNumberAndEmail(
                "name", "lastName", "+37060000000", "test@test.test") >> Optional.of(Customer.builder().build())
        ServiceValidationException ex = thrown()
        ex.getMessage() == "customer exists"
    }

    def "createCustomer: Should not throw exception and save customer"() {
        given:
        var customerDto = CreateCustomerDto.builder()
                .name("name")
                .lastName("lastName")
                .birthDate(LocalDate.of(2000, 01, 01))
                .phoneNumber("+37060000000")
                .email("test@test.test")
                .customerType(ECustomerType.INDIVIDUAL)
                .addresses(List.of(CreateAddressDto.builder().build()))
                .build()

        var customer = Customer.builder()
                .id(123L)
                .name("name")
                .lastName("lastName")
                .birthDate(LocalDate.of(2000, 01, 01))
                .phoneNumber("+37060000000")
                .email("test@test.test")
                .customerType(ECustomerType.INDIVIDUAL)
                .build()

        when:
        var response = customerService.createCustomer(customerDto)
        then:
        1 * customerRepository.findCustomerByNameAndLastNameAndPhoneNumberAndEmail(
                "name", "lastName", "+37060000000", "test@test.test") >> Optional.empty()
        1 * customerMapper.map(customerDto) >> customer
        1 * addressService.createAddresses(customerDto.getAddresses(), customer)
        1 * accountService.createAccount(customer)
        response == 123L
    }

    def "updateCustomer: Should throw exception when customer exists with unique fields"() {
        given:
        var customerDto = UpdateCustomerDto.builder()
                .name("name")
                .lastName("lastName")
                .birthDate(LocalDate.of(2000, 01, 01))
                .phoneNumber("+37060000000")
                .email("test@test.test")
                .customerType(ECustomerType.INDIVIDUAL)
                .addresses(List.of(UpdateAddressDto.builder().build()))
                .build()

        when:
        customerService.updateCustomer(123L, customerDto)
        then:
        1 * customerRepository.findCustomerByNameAndLastNameAndPhoneNumberAndEmail(
                "name", "lastName", "+37060000000", "test@test.test") >> Optional.of(Customer.builder().build())
        ServiceValidationException ex = thrown()
        ex.getMessage() == "customer exists"
    }

    def "updateCustomer: Should throw exception when customer not exists with id"() {
        given:
        var customerDto = UpdateCustomerDto.builder()
                .name("name")
                .lastName("lastName")
                .birthDate(LocalDate.of(2000, 01, 01))
                .phoneNumber("+37060000000")
                .email("test@test.test")
                .customerType(ECustomerType.INDIVIDUAL)
                .addresses(List.of(UpdateAddressDto.builder().build()))
                .build()

        when:
        customerService.updateCustomer(123L, customerDto)
        then:
        1 * customerRepository.findCustomerByNameAndLastNameAndPhoneNumberAndEmail(
                "name", "lastName", "+37060000000", "test@test.test") >> Optional.empty()
        1 * customerRepository.findById(123L) >> Optional.empty()
        ServiceValidationException ex = thrown()
        ex.getMessage() == "customer not exists"
    }

    def "updateCustomer: Should throw exception when updating not existing address"() {
        given:
        var customerDto = UpdateCustomerDto.builder()
                .name("newName")
                .lastName("newLastName")
                .birthDate(LocalDate.of(2000, 02, 01))
                .phoneNumber("+37060000001")
                .email("newtest@test.test")
                .customerType(ECustomerType.PUBLIC)
                .addresses(List.of(UpdateAddressDto.builder()
                        .id(148L)
                        .country("NewCountry")
                        .city("NewCity")
                        .street("NewStreet")
                        .houseNumber(4)
                        .build()))
                .build()

        var customer = Customer.builder()
                .id(123L)
                .name("name")
                .lastName("lastName")
                .birthDate(LocalDate.of(2000, 01, 01))
                .phoneNumber("+37060000000")
                .email("test@test.test")
                .customerType(ECustomerType.INDIVIDUAL)
                .addresses(List.of(Address.builder()
                        .id(147L)
                        .country("Country")
                        .city("City")
                        .street("Street")
                        .houseNumber(3)
                        .build()))
                .build()

        when:
        customerService.updateCustomer(123L, customerDto)
        then:
        1 * customerRepository.findCustomerByNameAndLastNameAndPhoneNumberAndEmail(
                "newName", "newLastName", "+37060000001", "newtest@test.test") >> Optional.empty()
        1 * customerRepository.findById(123L) >> Optional.of(customer)
        ServiceValidationException ex = thrown()
        ex.getMessage() == "address not exists"
    }

    def "updateCustomer: Should not throw exception and update customer"() {
        given:
        var customerDto = UpdateCustomerDto.builder()
                .name("newName")
                .lastName("newLastName")
                .birthDate(LocalDate.of(2000, 02, 01))
                .phoneNumber("+37060000001")
                .email("newtest@test.test")
                .customerType(ECustomerType.PUBLIC)
                .addresses(List.of(UpdateAddressDto.builder()
                        .id(147L)
                        .country("NewCountry")
                        .city("NewCity")
                        .street("NewStreet")
                        .houseNumber(4)
                        .build()))
                .build()

        var customer = Customer.builder()
                .id(123L)
                .name("name")
                .lastName("lastName")
                .birthDate(LocalDate.of(2000, 01, 01))
                .phoneNumber("+37060000000")
                .email("test@test.test")
                .customerType(ECustomerType.INDIVIDUAL)
                .addresses(List.of(Address.builder()
                        .id(147L)
                        .country("Country")
                        .city("City")
                        .street("Street")
                        .houseNumber(3)
                        .build()))
                .build()

        when:
        customerService.updateCustomer(123L, customerDto)
        then:
        1 * customerRepository.findCustomerByNameAndLastNameAndPhoneNumberAndEmail(
                "newName", "newLastName", "+37060000001", "newtest@test.test") >> Optional.empty()
        1 * customerRepository.findById(123L) >> Optional.of(customer)
        1 * accountService.getAccounts(123L) >> List.of(AccountViewDto.builder().build())
        1 * customerRepository.save(_) >> { Customer savedCustomer ->
            assert savedCustomer.name == "newName"
            assert savedCustomer.lastName == "newLastName"
            assert savedCustomer.birthDate == LocalDate.of(2000, 2, 1)
            assert savedCustomer.phoneNumber == "+37060000001"
            assert savedCustomer.email == "newtest@test.test"
            assert savedCustomer.customerType == ECustomerType.PUBLIC
            assert savedCustomer.addresses.size() == 1
            assert savedCustomer.addresses[0].country == "NewCountry"
            assert savedCustomer.addresses[0].city == "NewCity"
            assert savedCustomer.addresses[0].street == "NewStreet"
            assert savedCustomer.addresses[0].houseNumber == 4
            return savedCustomer
        }
    }
}
