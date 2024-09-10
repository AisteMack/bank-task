package com.example.bank.mapper;

import com.example.bank.dto.AccountViewDto;
import com.example.bank.dto.CreateCustomerDto;
import com.example.bank.dto.CustomerViewDto;
import com.example.bank.entities.Customer;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CustomerMapper {
    private final AddressMapper addressMapper;

    public Customer map(@Nonnull CreateCustomerDto newCustomer) {
        return Customer.builder()
                .name(newCustomer.getName())
                .lastName(newCustomer.getLastName())
                .customerType(newCustomer.getCustomerType())
                .birthDate(newCustomer.getBirthDate())
                .phoneNumber(newCustomer.getPhoneNumber())
                .email(newCustomer.getEmail())
                .build();
    }

    public CustomerViewDto map(@Nonnull Customer customer, @Nonnull List<AccountViewDto> accounts) {
        return CustomerViewDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .lastName(customer.getLastName())
                .birthDate(customer.getBirthDate())
                .customerType(customer.getCustomerType())
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getEmail())
                .addresses(customer.getAddresses().stream()
                        .map(addressMapper::map)
                        .toList())
                .accounts(accounts)
                .build();
    }
}
