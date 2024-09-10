package com.example.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class AddressViewDto {

    private final Long id;

    private final String country;

    private final String city;

    private final String street;

    private final int houseNumber;

}
