package com.example.bank.services;

import com.example.bank.dto.CreateCustomerDto;
import com.example.bank.dto.CustomerViewDto;
import com.example.bank.dto.CustomersDto;
import com.example.bank.dto.UpdateCustomerDto;
import com.example.bank.entities.Address;
import com.example.bank.entities.Customer;
import com.example.bank.exceptions.ServiceValidationException;
import com.example.bank.mapper.CustomerMapper;
import com.example.bank.repositories.CustomerRepository;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final AccountService accountService;
    private final AddressService addressService;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final MessageSourceService messageSourceService;

    @Transactional
    public Long createCustomer(@Nonnull CreateCustomerDto createCustomerDTO) {
        validateCustomerExists(createCustomerDTO.getName(),
                createCustomerDTO.getLastName(),
                createCustomerDTO.getPhoneNumber(),
                createCustomerDTO.getEmail());

        var customer = customerMapper.map(createCustomerDTO);
        customerRepository.save(customer);

        addressService.createAddresses(createCustomerDTO.getAddresses(), customer);

        accountService.createAccount(customer);

        return customer.getId();
    }

    @Transactional
    public CustomerViewDto updateCustomer(@Nonnull Long customerId, @Nonnull UpdateCustomerDto updatedCustomer) {
        validateCustomerExists(updatedCustomer.getName(),
                updatedCustomer.getLastName(),
                updatedCustomer.getPhoneNumber(),
                updatedCustomer.getEmail(),
                customerId);

        var existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ServiceValidationException(messageSourceService.getMessage("customer.not.exists")));
        updateCustomerInfo(existingCustomer, updatedCustomer);

        updateCustomerAddressInfo(existingCustomer, updatedCustomer);
        var accounts = accountService.getAccounts(customerId);

        customerRepository.save(existingCustomer);
        return customerMapper.map(existingCustomer, accounts);
    }

    @Transactional(readOnly = true)
    public CustomersDto getCustomers(@Nullable String searchTerm, int page, int size) {
        var pageable = PageRequest.of(page, size);

        var customersPage = Objects.isNull(searchTerm)
                ? customerRepository.findAll(pageable)
                : customerRepository.findBySearchTerm(searchTerm, pageable);

        var customers = customersPage.get()
                .map(customer -> {
                    var accounts = accountService.getAccounts(customer.getId());
                    return customerMapper.map(customer, accounts);
                }).toList();

        return CustomersDto.builder()
                .totalCustomerCount(customersPage.getTotalElements())
                .customers(customers)
                .build();
    }

    private void validateCustomerExists(String name, String lastName, String phone, String email) {
        validateCustomerExists(name, lastName, phone, email, null);
    }

    private void validateCustomerExists(String name, String lastName, String phone, String email, Long customerId) {
        var existingCustomer = customerRepository.findCustomerByNameAndLastNameAndPhoneNumberAndEmail(name, lastName, phone, email);
        if (existingCustomer.isPresent() && (Objects.isNull(customerId) || !customerId.equals(existingCustomer.get().getId()))) {
            throw new ServiceValidationException(messageSourceService.getMessage("customer.exists"));
        }
    }

    private void updateCustomerInfo(Customer existingCustomer, UpdateCustomerDto updatedCustomer) {
        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setLastName(updatedCustomer.getLastName());
        existingCustomer.setCustomerType(updatedCustomer.getCustomerType());
        existingCustomer.setBirthDate(updatedCustomer.getBirthDate());
        existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        existingCustomer.setEmail(updatedCustomer.getEmail());
    }

    private void updateCustomerAddressInfo(Customer existingCustomer, UpdateCustomerDto updatedCustomer) {
        if (!CollectionUtils.isEmpty(updatedCustomer.getAddresses())) {
            updatedCustomer.getAddresses()
                    .forEach(updatedAddress -> {
                        var existingAddress = findAddressById(existingCustomer, updatedAddress.getId())
                                .orElseThrow(() -> new ServiceValidationException(messageSourceService.getMessage("address.not.exists")));

                        existingAddress.setCountry(updatedAddress.getCountry());
                        existingAddress.setStreet(updatedAddress.getStreet());
                        existingAddress.setCity(updatedAddress.getCity());
                        existingAddress.setHouseNumber(updatedAddress.getHouseNumber());
                    });
        }
    }

    private Optional<Address> findAddressById(Customer customer, Long addressId) {
        return customer.getAddresses().stream()
                .filter(address -> address.getId().equals(addressId))
                .findFirst();
    }
}
