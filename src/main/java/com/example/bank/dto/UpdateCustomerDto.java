package com.example.bank.dto;

import com.example.bank.enums.ECustomerType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Validated
public class UpdateCustomerDto {

    @NotBlank(message = "{notblank.field}")
    private final String name;

    @NotBlank(message = "{notblank.field}")
    private final String lastName;

    @NotNull(message = "{notnull.field}")
    private final ECustomerType customerType;

    @NotNull(message = "{notnull.field}")
    private final LocalDate birthDate;

    @NotNull(message = "{notnull.field}")
    @Pattern(regexp = "^\\+?[0-9\\s-]{7,15}$", message = "{phone.invalid}")
    private final String phoneNumber;

    @NotNull(message = "{notnull.field}")
    @Email(message = "{email.invalid}")
    private final String email;

    @NotEmpty(message = "{address.empty}")
    private final List<@Valid @NotNull(message = "{address.empty}") UpdateAddressDto> addresses;

}
