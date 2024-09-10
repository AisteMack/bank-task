package com.example.bank.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateAddressDto {

    @NotNull(message = "{notnull.field}")
    private final Long id;

    @NotBlank(message = "{notblank.field}")
    private final String country;

    @NotBlank(message = "{notblank.field}")
    private final String city;

    @NotBlank(message = "{notblank.field}")
    private final String street;

    @Min(value = 1, message = "{housenumber.invalid}")
    private final int houseNumber;

}
