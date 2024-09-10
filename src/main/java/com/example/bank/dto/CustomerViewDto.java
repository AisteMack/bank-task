package com.example.bank.dto;

import com.example.bank.enums.ECustomerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CustomerViewDto {

    private final String name;

    private final String lastName;

    private final LocalDate birthDate;

    private final ECustomerType customerType;

    private final String phoneNumber;

    private final String email;

    private final List<AddressViewDto> addresses;

    private final List<AccountViewDto> accounts;

    private Long id;

}
